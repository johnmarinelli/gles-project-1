package com.robsexample.glhelloworld;

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
    
    private Pyramid mPyramid;
    private Object3dManager mObject3dManager = new Object3dManager();
    private long mLastUpdated;
	
	private Vector3 m_AccelerometerDeltas = new Vector3(0.f, 0.f, 0.f);
		
	public MyGLRenderer(Context context) 
	{
	   m_Context = context; 
	   mLastUpdated = System.currentTimeMillis();
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
		 Vector3 Eye = new Vector3(0,0,10);
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
	    
	 void createPyramid(Context context, Vector3 position) {
		 Shader shader = new Shader(context, R.raw.vsonelight, R.raw.fsonelight);
		 MeshEx pyramidMesh = new MeshEx(5, 0, -1, 3, Pyramid.pyramidDataNoTexture,
				 Pyramid.pyramidDrawOrder);
		 
		 Material material = new Material();
		 
		 mPyramid = new Pyramid(context, pyramidMesh, null, material, shader);

		 // Set Intial Position and Orientation
		 Vector3 Axis = new Vector3(0,1,0);
		 Vector3 Position = position;
		 Vector3 Scale = new Vector3(1.0f,1.0f,1.0f);
        
		 mPyramid.m_Orientation.SetPosition(Position);
		 mPyramid.m_Orientation.SetRotationAxis(Axis);
		 mPyramid.m_Orientation.SetScale(Scale);
	 }
	 
	 Cube CreateCube(Context iContext, Vector3 position, boolean enemy)
	 {
		 //Create Cube Shader
		 Shader Shader = new Shader(iContext, R.raw.vsonelight, R.raw.fsonelight);	// ok
    	         
		 //MeshEx(int CoordsPerVertex, 
		 //		int MeshVerticesDataPosOffset, 
		 //		int MeshVerticesUVOffset , 
		 //		int MeshVerticesNormalOffset,
		 //		float[] Vertices,
		 //		short[] DrawOrder
		 float cubeData[] = enemy ? Cube.LargeCubeData : Cube.CubeData;
		 MeshEx CubeMesh = new MeshEx(8,0,3,5, cubeData, Cube.CubeDrawOrder);
        
		 // Create Material for this object
		 Material Material1 = new Material();
		 //Material1.SetEmissive(0.0f, 0, 0.25f);
    
		 // Create Texture
		 Texture TexAndroid = new Texture(iContext,R.drawable.ic_launcher);		
        
		 Texture[] CubeTex = new Texture[1];
		 CubeTex[0] = TexAndroid;
		 
		 Cube cube = CubeFactory.instance().create(iContext, CubeMesh, 
				 CubeTex, Material1, Shader);     
		 		 
		 // Set Intial Position and Orientation
		 Vector3 Axis = new Vector3(0,1,0);
		 Vector3 Position = position;
		 Vector3 Scale = new Vector3(1.0f,1.0f,1.0f);
        
		 cube.m_Orientation.SetPosition(Position);
		 cube.m_Orientation.SetRotationAxis(Axis);
		 cube.m_Orientation.SetScale(Scale);
		 
		 return cube;
		 //m_Cube.m_Orientation.AddRotation(45);
	 }
	 
	 public void setAccelerometerDeltas(float x, float y, float z) {
		 /* for some reason x axis is reversed */
		 if(Math.abs(x) > .1) {
			 m_AccelerometerDeltas.x = (float) (x * .015);
		 }
		 else {
			 m_AccelerometerDeltas.x = 0.f;
		 }
		 
		 /* if y is between 0 and 9, it's facing towards user */
		 if(y > 0.f && y < 12.f) {
			 /* ghetto map function from (0,9) to (-6, 6) */
			 y -= 6.f;
			 m_AccelerometerDeltas.y = (float) (y * -.015);
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
		 
		 mObject3dManager.setPlayerPositionDelta(m_AccelerometerDeltas);
	 }
	 
    	@Override
    	public void onSurfaceCreated(GL10 unused, EGLConfig config) 
    	{
    		m_PointLight = new PointLight(m_Context);
    		SetupLights();
    		
    		mObject3dManager.addPlayer(
    				CreateCube(m_Context, new Vector3(0.f, -0.5f, 4.f), false));
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
    		long currentTime = System.currentTimeMillis();
    		
    		if(currentTime - mLastUpdated > 1000) {
    			float randomXPos = Utility.getRandomFloat(-2f, 2f);
    			float randomYPos = Utility.getRandomFloat(-2f, 2f);
    			float randomZPos = Utility.getRandomFloat(-5.f, -4.f);
    			
    			float randomXAxis = Utility.getRandomFloat(-1.f, 1.f);
    			float randomYAxis = Utility.getRandomFloat(-1.f, 1.f);
    			float randomZAxis = Utility.getRandomFloat(-1.f, 1.f);
    			
    			Cube c = CreateCube(m_Context, new Vector3(randomXPos, 
    					randomYPos, randomZPos), true);
   			 	c.setPositionDelta(new Vector3(0.f,0.f,.1f));
   			 	c.m_Orientation.SetRotationAxis(new Vector3(randomXAxis,
   			 			randomYAxis, randomZAxis));
   			 	
    			mObject3dManager.add(c);    			
    			
    			mLastUpdated = currentTime;
    		}
    		
    		GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
    		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    	    
    		m_Camera.UpdateCamera();
    		mObject3dManager.update();
    		mObject3dManager.draw(m_Camera, m_PointLight);
    		CubeFactory.instance().clean();
    	}
}

