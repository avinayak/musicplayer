/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

/**
 *
 * @author gene
 */
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

public class VLCEngine {

    ThreadedVLC tvlc;
    Thread tp;

    void load(String file) throws InterruptedException {
        if (tp != null) {
            if (tp.isAlive()) {
                tvlc.setPlay = 3;
                tp.join();
            }
        }
        tvlc = new ThreadedVLC(file);
        tp = new Thread(tvlc, "VLC Thread");

    }

    void play() throws InterruptedException {
        if (!tp.isAlive()) {
            tp.start();
        }
        tvlc.setPlay = 1;
    }

    void pause() throws InterruptedException {
        tvlc.setPlay = 2;
    }

    void seek(int sec) {
        tvlc.mp.setTime(sec * 1000);
        tvlc.elapsed = sec;
    }

    int getElapsedTime() {
        tvlc.setPlay = 6;
        return tvlc.elapsed;
    }

    boolean State() {
        return tvlc.state;
    }

}

class ThreadedVLC implements Runnable {

    private AudioMediaPlayerComponent audioPlayer;
    MediaPlayer mp = null;
    String currentFile;
    boolean state = true;
    int setPlay = 4;
    int length = 200;
    int elapsed = 0;

    public ThreadedVLC(String File) {
        audioPlayer = new AudioMediaPlayerComponent() {
    
           

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("ERROR MF");
            }
        };
        mp = audioPlayer.getMediaPlayer();

        currentFile = File;
        mp.prepareMedia(currentFile);
        //mp.play();
        length = 400;
    }

    @Override
    public void run() {
        while (true) {


            if (setPlay == 1) {
                mp.play();
                setPlay = 4;
                state = false;
            }
            if (setPlay == 2) {
                mp.pause();
                setPlay = 4;
                state = true;
            }
            if (setPlay == 5) {
                length = (int) (mp.getLength() / 1000);
                setPlay = 4;
            }
            if (setPlay == 6) {
                elapsed = (int) (mp.getTime() / 1000);
                setPlay = 4;
            }
            if (setPlay == 3) {
                mp.stop();
                state = false;
                break;
            }

        }

    }
}
