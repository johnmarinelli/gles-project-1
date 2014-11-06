package com.robsexample.glhelloworld;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.content.Context;

public class MyGLRenderer implements GLSurfaceView.Renderer 
{
	private Context m_Context;
	
	private PointLight m_PointLight;
	private Camera m_Camera;
	
	private int m_ViewPortWidth;
    private int m_ViewPortHeight;
	
	private ArrayList<Cube> m_Cubes = new ArrayList<Cube>();
	private Vector3 m_CubePositionDelta = new Vector3(0.f, 0.f, 0.f);
	private Vector3 m_CubeRotationAxisDelta = new Vector3(0.f, 0.f, 0.f);
	private Vector3 m_CubeScale = new Vector3(0.2f, 0.1f, 0.1f);
	
	private Vector3 m_AccelerometerDeltas = new Vector3(0.f, 0.f, 0.f);
	
	public MyGLRenderer(Context context) 
	{
	   m_Context = context; 
	}
	
	 void SetupLights()
	 {
		 // Set Light Characteristics
	     Vector3 LightPosition = new Vector3(0, 125, 125);
	     
	     float[] AmbientColor = new float [3];
	     AmbientColor[0] = 0.0f;
	     AmbientColor[1] = 0.0f;
	     AmbientColor[2] = 0.0f;  
	        
	     float[] DiffuseColor = new float[3];
	     DiffuseColor[0] = 1.0f;
	     DiffuseColor[1] = 1.0f;
	     DiffuseColor[2] = 1.0f;
	        
	     float[] SpecularColor = new float[3];
	     SpecularColor[0] = 1.0f;
	     SpecularColor[1] = 1.0f;
	     SpecularColor[2] = 1.0f;
	          
	     m_PointLight.SetPosition(LightPosition);
	     m_PointLight.SetAmbientColor(AmbientColor);
	     m_PointLight.SetDiffuseColor(DiffuseColor);
	     m_PointLight.SetSpecularColor(SpecularColor);
	 }
	   
	 void SetupCamera()
	 {	
		// Set Camera View
		 Vector3 Eye = new Vector3(0,0,8);
	     Vector3 Center = new Vector3(0, 0,-1);
	     Vector3 Up = new Vector3(0,1,0);  
	        
	     float ratio = (float) m_ViewPortWidth / m_ViewPortHeight;
	     float Projleft	= -ratio;
	     float Projright = ratio;
	     float Projbottom= -1;
	     float Projtop	= 1;
	     float Projnear	= 3;
	     float Projfar	= 50; //100;
	    	
	     m_Camera = new Camera(m_Context,
	        				   Eye,
	        				   Center,
	        				   Up,
	        				   Projleft, Projright, 
	        				   Projbottom,Projtop, 
	        				   Projnear, Projfar);
	  }
	    
	 void CreateCube(Context iContext, Vector3 position)
	 {
		 //Create Cube Shader
		 Shader Shader = new Shader(iContext, R.raw.vsonelight, R.raw.fsonelight);	// ok
    	         
		 //MeshEx(int CoordsPerVertex, 
		 //		int MeshVerticesDataPosOffset, 
		 //		int MeshVerticesUVOffset , 
		 //		int MeshVerticesNormalOffset,
		 //		float[] Vertices,
		 //		short[] DrawOrder
		 MeshEx CubeMesh = new MeshEx(8,0,-1,-1,Cube.CubeData, Cube.CubeDrawOrder);
        
		 // Create Material for this object
		 Material Material1 = new Material();
		 //Material1.SetEmissive(0.0f, 0, 0.25f);
    
       
		 // Create Texture
		 Texture TexAndroid = new Texture(iContext,R.drawable.ic_launcher);		
        
		 Texture[] CubeTex = new Texture[1];
		 CubeTex[0] = TexAndroid;
           
        
		 Cube cube = new Cube(iContext, 
				 		   CubeMesh, 
        				   CubeTex, 
        				   Material1, 
        				   Shader);
          
		 // Set Intial Position and Orientation
		 Vector3 Axis = new Vector3(0,1,0);
		 Vector3 Position = position;
		 Vector3 Scale = new Vector3(1.0f,1.0f,1.0f);
        
		 cube.m_Orientation.SetPosition(Position);
		 cube.m_Orientation.SetRotationAxis(Axis);
		 cube.m_Orientation.SetScale(Scale);
		 
		 m_Cubes.add(cube);
		 
		 //m_Cube.m_Orientation.AddRotation(45);
	 }
	 
	 public void setAccelerometerDeltas(float x, float y, float z) {
		 /* for some reason x axis is reversed */
		 if(Math.abs(x) > .1) {
			 m_AccelerometerDeltas.x = (float) (x * -.005);
		 }
		 else {
			 m_AccelerometerDeltas.x = 0.f;
		 }
		 
		 /* and y axis...idk */
		 if(Math.abs(y) > .1) {
			 m_AccelerometerDeltas.y = (float) (y * -.005);
		 }
		 else {
			 m_AccelerometerDeltas.y = 0.f;
		 }
		 
		 if(Math.abs(z) > .1 && Math.abs(z) < 1.5) {
			 m_AccelerometerDeltas.z = (float) (z * .005);
		 }
		 else {
			 m_AccelerometerDeltas.z = 0.f;
		 }
		 
	 }
	 
	 private void updateModelMatrix() {
		 for(Cube c : m_Cubes) {
			 Vector3 position = c.m_Orientation.GetPosition();
			 /*
			 if(position.x <= -2 || position.x >= 2) {
				 //m_CubePositionDelta.Negate();
			 }
			 else { */	 
				 m_CubePositionDelta.x = m_AccelerometerDeltas.x;
				 m_CubePositionDelta.y = m_AccelerometerDeltas.y;
				 m_CubePositionDelta.z = m_AccelerometerDeltas.z;
				 Vector3 newPosition = Vector3.Add(position, m_CubePositionDelta);
			 	 position.Set(newPosition.x, newPosition.y, newPosition.z);
			 
			 /*}*/		  
			 	 
			 Vector3 rotationAxis = c.m_Orientation.GetRotationAxis();
			 m_CubeRotationAxisDelta.x = m_AccelerometerDeltas.x;
			 m_CubeRotationAxisDelta.y = m_AccelerometerDeltas.y;
			 Vector3 newRotationAxis = Vector3.Add(rotationAxis, m_CubeRotationAxisDelta);
			 rotationAxis.Set(newRotationAxis.x, newRotationAxis.y, 0);
		 }
	 }
	
    	@Override
    	public void onSurfaceCreated(GL10 unused, EGLConfig config) 
    	{
    		m_PointLight = new PointLight(m_Context);
    		SetupLights();
    		
    		CreateCube(m_Context, new Vector3(0.f, 0.f, 0.f));
    		CreateCube(m_Context, new Vector3(1.f, 1.f, 0.f));
    	}

    	@Override
    	public void onSurfaceChanged(GL10 unused, int width, int height) 
    	{
    		// Ignore the passed-in GL10 interface, and use the GLES20
            // class's static methods instead.
            GLES20.glViewport(0, 0, width, height);
           
            m_ViewPortWidth = width;
            m_ViewPortHeight = height;
            
            SetupCamera();
    	}

    	@Override
    	public void onDrawFrame(GL10 unused) 
    	{
    		 GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
    		 GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    	     
    		 m_Camera.UpdateCamera();
    		 
    		 for(Cube c : m_Cubes) {
	    		 c.m_Orientation.AddRotation(1);
	    		 
	    		 //updateModelMatrix();
	    		 
	    		 c.DrawObject(m_Camera, m_PointLight);
    		 }
    	}
}

