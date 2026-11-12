package components;

import Unity.Direction;
import Unity.GameObject;
import Unity.Transform;
import Util.AssetPool;
import physics2d.enums.BodyType;
import org.joml.Vector2f;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces GSON: reads the engine's own level.txt format with the browser JSON parser and builds the
 * same GameObjects and Components through an explicit type registry (no reflection).
 */
public class LevelLoader {
    @JSBody(params = {"s"}, script = "return JSON.parse(s);") private static native JSObject parse(String s);
    @JSBody(params = {"o", "k"}, script = "return o[k] !== undefined && o[k] !== null;") private static native boolean has(JSObject o, String k);
    @JSBody(params = {"o", "k"}, script = "return o[k];") private static native JSObject get(JSObject o, String k);
    @JSBody(params = {"o", "k"}, script = "return o[k];") private static native float num(JSObject o, String k);
    @JSBody(params = {"o", "k"}, script = "return o[k];") private static native int integer(JSObject o, String k);
    @JSBody(params = {"o", "k"}, script = "return o[k];") private static native boolean bool(JSObject o, String k);
    @JSBody(params = {"o", "k"}, script = "return String(o[k]);") private static native String str(JSObject o, String k);
    @JSBody(params = {"a"}, script = "return a.length;") private static native int len(JSObject a);
    @JSBody(params = {"a", "i"}, script = "return a[i];") private static native JSObject at(JSObject a, int i);

    public static List<GameObject> load(String json) {
        List<GameObject> result = new ArrayList<>();
        JSObject objs = parse(json);
        for (int i = 0; i < len(objs); i++) {
            JSObject jo = at(objs, i);
            GameObject go = new GameObject(str(jo, "name"));
            JSObject comps = get(jo, "components");
            for (int c = 0; c < len(comps); c++) {
                JSObject jc = at(comps, c);
                Component comp = build(str(jc, "type"), get(jc, "properties"));
                go.addComponent(comp);
            }
            go.transform = go.getComponent(Transform.class);
            result.add(go);
        }
        return result;
    }

    private static Vector2f vec2(JSObject o, String k) {
        JSObject v = get(o, k);
        return new Vector2f(num(v, "x"), num(v, "y"));
    }

    private static Sprite sprite(JSObject s) {
        Sprite sp = new Sprite();
        sp.setWidth(num(s, "width"));
        sp.setHeight(num(s, "height"));
        if (has(s, "texture")) {
            sp.setTexture(AssetPool.getTexture(str(get(s, "texture"), "filepath")));
        }
        JSObject tc = get(s, "texCoords");
        Vector2f[] coords = new Vector2f[len(tc)];
        for (int i = 0; i < coords.length; i++) {
            JSObject p = at(tc, i);
            coords[i] = new Vector2f(num(p, "x"), num(p, "y"));
        }
        sp.setTexCoords(coords);
        return sp;
    }

    private static Component build(String type, JSObject p) {
        switch (type) {
            case "Unity.Transform": {
                Transform t = new Transform();
                t.position.set(vec2(p, "position"));
                t.scale.set(vec2(p, "scale"));
                t.rotation = num(p, "rotation");
                t.zIndex = integer(p, "zIndex");
                return t;
            }
            case "components.SpriteRenderer": {
                SpriteRenderer r = new SpriteRenderer();
                JSObject c = get(p, "color");
                r.getColor().set(num(c, "x"), num(c, "y"), num(c, "z"), num(c, "w"));
                if (has(p, "sprite")) r.setSprite(sprite(get(p, "sprite")));
                return r;
            }
            case "physics2d.components.Rigidbody2D": {
                Rigidbody2D rb = new Rigidbody2D();
                rb.velocity.set(vec2(p, "velocity"));
                rb.angularDamping = num(p, "angularDamping");
                rb.linearDamping = num(p, "linearDamping");
                rb.mass = num(p, "mass");
                rb.bodyType = BodyType.valueOf(str(p, "bodyType"));
                rb.friction = num(p, "friction");
                rb.angularVelocity = num(p, "angularVelocity");
                rb.gravityScale = num(p, "gravityScale");
                rb.isSensor = bool(p, "isSensor");
                rb.fixedRotation = bool(p, "fixedRotation");
                rb.continuousCollision = bool(p, "continuousCollision");
                return rb;
            }
            case "physics2d.components.Box2DCollider": {
                Box2DCollider b = new Box2DCollider();
                b.halfSize.set(vec2(p, "halfSize"));
                b.origin.set(vec2(p, "origin"));
                b.offset.set(vec2(p, "offset"));
                return b;
            }
            case "physics2d.components.CircleCollider": {
                CircleCollider c = new CircleCollider();
                c.radius = num(p, "radius");
                c.offset.set(vec2(p, "offset"));
                return c;
            }
            case "physics2d.components.PillboxCollider": {
                PillboxCollider c = new PillboxCollider();
                c.width = num(p, "width");
                c.height = num(p, "height");
                c.offset.set(vec2(p, "offset"));
                return c;
            }
            case "components.Ground": return new Ground();
            case "components.GoombaAI": return new GoombaAI();
            case "components.TurtleAI": {
                TurtleAI t = new TurtleAI();
                t.movingDebounce = num(p, "movingDebounce");
                return t;
            }
            case "components.BreakableBrick": {
                BreakableBrick b = new BreakableBrick();
                b.bopSpeed = num(p, "bopSpeed");
                return b;
            }
            case "components.QuestionBlock": {
                QuestionBlock q = new QuestionBlock();
                q.blockType = QuestionBlock.BlockType.valueOf(str(p, "blockType"));
                q.bopSpeed = num(p, "bopSpeed");
                return q;
            }
            case "components.Pipe": {
                Pipe pipe = new Pipe(Direction.valueOf(str(p, "direction")));
                pipe.connectingPipeName = str(p, "connectingPipeName");
                pipe.isEntrance = bool(p, "isEntrance");
                return pipe;
            }
            case "components.Coin": {
                Coin c = new Coin();
                c.coinSpeed = num(p, "coinSpeed");
                if (has(p, "topY")) c.topY = vec2(p, "topY");
                return c;
            }
            case "components.Flagpole": {
                Flagpole f = new Flagpole(false);
                f.isTop = bool(p, "isTop");
                return f;
            }
            case "components.PlayerController": {
                PlayerController pc = new PlayerController();
                pc.walkSpeed = num(p, "walkSpeed");
                pc.jumpBoost = num(p, "jumpBoost");
                pc.jumpImpulse = num(p, "jumpImpulse");
                pc.slowDownForce = num(p, "slowDownForce");
                pc.terminalVelocity.set(vec2(p, "terminalVelocity"));
                pc.playerState = PlayerController.PlayerState.valueOf(str(p, "playerState"));
                return pc;
            }
            case "components.StateMachine": return stateMachine(p);
            default:
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
    }

    private static StateMachine stateMachine(JSObject p) {
        StateMachine sm = new StateMachine();
        JSObject states = get(p, "states");
        for (int i = 0; i < len(states); i++) {
            JSObject js = at(states, i);
            AnimationState st = new AnimationState();
            st.title = str(js, "title");
            st.setLoop(bool(js, "doesLoop"));
            JSObject frames = get(js, "animationFrames");
            for (int f = 0; f < len(frames); f++) {
                JSObject jf = at(frames, f);
                st.addFrame(sprite(get(jf, "sprite")), num(jf, "frameTime"));
            }
            sm.addState(st);
        }
        JSObject transfers = get(p, "stateTransfers");
        for (int i = 0; i < len(transfers); i++) {
            JSObject pair = at(transfers, i);
            JSObject key = at(pair, 0);
            sm.addState(str(key, "state"), str(pair, "1"), str(key, "trigger"));
        }
        sm.defaultStateTitle = str(p, "defaultStateTitle");
        return sm;
    }
}
