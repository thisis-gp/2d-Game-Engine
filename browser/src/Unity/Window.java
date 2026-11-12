package Unity;

import Scenes.LevelSceneInitializer;
import Scenes.Scene;
import Scenes.SceneInitializer;
import Util.AssetPool;
import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import org.joml.Vector4f;
import physics2d.Physics2D;
import renderer.Renderer;
import renderer.Shader;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.typedarrays.Int32Array;
import web.Platform;

import static org.lwjgl.opengl.GL11.*;

/**
 * Play-mode only, browser version of the desktop Window: no GLFW, no ImGui, no picking pass and no editor.
 * Starts straight in LevelSceneInitializer and is driven by requestAnimationFrame instead of a blocking loop.
 */
public class Window implements Observer {
    private static final float MAX_FRAME_SECONDS = 0.05f;

    private int width, height;
    private String title;
    private Shader defaultShader;
    private double lastFrameSeconds;

    private static Window window = null;
    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Unity";
        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer) {
        if (currentScene != null) {
            currentScene.destroy();
        }

        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Physics2D getPhysics() { return currentScene.getPhysics(); }

    public static Scene getScene() {
        return currentScene;
    }

    /** Called once from the JS entry point after assets are preloaded. */
    public void run() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Window.changeScene(new LevelSceneInitializer());

        lastFrameSeconds = Platform.nowSeconds();
        org.teavm.jso.browser.Window.requestAnimationFrame(frameCallback);
    }

    private final AnimationFrameCallback frameCallback = new AnimationFrameCallback() {
        @Override
        public void onAnimationFrame(double timestamp) {
            frame();
            org.teavm.jso.browser.Window.requestAnimationFrame(this);
        }
    };

    private void frame() {
        double now = Platform.nowSeconds();
        float dt = Math.min((float) (now - lastFrameSeconds), MAX_FRAME_SECONDS);
        lastFrameSeconds = now;

        Int32Array keys = Platform.drainKeyEvents();
        for (int i = 0; i + 1 < keys.getLength(); i += 2) {
            KeyListener.keyCallback(0, keys.get(i), 0, keys.get(i + 1), 0);
        }

        Vector4f clearColor = currentScene.camera().clearColor;
        Platform.beginFrame(clearColor.x, clearColor.y, clearColor.z, clearColor.w);

        Renderer.bindShader(defaultShader);
        currentScene.update(dt);
        currentScene.render();

        KeyListener.endFrame();
    }

    public static int getWidth() {
        return 3840;
    }

    public static int getHeight() {
        return 2160;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
            case LoadLevel:
                Window.changeScene(new LevelSceneInitializer());
                break;
            default:
                break;
        }
    }
}
