package com.robsexample.glhelloworld;

public final class Utility {
	public static final float MIN_ACCELEROMETER_Z_DELTA = 0.1f;
	public static final float MAX_ACCELEROMETER_Z_DELTA = 1.5f;
	public static final float MAX_ACCELEROMETER_Y_DELTA = 10.f;
	public static final float MAX_ACCELEROMETER_X_DELTA = .0f;
	public static final float ACCELEROMETER_TO_VELOCITY = .015f;
	public static final float GENERATED_CUBE_Z_POSITION_MIN = -5.f;
	public static final float GENERATED_CUBE_Z_POSITION_MAX = -4.f;
	
	public static final float EYE_Z_MAX = 7.25f;
	
	public static int GENERATED_CUBE_SPAWN_INTERVAL = 1000;
	
	public static float GENERATED_CUBE_X_POSITION = 2.f;
	public static float GENERATED_CUBE_Y_POSITION = 2.f;
	
	public static Vector3 GENERATED_CUBE_POSITION_DELTA = new Vector3(0.f, 0.f, 0.1f);
	
	static float getRandomFloat(float min, float max) {
		return (float) (Math.random() * (max - min) + min);
	}
	
	static int getRandomInt(int min, int max) {
		return Math.round((float)(Math.random() * (max - min) + min));
	}
	
	static int getRandomSign(int number) {
		return Math.random() > 0.5f ? -number : number;
	}
}
