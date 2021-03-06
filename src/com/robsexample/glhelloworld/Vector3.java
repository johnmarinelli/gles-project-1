package com.robsexample.glhelloworld;

import android.util.FloatMath;
import java.lang.Math;

class Vector3
{
	 public float x;
     public float y;
     public float z;
    
     public Vector3(float _x, float _y, float _z)
     {
    	 x = _x;
    	 y = _y;
    	 z = _z;
     }
     
      //////////////////// Vector Operators ////////////////////////////////////
    
      void Multiply(float v)
      {
    	  x *= v;
    	  y *= v;
    	  z *= v;
      }
      
      void Negate()
      {
    	  x *= -1;
    	  y *= -1;
    	  z *= -1;
      }
   
      ///////////// Static Vector Operations /////////////////////////////////// 
   
      static Vector3 Add(Vector3 vec1, Vector3 vec2)
      {
    	  Vector3 result = new Vector3(0,0,0);
    	  
    	  result.x = vec1.x + vec2.x;
    	  result.y = vec1.y + vec2.y;
    	  result.z = vec1.z + vec2.z;
    	  
    	  return result;
      }
      
      static Vector3 Subtract(Vector3 vec1, Vector3 vec2) {
    	  Vector3 r = new Vector3(0, 0, 0);
    	  r.x = vec1.x - vec2.x;
    	  r.y = vec1.y - vec2.y;
    	  r.z = vec1.z - vec2.z;
    	  
    	  return r;
      }
     
      /////////////////////////////////////////////////////////////////////////
      
     void Set(float _x, float _y, float _z)
     {
	      x = _x;
	      y = _y;
	      z = _z;
     }
      
     void Normalize()
     {
	      float l = Length();

	      x = x/l;
	      y = y/l;
	      z = z/l;
     }
     
     Vector3 normalize()
     {
    	 float l = Length();
    	 
    	 Vector3 v = new Vector3(x/l, y/l, z/l);
    	 return v;
     }

     float Length()
     {
	     return FloatMath.sqrt(x*x + y*y + z*z);
    	 //return java.lang.Math.sqrt(x*x + y*y + z*z);
     }

     static Vector3 CrossProduct(Vector3 a, Vector3 b)
     {
    	 Vector3 result = new Vector3(0,0,0);
    	 
    	 result.x= (a.y*b.z) - (a.z*b.y);
    	 result.y= (a.z*b.x) - (a.x*b.z);
    	 result.z= (a.x*b.y) - (a.y*b.x);
    	 
    	 return result;
     }
     
     static float dotProduct(Vector3 a, Vector3 b) {
    	 return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
     }
     
     float length() {
	     return FloatMath.sqrt(x*x + y*y + z*z);
     }
     
     public String toString() 
     {
    	 String sX = Float.toString(x);
    	 String sY = Float.toString(y);
    	 String sZ = Float.toString(z);
    	 
    	 return "{ x: " + sX + ", y: " + sY + ", z: " + sZ + " } ";
     }
       
}
