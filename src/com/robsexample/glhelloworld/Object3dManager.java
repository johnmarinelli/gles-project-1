package com.robsexample.glhelloworld;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.util.Log;

public class Object3dManager {
	private ArrayList<Object3d> mObjects;
	private Cube mPlayer = null;
	
	public Object3dManager() {
		mObjects = new ArrayList<Object3d>();
	}
	
	public void update() {
		/* list to store any dirty objects */
		ArrayList<Object3d> dirty = new ArrayList<Object3d>();
		
		for(Object3d obj : mObjects) {
			float z = obj.m_Orientation.GetPosition().z;
			
			/* check if object is past viewer */
			if(z < 7.25) {
				obj.update();
			}
			else {
				obj.setFlag(true);
				dirty.add(obj);
			}
		}
		
		/*
		 * LOL BOUNDS CHECKING
		 */
		if(mPlayer.m_Orientation.GetPosition().x >= 5.f) {
			mPlayer.setPositionDeltaX(-0.01f);
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

		/* clear out dirty objects */
		for(Object3d dObj : dirty) {
			mObjects.remove(dObj);
		}
		
		/* check collision between mObjects & mPlayer */
		float playerRadius = mPlayer.getMesh().getRadius();
		Vector3 playerPos = mPlayer.m_Orientation.GetPosition();
		
		for(Object3d obj : mObjects) {
			float objRadius = obj.getMesh().getRadius();
			float totalRadius = playerRadius + objRadius;
			
			Vector3 diff = Vector3.Subtract(obj.m_Orientation.GetPosition(), playerPos);
			float dis = diff.length();
			if(dis < totalRadius*totalRadius) {
				Log.d("col", "collision");
			}
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
