package org.lwjgl.glfw;

/** Key and action codes only; the browser has no GLFW. Values match GLFW so KeyListener is unchanged. */
public final class GLFW {
    public static final int GLFW_RELEASE = 0, GLFW_PRESS = 1;
    public static final int GLFW_KEY_SPACE = 32, GLFW_KEY_A = 65, GLFW_KEY_D = 68, GLFW_KEY_E = 69,
            GLFW_KEY_S = 83, GLFW_KEY_W = 87, GLFW_KEY_RIGHT = 262, GLFW_KEY_LEFT = 263,
            GLFW_KEY_DOWN = 264, GLFW_KEY_UP = 265;
    private GLFW() { }
}
