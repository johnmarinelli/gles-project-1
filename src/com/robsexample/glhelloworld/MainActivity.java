package com.robsexample.glhelloworld;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.content.Intent;

public class MainActivity extends Activity {
	
	private ProgressBar mProgressBar;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
	}
	
	public void startGame(View v) {
		mProgressBar.setVisibility(View.VISIBLE);
		
		findViewById(R.id.mainscreenbg).setVisibility(View.GONE);
		Intent intent = new Intent(this, GameActivity.class);
		
		mProgressBar.setVisibility(View.GONE);
		startActivity(intent);
	}
}
