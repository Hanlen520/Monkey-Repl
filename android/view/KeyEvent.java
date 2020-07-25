
package android.view;

import android.os.Parcel;

public class KeyEvent extends InputEvent {
    public static final int KEYCODE_UNKNOWN = 0;
    public static final int KEYCODE_SOFT_LEFT = 1;
    public static final int KEYCODE_SOFT_RIGHT = 2;
    public static final int KEYCODE_HOME = 3;
    public static final int KEYCODE_BACK = 4;
    public static final int KEYCODE_CALL = 5;
    public static final int KEYCODE_ENDCALL = 6;
    public static final int KEYCODE_0 = 7;
    public static final int KEYCODE_1 = 8;
    public static final int KEYCODE_2 = 9;
    public static final int KEYCODE_3 = 10;
    public static final int KEYCODE_4 = 11;
    public static final int KEYCODE_5 = 12;
    public static final int KEYCODE_6 = 13;
    public static final int KEYCODE_7 = 14;
    public static final int KEYCODE_8 = 15;
    public static final int KEYCODE_9 = 16;
    public static final int KEYCODE_STAR = 17;
    public static final int KEYCODE_POUND = 18;
    public static final int KEYCODE_DPAD_UP = 19;
    public static final int KEYCODE_DPAD_DOWN = 20;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22;
    public static final int KEYCODE_DPAD_CENTER = 23;
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_VOLUME_DOWN = 25;
    public static final int KEYCODE_POWER = 26;
    public static final int KEYCODE_CAMERA = 27;
    public static final int KEYCODE_CLEAR = 28;
    public static final int KEYCODE_A = 29;
    public static final int KEYCODE_B = 30;
    public static final int KEYCODE_C = 31;
    public static final int KEYCODE_D = 32;
    public static final int KEYCODE_E = 33;
    public static final int KEYCODE_F = 34;
    public static final int KEYCODE_G = 35;
    public static final int KEYCODE_H = 36;
    public static final int KEYCODE_I = 37;
    public static final int KEYCODE_J = 38;
    public static final int KEYCODE_K = 39;
    public static final int KEYCODE_L = 40;
    public static final int KEYCODE_M = 41;
    public static final int KEYCODE_N = 42;
    public static final int KEYCODE_O = 43;
    public static final int KEYCODE_P = 44;
    public static final int KEYCODE_Q = 45;
    public static final int KEYCODE_R = 46;
    public static final int KEYCODE_S = 47;
    public static final int KEYCODE_T = 48;
    public static final int KEYCODE_U = 49;
    public static final int KEYCODE_V = 50;
    public static final int KEYCODE_W = 51;
    public static final int KEYCODE_X = 52;
    public static final int KEYCODE_Y = 53;
    public static final int KEYCODE_Z = 54;
    public static final int KEYCODE_COMMA = 55;
    public static final int KEYCODE_PERIOD = 56;
    public static final int KEYCODE_ALT_LEFT = 57;
    public static final int KEYCODE_ALT_RIGHT = 58;
    public static final int KEYCODE_SHIFT_LEFT = 59;
    public static final int KEYCODE_SHIFT_RIGHT = 60;
    public static final int KEYCODE_TAB = 61;
    public static final int KEYCODE_SPACE = 62;
    public static final int KEYCODE_SYM = 63;
    public static final int KEYCODE_EXPLORER = 64;
    public static final int KEYCODE_ENVELOPE = 65;
    public static final int KEYCODE_ENTER = 66;
    public static final int KEYCODE_DEL = 67;
    public static final int KEYCODE_GRAVE = 68;
    public static final int KEYCODE_MINUS = 69;
    public static final int KEYCODE_EQUALS = 70;
    public static final int KEYCODE_LEFT_BRACKET = 71;
    public static final int KEYCODE_RIGHT_BRACKET = 72;
    public static final int KEYCODE_BACKSLASH = 73;
    public static final int KEYCODE_SEMICOLON = 74;
    public static final int KEYCODE_APOSTROPHE = 75;
    public static final int KEYCODE_SLASH = 76;
    public static final int KEYCODE_AT = 77;
    public static final int KEYCODE_NUM = 78;
    public static final int KEYCODE_HEADSETHOOK = 79;
    public static final int KEYCODE_PLUS = 81;
    public static final int KEYCODE_MENU = 82;
    public static final int KEYCODE_NOTIFICATION = 83;
    public static final int KEYCODE_SEARCH = 84;
    public static final int KEYCODE_MEDIA_PLAY_PAUSE = 85;
    public static final int KEYCODE_MEDIA_STOP = 86;
    public static final int KEYCODE_MEDIA_NEXT = 87;
    public static final int KEYCODE_MEDIA_PREVIOUS = 88;
    public static final int KEYCODE_MEDIA_REWIND = 89;
    public static final int KEYCODE_MEDIA_FAST_FORWARD = 90;
    public static final int KEYCODE_MUTE = 91;
    public static final int KEYCODE_PAGE_UP = 92;
    public static final int KEYCODE_PAGE_DOWN = 93;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_C = 98;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_BUTTON_Z = 101;
    public static final int KEYCODE_BUTTON_L1 = 102;
    public static final int KEYCODE_BUTTON_R1 = 103;
    public static final int KEYCODE_BUTTON_L2 = 104;
    public static final int KEYCODE_BUTTON_R2 = 105;
    public static final int KEYCODE_BUTTON_THUMBL = 106;
    public static final int KEYCODE_BUTTON_THUMBR = 107;
    public static final int KEYCODE_BUTTON_START = 108;
    public static final int KEYCODE_BUTTON_SELECT = 109;
    public static final int KEYCODE_BUTTON_MODE = 110;
    public static final int KEYCODE_ESCAPE = 111;
    public static final int KEYCODE_FORWARD_DEL = 112;
    public static final int KEYCODE_CTRL_LEFT = 113;
    public static final int KEYCODE_CTRL_RIGHT = 114;
    public static final int KEYCODE_CAPS_LOCK = 115;
    public static final int KEYCODE_SCROLL_LOCK = 116;
    public static final int KEYCODE_META_LEFT = 117;
    public static final int KEYCODE_META_RIGHT = 118;
    public static final int KEYCODE_FUNCTION = 119;
    public static final int KEYCODE_SYSRQ = 120;
    public static final int KEYCODE_BREAK = 121;
    public static final int KEYCODE_MOVE_HOME = 122;
    public static final int KEYCODE_MOVE_END = 123;
    public static final int KEYCODE_INSERT = 124;
    public static final int KEYCODE_FORWARD = 125;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_CLOSE = 128;
    public static final int KEYCODE_MEDIA_EJECT = 129;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    public static final int KEYCODE_F1 = 131;
    public static final int KEYCODE_F2 = 132;
    public static final int KEYCODE_F3 = 133;
    public static final int KEYCODE_F4 = 134;
    public static final int KEYCODE_F5 = 135;
    public static final int KEYCODE_F6 = 136;
    public static final int KEYCODE_F7 = 137;
    public static final int KEYCODE_F8 = 138;
    public static final int KEYCODE_F9 = 139;
    public static final int KEYCODE_F10 = 140;
    public static final int KEYCODE_F11 = 141;
    public static final int KEYCODE_F12 = 142;
    public static final int KEYCODE_NUM_LOCK = 143;
    public static final int KEYCODE_NUMPAD_0 = 144;
    public static final int KEYCODE_NUMPAD_1 = 145;
    public static final int KEYCODE_NUMPAD_2 = 146;
    public static final int KEYCODE_NUMPAD_3 = 147;
    public static final int KEYCODE_NUMPAD_4 = 148;
    public static final int KEYCODE_NUMPAD_5 = 149;
    public static final int KEYCODE_NUMPAD_6 = 150;
    public static final int KEYCODE_NUMPAD_7 = 151;
    public static final int KEYCODE_NUMPAD_8 = 152;
    public static final int KEYCODE_NUMPAD_9 = 153;
    public static final int KEYCODE_NUMPAD_DIVIDE = 154;
    public static final int KEYCODE_NUMPAD_MULTIPLY = 155;
    public static final int KEYCODE_NUMPAD_SUBTRACT = 156;
    public static final int KEYCODE_NUMPAD_ADD = 157;
    public static final int KEYCODE_NUMPAD_DOT = 158;
    public static final int KEYCODE_NUMPAD_COMMA = 159;
    public static final int KEYCODE_NUMPAD_ENTER = 160;
    public static final int KEYCODE_NUMPAD_EQUALS = 161;
    public static final int KEYCODE_NUMPAD_LEFT_PAREN = 162;
    public static final int KEYCODE_NUMPAD_RIGHT_PAREN = 163;
    public static final int KEYCODE_VOLUME_MUTE = 164;
    public static final int KEYCODE_INFO = 165;
    public static final int KEYCODE_CHANNEL_UP = 166;
    public static final int KEYCODE_CHANNEL_DOWN = 167;
    public static final int KEYCODE_ZOOM_IN = 168;
    public static final int KEYCODE_ZOOM_OUT = 169;
    public static final int KEYCODE_TV = 170;
    public static final int KEYCODE_WINDOW = 171;
    public static final int KEYCODE_GUIDE = 172;
    public static final int KEYCODE_DVR = 173;
    public static final int KEYCODE_BOOKMARK = 174;
    public static final int KEYCODE_CAPTIONS = 175;
    public static final int KEYCODE_SETTINGS = 176;
    public static final int KEYCODE_TV_POWER = 177;
    public static final int KEYCODE_TV_INPUT = 178;
    public static final int KEYCODE_STB_POWER = 179;
    public static final int KEYCODE_STB_INPUT = 180;
    public static final int KEYCODE_AVR_POWER = 181;
    public static final int KEYCODE_AVR_INPUT = 182;
    public static final int KEYCODE_PROG_RED = 183;
    public static final int KEYCODE_PROG_GREEN = 184;
    public static final int KEYCODE_PROG_YELLOW = 185;
    public static final int KEYCODE_PROG_BLUE = 186;
    public static final int KEYCODE_APP_SWITCH = 187;
    public static final int KEYCODE_BUTTON_1 = 188;
    public static final int KEYCODE_BUTTON_2 = 189;
    public static final int KEYCODE_BUTTON_3 = 190;
    public static final int KEYCODE_BUTTON_4 = 191;
    public static final int KEYCODE_BUTTON_5 = 192;
    public static final int KEYCODE_BUTTON_6 = 193;
    public static final int KEYCODE_BUTTON_7 = 194;
    public static final int KEYCODE_BUTTON_8 = 195;
    public static final int KEYCODE_BUTTON_9 = 196;
    public static final int KEYCODE_BUTTON_10 = 197;
    public static final int KEYCODE_BUTTON_11 = 198;
    public static final int KEYCODE_BUTTON_12 = 199;
    public static final int KEYCODE_BUTTON_13 = 200;
    public static final int KEYCODE_BUTTON_14 = 201;
    public static final int KEYCODE_BUTTON_15 = 202;
    public static final int KEYCODE_BUTTON_16 = 203;
    public static final int KEYCODE_LANGUAGE_SWITCH = 204;
    public static final int KEYCODE_MANNER_MODE = 205;
    public static final int KEYCODE_3D_MODE = 206;
    public static final int KEYCODE_CONTACTS = 207;
    public static final int KEYCODE_CALENDAR = 208;
    public static final int KEYCODE_MUSIC = 209;
    public static final int KEYCODE_CALCULATOR = 210;
    public static final int KEYCODE_ZENKAKU_HANKAKU = 211;
    public static final int KEYCODE_EISU = 212;
    public static final int KEYCODE_MUHENKAN = 213;
    public static final int KEYCODE_HENKAN = 214;
    public static final int KEYCODE_KATAKANA_HIRAGANA = 215;
    public static final int KEYCODE_YEN = 216;
    public static final int KEYCODE_RO = 217;
    public static final int KEYCODE_KANA = 218;
    public static final int KEYCODE_ASSIST = 219;
    public static final int KEYCODE_BRIGHTNESS_DOWN = 220;
    public static final int KEYCODE_BRIGHTNESS_UP = 221;
    public static final int KEYCODE_MEDIA_AUDIO_TRACK = 222;
    public static final int KEYCODE_SLEEP = 223;
    public static final int KEYCODE_WAKEUP = 224;
    public static final int KEYCODE_PAIRING = 225;
    public static final int KEYCODE_MEDIA_TOP_MENU = 226;
    public static final int KEYCODE_11 = 227;
    public static final int KEYCODE_12 = 228;
    public static final int KEYCODE_LAST_CHANNEL = 229;
    public static final int KEYCODE_TV_DATA_SERVICE = 230;
    public static final int KEYCODE_VOICE_ASSIST = 231;
    public static final int KEYCODE_TV_RADIO_SERVICE = 232;
    public static final int KEYCODE_TV_TELETEXT = 233;
    public static final int KEYCODE_TV_NUMBER_ENTRY = 234;
    public static final int KEYCODE_TV_TERRESTRIAL_ANALOG = 235;
    public static final int KEYCODE_TV_TERRESTRIAL_DIGITAL = 236;
    public static final int KEYCODE_TV_SATELLITE = 237;
    public static final int KEYCODE_TV_SATELLITE_BS = 238;
    public static final int KEYCODE_TV_SATELLITE_CS = 239;
    public static final int KEYCODE_TV_SATELLITE_SERVICE = 240;
    public static final int KEYCODE_TV_NETWORK = 241;
    public static final int KEYCODE_TV_ANTENNA_CABLE = 242;
    public static final int KEYCODE_TV_INPUT_HDMI_1 = 243;
    public static final int KEYCODE_TV_INPUT_HDMI_2 = 244;
    public static final int KEYCODE_TV_INPUT_HDMI_3 = 245;
    public static final int KEYCODE_TV_INPUT_HDMI_4 = 246;
    public static final int KEYCODE_TV_INPUT_COMPOSITE_1 = 247;
    public static final int KEYCODE_TV_INPUT_COMPOSITE_2 = 248;
    public static final int KEYCODE_TV_INPUT_COMPONENT_1 = 249;
    public static final int KEYCODE_TV_INPUT_COMPONENT_2 = 250;
    public static final int KEYCODE_TV_INPUT_VGA_1 = 251;
    public static final int KEYCODE_TV_AUDIO_DESCRIPTION = 252;
    public static final int KEYCODE_TV_AUDIO_DESCRIPTION_MIX_UP = 253;
    public static final int KEYCODE_TV_AUDIO_DESCRIPTION_MIX_DOWN = 254;
    public static final int KEYCODE_TV_ZOOM_MODE = 255;
    public static final int KEYCODE_TV_CONTENTS_MENU = 256;
    public static final int KEYCODE_TV_MEDIA_CONTEXT_MENU = 257;
    public static final int KEYCODE_TV_TIMER_PROGRAMMING = 258;
    public static final int KEYCODE_HELP = 259;
    public static final int KEYCODE_NAVIGATE_PREVIOUS = 260;
    public static final int KEYCODE_NAVIGATE_NEXT = 261;
    public static final int KEYCODE_NAVIGATE_IN = 262;
    public static final int KEYCODE_NAVIGATE_OUT = 263;
    public static final int KEYCODE_STEM_PRIMARY = 264;
    public static final int KEYCODE_STEM_1 = 265;
    public static final int KEYCODE_STEM_2 = 266;
    public static final int KEYCODE_STEM_3 = 267;
    public static final int KEYCODE_DPAD_UP_LEFT = 268;
    public static final int KEYCODE_DPAD_DOWN_LEFT = 269;
    public static final int KEYCODE_DPAD_UP_RIGHT = 270;
    public static final int KEYCODE_DPAD_DOWN_RIGHT = 271;
    public static final int KEYCODE_MEDIA_SKIP_FORWARD = 272;
    public static final int KEYCODE_MEDIA_SKIP_BACKWARD = 273;
    public static final int KEYCODE_MEDIA_STEP_FORWARD = 274;
    public static final int KEYCODE_MEDIA_STEP_BACKWARD = 275;
    public static final int KEYCODE_SOFT_SLEEP = 276;
    public static final int KEYCODE_CUT = 277;
    public static final int KEYCODE_COPY = 278;
    public static final int KEYCODE_PASTE = 279;
    public static final int KEYCODE_SYSTEM_NAVIGATION_UP = 280;
    public static final int KEYCODE_SYSTEM_NAVIGATION_DOWN = 281;
    public static final int KEYCODE_SYSTEM_NAVIGATION_LEFT = 282;
    public static final int KEYCODE_SYSTEM_NAVIGATION_RIGHT = 283;
    public static final int KEYCODE_ALL_APPS = 284;
    public static final int KEYCODE_REFRESH = 285;

    @Deprecated
    public static final int MAX_KEYCODE = 84;

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_MULTIPLE = 2;

    public static final int META_CAP_LOCKED = 0x100;

    public static final int META_ALT_LOCKED = 0x200;

    public static final int META_SYM_LOCKED = 0x400;

    public static final int META_SELECTING = 0x800;

    public static final int META_ALT_ON = 0x02;

    public static final int META_ALT_LEFT_ON = 0x10;

    public static final int META_ALT_RIGHT_ON = 0x20;

    public static final int META_SHIFT_ON = 0x1;

    public static final int META_SHIFT_LEFT_ON = 0x40;

    public static final int META_SHIFT_RIGHT_ON = 0x80;

    public static final int META_SYM_ON = 0x4;

    public static final int META_FUNCTION_ON = 0x8;

    public static final int META_CTRL_ON = 0x1000;

    public static final int META_CTRL_LEFT_ON = 0x2000;

    public static final int META_CTRL_RIGHT_ON = 0x4000;

    public static final int META_META_ON = 0x10000;

    public static final int META_META_LEFT_ON = 0x20000;

    public static final int META_META_RIGHT_ON = 0x40000;

    public static final int META_CAPS_LOCK_ON = 0x100000;

    public static final int META_NUM_LOCK_ON = 0x200000;

    public static final int META_SCROLL_LOCK_ON = 0x400000;

    public static final int META_SHIFT_MASK = META_SHIFT_ON | META_SHIFT_LEFT_ON | META_SHIFT_RIGHT_ON;

    public static final int META_ALT_MASK = META_ALT_ON | META_ALT_LEFT_ON | META_ALT_RIGHT_ON;

    public static final int META_CTRL_MASK = META_CTRL_ON | META_CTRL_LEFT_ON | META_CTRL_RIGHT_ON;

    public static final int META_META_MASK = META_META_ON | META_META_LEFT_ON | META_META_RIGHT_ON;

    @Deprecated
    public static final int FLAG_WOKE_HERE = 0x1;

    public static final int FLAG_SOFT_KEYBOARD = 0x2;

    public static final int FLAG_KEEP_TOUCH_MODE = 0x4;

    public static final int FLAG_FROM_SYSTEM = 0x8;

    public static final int FLAG_EDITOR_ACTION = 0x10;

    public static final int FLAG_CANCELED = 0x20;

    public static final int FLAG_VIRTUAL_HARD_KEY = 0x40;

    public static final int FLAG_LONG_PRESS = 0x80;

    public static final int FLAG_CANCELED_LONG_PRESS = 0x100;

    public static final int FLAG_TRACKING = 0x200;

    public static final int FLAG_FALLBACK = 0x400;

    public static final int FLAG_PREDISPATCH = 0x20000000;

    public static final int FLAG_START_TRACKING = 0x40000000;

    public static final int FLAG_TAINTED = 0x80000000;

    public static int getMaxKeyCode() {
        throw new RuntimeException("Stub!");
    }

    static final boolean DEBUG = false;
    static final String TAG = "KeyEvent";

    public interface Callback {
        boolean onKeyDown(int keyCode, KeyEvent event);

        boolean onKeyLongPress(int keyCode, KeyEvent event);

        boolean onKeyUp(int keyCode, KeyEvent event);

        boolean onKeyMultiple(int keyCode, int count, KeyEvent event);
    }

    public KeyEvent(long downTime, long eventTime, int mAction2, int mKeyCode2, int mRepeatCount2, int mMetaState2,
            int mDeviceId2, int mScanCode2, int flagFromSystem, int sourceKeyboard) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getDeviceId() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getSource() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setSource(int source) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public InputEvent copy() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isTainted() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setTainted(boolean tainted) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public long getEventTime() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public long getEventTimeNano() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public static String keyCodeToString(int keycode) {
        throw new RuntimeException("Stub!");
    }

    public static int keyCodeFromString(String keyName) {
        throw new RuntimeException("Stub!");
    }

    public int getKeyCode() {
        throw new RuntimeException("Stub!");
    }

    public int getAction() {
        throw new RuntimeException("Stub!");
    }

    public long getDownTime() {
        throw new RuntimeException("Stub!");
    }

}
