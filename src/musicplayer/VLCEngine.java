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

    static int length;
    static int elapsed=0;
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
        tp = new Thread(tvlc, "VLCThreaded Thread1");

    }

    void play() throws InterruptedException {
        tp.start();
        tvlc.setPlay = 1;
    }

    void pause() throws InterruptedException {
        tvlc.setPlay = 2;
    }

    void seek(int sec) {
    }

    int getMusicDuration() {
//System.out.println(length);
        return 200;
    }

    int getElapsedTime() {
        //return 1;
        return 50;
        //System.out.println(elapsed);
        //return elapsed;
    }

    boolean State() {
        return tvlc.state;
    }

    int getCompletedPerc() {
        return 25;//(int)(tvlc.mp.getPosition()*100);

    }
}

class ThreadedVLC implements Runnable {

    private AudioMediaPlayerComponent audioPlayer;
    MediaPlayer mp = null;
    String currentFile;
    boolean state = false;
    int setPlay = 4;

    public ThreadedVLC(String File) {
        audioPlayer = new AudioMediaPlayerComponent() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.exit(0);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {

                System.exit(0);
                System.out.println("ERROR MF");
            }
        };
        mp = audioPlayer.getMediaPlayer();

        currentFile = File;
        mp.prepareMedia(currentFile);
        VLCEngine.length = (int)(mp.getLength()/1000);
        VLCEngine.elapsed = 0;
      
    }

    @Override
    public void run() {
        while (true) {
            VLCEngine.elapsed = (int) (mp.getTime() / 1000);
                 
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
            if (setPlay == 3) {
                mp.stop();
                state = false;
                break;
            }
        }

    }
}
