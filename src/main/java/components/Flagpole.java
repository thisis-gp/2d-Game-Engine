package components;

import Unity.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import Util.AssetPool;

public class Flagpole extends Component {
    private boolean isTop = false;

    public Flagpole(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.playWinAnimation(this.gameObject);
        }
    }
}