package Components;

import Unity.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f color;

    public  SpriteRenderer(Vector4f color){
        this.color = color;
    }
    public void start(){

    }

    @Override
    public void update(float dt){

    }

    public Vector4f getColor(){
        return this.color;
    }
}
