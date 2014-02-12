package com.example.blacksails;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class SimpleObject {
	private static final float RADIUS = 100;
	public boolean locked;
	public int type;
	public String name;
	int texture_id;
	private FloatBuffer VertexBuffer;
	private FloatBuffer TextureBuffer;
	
	public SimpleObject(boolean locked, int type, String name, int texture_id) {
		this.locked = locked;
		this.type = type;
		this.name = name;
		this.texture_id = texture_id;
		
		float pi = 3.141592f;
		
		ByteBuffer bufbyte = ByteBuffer.allocateDirect(4*3*10);
		bufbyte.order(ByteOrder.nativeOrder());
		
		VertexBuffer=bufbyte.asFloatBuffer(); 
		
		bufbyte = ByteBuffer.allocateDirect(4*2*10);
		bufbyte.order(ByteOrder.nativeOrder());
        
        TextureBuffer=bufbyte.asFloatBuffer();
		
		for(float alpha = 0; alpha<2*pi; alpha+=pi/3) {
			VertexBuffer.put((float)(Math.cos(alpha)*RADIUS));
			VertexBuffer.put((float)(Math.sin(alpha)*RADIUS));
			VertexBuffer.put(0);
			
			TextureBuffer.put((float)(Math.cos(alpha)*0.5f+0.5f));
			TextureBuffer.put((float)(Math.sin(-alpha)*0.5f+0.5f));
		}
		
		VertexBuffer.position(0);
		TextureBuffer.position(0);
	}
	
	public void Draw(GL10 gl) {
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D,   MyRenderer.textures[texture_id]);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3,GL10.GL_FLOAT, 0, VertexBuffer);
        
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
   	 	gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,TextureBuffer);
   	 	
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN,0,6);
        

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        
   	 
   	 	gl.glDisable(GL10.GL_BLEND);
   	 	gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	

}
