package com.example.blacksails;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Field {
	int status;
	int flag;
	int GPS;//Gold per step
	FloatBuffer coord;
	static final float RADIUS = 100;
	static final int SIZE_TYPE = 3; 
	SimpleObject Types[] = new SimpleObject[SIZE_TYPE];
	int TypeField;
	
	
	public Field() {
		status = 1;
		flag = 0;
		GPS = 10;
		float pi = 3.141592f;
		
		ByteBuffer bufbyte = ByteBuffer.allocateDirect(4*3*10);
		bufbyte.order(ByteOrder.nativeOrder());
		
		coord=bufbyte.asFloatBuffer(); 
		
		for(float alpha = 0; alpha<2*pi; alpha+=pi/3) {
			coord.put((float)(Math.cos(alpha)*RADIUS));
			coord.put((float)(Math.sin(alpha)*RADIUS));
			coord.put(0);
		}
		
		coord.position(0);
		Types[0] = new SimpleObject(false, 0, "None", 0);
		Types[1] = new SimpleObject(false, 1, "fish", 2);
		Types[2] = new SimpleObject(true, 2, "island", 3);
		
		TypeField = (int)(Math.random()*SIZE_TYPE);
	}
	
	public void Draw(GL10 gl) {
		gl.glLineWidth(6);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3,GL10.GL_FLOAT, 0, coord);
        gl.glDrawArrays(GL10.GL_LINE_LOOP,0,6);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        
        if(TypeField != 0) Types[TypeField].Draw(gl);
        
	}

}
