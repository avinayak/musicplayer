/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;


/**
 *
 * @author gene
 */
public class MusicEngine {

    Player audioPlayer;
    String currentFile;
    int currentSongDuration;

    void load(String filename) {
        currentFile = filename;
        try {
            URL url = new File(filename).toURL();
            audioPlayer = Manager.createRealizedPlayer(url);
        currentSongDuration = (int) audioPlayer.getDuration().getSeconds();
        } catch (IOException | NoPlayerException | CannotRealizeException ex) {
        }
        

    }

    void play() {
        audioPlayer.start();

    }

    void pause() {
        audioPlayer.stop();
    }

    void seek(int sec) {
        Time a = new Time((double) sec);

        audioPlayer.setMediaTime(a);

    }

    void stop() {
        audioPlayer.stop();
        audioPlayer.setMediaTime(new Time(0.0));
        
    }

    int getMusicDuration() {
        return (int) currentSongDuration;

    }

    int getElapsedTime() {
        return (int) audioPlayer.getMediaTime().getSeconds();
    }

    int getState() {
        return audioPlayer.getState();
    }
    
}
