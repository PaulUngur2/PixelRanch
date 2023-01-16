package main;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

    Clip clip;
    URL[] soundURL = new URL[30];
    //Constructor
    public Sound() {
        soundURL[0] = getClass().getResource("/sounds/bg.wav");
    }
    //Adds the sound file from the path from the constructor to the clip interface
    public void setFile(int i) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Plays the sound
    public void play() {
        clip.start();
    }
    //Stops the sound
    public void stop() {
        clip.stop();
    }
    //Loops the sound
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
