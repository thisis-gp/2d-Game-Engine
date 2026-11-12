package renderer;

import org.joml.Vector2f;

/** Collider outlines are an editor aid; play mode has no debug line pass, so these are no-ops. */
public class DebugDraw {
    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) { }

    public static void addCircle(Vector2f center, float radius) { }
}
