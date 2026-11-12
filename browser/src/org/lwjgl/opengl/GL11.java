package org.lwjgl.opengl;

import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int32Array;

/**
 * WebGL2 backed stand-in for the LWJGL GL entry points the engine calls.
 * GL13/GL15/GL20/GL30 extend this class, so static imports of any of them resolve here.
 * Handles (programs, shaders, buffers, VAOs, textures, uniform locations) are ints that
 * index the JS array window.__H.
 */
public class GL11 {
    public static final int GL_FALSE = 0, GL_TRUE = 1;
    public static final int GL_DEPTH_BUFFER_BIT = 0x100, GL_COLOR_BUFFER_BIT = 0x4000;
    public static final int GL_TRIANGLES = 4, GL_LINES = 1;
    public static final int GL_UNSIGNED_INT = 0x1405, GL_FLOAT = 0x1406, GL_UNSIGNED_BYTE = 0x1401;
    public static final int GL_BLEND = 0xBE2, GL_DEPTH_TEST = 0xB71;
    public static final int GL_ONE = 1, GL_ONE_MINUS_SRC_ALPHA = 0x303, GL_SRC_ALPHA = 0x302;
    public static final int GL_TEXTURE_2D = 0xDE1;
    public static final int GL_TEXTURE_WRAP_S = 0x2802, GL_TEXTURE_WRAP_T = 0x2803;
    public static final int GL_TEXTURE_MIN_FILTER = 0x2801, GL_TEXTURE_MAG_FILTER = 0x2800;
    public static final int GL_REPEAT = 0x2901, GL_NEAREST = 0x2600, GL_LINEAR = 0x2601;
    public static final int GL_RGB = 0x1907, GL_RGBA = 0x1908;
    public static final int GL_ARRAY_BUFFER = 0x8892, GL_ELEMENT_ARRAY_BUFFER = 0x8893;
    public static final int GL_STATIC_DRAW = 0x88E4, GL_DYNAMIC_DRAW = 0x88E8;
    public static final int GL_FRAGMENT_SHADER = 0x8B30, GL_VERTEX_SHADER = 0x8B31;
    public static final int GL_COMPILE_STATUS = 0x8B81, GL_LINK_STATUS = 0x8B82;
    public static final int GL_INFO_LOG_LENGTH = 0x8B84;
    public static final int GL_TEXTURE0 = 0x84C0;

    @JSBody(params = {"c"}, script = "window.__gl.enable(c);") public static native void glEnable(int c);
    @JSBody(params = {"c"}, script = "window.__gl.disable(c);") public static native void glDisable(int c);
    @JSBody(params = {"s", "d"}, script = "window.__gl.blendFunc(s, d);") public static native void glBlendFunc(int s, int d);
    @JSBody(params = {"m"}, script = "window.__gl.clear(m);") public static native void glClear(int m);
    @JSBody(params = {"r", "g", "b", "a"}, script = "window.__gl.clearColor(r, g, b, a);")
    public static native void glClearColor(float r, float g, float b, float a);
    @JSBody(params = {"x", "y", "w", "h"}, script = "window.__gl.viewport(x, y, w, h);")
    public static native void glViewport(int x, int y, int w, int h);

    @JSBody(script = "return window.__H.push(window.__gl.createTexture()) - 1;") public static native int glGenTextures();
    @JSBody(params = {"t", "id"}, script = "window.__gl.bindTexture(t, id ? window.__H[id] : null);")
    public static native void glBindTexture(int t, int id);
    @JSBody(params = {"t", "p", "v"}, script = "window.__gl.texParameteri(t, p, v);")
    public static native void glTexParameteri(int t, int p, int v);

    @JSBody(params = {"m", "n", "t", "o"}, script = "window.__gl.drawElements(m, n, t, o);")
    private static native void drawElements(int m, int n, int t, int o);
    public static void glDrawElements(int mode, int count, int type, long offset) { drawElements(mode, count, type, (int) offset); }

    // ---- GL13
    @JSBody(params = {"t"}, script = "window.__gl.activeTexture(t);") public static native void glActiveTexture(int t);

    // ---- GL15
    @JSBody(script = "return window.__H.push(window.__gl.createBuffer()) - 1;") public static native int glGenBuffers();
    @JSBody(params = {"t", "id"}, script = "window.__gl.bindBuffer(t, id ? window.__H[id] : null);")
    public static native void glBindBuffer(int t, int id);
    @JSBody(params = {"t", "size", "u"}, script = "window.__gl.bufferData(t, size, u);")
    private static native void bufferDataSize(int t, int size, int u);
    public static void glBufferData(int target, long size, int usage) { bufferDataSize(target, (int) size, usage); }
    @JSBody(params = {"t", "d", "u"}, script = "window.__gl.bufferData(t, d, u);")
    private static native void bufferDataInts(int t, Int32Array d, int u);
    public static void glBufferData(int target, int[] data, int usage) { bufferDataInts(target, Int32Array.fromJavaArray(data), usage); }
    @JSBody(params = {"t", "o", "d"}, script = "window.__gl.bufferSubData(t, o, d);")
    private static native void bufferSubDataFloats(int t, int o, Float32Array d);
    public static void glBufferSubData(int target, long offset, float[] data) { bufferSubDataFloats(target, (int) offset, Float32Array.fromJavaArray(data)); }

    // ---- GL20
    @JSBody(params = {"t"}, script = "return window.__H.push(window.__gl.createShader(t)) - 1;") public static native int glCreateShader(int t);
    @JSBody(params = {"id", "src"}, script = "window.__gl.shaderSource(window.__H[id], window.__toEs300(src));")
    public static native void glShaderSource(int id, String src);
    @JSBody(params = {"id"}, script = "window.__gl.compileShader(window.__H[id]);") public static native void glCompileShader(int id);
    @JSBody(params = {"id", "p"}, script = "if (p === 0x8B84) return (window.__gl.getShaderInfoLog(window.__H[id]) || '').length; var v = window.__gl.getShaderParameter(window.__H[id], p); return v === true ? 1 : v === false ? 0 : v;")
    public static native int glGetShaderi(int id, int p);
    @JSBody(params = {"id"}, script = "return window.__gl.getShaderInfoLog(window.__H[id]) || '';")
    private static native String shaderLog(int id);
    public static String glGetShaderInfoLog(int id, int maxLen) { return shaderLog(id); }
    @JSBody(script = "return window.__H.push(window.__gl.createProgram()) - 1;") public static native int glCreateProgram();
    @JSBody(params = {"p", "s"}, script = "window.__gl.attachShader(window.__H[p], window.__H[s]);") public static native void glAttachShader(int p, int s);
    @JSBody(params = {"p"}, script = "window.__gl.linkProgram(window.__H[p]);") public static native void glLinkProgram(int p);
    @JSBody(params = {"id", "p"}, script = "if (p === 0x8B84) return (window.__gl.getProgramInfoLog(window.__H[id]) || '').length; var v = window.__gl.getProgramParameter(window.__H[id], p); return v === true ? 1 : v === false ? 0 : v;")
    public static native int glGetProgrami(int id, int p);
    @JSBody(params = {"id"}, script = "return window.__gl.getProgramInfoLog(window.__H[id]) || '';")
    private static native String programLog(int id);
    public static String glGetProgramInfoLog(int id, int maxLen) { return programLog(id); }
    @JSBody(params = {"p"}, script = "window.__gl.useProgram(p ? window.__H[p] : null);") public static native void glUseProgram(int p);
    @JSBody(params = {"p", "n"}, script = "return window.__H.push(window.__gl.getUniformLocation(window.__H[p], n)) - 1;")
    public static native int glGetUniformLocation(int p, String n);

    @JSBody(params = {"l", "v"}, script = "window.__gl.uniform1f(window.__H[l], v);") public static native void glUniform1f(int l, float v);
    @JSBody(params = {"l", "v"}, script = "window.__gl.uniform1i(window.__H[l], v);") public static native void glUniform1i(int l, int v);
    @JSBody(params = {"l", "x", "y"}, script = "window.__gl.uniform2f(window.__H[l], x, y);") public static native void glUniform2f(int l, float x, float y);
    @JSBody(params = {"l", "x", "y", "z"}, script = "window.__gl.uniform3f(window.__H[l], x, y, z);") public static native void glUniform3f(int l, float x, float y, float z);
    @JSBody(params = {"l", "x", "y", "z", "w"}, script = "window.__gl.uniform4f(window.__H[l], x, y, z, w);")
    public static native void glUniform4f(int l, float x, float y, float z, float w);
    @JSBody(params = {"l", "v"}, script = "window.__gl.uniform1iv(window.__H[l], v);")
    private static native void uniform1iv(int l, Int32Array v);
    public static void glUniform1iv(int l, int[] v) { uniform1iv(l, Int32Array.fromJavaArray(v)); }
    @JSBody(params = {"l", "t", "v"}, script = "window.__gl.uniformMatrix4fv(window.__H[l], t, v);")
    private static native void uniformMatrix4fv(int l, boolean t, Float32Array v);
    public static void glUniformMatrix4fv(int l, boolean transpose, float[] v) { uniformMatrix4fv(l, transpose, Float32Array.fromJavaArray(v)); }
    @JSBody(params = {"l", "t", "v"}, script = "window.__gl.uniformMatrix3fv(window.__H[l], t, v);")
    private static native void uniformMatrix3fv(int l, boolean t, Float32Array v);
    public static void glUniformMatrix3fv(int l, boolean transpose, float[] v) { uniformMatrix3fv(l, transpose, Float32Array.fromJavaArray(v)); }

    @JSBody(params = {"i", "s", "t", "n", "st", "o"}, script = "window.__gl.vertexAttribPointer(i, s, t, n, st, o);")
    private static native void vertexAttribPointer(int i, int s, int t, boolean n, int st, int o);
    public static void glVertexAttribPointer(int idx, int size, int type, boolean norm, int stride, long offset) {
        vertexAttribPointer(idx, size, type, norm, stride, (int) offset);
    }
    @JSBody(params = {"i"}, script = "window.__gl.enableVertexAttribArray(i);") public static native void glEnableVertexAttribArray(int i);
    @JSBody(params = {"i"}, script = "window.__gl.disableVertexAttribArray(i);") public static native void glDisableVertexAttribArray(int i);

    // ---- GL30
    @JSBody(script = "return window.__H.push(window.__gl.createVertexArray()) - 1;") public static native int glGenVertexArrays();
    @JSBody(params = {"id"}, script = "window.__gl.bindVertexArray(id ? window.__H[id] : null);") public static native void glBindVertexArray(int id);
}
