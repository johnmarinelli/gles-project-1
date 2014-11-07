package com.robsexample.glhelloworld;

import java.util.ArrayList;
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
	}
	
	public void clean() {
		/* arraylist to store ID's of dirty objects */
		ArrayList<Integer> dirty = new ArrayList<Integer>();
		
		for(Entry<Integer, Object3d> e : mRegisteredObjects.entrySet()) {
			if(e.getValue().getFlag()) {
				dirty.add(e.getKey());
			}
		}
		
		/* clear out dirty objects */
		for(Integer dInt : dirty) {
			mRegisteredObjects.remove(dInt);
		}
	}
}
