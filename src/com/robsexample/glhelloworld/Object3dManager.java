package com.robsexample.glhelloworld;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;

public class Object3dManager {
	private ArrayList<Object3d> mObjects;
	private MediaPlayer mMediaPlayer;
	private Cube mPlayer = null;
	
	public Object3dManager(Context ctx) {
		mObjects = new ArrayList<Object3d>();
		mMediaPlayer = MediaPlayer.create(ctx, R.raw.dogecollision);
	}
	
	public void update() {
		/* list to store any dirty objects */
		ArrayList<Object3d> dirty = new ArrayList<Object3d>();
		
		for(Object3d obj : mObjects) {
			float z = obj.m_Orientation.GetPosition().z;
			
			/* check if object is past viewer */
			if(z < Utility.EYE_Z_MAX) {
				obj.update();
			}
			else {
				obj.setDirtyFlag(true);
				dirty.add(obj);
			}
		}
		
		/*
		 * LOL BOUNDS CHECKING
		 * these are magic values and i'm very sorry for it
		 */
		if(mPlayer.m_Orientation.GetPosition().x >= 5.f) {
			mPlayer.setPositionDeltaX(-.01f);
		}
		else if(mPlayer.m_Orientation.GetPosition().x <= -5.f) {
			mPlayer.setPositionDeltaX(.01f);
		}
		if(mPlayer.m_Orientation.GetPosition().y >= 2.f) {
			mPlayer.setPositionDeltaY(-.01f);
		}
		else if(mPlayer.m_Orientation.GetPosition().y <= -2.f) {
			mPlayer.setPositionDeltaY(.01f);
		}
		mPlayer.update();
		
		/* check collision between mObjects & mPlayer */
		float playerRadius = mPlayer.getMesh().getRadius();
		Vector3 playerPos = mPlayer.m_Orientation.GetPosition();
		
		for(Object3d obj : mObjects) {
			float objRadius = obj.getMesh().getRadius();
			float totalRadius = playerRadius + objRadius;
			
			Vector3 diff = Vector3.Subtract(obj.m_Orientation.GetPosition(), playerPos);
			float dis = diff.length();
			if(dis < totalRadius*totalRadius) {
				obj.setDirtyFlag(true);
				dirty.add(obj);
				mMediaPlayer.start();
			}
		}

		/* clear out dirty objects */
		for(Object3d dObj : dirty) {
			mObjects.remove(dObj);
		}
	}
	
	public void draw(Camera camera, PointLight pointlight) {
		for(Object3d obj : mObjects) {
			obj.DrawObject(camera, pointlight);
		}
		
		mPlayer.DrawObject(camera, pointlight);
	}
	
	public void add(Object3d obj) {
		mObjects.add(obj);
	}
	
	public void addPlayer(Cube player) {
		mPlayer = player;
	}
	
	public void setPlayerPositionDelta(Vector3 posDelta) {		
		if(mPlayer != null) mPlayer.setPositionDelta(posDelta);
	}
}
