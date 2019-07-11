/*
 * Copyright 2007, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.commands.monkey;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.IActivityController;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.Process;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.util.Base64;
import android.view.IWindowManager;
import android.view.Surface;

/**
 * Application that injects random key events and other actions into the system.
 */
public class Monkey {

    private IActivityManager mAm;

    private IWindowManager mWm;

    private IPackageManager mPm;

    MonkeyEventSource mEventSource;

    // information on the current activity.
    public static Intent currentIntent;

    public static String currentPackage;

    /**
     * Monitor operations happening in the system.
     */
    private class ActivityController extends IActivityController.Stub {

        public boolean activityStarting(Intent intent, String pkg) {
            Logger.out.println("    // activityStarting(" + pkg + ")");
            currentPackage = pkg;
            currentIntent = intent;
            return true;
        }

        public boolean activityResuming(String pkg) {
            StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
            Logger.out.println("    // activityResuming(" + pkg + ")");
            currentPackage = pkg;
            StrictMode.setThreadPolicy(savedPolicy);
            return true;
        }

        public boolean appCrashed(String processName, int pid, String shortMsg, String longMsg, long timeMillis,
                String stackTrace) {
            StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
            Logger.err.println("// CRASH: " + processName + " (pid " + pid + ")");
            Logger.err.println("// Short Msg: " + shortMsg);
            Logger.err.println("// Long Msg: " + longMsg);
            Logger.err.println("// Build Label: " + Build.FINGERPRINT);
            Logger.err.println("// Build Changelist: " + Build.VERSION.INCREMENTAL);
            Logger.err.println("// Build Time: " + Build.TIME);
            Logger.err.println("// " + stackTrace.replace("\n", "\n// "));
            StrictMode.setThreadPolicy(savedPolicy);
            return false;
        }

        public int appEarlyNotResponding(String processName, int pid, String annotation) {
            StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
            Logger.out.println("    // appEarlyNotResponding(" + processName + ")");
            StrictMode.setThreadPolicy(savedPolicy);
            return 0;
        }

        public int appNotResponding(String processName, int pid, String processStats) {
            StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
            Logger.err.println("// NOT RESPONDING: " + processName + " (pid " + pid + ")");
            Logger.err.println(processStats);
            StrictMode.setThreadPolicy(savedPolicy);
            return -1;
        }

        public int systemNotResponding(String message) {
            StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
            Logger.err.println("// WATCHDOG: " + message);
            StrictMode.setThreadPolicy(savedPolicy);
            return -1;
        }
    }

    public static void main(String[] args) {
        // Set the process name showing in "ps" or "top"
        Process.setArgV0("com.android.commands.monkey");
        int resultCode = (new Monkey()).run(args);
        System.exit(resultCode);
    }

    /**
     * Run the command!
     *
     * @param args The command-line arguments
     * @return Returns a posix-style result code. 0 for no error.
     */
    private int run(String[] args) {
        if (!getSystemInterfaces()) {
            return -3;
        }

        mEventSource = new MonkeySourceShell(mAm);
        printHelp();

        try {
            runMonkeyCycles();
        } finally {
            // Release the rotation lock if it's still held and restore the
            // original orientation.
            new MonkeyRotationEvent(Surface.ROTATION_0, false).injectEvent(mWm, mAm, 0);
        }

        mAm.setActivityController(null, true);

        return 0;
    }

    /**
     * Attach to the required system interfaces.
     *
     * @return Returns true if all system interfaces were available.
     */
    private boolean getSystemInterfaces() {

        try {
            mAm = ActivityManager.getService();
        } catch (Throwable e) {
            mAm = ActivityManagerNative.getDefault();
        }

        if (mAm == null) {
            Logger.err.println("** Error: Unable to connect to activity manager; is the system " + "running?");
            return false;
        }

        mWm = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        if (mWm == null) {
            Logger.err.println("** Error: Unable to connect to window manager; is the system " + "running?");
            return false;
        }

        mPm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        if (mPm == null) {
            Logger.err.println("** Error: Unable to connect to package manager; is the system " + "running?");
            return false;
        }

        try {
            mAm.setActivityController(new ActivityController(), true);
        } catch (Throwable e) {
            mAm.setActivityController(new ActivityController());
        }

        return true;
    }

    /**
     * Run mCount cycles and see if we hit any crashers.
     * <p>
     *
     * @return Returns the last cycle which executed. If the value == mCount, no
     *         errors detected.
     */
    private int runMonkeyCycles() {
        try {
            while (true) {
                MonkeyEvent ev = mEventSource.getNextEvent();
                if (ev != null) {
                    int injectCode = ev.injectEvent(mWm, mAm, 0);
                    if (injectCode == MonkeyEvent.INJECT_FAIL) {
                        Logger.out.println("    // Injection Failed");
                    } else if (injectCode == MonkeyEvent.INJECT_ERROR_REMOTE_EXCEPTION) {
                        Logger.err.println("** Error: RemoteException while injecting event.");
                    } else if (injectCode == MonkeyEvent.INJECT_ERROR_SECURITY_EXCEPTION) {
                        Logger.err.println("** Error: SecurityException while injecting event.");
                    }
                } else {
                    break;
                }
            }
        } catch (RuntimeException e) {
            Logger.error("** Error: A RuntimeException occurred:", e);
        }
        return 0; // eventCounter;
    }

    void printHelp() {
        String help = "IyBtb25rZXktcmVwbAoKIyMjIyDmj4/ov7AKbW9ua2V5LXJlcGwgOiDkuIDkuKrpgJrov4cgdXNiIOaOp+WItiBBbmRyb2lkIOiuvuWkh+eahOiHquWKqOWMluW3peWFt++8jOS/ruaUueiHqiBbQW5kcm9pZCBtb25rZXldKGh0dHBzOi8vYW5kcm9pZC5nb29nbGVzb3VyY2UuY29tL3BsYXRmb3JtL2RldmVsb3BtZW50LysvcmVmcy9oZWFkcy9tYXN0ZXIvY21kcy9tb25rZXkp44CCCgojIyMjIOeJueaApwotIOS4jemcgOimgSBgcm9vdGAg5p2D6ZmQCi0g6I635Y+W5bGP5bmV5o6n5Lu25L+h5oGvCi0g5oiq5bGP44CB5Y+W6ImyCi0g5qih5ouf5oyJ6ZSuCi0g5a2X56ym6L6T5YWl44CB5Lit5paH6L6T5YWlCi0g5a6e5pe25ZON5bqU5ZG95Luk5pON5L2cCi0g5Y+v5Lul6YCa6L+H6ISa5pys6LCD55So5L2/55So5pa55L6/Ci0g5Y+v5Lul6I635Y+WIGB3ZWJ2aWV3YCDkuK3nmoTmjqfku7YKCiMjIyMg5L2/55So5pa55byPCi0g5L2/55SoIHVzYiDov57mjqXmiYvmnLoKLSDmiZPlvIDmiYvmnLrnmoQgdXNiIOiwg+ivleaooeW8jwotIOi/m+WFpSBzdGFydCDnm67lvZXvvIzov5DooYwgc3RhcnQuY21kIOi/m+WFpeS6pOS6kueql+WPowotIOi+k+WFpSBgcXVlcnl2aWV3IGdldGxvY2F0aW9uYCDmjIkgYGVudGVyYAotIOi+k+WFpSBgcXVlcnl2aWV3IGdldHRyZWUgdGV4dGAg5oyJIGBlbnRlcmAKLSDpgIDlh7ogYHF1aXRgIOaMiSBgZW50ZXJgCgojIyMjIOiEmuacrAotIOWPguiAgyBkZW1vIOebruW9lQoKIyMjIyDlip/og73muIXljZUKLSDmqKHmi5/mjInplK7kuovku7YKLSDmqKHmi5/lsY/luZXop6bmkbjkuovku7YKICAgIC0g54K55Ye7CiAgICAtIOaMieS4iwogICAgLSDnp7vliqggCiAgICAtIOaKrOi1twotIOS8keecoOaMh+WumuaXtumXtCAKLSDovpPlhaXlrZfnrKbkuLLmlofmnKwKLSDlpI3liLbmlofmnKzliLDliarotLTmnb8KICAgIC0g5pmu6YCa5paH5pysCiAgICAtIGJhc2U2NOe8lueggeeahOaWh+acrAogICAgLSBVUkwg57yW56CB55qE5paH5pysCi0g6L6T5YWlKirkuK3mloflrZfnrKYqKgogICAgLSDpgJrov4flpI3liLblkozmqKHmi5/mjInplK7lj6/ku6Xlrp7njrDovpPlhaXkuK3mloflrZfnrKbnmoTlip/og70KICAgIC0gYGNvcHkgYmFzZTY0IDVMaXQ1cGFINWEyWDU2eW1gCiAgICAtIGBwcmVzcyBwYXN0ZWAKLSDojrflj5bmjqfku7bkv6Hmga8KICAgIC0g5L2N572uCiAgICAtIOaWh+acrAotIOiOt+WPluagkeW9oue7k+aehOeahOeVjOmdouaOp+S7tuS/oeaBrwogICAgLSDmlofmnKzmoLzlvI8KICAgIC0ganNvbiDmoLzlvI8KICAgIC0g6I635Y+W55WM6Z2i5YWo6YOo5o6n5Lu25qCR5b2i57uT5p6ECiAgICAtIOiOt+WPluaMh+WumuaOp+S7tuS4i+eahOaOp+S7tuagkeW9oue7k+aehAotIOaIquWxj+WKn+iDvQogICAgLSDmiKrlj5bmlbTkuKrlsY/luZUKICAgIC0g5oiq5Y+W5oyH5a6a5Yy65Z+f55qE5bGP5bmVCiAgICAtIOe8qeaUvuaIquWPlueahOWbvueJhwogICAgLSDojrflj5blsY/luZXmjIflrprlnZDmoIfnmoTlg4/ntKDpopzoibIKLSDojrflj5bns7vnu5/kv6Hmga8KICAgIC0gYGJ1aWxkLmJvYXJkYAogICAgLSBgYnVpbGQuYnJhbmRgCiAgICAtIGBidWlsZC5kZXZpY2VgCiAgICAtIGBidWlsZC5kaXNwbGF5YAogICAgLSBgYnVpbGQuZmluZ2VycHJpbnRgCiAgICAtIGBidWlsZC5ob3N0YAogICAgLSBgYnVpbGQuaWRgCiAgICAtIGBidWlsZC5tb2RlbGAKICAgIC0gYGJ1aWxkLnByb2R1Y3RgCiAgICAtIGBidWlsZC50YWdzYAogICAgLSBgYnVpbGQuYnJhbmRgCiAgICAtIGBidWlsZC50eXBlYAogICAgLSBgYnVpbGQudXNlcmAKICAgIC0gYGJ1aWxkLmNwdV9hYmlgCiAgICAtIGBidWlsZC5tYW51ZmFjdHVyZXJgCi0g5Zue5pi+5a2X56ym5LiyCiAgICAtIOeUqOS6juWcqOiEmuacrOS4reWQjOatpeaTjeS9nAotIOiOt+WPlueVjOmdouaYr+WQpuacieabtOaWsAoKIyMjIyDlip/og73kvb/nlKjnpLrkvosKLSDmqKHmi5/mjInplK7kuovku7YKICAgIC0gYHByZXNzIEtFWUNPREVfRU5URVJgCiAgICAtIGBwcmVzcyBLRVlDT0RFX1BBU1RFYAogICAgLSBgcHJlc3MgS0VZQ09ERV9VUGAKICAgIC0gYHByZXNzIEtFWUNPREVfRE9XTmAKLSDmqKHmi5/lsY/luZXop6bmkbjkuovku7YKICAgIC0gYHRvdWNoIFtkb3dufHVwfG1vdmVdIFt4XSBbeV1gCiAgICAtIOeCueWHuwogICAgICAgIC0gYHRhcCB4IHlgCiAgICAgICAgLSBgdGFwIDMwIDUwYAogICAgLSDmjInkuIsKICAgICAgICAtIGB0b3VjaCBkb3duIHggeWAKICAgICAgICAtIGB0b3VjaCBkb3duIDMwIDUwYAogICAgLSDnp7vliqggCiAgICAgICAgLSBgdG91Y2ggbW92ZSB4IHlgCiAgICAgICAgLSBgdG91Y2ggbW92ZSA1MCA2MGAKICAgIC0g5oqs6LW3CiAgICAgICAgLSBgdG91Y2ggdXAgeCB5YAogICAgICAgIC0gYHRvdWNoIHVwIDcwIDgwYAotIOS8keecoOaMh+WumuaXtumXtCAKICAgIC0gYHNsZWVwIDEwMjRgCi0g6L6T5YWl5a2X56ym5Liy5paH5pysCiAgICAtIGB0eXBlIDEyMzRgCiAgICAtIGB0eXBlIHN0cmluZ2AKICAgIC0gYHR5cGUgdXNlcm5hbWVgCi0g5aSN5Yi25paH5pys5Yiw5Ymq6LS05p2/CiAgICAtIGBjb3B5IFt0ZXh0fGJhc2U2NHx1cmxlbmNvZGVdIHN0cmluZ2AKICAgIC0g5pmu6YCa5paH5pysCiAgICAgICAgLSBgY29weSB0ZXh0IHN0cmluZ2AKICAgICAgICAtIGBjb3B5IHRleHQgInN0cmluZyBzdHJpbmcgc3RyaW5nImAKICAgIC0gYmFzZTY057yW56CB55qE5paH5pysCiAgICAgICAgLSBgY29weSBiYXNlNjQgNkw2VDVZV2w1TGl0NXBhSDVhMlg1NnltYAogICAgLSBVUkwg57yW56CB55qE5paH5pysCiAgICAgICAgLSBgY29weSB1cmxlbmNvZGUgJUU4JUJFJTkzJUU1JTg1JUE1JUU0JUI4JUFEJUU2JTk2JTg3JUU1JUFEJTk3JUU3JUFDJUE2YAotIOi+k+WFpSoq5Lit5paH5a2X56ymKioKICAgIC0g6YCa6L+H5aSN5Yi25ZKM5qih5ouf5oyJ6ZSu5Y+v5Lul5a6e546w6L6T5YWl5Lit5paH5a2X56ym55qE5Yqf6IO9CiAgICAtIGBjb3B5IGJhc2U2NCA1TGl0NXBhSDVhMlg1NnltYAogICAgLSBgcHJlc3MgS0VZQ09ERV9QQVNURWAKLSDojrflj5bmjqfku7bkv6Hmga8KICAgIC0gYHF1ZXJ5dmlldyBbaWQgdHlwZV0gW2lkKHMpXSBbY29tbWFuZF1gCiAgICAgICAgLSBgaWQgdHlwZWAKICAgICAgICAgICAgLSBgIGAKICAgICAgICAgICAgLSBgdmlld2lkYAogICAgICAgICAgICAtIGBhY2Nlc3NpYmlsaXR5aWRzYAogICAgLSDojrflj5blsY/luZXlpKflsI8KICAgICAgICAtIGBxdWVyeXZpZXcgZ2V0bG9jYXRpb25gID4gYE9LOjAgMCAxNDQwIDI4ODBgCiAgICAtIOS9jee9rgogICAgICAgIC0gYHF1ZXJ5dmlldyB2aWV3aWQgY29tLnh4eC54eHh4OmlkL3h4eHh4IGdldGxvY2F0aW9uYAogICAgICAgIC0gYHF1ZXJ5dmlldyBhY2Nlc3NpYmlsaXR5aWRzIFt3aW5kb3dJZF0gW3ZpZXdJZF0gZ2V0bG9jYXRpb25gCiAgICAgICAgLSBgcXVlcnl2aWV3IGFjY2Vzc2liaWxpdHlpZHMgIDEzODEgICAgICAgODkwICAgICBnZXRsb2NhdGlvbmAKICAgICAgICAtIOekuuS+iwogICAgICAgICAgICBgYGAKICAgICAgICAgICAgPiBxdWVyeXZpZXcgdmlld2lkIGFuZHJvaWQ6aWQvYnV0dG9uMSBnZXRsb2NhdGlvbgogICAgICAgICAgICA8IE9LOjEwODEgMTQ3OSAyMjQgMTg5CiAgICAgICAgICAgIGBgYAogICAgLSDmlofmnKwKICAgICAgICAtIGBxdWVyeXZpZXcgdmlld2lkIGFuZHJvaWQ6aWQvYnV0dG9uMSBnZXR0ZXh0YAogICAgICAgIGBgYAogICAgICAgID4gcXVlcnl2aWV3IHZpZXdpZCBhbmRyb2lkOmlkL2J1dHRvbjEgZ2V0dGV4dAogICAgICAgIDwgT0s656Gu5a6aCiAgICAgICAgYGBgCi0g6I635Y+W5qCR5b2i57uT5p6E55qE55WM6Z2i5o6n5Lu25L+h5oGvCiAgICAtIOaWh+acrOagvOW8jwogICAgICAgIC0gYHF1ZXJ5dmlldyBnZXR0cmVlIHRleHRgCiAgICAgICAgLSBgcXVlcnl2aWV3IHZpZXdpZCBjb20ueHh4Lnh4eHg6aWQveHh4eHggZ2V0dHJlZSB0ZXh0YAogICAgICAgIC0gYHF1ZXJ5dmlldyBhY2Nlc3NpYmlsaXR5aWRzIDEzODEgODkwIGdldHRyZWUgdGV4dGAKICAgIC0ganNvbiDmoLzlvI8KICAgICAgICAtIGBxdWVyeXZpZXcgZ2V0dHJlZSBqc29uYAogICAgICAgIC0gYHF1ZXJ5dmlldyB2aWV3aWQgY29tLnh4eC54eHh4OmlkL3h4eHh4IGdldHRyZWUganNvbmAKICAgICAgICAtIGBxdWVyeXZpZXcgYWNjZXNzaWJpbGl0eWlkcyAxMzgxIDg5MCBnZXR0cmVlIGpzb25gCiAgICAtIOiOt+WPlueVjOmdouWFqOmDqOaOp+S7tuagkeW9oue7k+aehAogICAgICAgIC0gYHF1ZXJ5dmlldyBnZXR0cmVlIHRleHRgCiAgICAgICAgLSBgcXVlcnl2aWV3IGdldHRyZWUganNvbmAKICAgIC0g6I635Y+W5oyH5a6a5o6n5Lu25LiL55qE5o6n5Lu25qCR5b2i57uT5p6ECiAgICAgICAgLSBgcXVlcnl2aWV3IHZpZXdpZCBjb20ueHh4Lnh4eHg6aWQveHh4eHggZ2V0dHJlZSB0ZXh0YAogICAgICAgIC0gYHF1ZXJ5dmlldyBhY2Nlc3NpYmlsaXR5aWRzIDEzODEgODkwIGdldHRyZWUgdGV4dGAKICAgICAgICAtIGBxdWVyeXZpZXcgdmlld2lkIGNvbS54eHgueHh4eDppZC94eHh4eCBnZXR0cmVlIGpzb25gCiAgICAgICAgLSBgcXVlcnl2aWV3IGFjY2Vzc2liaWxpdHlpZHMgMTM4MSA4OTAgZ2V0dHJlZSBqc29uYAotIOaIquWxj+WKn+iDvQogICAgLSDmiKrlj5bnmoTlm77niYfkuLoganBnIOagvOW8j++8jOe7k+aenOmAmui/hyBiYXNlNjQg57yW56CB6L+U5ZueCiAgICAtIGB0YWtlc2NyZWVuc2hvdCBbc2NhbGV8cmVjdHxnZXRjb2xvcnxxdWFsaXR5XWAKICAgIC0g5oiq5Y+W5pW05Liq5bGP5bmVCiAgICAgICAgLSBgdGFrZXNjcmVlbnNob3RgCiAgICAtIOaIquWPluaMh+WumuWMuuWfn+eahOWxj+W5lQogICAgICAgIC0gYHRha2VzY3JlZW5zaG90IHJlY3QgMzAgMzAgNTAgNTBgCiAgICAtIOe8qeaUvuaIquWPlueahOWbvueJhwogICAgICAgIC0gYHRha2VzY3JlZW5zaG90IHNjYWxlIDAuM2AKICAgIC0g6I635Y+W5bGP5bmV5oyH5a6a5Z2Q5qCH55qE5YOP57Sg6aKc6ImyCiAgICAgICAgLSBgdGFrZXNjcmVlbnNob3QgZ2V0Y29sb3IgMzAwIDMzMGAKICAgIC0g6K6+572u5Zu+54mH55qE6LSo6YePCiAgICAgICAgLSBgdGFrZXNjcmVlbnNob3QgcXVhbGl0eSA5MGAKICAgIC0g57uE5ZCI5ZG95LukCiAgICAgICAgLSBgdGFrZXNjcmVlbnNob3QgcmVjdCAzMCAzMCA1MCA1MCBzY2FsZSAwLjUgcXVhbGl0eSA4MGAKICAgICAgICAtIGB0YWtlc2NyZWVuc2hvdCBzY2FsZSAwLjUgcmVjdCAzMCAzMCA1MCA1MCBxdWFsaXR5IDgwYAogICAgICAgIC0gYHRha2VzY3JlZW5zaG90IHF1YWxpdHkgODAgc2NhbGUgMC41IHJlY3QgMzAgMzAgNTAgNTBgCi0g6I635Y+W57O757uf5L+h5oGvCiAgICAtIOWRveS7pOagvOW8jyBgZ2V0dmFyIHZhcm5hbWVgCiAgICAtIGBidWlsZC5ib2FyZGAKICAgICAgICAtIGBnZXR2YXIgYnVpbGQuYm9hcmRgID4gYE9LOmdvbGRmaXNoX3g4NmAKICAgIC0gYGJ1aWxkLmJyYW5kYAogICAgLSBgYnVpbGQuZGV2aWNlYAogICAgLSBgYnVpbGQuZGlzcGxheWAKICAgICAgICAtIGBnZXR2YXIgYnVpbGQuZGlzcGxheWAgPiBgT0s6c2RrX2dwaG9uZV94ODYtdXNlcmRlYnVnIDkgUFNSMS4xODA3MjAuMDkzIDU0NTY0NDYgZGV2LWtleXNgCiAgICAtIGBidWlsZC5maW5nZXJwcmludGAKICAgIC0gYGJ1aWxkLmhvc3RgCiAgICAtIGBidWlsZC5pZGAKICAgIC0gYGJ1aWxkLm1vZGVsYAogICAgLSBgYnVpbGQucHJvZHVjdGAKICAgIC0gYGJ1aWxkLnRhZ3NgCiAgICAtIGBidWlsZC5icmFuZGAKICAgIC0gYGJ1aWxkLnR5cGVgCiAgICAtIGBidWlsZC51c2VyYAogICAgLSBgYnVpbGQuY3B1X2FiaWAKICAgIC0gYGJ1aWxkLm1hbnVmYWN0dXJlcmAKLSDlm57mmL7lrZfnrKbkuLIKICAgIC0g55So5LqO5Zyo6ISa5pys5Lit5ZCM5q2l5pON5L2cCiAgICAtIGBlY2hvIHN0cmluZ2AKLSDojrflj5bnlYzpnaLmmK/lkKbmnInmm7TmlrAKICAgIC0gYGdldGlzdmlld2NoYW5nZWAKLSDojrflj5bpobblsYIgYWN0aXZpdHkgCiAgICAtIGBnZXR0b3BhY3Rpdml0eWAgPiBgT0s6Y29tLmdvb2dsZS5hbmRyb2lkLmFwcHMubmV4dXNsYXVuY2hlci9jb20uZ29vZ2xlLmFuZHJvaWQuYXBwcy5uZXh1c2xhdW5jaGVyLk5leHVzTGF1bmNoZXJBY3Rpdml0eWAKLSDpgIDlh7oKICAgIC0gYHF1aXRgCgo=";
        System.out.println(new String(Base64.decode(help, Base64.DEFAULT)));
    }
}
