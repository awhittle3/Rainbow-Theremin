package com.awhittle.rainbowtheremin;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;

public class Tone {

    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>.
	// Modified further by awhittle3.
	
    private final static float duration = 0.05f; // seconds
    private final static int sampleRate = 8000;
    private final static int numSamples = (int) (duration * sampleRate);
    private final static double sample[] = new double[numSamples];
    private static double freqOfTone = 440; // hz

    private final static byte generatedSnd[] = new byte[2 * numSamples];

    static Handler handler = new Handler();

    protected static void playTone() {

        // Use a new thread as this can take a while
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                genTone();
                handler.post(new Runnable() {

                    public void run() {
                        playSound();
                    }
                });
            }
        });
        thread.start();
    }

    static void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    static void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        
        int x = 0;
        // Montior playback to find when done
        do {
        	if (audioTrack != null)
        		x = audioTrack.getPlaybackHeadPosition();
        	else
        		x = numSamples;
        }
        while (x<numSamples);

        // Track play done. Release track.
        if (audioTrack != null) audioTrack.release();
        
     
    }
    
    public static void getTone(float x){
    	freqOfTone = (double)(400 + 13000 * x);
    }
}

