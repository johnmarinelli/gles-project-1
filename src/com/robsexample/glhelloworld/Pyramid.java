package com.robsexample.glhelloworld;

import android.content.Context;

public class Pyramid extends Object3d {
	
	static final float pyramidDataNoTexture[] = 
	{
		//x  	  y      z      nx ny nz
		-0.25f, -0.25f, 0.25f, -1, -1, 1,   //bottom front left. 1
		-0.25f, -0.25f, -0.25f, -1, -1, -1, //bottom back left. 2
		0.25f,  -0.25f, 0.25f,  1, -1, 1,   //bottom front right. 3
		0.25f,  -0.25f, -0.25f, 1, -1, -1,  //bottom back right. 4
		0.0f,    0.25f, 0.0f,   0, 1, 0,    //top. 5
	};
	
	static final short pyramidDrawOrder[] = 
	{
	};
	
	Pyramid(Context iContext, MeshEx iMeshEx, Texture[] iTextures,
			Material iMaterial, Shader iShader) {
		super(iContext, iMeshEx, iTextures, iMaterial, iShader);
	}

}
