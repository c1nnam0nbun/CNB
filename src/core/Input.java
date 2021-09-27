package core;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static final HashMap<Integer, Integer> keyState = new HashMap<>();
    private static final HashMap<Integer, Integer> mouseButtonState = new HashMap<>();

    private static float dy;

    //region Key Constants
    public static final int CNB_KEY_UNKNOWN        = -1;
    public static final int CNB_KEY_SPACE          = 32;
    public static final int CNB_KEY_APOSTROPHE     = 39; /* ' */
    public static final int CNB_KEY_COMMA          = 44; /* , */
    public static final int CNB_KEY_MINUS          = 45; /* - */
    public static final int CNB_KEY_PERIOD         = 46; /* . */
    public static final int CNB_KEY_SLASH          = 47; /* / */
    public static final int CNB_KEY_0              = 48;
    public static final int CNB_KEY_1              = 49;
    public static final int CNB_KEY_2              = 50;
    public static final int CNB_KEY_3              = 51;
    public static final int CNB_KEY_4              = 52;
    public static final int CNB_KEY_5              = 53;
    public static final int CNB_KEY_6              = 54;
    public static final int CNB_KEY_7              = 55;
    public static final int CNB_KEY_8              = 56;
    public static final int CNB_KEY_9              = 57;
    public static final int CNB_KEY_SEMICOLON      = 59; /* ; */
    public static final int CNB_KEY_EQUAL          = 61; /* = */
    public static final int CNB_KEY_A              = 65;
    public static final int CNB_KEY_B              = 66;
    public static final int CNB_KEY_C              = 67;
    public static final int CNB_KEY_D              = 68;
    public static final int CNB_KEY_E              = 69;
    public static final int CNB_KEY_F              = 70;
    public static final int CNB_KEY_G              = 71;
    public static final int CNB_KEY_H              = 72;
    public static final int CNB_KEY_I              = 73;
    public static final int CNB_KEY_J              = 74;
    public static final int CNB_KEY_K              = 75;
    public static final int CNB_KEY_L              = 76;
    public static final int CNB_KEY_M              = 77;
    public static final int CNB_KEY_N              = 78;
    public static final int CNB_KEY_O              = 79;
    public static final int CNB_KEY_P              = 80;
    public static final int CNB_KEY_Q              = 81;
    public static final int CNB_KEY_R              = 82;
    public static final int CNB_KEY_S              = 83;
    public static final int CNB_KEY_T              = 84;
    public static final int CNB_KEY_U              = 85;
    public static final int CNB_KEY_V              = 86;
    public static final int CNB_KEY_W              = 87;
    public static final int CNB_KEY_X              = 88;
    public static final int CNB_KEY_Y              = 89;
    public static final int CNB_KEY_Z              = 90;
    public static final int CNB_KEY_LEFT_BRACKET   = 91; /* [ */
    public static final int CNB_KEY_BACKSLASH      = 92; /* \ */
    public static final int CNB_KEY_RIGHT_BRACKET  = 93; /* ] */
    public static final int CNB_KEY_GRAVE_ACCENT   = 96; /* ` */
    public static final int CNB_KEY_WORLD_1        = 161; /* non-US #1 */
    public static final int CNB_KEY_WORLD_2        = 162; /* non-US #2 */
    public static final int CNB_KEY_ESCAPE         = 256;
    public static final int CNB_KEY_ENTER          = 257;
    public static final int CNB_KEY_TAB            = 258;
    public static final int CNB_KEY_BACKSPACE      = 259;
    public static final int CNB_KEY_INSERT         = 260;
    public static final int CNB_KEY_DELETE         = 261;
    public static final int CNB_KEY_RIGHT          = 262;
    public static final int CNB_KEY_LEFT           = 263;
    public static final int CNB_KEY_DOWN           = 264;
    public static final int CNB_KEY_UP             = 265;
    public static final int CNB_KEY_PAGE_UP        = 266;
    public static final int CNB_KEY_PAGE_DOWN      = 267;
    public static final int CNB_KEY_HOME           = 268;
    public static final int CNB_KEY_END            = 269;
    public static final int CNB_KEY_CAPS_LOCK      = 280;
    public static final int CNB_KEY_SCROLL_LOCK    = 281;
    public static final int CNB_KEY_NUM_LOCK       = 282;
    public static final int CNB_KEY_PRINT_SCREEN   = 283;
    public static final int CNB_KEY_PAUSE          = 284;
    public static final int CNB_KEY_F1             = 290;
    public static final int CNB_KEY_F2             = 291;
    public static final int CNB_KEY_F3             = 292;
    public static final int CNB_KEY_F4             = 293;
    public static final int CNB_KEY_F5             = 294;
    public static final int CNB_KEY_F6             = 295;
    public static final int CNB_KEY_F7             = 296;
    public static final int CNB_KEY_F8             = 297;
    public static final int CNB_KEY_F9             = 298;
    public static final int CNB_KEY_F10            = 299;
    public static final int CNB_KEY_F11            = 300;
    public static final int CNB_KEY_F12            = 301;
    public static final int CNB_KEY_F13            = 302;
    public static final int CNB_KEY_F14            = 303;
    public static final int CNB_KEY_F15            = 304;
    public static final int CNB_KEY_F16            = 305;
    public static final int CNB_KEY_F17            = 306;
    public static final int CNB_KEY_F18            = 307;
    public static final int CNB_KEY_F19            = 308;
    public static final int CNB_KEY_F20            = 309;
    public static final int CNB_KEY_F21            = 310;
    public static final int CNB_KEY_F22            = 311;
    public static final int CNB_KEY_F23            = 312;
    public static final int CNB_KEY_F24            = 313;
    public static final int CNB_KEY_F25            = 314;
    public static final int CNB_KEY_KP_0           = 320;
    public static final int CNB_KEY_KP_1           = 321;
    public static final int CNB_KEY_KP_2           = 322;
    public static final int CNB_KEY_KP_3           = 323;
    public static final int CNB_KEY_KP_4           = 324;
    public static final int CNB_KEY_KP_5           = 325;
    public static final int CNB_KEY_KP_6           = 326;
    public static final int CNB_KEY_KP_7           = 327;
    public static final int CNB_KEY_KP_8           = 328;
    public static final int CNB_KEY_KP_9           = 329;
    public static final int CNB_KEY_KP_DECIMAL     = 330;
    public static final int CNB_KEY_KP_DIVIDE      = 331;
    public static final int CNB_KEY_KP_MULTIPLY    = 332;
    public static final int CNB_KEY_KP_SUBTRACT    = 333;
    public static final int CNB_KEY_KP_ADD         = 334;
    public static final int CNB_KEY_KP_ENTER       = 335;
    public static final int CNB_KEY_KP_EQUAL       = 336;
    public static final int CNB_KEY_LEFT_SHIFT     = 340;
    public static final int CNB_KEY_LEFT_CONTROL   = 341;
    public static final int CNB_KEY_LEFT_ALT       = 342;
    public static final int CNB_KEY_LEFT_SUPER     = 343;
    public static final int CNB_KEY_RIGHT_SHIFT    = 344;
    public static final int CNB_KEY_RIGHT_CONTROL  = 345;
    public static final int CNB_KEY_RIGHT_ALT      = 346;
    public static final int CNB_KEY_RIGHT_SUPER    = 347;
    public static final int CNB_KEY_MENU           = 348;
    public static final int CNB_KEY_LAST           = CNB_KEY_MENU;

    private static int[] keyCodes = {-1, 32, 39, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 61, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 96, 161, 162, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 280, 281, 282, 283, 284, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 340, 341, 342, 343, 344, 345, 346, 347, 348};
    //endregion

    public static void init() {
        for (int key : keyCodes) {
            keyState.put(key, 3);
        }
        for (int i = 0; i <= 10; i++) {
            mouseButtonState.put(i, 2);
        }
    }

    public static void onKeyboardInputReceived(long window, int key, int scancode, int action, int mods) {
        keyState.put(key, action);
    }

    public static boolean getKeyDown(int key) {
        return keyState.get(key) == GLFW_PRESS || keyState.get(key) == GLFW_REPEAT;
    }

    public static boolean getKeyCombinationDown(int... keys) {
        boolean result = true;
        for (int key : keys) result &= getKeyDown(key);
        return result;
    }

    public static boolean getKeyUp(int key) {
        boolean state = keyState.get(key) == GLFW_RELEASE;
        if (state) keyState.put(key, 3);
        return state;
    }

    public static void onMouseInputReceived(long window, int button, int action, int mods) {
        mouseButtonState.put(button, action);
    }

    public static boolean getMouseButtonDown(int button) {
        return mouseButtonState.get(button) == GLFW_PRESS;
    }

    public static boolean getMouseButtonUp(int button) {
        boolean state = mouseButtonState.get(button) == GLFW_RELEASE;
        if (mouseButtonState.get(button) == GLFW_RELEASE) mouseButtonState.put(button, 3);
        return state;
    }

    public static void onMouseWheelScrollReceived(long window, double dx, double dy) {
        Input.dy = (float) dy;
    }

    public static float getMouseWheelDelta() {
        float delta = dy;
        dy = 0;
        return delta;
    }
}
