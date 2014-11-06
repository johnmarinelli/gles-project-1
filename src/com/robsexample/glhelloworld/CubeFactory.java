package com.robsexample.glhelloworld;

import android.content.Context;

/*
 * singleton factory
 */
public class CubeFactory extends Object3dFactory {
	private static CubeFactory mInstance = null;
	
	public static CubeFactory instance() {
		if(null == mInstance) {
			mInstance = new CubeFactory();
		}
		
		return mInstance;
	}
	
	protected CubeFactory() {};

	public Cube create(Context context, MeshEx meshex, Texture[] textures,
			Material material, Shader shader) {
		Cube cube = new Cube(context, meshex, textures, material, shader);
		super.register(cube);
		
		return cube;
	}
}
