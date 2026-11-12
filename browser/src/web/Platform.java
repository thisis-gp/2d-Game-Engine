package web;

import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.Int32Array;

/** Browser services the desktop engine got from GLFW, STB, OpenAL and java.nio.file. All data is preloaded by engine-loader.js. */
public final class Platform {
    private Platform() { }

    @JSBody(params = {"p"}, script = "return window.__assets.text[p];") public static native String readText(String p);
    @JSBody(params = {"p"}, script = "return window.__assets.img[p].naturalWidth;") public static native int imageWidth(String p);
    @JSBody(params = {"p"}, script = "return window.__assets.img[p].naturalHeight;") public static native int imageHeight(String p);

    /** Equivalent of stbi_set_flip_vertically_on_load(true) + glTexImage2D(RGBA) for the bound texture. */
    @JSBody(params = {"p"}, script =
            "var gl = window.__gl; gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);"
          + "gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, window.__assets.img[p]);"
          + "(window.__texByPath = window.__texByPath || {})[p] = gl.getParameter(gl.TEXTURE_BINDING_2D);"
          + "gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, false);")
    public static native void texImageFromAsset(String p);

    @JSBody(params = {"p", "loops"}, script = "window.__sound.register(p, loops);") public static native void soundRegister(String p, boolean loops);
    @JSBody(params = {"p"}, script = "window.__sound.play(p);") public static native void soundPlay(String p);
    @JSBody(params = {"p"}, script = "window.__sound.stop(p);") public static native void soundStop(String p);
    @JSBody(params = {"p"}, script = "return window.__sound.isPlaying(p);") public static native boolean soundIsPlaying(String p);

    /** Fits the canvas to the page, clears it black, then sets a centred 16:9 viewport and clears it to the scene colour. */
    @JSBody(params = {"r", "g", "b", "a"}, script = "window.__beginFrame(r, g, b, a);")
    public static native void beginFrame(float r, float g, float b, float a);

    @JSBody(script = "return performance.now() / 1000;") public static native double nowSeconds();

    /** Pending key events as [key, action, key, action...]; drained each frame. */
    @JSBody(script = "var q = window.__keyq; window.__keyq = []; return Int32Array.from(q);") public static native Int32Array drainKeyEvents();
}
