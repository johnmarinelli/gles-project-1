package com.robsexample.glhelloworld;

import java.io.FileInputStream;
import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import android.opengl.GLSurfaceView;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends Activity implements SensorEventListener{
	
	private MyGLSurfaceView m_GLView;
	private SensorManager m_SensorManager;
	private long m_LastUpdated;
	private MediaPlayer mMediaPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

    	// Create a MyGLSurfaceView instance and set it
    	// as the ContentView for this Activity
    	m_GLView = new MyGLSurfaceView(this);
    	
    	String mp3File = "raw/schubert-march.mp3";
    	AssetManager assetManager = getAssets();
    	mMediaPlayer = new MediaPlayer();
    	FileInputStream mp3Stream;
		try {
			mp3Stream = assetManager.openFd(mp3File)
					.createInputStream();
	    	
	    	mMediaPlayer.setDataSource(mp3Stream.getFD());
	    	mMediaPlayer.prepare();
	    	mMediaPlayer.start();
		} catch (IOException e) {
			Log.d("exception", e.toString());
			e.printStackTrace();
		}
    	
    	setContentView(m_GLView);
    	m_SensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
    	m_LastUpdated = System.currentTimeMillis();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
    	m_GLView.onPause();
    	mMediaPlayer.pause();
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
    	m_GLView.onResume();
    	try {
	    	mMediaPlayer.start();
		} catch (IllegalStateException e) {
			Log.d("exception", e.toString());			
			e.printStackTrace();
		}
    	
		m_SensorManager.registerListener(this, 
				m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				m_SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {		
			long currentTime = System.currentTimeMillis();
			
			if(currentTime-m_LastUpdated > 200) {
				/* 
				 * since we're in landscape mode, x and y are reversed 
				 * should probably find where in the GL code this is instead of
				 * hacking it together....
				 */
				float x = event.values[1];
				float y = event.values[0];
				float z = event.values[2];
				
			    m_GLView.updateRenderer(x, y, z);
				m_LastUpdated = currentTime;
			}
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////

class MyGLSurfaceView extends GLSurfaceView 
{
	private MyGLRenderer m_Renderer;
	
    public MyGLSurfaceView(Context context) 
    {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
    	
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    	setBackgroundResource(R.drawable.spacebg);
    	setZOrderOnTop(true);
    	        
        // Set the Renderer for drawing on the GLSurfaceView
        m_Renderer = new MyGLRenderer(context);
        setRenderer(m_Renderer);
    }
    
    public void updateRenderer(float x, float y, float z) {
    	m_Renderer.setAccelerometerDeltas(x, y, z);
    }
}



