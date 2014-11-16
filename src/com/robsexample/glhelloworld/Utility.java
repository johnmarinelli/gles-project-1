package com.robsexample.glhelloworld;

public final class Utility {
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
