package components;

import Unity.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import Util.AssetPool;

public class Coin extends Component {
    public Vector2f topY;
    public float coinSpeed = 1.4f;
    public transient boolean playAnim = false;

    @Override
    public void start() {
        topY = new Vector2f(this.gameObject.transform.position.y).add(0, 0.5f);
    }

    @Override
    public void update(float dt) {
        if (playAnim) {
            if (this.gameObject.transform.position.y < topY.y) {
                this.gameObject.transform.position.y += dt * coinSpeed;
                this.gameObject.transform.scale.x -= (0.5f * dt) % -1.0f;
            } else {
                gameObject.destroy();
            }
        }
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (obj.getComponent(PlayerController.class) != null) {
            AssetPool.getSound("assets/sounds/coin.ogg").play();
            playAnim = true;
            contact.setEnabled(false);
        }
    }
}
