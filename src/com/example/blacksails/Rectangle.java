package com.example.blacksails;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Rectangle {
	
	FloatBuffer f; 
	FloatBuffer texBuffer;
	
	public Rectangle(int width, int height) {
		float []a = new float[]{ 
				 0,0,0, 
				 0,height,0, 
				 width,0,0, 
				 width,height,0 
				 };  
		
		float[] texCoords = { 
				 0.0f, 1.0f, // A. left-bottom (NEW) 
				 0.0f, 0.0f, // B. right-bottom (NEW) 
				 1.0f, 1.0f, // C. left-top (NEW) 
				 1.0f, 0.0f // D. right-top (NEW) 
				 };
		
		ByteBuffer b;
		b=ByteBuffer.allocateDirect(4*3*4); 
		b.order(ByteOrder.nativeOrder()); 
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4); 
		tbb.order(ByteOrder.nativeOrder()); 
		
		f=b.asFloatBuffer(); 
		f.put(a); 
		f.position(0);
		
		texBuffer = tbb.asFloatBuffer(); 
		texBuffer.put(texCoords); 
		texBuffer.position(0); 
	}
	
	public void Draw(GL10 gl) {
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3,GL10.GL_FLOAT, 0, f);
        
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
   	 	gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,texBuffer);
   	 	
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        
        
   	 	gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); 
   	 
   	 	gl.glDisable(GL10.GL_BLEND);
   	 	gl.glDisable(GL10.GL_TEXTURE_2D); 
	}

}
