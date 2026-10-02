package Model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class WavPlay {

    private Clip audioClip;
    private AudioInputStream audioStream;

    public WavPlay(String filePath) {
        try {
            // Loading Audio Files
            File audioFile = new File(filePath);
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            // Get Audio Clip
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            FloatControl volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue((float) (Math.log(50 / 100.0) * 20));
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Audio file read failure: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("Audio resources not available: " + e.getMessage());
        }
    }

    // Play audio
    public void play() {
        if (audioClip != null) {
            audioClip.start();
        }
    }

    // Stop Audio
    public void stop() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

    // Release of resources
    public void close() {
        try {
            if (audioClip != null) {
                audioClip.close();
            }
            if (audioStream != null) {
                audioStream.close();
            }
        } catch (IOException e) {
            System.out.println("Failed to close audio resource: " + e.getMessage());
        }
    }
}
