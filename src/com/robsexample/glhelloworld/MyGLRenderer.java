package com.robsexample.glhelloworld;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.content.Context;

public class MyGLRenderer implements GLSurfaceView.Renderer 
{
	private Context m_Context;
	
	private PointLight m_PointLight;
	private Camera m_Camera;
	
	private int m_ViewPortWidth;
    private int m_ViewPortHeight;
    
    private Object3dManager mObject3dManager;
    private long mLastUpdated;
	
	private Vector3 m_AccelerometerDeltas = new Vector3(0.f, 0.f, 0.f);
	private Vector3 mInitialOrientation = new Vector3(0.f, 0.f, 0.f);
		
	public MyGLRenderer(Context context) 
	{
	   m_Context = context; 
	   mObject3dManager = new Object3dManager(context);
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
		 Vector3 Eye = new Vector3(0,0, Utility.EYE_Z_MAX);
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
		 Texture TexAndroid = new Texture(iContext,R.drawable.doge);	
        
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
	 }
	 
	 public void setAccelerometerDeltas(float x, float y, float z) {
		 if(Math.abs(x) > Utility.MAX_ACCELEROMETER_X_DELTA) {
			 m_AccelerometerDeltas.x = (float) (x * Utility.ACCELEROMETER_TO_VELOCITY);
		 }
		 else {
			 m_AccelerometerDeltas.x = 0.f;
		 }
		 
		 if(y > mInitialOrientation.y - Utility.MAX_ACCELEROMETER_Y_DELTA && 
				 y < mInitialOrientation.y + Utility.MAX_ACCELEROMETER_Y_DELTA) {
			 /* ghetto map function from (y-10, y+10) to (y-6, y+6) */
			 y -= 6.f;
			 m_AccelerometerDeltas.y = (float) (y * -Utility.ACCELEROMETER_TO_VELOCITY);
		 }
		 else {
			 m_AccelerometerDeltas.y = 0.f;
		 }
		 
		 if(Math.abs(z) > Utility.MIN_ACCELEROMETER_Z_DELTA && 
				 Math.abs(z) < Utility.MAX_ACCELEROMETER_Z_DELTA) {
			 m_AccelerometerDeltas.z = (float) (z * Utility.ACCELEROMETER_TO_VELOCITY);
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
    				CreateCube(m_Context, new Vector3(0.f, 0f, 2.f), false));
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
    		
    		if(currentTime - mLastUpdated > Utility.GENERATED_CUBE_SPAWN_INTERVAL) {
    			float randomXPos = Utility.getRandomFloat(-Utility.GENERATED_CUBE_X_POSITION, Utility.GENERATED_CUBE_X_POSITION);
    			float randomYPos = Utility.getRandomFloat(-Utility.GENERATED_CUBE_Y_POSITION, Utility.GENERATED_CUBE_Y_POSITION);
    			float randomZPos = Utility.getRandomFloat(Utility.GENERATED_CUBE_Z_POSITION_MIN, Utility.GENERATED_CUBE_Z_POSITION_MAX);
    			
    			float randomXAxis = Utility.getRandomFloat(-1.f, 1.f);
    			float randomYAxis = Utility.getRandomFloat(-1.f, 1.f);
    			float randomZAxis = Utility.getRandomFloat(-1.f, 1.f);
    			
    			Cube c = CreateCube(m_Context, new Vector3(randomXPos, 
    					randomYPos, randomZPos), true);
   			 	c.setPositionDelta(Utility.GENERATED_CUBE_POSITION_DELTA);
   			 	c.m_Orientation.SetRotationAxis(new Vector3(randomXAxis,
   			 			randomYAxis, randomZAxis));
   			 	
    			mObject3dManager.add(c);    			
    			
    			mLastUpdated = currentTime;
    		}
    		
    		//GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
    		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    		m_Camera.UpdateCamera();
    		mObject3dManager.update();
    		mObject3dManager.draw(m_Camera, m_PointLight);
    		CubeFactory.instance().clean();
    	}

		public void setInitialOrientation(float x, float y, float z) {
			mInitialOrientation.x = x;
			mInitialOrientation.y = y;
			mInitialOrientation.z = z;
		}
}

