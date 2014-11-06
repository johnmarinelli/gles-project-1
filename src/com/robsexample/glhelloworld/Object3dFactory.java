package com.robsexample.glhelloworld;

import java.util.HashMap;
import java.util.Map.Entry;

import android.util.Log;

import com.robsexample.glhelloworld.Object3d;

/*
 * singleton for creating Object3ds
 */
public abstract class Object3dFactory {
	private static int mIDCounter = 0;
	
	private HashMap<Integer, Object3d> mRegisteredObjects = 
			new HashMap<Integer, Object3d>();
	
	protected void register(Object3d obj) {
		mRegisteredObjects.put(mIDCounter++, obj);
		Log.i("id", Integer.toString(mIDCounter));
	}
	
	/*
	 * make this a function in an Object3dManager along with updating matrices etc
	 */
	public void draw(Camera camera, PointLight pointlight) {
		for(Entry<Integer, Object3d> e : mRegisteredObjects.entrySet()) {
			e.getValue().DrawObject(camera, pointlight);
		}
	}
}
