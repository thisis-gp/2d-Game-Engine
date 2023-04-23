package Unity;


import Components.Sprite;
import Components.SpriteRenderer;
import Components.Spritesheet;
import Util.AssetPool;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/Images/spritesheet.png");


        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)),2);
        obj1.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)),1);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/Images/blendImage2.png")
        )));
        this.addGameObjectToScene(obj2);



    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/Images/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),16,16,26,0));
    }



    @Override
    public void update(float dt) {


        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Test Window");
        ImGui.text("Some random text");
        ImGui.end();
    }
}