
package android.media;

import java.util.Map;

import android.content.Context;
import android.util.Log;

public class AudioSystem {
    private static final String TAG = "AudioSystem";
    public static final int STREAM_DEFAULT = -1;
    public static final int STREAM_VOICE_CALL = 0;
    public static final int STREAM_SYSTEM = 1;
    public static final int STREAM_RING = 2;
    public static final int STREAM_MUSIC = 3;
    public static final int STREAM_ALARM = 4;
    public static final int STREAM_NOTIFICATION = 5;
    public static final int STREAM_BLUETOOTH_SCO = 6;
    public static final int STREAM_SYSTEM_ENFORCED = 7;
    public static final int STREAM_DTMF = 8;
    public static final int STREAM_TTS = 9;
    public static final int STREAM_ACCESSIBILITY = 10;
    public static final int NUM_STREAMS = 5;

    private static final int NUM_STREAM_TYPES = 11;

    public static final int getNumStreamTypes() {
        return NUM_STREAM_TYPES;
    }

    public static final String[] STREAM_NAMES = new String[] { "STREAM_VOICE_CALL", "STREAM_SYSTEM", "STREAM_RING",
            "STREAM_MUSIC", "STREAM_ALARM", "STREAM_NOTIFICATION", "STREAM_BLUETOOTH_SCO", "STREAM_SYSTEM_ENFORCED",
            "STREAM_DTMF", "STREAM_TTS", "STREAM_ACCESSIBILITY" };

    public static native int muteMicrophone(boolean on);

    public static native boolean isMicrophoneMuted();

    public static final int MODE_INVALID = -2;
    public static final int MODE_CURRENT = -1;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_RINGTONE = 1;
    public static final int MODE_IN_CALL = 2;
    public static final int MODE_IN_COMMUNICATION = 3;
    public static final int NUM_MODES = 4;

    public static String modeToString(int mode) {
        switch (mode) {
        case MODE_CURRENT:
            return "MODE_CURRENT";
        case MODE_IN_CALL:
            return "MODE_IN_CALL";
        case MODE_IN_COMMUNICATION:
            return "MODE_IN_COMMUNICATION";
        case MODE_INVALID:
            return "MODE_INVALID";
        case MODE_NORMAL:
            return "MODE_NORMAL";
        case MODE_RINGTONE:
            return "MODE_RINGTONE";
        default:
            return "unknown mode (" + mode + ")";
        }
    }

    public static final int ROUTE_EARPIECE = (1 << 0);
    public static final int ROUTE_SPEAKER = (1 << 1);
    public static final int ROUTE_BLUETOOTH = (1 << 2);
    public static final int ROUTE_BLUETOOTH_SCO = (1 << 2);
    public static final int ROUTE_HEADSET = (1 << 3);
    public static final int ROUTE_BLUETOOTH_A2DP = (1 << 4);
    public static final int ROUTE_ALL = 0xFFFFFFFF;

    public static final int AUDIO_SESSION_ALLOCATE = 0;

    public static native boolean isStreamActive(int stream, int inPastMs);

    public static native boolean isStreamActiveRemotely(int stream, int inPastMs);

    public static native boolean isSourceActive(int source);

    public static native int newAudioSessionId();

    public static native int newAudioPlayerId();

    public static native int setParameters(String keyValuePairs);

    public static native String getParameters(String keys);

    public static final int AUDIO_STATUS_OK = 0;
    public static final int AUDIO_STATUS_ERROR = 1;
    public static final int AUDIO_STATUS_SERVER_DIED = 100;

    private static ErrorCallback mErrorCallback;

    public interface ErrorCallback {
        void onError(int error);
    };

    public static void setErrorCallback(ErrorCallback cb) {
        synchronized (AudioSystem.class) {
            mErrorCallback = cb;
            if (cb != null) {
                cb.onError(checkAudioFlinger());
            }
        }
    }

    private static void errorCallbackFromNative(int error) {
        ErrorCallback errorCallback = null;
        synchronized (AudioSystem.class) {
            if (mErrorCallback != null) {
                errorCallback = mErrorCallback;
            }
        }
        if (errorCallback != null) {
            errorCallback.onError(error);
        }
    }

    public interface DynamicPolicyCallback {
        void onDynamicPolicyMixStateUpdate(String regId, int state);
    }

    private final static int DYNAMIC_POLICY_EVENT_MIX_STATE_UPDATE = 0;

    private static DynamicPolicyCallback sDynPolicyCallback;

    public static void setDynamicPolicyCallback(DynamicPolicyCallback cb) {
        synchronized (AudioSystem.class) {
            sDynPolicyCallback = cb;
            native_register_dynamic_policy_callback();
        }
    }

    private static void dynamicPolicyCallbackFromNative(int event, String regId, int val) {
        DynamicPolicyCallback cb = null;
        synchronized (AudioSystem.class) {
            if (sDynPolicyCallback != null) {
                cb = sDynPolicyCallback;
            }
        }
        if (cb != null) {
            switch (event) {
            case DYNAMIC_POLICY_EVENT_MIX_STATE_UPDATE:
                cb.onDynamicPolicyMixStateUpdate(regId, val);
                break;
            default:
                Log.e(TAG, "dynamicPolicyCallbackFromNative: unknown event " + event);
            }
        }
    }

    public interface AudioRecordingCallback {
        void onRecordingConfigurationChanged(int event, int uid, int session, int source, int[] recordingFormat,
                String packName);
    }

    private static AudioRecordingCallback sRecordingCallback;

    public static void setRecordingCallback(AudioRecordingCallback cb) {
        synchronized (AudioSystem.class) {
            sRecordingCallback = cb;
            native_register_recording_callback();
        }
    }

    private static void recordingCallbackFromNative(int event, int uid, int session, int source,
            int[] recordingFormat) {
        AudioRecordingCallback cb = null;
        synchronized (AudioSystem.class) {
            cb = sRecordingCallback;
        }
        if (cb != null) {
            cb.onRecordingConfigurationChanged(event, uid, session, source, recordingFormat, "");
        }
    }

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int BAD_VALUE = -2;
    public static final int INVALID_OPERATION = -3;
    public static final int PERMISSION_DENIED = -4;
    public static final int NO_INIT = -5;
    public static final int DEAD_OBJECT = -6;
    public static final int WOULD_BLOCK = -7;

    public static final int DEVICE_NONE = 0x0;
    public static final int DEVICE_BIT_IN = 0x80000000;
    public static final int DEVICE_BIT_DEFAULT = 0x40000000;
    public static final int DEVICE_OUT_EARPIECE = 0x1;
    public static final int DEVICE_OUT_SPEAKER = 0x2;
    public static final int DEVICE_OUT_WIRED_HEADSET = 0x4;
    public static final int DEVICE_OUT_WIRED_HEADPHONE = 0x8;
    public static final int DEVICE_OUT_BLUETOOTH_SCO = 0x10;
    public static final int DEVICE_OUT_BLUETOOTH_SCO_HEADSET = 0x20;
    public static final int DEVICE_OUT_BLUETOOTH_SCO_CARKIT = 0x40;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP = 0x80;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES = 0x100;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER = 0x200;
    public static final int DEVICE_OUT_AUX_DIGITAL = 0x400;
    public static final int DEVICE_OUT_HDMI = DEVICE_OUT_AUX_DIGITAL;
    public static final int DEVICE_OUT_ANLG_DOCK_HEADSET = 0x800;
    public static final int DEVICE_OUT_DGTL_DOCK_HEADSET = 0x1000;
    public static final int DEVICE_OUT_USB_ACCESSORY = 0x2000;
    public static final int DEVICE_OUT_USB_DEVICE = 0x4000;
    public static final int DEVICE_OUT_REMOTE_SUBMIX = 0x8000;
    public static final int DEVICE_OUT_TELEPHONY_TX = 0x10000;
    public static final int DEVICE_OUT_LINE = 0x20000;
    public static final int DEVICE_OUT_HDMI_ARC = 0x40000;
    public static final int DEVICE_OUT_SPDIF = 0x80000;
    public static final int DEVICE_OUT_FM = 0x100000;
    public static final int DEVICE_OUT_AUX_LINE = 0x200000;
    public static final int DEVICE_OUT_SPEAKER_SAFE = 0x400000;
    public static final int DEVICE_OUT_IP = 0x800000;
    public static final int DEVICE_OUT_BUS = 0x1000000;
    public static final int DEVICE_OUT_PROXY = 0x2000000;
    public static final int DEVICE_OUT_USB_HEADSET = 0x4000000;
    public static final int DEVICE_OUT_HEARING_AID = 0x8000000;

    public static final int DEVICE_OUT_DEFAULT = DEVICE_BIT_DEFAULT;

    public static final int DEVICE_OUT_ALL = (DEVICE_OUT_EARPIECE | DEVICE_OUT_SPEAKER | DEVICE_OUT_WIRED_HEADSET
            | DEVICE_OUT_WIRED_HEADPHONE | DEVICE_OUT_BLUETOOTH_SCO | DEVICE_OUT_BLUETOOTH_SCO_HEADSET
            | DEVICE_OUT_BLUETOOTH_SCO_CARKIT | DEVICE_OUT_BLUETOOTH_A2DP | DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES
            | DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER | DEVICE_OUT_HDMI | DEVICE_OUT_ANLG_DOCK_HEADSET
            | DEVICE_OUT_DGTL_DOCK_HEADSET | DEVICE_OUT_USB_ACCESSORY | DEVICE_OUT_USB_DEVICE | DEVICE_OUT_REMOTE_SUBMIX
            | DEVICE_OUT_TELEPHONY_TX | DEVICE_OUT_LINE | DEVICE_OUT_HDMI_ARC | DEVICE_OUT_SPDIF | DEVICE_OUT_FM
            | DEVICE_OUT_AUX_LINE | DEVICE_OUT_SPEAKER_SAFE | DEVICE_OUT_IP | DEVICE_OUT_BUS | DEVICE_OUT_PROXY
            | DEVICE_OUT_USB_HEADSET | DEVICE_OUT_HEARING_AID | DEVICE_OUT_DEFAULT);
    public static final int DEVICE_OUT_ALL_A2DP = (DEVICE_OUT_BLUETOOTH_A2DP | DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES
            | DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER);
    public static final int DEVICE_OUT_ALL_SCO = (DEVICE_OUT_BLUETOOTH_SCO | DEVICE_OUT_BLUETOOTH_SCO_HEADSET
            | DEVICE_OUT_BLUETOOTH_SCO_CARKIT);
    public static final int DEVICE_OUT_ALL_USB = (DEVICE_OUT_USB_ACCESSORY | DEVICE_OUT_USB_DEVICE
            | DEVICE_OUT_USB_HEADSET);
    public static final int DEVICE_OUT_ALL_HDMI_SYSTEM_AUDIO = (DEVICE_OUT_AUX_LINE | DEVICE_OUT_HDMI_ARC
            | DEVICE_OUT_SPDIF);
    public static final int DEVICE_ALL_HDMI_SYSTEM_AUDIO_AND_SPEAKER = (DEVICE_OUT_ALL_HDMI_SYSTEM_AUDIO
            | DEVICE_OUT_SPEAKER);

    public static final int DEVICE_IN_COMMUNICATION = DEVICE_BIT_IN | 0x1;
    public static final int DEVICE_IN_AMBIENT = DEVICE_BIT_IN | 0x2;
    public static final int DEVICE_IN_BUILTIN_MIC = DEVICE_BIT_IN | 0x4;
    public static final int DEVICE_IN_BLUETOOTH_SCO_HEADSET = DEVICE_BIT_IN | 0x8;
    public static final int DEVICE_IN_WIRED_HEADSET = DEVICE_BIT_IN | 0x10;
    public static final int DEVICE_IN_AUX_DIGITAL = DEVICE_BIT_IN | 0x20;
    public static final int DEVICE_IN_HDMI = DEVICE_IN_AUX_DIGITAL;
    public static final int DEVICE_IN_VOICE_CALL = DEVICE_BIT_IN | 0x40;
    public static final int DEVICE_IN_TELEPHONY_RX = DEVICE_IN_VOICE_CALL;
    public static final int DEVICE_IN_BACK_MIC = DEVICE_BIT_IN | 0x80;
    public static final int DEVICE_IN_REMOTE_SUBMIX = DEVICE_BIT_IN | 0x100;
    public static final int DEVICE_IN_ANLG_DOCK_HEADSET = DEVICE_BIT_IN | 0x200;
    public static final int DEVICE_IN_DGTL_DOCK_HEADSET = DEVICE_BIT_IN | 0x400;
    public static final int DEVICE_IN_USB_ACCESSORY = DEVICE_BIT_IN | 0x800;
    public static final int DEVICE_IN_USB_DEVICE = DEVICE_BIT_IN | 0x1000;
    public static final int DEVICE_IN_FM_TUNER = DEVICE_BIT_IN | 0x2000;
    public static final int DEVICE_IN_TV_TUNER = DEVICE_BIT_IN | 0x4000;
    public static final int DEVICE_IN_LINE = DEVICE_BIT_IN | 0x8000;
    public static final int DEVICE_IN_SPDIF = DEVICE_BIT_IN | 0x10000;
    public static final int DEVICE_IN_BLUETOOTH_A2DP = DEVICE_BIT_IN | 0x20000;
    public static final int DEVICE_IN_LOOPBACK = DEVICE_BIT_IN | 0x40000;
    public static final int DEVICE_IN_IP = DEVICE_BIT_IN | 0x80000;
    public static final int DEVICE_IN_BUS = DEVICE_BIT_IN | 0x100000;
    public static final int DEVICE_IN_PROXY = DEVICE_BIT_IN | 0x1000000;
    public static final int DEVICE_IN_USB_HEADSET = DEVICE_BIT_IN | 0x2000000;
    public static final int DEVICE_IN_DEFAULT = DEVICE_BIT_IN | DEVICE_BIT_DEFAULT;

    public static final int DEVICE_IN_ALL = (DEVICE_IN_COMMUNICATION | DEVICE_IN_AMBIENT | DEVICE_IN_BUILTIN_MIC
            | DEVICE_IN_BLUETOOTH_SCO_HEADSET | DEVICE_IN_WIRED_HEADSET | DEVICE_IN_HDMI | DEVICE_IN_TELEPHONY_RX
            | DEVICE_IN_BACK_MIC | DEVICE_IN_REMOTE_SUBMIX | DEVICE_IN_ANLG_DOCK_HEADSET | DEVICE_IN_DGTL_DOCK_HEADSET
            | DEVICE_IN_USB_ACCESSORY | DEVICE_IN_USB_DEVICE | DEVICE_IN_FM_TUNER | DEVICE_IN_TV_TUNER | DEVICE_IN_LINE
            | DEVICE_IN_SPDIF | DEVICE_IN_BLUETOOTH_A2DP | DEVICE_IN_LOOPBACK | DEVICE_IN_IP | DEVICE_IN_BUS
            | DEVICE_IN_PROXY | DEVICE_IN_USB_HEADSET | DEVICE_IN_DEFAULT);
    public static final int DEVICE_IN_ALL_SCO = DEVICE_IN_BLUETOOTH_SCO_HEADSET;
    public static final int DEVICE_IN_ALL_USB = (DEVICE_IN_USB_ACCESSORY | DEVICE_IN_USB_DEVICE
            | DEVICE_IN_USB_HEADSET);

    public static final int DEVICE_STATE_UNAVAILABLE = 0;
    public static final int DEVICE_STATE_AVAILABLE = 1;
    private static final int NUM_DEVICE_STATES = 1;

    public static String deviceStateToString(int state) {
        switch (state) {
        case DEVICE_STATE_UNAVAILABLE:
            return "DEVICE_STATE_UNAVAILABLE";
        case DEVICE_STATE_AVAILABLE:
            return "DEVICE_STATE_AVAILABLE";
        default:
            return "unknown state (" + state + ")";
        }
    }

    public static final String DEVICE_OUT_EARPIECE_NAME = "earpiece";
    public static final String DEVICE_OUT_SPEAKER_NAME = "speaker";
    public static final String DEVICE_OUT_WIRED_HEADSET_NAME = "headset";
    public static final String DEVICE_OUT_WIRED_HEADPHONE_NAME = "headphone";
    public static final String DEVICE_OUT_BLUETOOTH_SCO_NAME = "bt_sco";
    public static final String DEVICE_OUT_BLUETOOTH_SCO_HEADSET_NAME = "bt_sco_hs";
    public static final String DEVICE_OUT_BLUETOOTH_SCO_CARKIT_NAME = "bt_sco_carkit";
    public static final String DEVICE_OUT_BLUETOOTH_A2DP_NAME = "bt_a2dp";
    public static final String DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES_NAME = "bt_a2dp_hp";
    public static final String DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER_NAME = "bt_a2dp_spk";
    public static final String DEVICE_OUT_AUX_DIGITAL_NAME = "aux_digital";
    public static final String DEVICE_OUT_HDMI_NAME = "hdmi";
    public static final String DEVICE_OUT_ANLG_DOCK_HEADSET_NAME = "analog_dock";
    public static final String DEVICE_OUT_DGTL_DOCK_HEADSET_NAME = "digital_dock";
    public static final String DEVICE_OUT_USB_ACCESSORY_NAME = "usb_accessory";
    public static final String DEVICE_OUT_USB_DEVICE_NAME = "usb_device";
    public static final String DEVICE_OUT_REMOTE_SUBMIX_NAME = "remote_submix";
    public static final String DEVICE_OUT_TELEPHONY_TX_NAME = "telephony_tx";
    public static final String DEVICE_OUT_LINE_NAME = "line";
    public static final String DEVICE_OUT_HDMI_ARC_NAME = "hmdi_arc";
    public static final String DEVICE_OUT_SPDIF_NAME = "spdif";
    public static final String DEVICE_OUT_FM_NAME = "fm_transmitter";
    public static final String DEVICE_OUT_AUX_LINE_NAME = "aux_line";
    public static final String DEVICE_OUT_SPEAKER_SAFE_NAME = "speaker_safe";
    public static final String DEVICE_OUT_IP_NAME = "ip";
    public static final String DEVICE_OUT_BUS_NAME = "bus";
    public static final String DEVICE_OUT_PROXY_NAME = "proxy";
    public static final String DEVICE_OUT_USB_HEADSET_NAME = "usb_headset";
    public static final String DEVICE_OUT_HEARING_AID_NAME = "hearing_aid_out";

    public static final String DEVICE_IN_COMMUNICATION_NAME = "communication";
    public static final String DEVICE_IN_AMBIENT_NAME = "ambient";
    public static final String DEVICE_IN_BUILTIN_MIC_NAME = "mic";
    public static final String DEVICE_IN_BLUETOOTH_SCO_HEADSET_NAME = "bt_sco_hs";
    public static final String DEVICE_IN_WIRED_HEADSET_NAME = "headset";
    public static final String DEVICE_IN_AUX_DIGITAL_NAME = "aux_digital";
    public static final String DEVICE_IN_TELEPHONY_RX_NAME = "telephony_rx";
    public static final String DEVICE_IN_BACK_MIC_NAME = "back_mic";
    public static final String DEVICE_IN_REMOTE_SUBMIX_NAME = "remote_submix";
    public static final String DEVICE_IN_ANLG_DOCK_HEADSET_NAME = "analog_dock";
    public static final String DEVICE_IN_DGTL_DOCK_HEADSET_NAME = "digital_dock";
    public static final String DEVICE_IN_USB_ACCESSORY_NAME = "usb_accessory";
    public static final String DEVICE_IN_USB_DEVICE_NAME = "usb_device";
    public static final String DEVICE_IN_FM_TUNER_NAME = "fm_tuner";
    public static final String DEVICE_IN_TV_TUNER_NAME = "tv_tuner";
    public static final String DEVICE_IN_LINE_NAME = "line";
    public static final String DEVICE_IN_SPDIF_NAME = "spdif";
    public static final String DEVICE_IN_BLUETOOTH_A2DP_NAME = "bt_a2dp";
    public static final String DEVICE_IN_LOOPBACK_NAME = "loopback";
    public static final String DEVICE_IN_IP_NAME = "ip";
    public static final String DEVICE_IN_BUS_NAME = "bus";
    public static final String DEVICE_IN_PROXY_NAME = "proxy";
    public static final String DEVICE_IN_USB_HEADSET_NAME = "usb_headset";

    public static String getOutputDeviceName(int device) {
        switch (device) {
        case DEVICE_OUT_EARPIECE:
            return DEVICE_OUT_EARPIECE_NAME;
        case DEVICE_OUT_SPEAKER:
            return DEVICE_OUT_SPEAKER_NAME;
        case DEVICE_OUT_WIRED_HEADSET:
            return DEVICE_OUT_WIRED_HEADSET_NAME;
        case DEVICE_OUT_WIRED_HEADPHONE:
            return DEVICE_OUT_WIRED_HEADPHONE_NAME;
        case DEVICE_OUT_BLUETOOTH_SCO:
            return DEVICE_OUT_BLUETOOTH_SCO_NAME;
        case DEVICE_OUT_BLUETOOTH_SCO_HEADSET:
            return DEVICE_OUT_BLUETOOTH_SCO_HEADSET_NAME;
        case DEVICE_OUT_BLUETOOTH_SCO_CARKIT:
            return DEVICE_OUT_BLUETOOTH_SCO_CARKIT_NAME;
        case DEVICE_OUT_BLUETOOTH_A2DP:
            return DEVICE_OUT_BLUETOOTH_A2DP_NAME;
        case DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES:
            return DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES_NAME;
        case DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER:
            return DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER_NAME;
        case DEVICE_OUT_HDMI:
            return DEVICE_OUT_HDMI_NAME;
        case DEVICE_OUT_ANLG_DOCK_HEADSET:
            return DEVICE_OUT_ANLG_DOCK_HEADSET_NAME;
        case DEVICE_OUT_DGTL_DOCK_HEADSET:
            return DEVICE_OUT_DGTL_DOCK_HEADSET_NAME;
        case DEVICE_OUT_USB_ACCESSORY:
            return DEVICE_OUT_USB_ACCESSORY_NAME;
        case DEVICE_OUT_USB_DEVICE:
            return DEVICE_OUT_USB_DEVICE_NAME;
        case DEVICE_OUT_REMOTE_SUBMIX:
            return DEVICE_OUT_REMOTE_SUBMIX_NAME;
        case DEVICE_OUT_TELEPHONY_TX:
            return DEVICE_OUT_TELEPHONY_TX_NAME;
        case DEVICE_OUT_LINE:
            return DEVICE_OUT_LINE_NAME;
        case DEVICE_OUT_HDMI_ARC:
            return DEVICE_OUT_HDMI_ARC_NAME;
        case DEVICE_OUT_SPDIF:
            return DEVICE_OUT_SPDIF_NAME;
        case DEVICE_OUT_FM:
            return DEVICE_OUT_FM_NAME;
        case DEVICE_OUT_AUX_LINE:
            return DEVICE_OUT_AUX_LINE_NAME;
        case DEVICE_OUT_SPEAKER_SAFE:
            return DEVICE_OUT_SPEAKER_SAFE_NAME;
        case DEVICE_OUT_IP:
            return DEVICE_OUT_IP_NAME;
        case DEVICE_OUT_BUS:
            return DEVICE_OUT_BUS_NAME;
        case DEVICE_OUT_PROXY:
            return DEVICE_OUT_PROXY_NAME;
        case DEVICE_OUT_USB_HEADSET:
            return DEVICE_OUT_USB_HEADSET_NAME;
        case DEVICE_OUT_HEARING_AID:
            return DEVICE_OUT_HEARING_AID_NAME;
        case DEVICE_OUT_DEFAULT:
        default:
            return Integer.toString(device);
        }
    }

    public static String getInputDeviceName(int device) {
        switch (device) {
        case DEVICE_IN_COMMUNICATION:
            return DEVICE_IN_COMMUNICATION_NAME;
        case DEVICE_IN_AMBIENT:
            return DEVICE_IN_AMBIENT_NAME;
        case DEVICE_IN_BUILTIN_MIC:
            return DEVICE_IN_BUILTIN_MIC_NAME;
        case DEVICE_IN_BLUETOOTH_SCO_HEADSET:
            return DEVICE_IN_BLUETOOTH_SCO_HEADSET_NAME;
        case DEVICE_IN_WIRED_HEADSET:
            return DEVICE_IN_WIRED_HEADSET_NAME;
        case DEVICE_IN_AUX_DIGITAL:
            return DEVICE_IN_AUX_DIGITAL_NAME;
        case DEVICE_IN_TELEPHONY_RX:
            return DEVICE_IN_TELEPHONY_RX_NAME;
        case DEVICE_IN_BACK_MIC:
            return DEVICE_IN_BACK_MIC_NAME;
        case DEVICE_IN_REMOTE_SUBMIX:
            return DEVICE_IN_REMOTE_SUBMIX_NAME;
        case DEVICE_IN_ANLG_DOCK_HEADSET:
            return DEVICE_IN_ANLG_DOCK_HEADSET_NAME;
        case DEVICE_IN_DGTL_DOCK_HEADSET:
            return DEVICE_IN_DGTL_DOCK_HEADSET_NAME;
        case DEVICE_IN_USB_ACCESSORY:
            return DEVICE_IN_USB_ACCESSORY_NAME;
        case DEVICE_IN_USB_DEVICE:
            return DEVICE_IN_USB_DEVICE_NAME;
        case DEVICE_IN_FM_TUNER:
            return DEVICE_IN_FM_TUNER_NAME;
        case DEVICE_IN_TV_TUNER:
            return DEVICE_IN_TV_TUNER_NAME;
        case DEVICE_IN_LINE:
            return DEVICE_IN_LINE_NAME;
        case DEVICE_IN_SPDIF:
            return DEVICE_IN_SPDIF_NAME;
        case DEVICE_IN_BLUETOOTH_A2DP:
            return DEVICE_IN_BLUETOOTH_A2DP_NAME;
        case DEVICE_IN_LOOPBACK:
            return DEVICE_IN_LOOPBACK_NAME;
        case DEVICE_IN_IP:
            return DEVICE_IN_IP_NAME;
        case DEVICE_IN_BUS:
            return DEVICE_IN_BUS_NAME;
        case DEVICE_IN_PROXY:
            return DEVICE_IN_PROXY_NAME;
        case DEVICE_IN_USB_HEADSET:
            return DEVICE_IN_USB_HEADSET_NAME;
        case DEVICE_IN_DEFAULT:
        default:
            return Integer.toString(device);
        }
    }

    public static final int PHONE_STATE_OFFCALL = 0;
    public static final int PHONE_STATE_RINGING = 1;
    public static final int PHONE_STATE_INCALL = 2;

    public static final int FORCE_NONE = 0;
    public static final int FORCE_SPEAKER = 1;
    public static final int FORCE_HEADPHONES = 2;
    public static final int FORCE_BT_SCO = 3;
    public static final int FORCE_BT_A2DP = 4;
    public static final int FORCE_WIRED_ACCESSORY = 5;
    public static final int FORCE_BT_CAR_DOCK = 6;
    public static final int FORCE_BT_DESK_DOCK = 7;
    public static final int FORCE_ANALOG_DOCK = 8;
    public static final int FORCE_DIGITAL_DOCK = 9;
    public static final int FORCE_NO_BT_A2DP = 10;
    public static final int FORCE_SYSTEM_ENFORCED = 11;
    public static final int FORCE_HDMI_SYSTEM_AUDIO_ENFORCED = 12;
    public static final int FORCE_ENCODED_SURROUND_NEVER = 13;
    public static final int FORCE_ENCODED_SURROUND_ALWAYS = 14;
    public static final int FORCE_ENCODED_SURROUND_MANUAL = 15;
    public static final int NUM_FORCE_CONFIG = 16;
    public static final int FORCE_DEFAULT = FORCE_NONE;

    public static String forceUseConfigToString(int config) {
        switch (config) {
        case FORCE_NONE:
            return "FORCE_NONE";
        case FORCE_SPEAKER:
            return "FORCE_SPEAKER";
        case FORCE_HEADPHONES:
            return "FORCE_HEADPHONES";
        case FORCE_BT_SCO:
            return "FORCE_BT_SCO";
        case FORCE_BT_A2DP:
            return "FORCE_BT_A2DP";
        case FORCE_WIRED_ACCESSORY:
            return "FORCE_WIRED_ACCESSORY";
        case FORCE_BT_CAR_DOCK:
            return "FORCE_BT_CAR_DOCK";
        case FORCE_BT_DESK_DOCK:
            return "FORCE_BT_DESK_DOCK";
        case FORCE_ANALOG_DOCK:
            return "FORCE_ANALOG_DOCK";
        case FORCE_DIGITAL_DOCK:
            return "FORCE_DIGITAL_DOCK";
        case FORCE_NO_BT_A2DP:
            return "FORCE_NO_BT_A2DP";
        case FORCE_SYSTEM_ENFORCED:
            return "FORCE_SYSTEM_ENFORCED";
        case FORCE_HDMI_SYSTEM_AUDIO_ENFORCED:
            return "FORCE_HDMI_SYSTEM_AUDIO_ENFORCED";
        case FORCE_ENCODED_SURROUND_NEVER:
            return "FORCE_ENCODED_SURROUND_NEVER";
        case FORCE_ENCODED_SURROUND_ALWAYS:
            return "FORCE_ENCODED_SURROUND_ALWAYS";
        case FORCE_ENCODED_SURROUND_MANUAL:
            return "FORCE_ENCODED_SURROUND_MANUAL";
        default:
            return "unknown config (" + config + ")";
        }
    }

    public static final int FOR_COMMUNICATION = 0;
    public static final int FOR_MEDIA = 1;
    public static final int FOR_RECORD = 2;
    public static final int FOR_DOCK = 3;
    public static final int FOR_SYSTEM = 4;
    public static final int FOR_HDMI_SYSTEM_AUDIO = 5;
    public static final int FOR_ENCODED_SURROUND = 6;
    public static final int FOR_VIBRATE_RINGING = 7;
    private static final int NUM_FORCE_USE = 8;

    public static String forceUseUsageToString(int usage) {
        switch (usage) {
        case FOR_COMMUNICATION:
            return "FOR_COMMUNICATION";
        case FOR_MEDIA:
            return "FOR_MEDIA";
        case FOR_RECORD:
            return "FOR_RECORD";
        case FOR_DOCK:
            return "FOR_DOCK";
        case FOR_SYSTEM:
            return "FOR_SYSTEM";
        case FOR_HDMI_SYSTEM_AUDIO:
            return "FOR_HDMI_SYSTEM_AUDIO";
        case FOR_ENCODED_SURROUND:
            return "FOR_ENCODED_SURROUND";
        case FOR_VIBRATE_RINGING:
            return "FOR_VIBRATE_RINGING";
        default:
            return "unknown usage (" + usage + ")";
        }
    }

    public static final int SYNC_EVENT_NONE = 0;
    public static final int SYNC_EVENT_PRESENTATION_COMPLETE = 1;

    public static native int setDeviceConnectionState(int device, int state, String device_address, String device_name);

    public static native int getDeviceConnectionState(int device, String device_address);

    public static native int handleDeviceConfigChange(int device, String device_address, String device_name);

    public static native int setPhoneState(int state);

    public static native int setForceUse(int usage, int config);

    public static native int getForceUse(int usage);

    public static native int initStreamVolume(int stream, int indexMin, int indexMax);

    public static native int setStreamVolumeIndex(int stream, int index, int device);

    public static native int getStreamVolumeIndex(int stream, int device);

    public static native int setMasterVolume(float value);

    public static native float getMasterVolume();

    public static native int setMasterMute(boolean mute);

    public static native boolean getMasterMute();

    public static native int getDevicesForStream(int stream);

    public static native boolean getMasterMono();

    public static native int setMasterMono(boolean mono);

    public static native int getPrimaryOutputSamplingRate();

    public static native int getPrimaryOutputFrameCount();

    public static native int getOutputLatency(int stream);

    public static native int setLowRamDevice(boolean isLowRamDevice, long totalMemory);

    public static native int checkAudioFlinger();

    private static native final void native_register_dynamic_policy_callback();

    private static native final void native_register_recording_callback();

    public static final int AUDIO_HW_SYNC_INVALID = 0;

    public static native int getAudioHwSyncForSession(int sessionId);

    public static native int systemReady();

    public static native float getStreamVolumeDB(int stream, int index, int device);

    private static native boolean native_is_offload_supported(int encoding, int sampleRate, int channelMask,
            int channelIndexMask);

    public static native int getSurroundFormats(Map<Integer, Boolean> surroundFormats, boolean reported);

    public static native int setSurroundFormatEnabled(int audioFormat, boolean enabled);

    public static final int PLAY_SOUND_DELAY = 300;

    public final static String IN_VOICE_COMM_FOCUS_ID = "AudioFocus_For_Phone_Ring_And_Calls";

    public static int getValueForVibrateSetting(int existingValue, int vibrateType, int vibrateSetting) {

        return existingValue;
    }

    public static String streamToString(int stream) {
        if (stream >= 0 && stream < STREAM_NAMES.length)
            return STREAM_NAMES[stream];
        if (stream == AudioManager.USE_DEFAULT_STREAM_TYPE)
            return "USE_DEFAULT_STREAM_TYPE";
        return "UNKNOWN_STREAM_" + stream;
    }

    public static final int PLATFORM_DEFAULT = 0;
    public static final int PLATFORM_VOICE = 1;
    public static final int PLATFORM_TELEVISION = 2;

    public static int getPlatformType(Context context) {
        return 0;

    }

    public static boolean isSingleVolume(Context context) {

        return getPlatformType(context) == PLATFORM_TELEVISION;
    }

    public static final int DEFAULT_MUTE_STREAMS_AFFECTED = (1 << STREAM_MUSIC) | (1 << STREAM_RING)
            | (1 << STREAM_NOTIFICATION) | (1 << STREAM_SYSTEM) | (1 << STREAM_VOICE_CALL)
            | (1 << STREAM_BLUETOOTH_SCO);

    final static int NATIVE_EVENT_ROUTING_CHANGE = 1000;
}
