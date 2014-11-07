package com.robsexample.glhelloworld;

import java.util.ArrayList;

public class Object3dManager {
	private ArrayList<Object3d> mObjects;
	
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
				obj.updatePosition();
			}
			else {
				obj.setFlag(true);
				dirty.add(obj);
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
	}
	
	public void add(Object3d obj) {
		mObjects.add(obj);
	}
}
