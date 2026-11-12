package Unity;

import web.Platform;

/** Web Audio replacement for the OpenAL/STB-vorbis Sound; decoding happens in engine-loader.js. */
public class Sound {
    private String filepath;

    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;
        Platform.soundRegister(filepath, loops);
    }

    public void delete() {
        stop();
    }

    public void play() {
        Platform.soundPlay(filepath);
    }

    public void stop() {
        Platform.soundStop(filepath);
    }

    public String getFilepath() {
        return this.filepath;
    }

    public boolean isPlaying() {
        return Platform.soundIsPlaying(filepath);
    }
}
