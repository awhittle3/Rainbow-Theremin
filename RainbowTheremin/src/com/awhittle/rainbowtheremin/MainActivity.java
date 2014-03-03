package com.awhittle.rainbowtheremin;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
//import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	private View mView;
	private AudioManager mAudioManager;
	private int maxVolume;
	private int volume;
	private final int a = 255;
	private int r = 0;
	private int g = 0;
	private int b = 0;
	private Point mSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Set up background with black
		mView = this.findViewById(android.R.id.content);
		mView.setBackgroundColor(Color.argb(a, r, g, b));
		
		//Initialize volume control
    	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    	maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    	mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
    	
    	//Get screen size
		Display mDisplay = getWindowManager().getDefaultDisplay();
		mSize = new Point();
		mDisplay.getSize(mSize);
	}

	 @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	
		 	//Get x and y location in pixels
	        float x = event.getX();
	        float y = event.getY();
	        
	        //Normalize x and y values
	        float xNorm = x/mSize.x;
	        float yNorm = y/mSize.y;
	        
	        //Use longest side for pitch control
	        if (mSize.x > mSize.y){
	        	setColour(xNorm);
	        	setVolume(yNorm);
	        	Tone.getTone(xNorm);
	        	Tone.playTone();
	        } else {
	        	setColour(yNorm);
	        	setVolume(xNorm);
	        	Tone.getTone(yNorm);
	        	Tone.playTone();
	        }
	        
	        return true;
	    }

	private void setColour(float x) {
			
		//The following curves are rough fits of what rbg wavelength proportions make up the rainbow
		if(x > 0.3f && x < 0.95f){
			r = (int)(255*(11.899*x*x*x - 28.666*x*x + 20.161*x - 3.4639));
		} else if (x < 0.1f){
			r = (int)(-2550*x + 255);
		} else {
			r = 0;
		}
		
		if (x > 0.2f && x < 0.8f){
			g = (int)(255*(2.1091*x*x*x - 10.644*x*x + 8.7295*x - 1.196));
		} else {
			g = 0;
		}
		
		if (x < 0.4f){
			b = (int)(255*(82.889*x*x*x - 66.927*x*x + 13.563*x));
		} else {
			b = 0;
		}

		mView.setBackgroundColor(Color.argb(a, r, g, b));
	}
	
	private void setVolume(float y) {
		volume = (int) (maxVolume * (1-y));
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
	}
	 
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
