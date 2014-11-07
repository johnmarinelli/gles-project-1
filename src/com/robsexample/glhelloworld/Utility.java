package com.robsexample.glhelloworld;

public final class Utility {
	static float getRandomFloat(float min, float max) {
		return (float) (Math.random() * (max - min) + min);
	}
}
