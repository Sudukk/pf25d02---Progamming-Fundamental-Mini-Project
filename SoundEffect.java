import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.initGame() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can the static variable SoundEffect.volume to SoundEffect.Volume.MUTE
 *    to mute the sound.
 *
 * For Eclipse, place the audio file under "src", which will be copied into "bin".
 */
public enum SoundEffect {
    EAT_FOOD("audio/eatfood.wav"),
    EXPLODE("audio/explode.wav"),
    DIE("audio/die.wav"),
    BG_MUSIC("audio/bgmusic.wav"),
    MOUSE_CLICK("audio/die.wav");

    /** Nested enumeration for specifying volume */
    public enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public Volume volume = Volume.LOW;

    /** Each sound effect has its own clip, loaded with its own sound file. */
    public static Volume masterVolume = Volume.LOW;

    private Clip clip;
    private FloatControl gainControl;
    /** Private Constructor to construct each element of the enum with its own sound file. */
    private SoundEffect(String soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Play or Re-play the sound effect from the beginning, by rewinding. */
    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();   // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            setClipVolume(masterVolume);
            clip.start();     // Start playing
        }
    }

    public static void playBGMusic() {
        SoundEffect.BG_MUSIC.stop();
        SoundEffect.BG_MUSIC.setClipVolume(SoundEffect.masterVolume);
        SoundEffect.BG_MUSIC.clip.setFramePosition(0);
        SoundEffect.BG_MUSIC.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    private void setClipVolume(Volume volume) {
        if (gainControl == null) return;

        float dB;
        switch (volume) {
            case HIGH:   dB = 10.0f; break;        // Full volume
            case MEDIUM: dB = 5.0f; break;
            case LOW:    dB = -10.0f; break;
            default:     dB = 5.0f; break;      // Practically mute
        }
        gainControl.setValue(dB);
    }

    /** Optional static method to pre-load all the sound files. */
    static void initGame() {
        values(); // calls the constructor for all the elements
    }
}
