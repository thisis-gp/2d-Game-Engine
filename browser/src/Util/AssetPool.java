package Util;

import components.Spritesheet;
import renderer.Shader;
import renderer.Texture;
import Unity.Sound;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String resourceName) {
        if (AssetPool.shaders.containsKey(resourceName)) {
            return AssetPool.shaders.get(resourceName);
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(resourceName, shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        if (AssetPool.textures.containsKey(resourceName)) {
            return AssetPool.textures.get(resourceName);
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(resourceName, texture);
            return texture;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        if (!AssetPool.spritesheets.containsKey(resourceName)) {
            AssetPool.spritesheets.put(resourceName, spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        if (!AssetPool.spritesheets.containsKey(resourceName)) {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool.";
        }
        return AssetPool.spritesheets.getOrDefault(resourceName, null);
    }

    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String soundFile) {
        if (sounds.containsKey(soundFile)) {
            return sounds.get(soundFile);
        } else {
            assert false : "Sound file not added '" + soundFile + "'";
        }
        return null;
    }

    public static Sound addSound(String soundFile, boolean loops) {
        if (sounds.containsKey(soundFile)) {
            return sounds.get(soundFile);
        } else {
            Sound sound = new Sound(soundFile, loops);
            AssetPool.sounds.put(soundFile, sound);
            return sound;
        }
    }
}
