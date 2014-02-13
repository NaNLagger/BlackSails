package com.example.blacksails;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Ship {
	float coordinates[] = new float[2];
	int position[] = new int[2];
	int type;
	String name;
	int Obzor;
	int Range;
	float Damage;
	float HitPoints;
	int texture_id;
	
	private FloatBuffer VertexBuffer;
	private FloatBuffer TextureBuffer;
	private static final float RADIUS = 100;
	
	
	public Ship(int pos_x, int pos_y, int type, String name, int Obzor, int Range, float Damage, float HitPoints, int texture_id) {
		this.position[0] = pos_x;
		this.position[1] = pos_y;
		this.coordinates[0] = (3*Field.RADIUS/2)*position[0];
		this.coordinates[1] = 1.73205f*Field.RADIUS*position[1] + (1.73205f*Field.RADIUS/2)*(position[0]%2);
		this.type = type;
		this.name = name;
		this.Obzor = Obzor;
		this.Range = Range;
		this.Damage = Damage;
		this.HitPoints = HitPoints;
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
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D,MyRenderer.textures[texture_id]);
		
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
	
	public boolean Check(float x, float y, float range) {
		x-=coordinates[0];
		y-=coordinates[1];
		if(x*x+y*y <= range*range) return true;
		else return false;
	}
	
	public boolean Move(int pos_x, int pos_y) {
		float cord_temp_x = (3*Field.RADIUS/2)*position[0];
		float cord_temp_y = 1.73205f*Field.RADIUS*position[1] + (1.73205f*Field.RADIUS/2)*(position[0]%2);
		
		float cord_new_x = (3*Field.RADIUS/2)*pos_x;
		float cord_new_y = 1.73205f*Field.RADIUS*pos_y + (1.73205f*Field.RADIUS/2)*(pos_x%2);
		
		boolean logic_move = (cord_new_x - cord_temp_x)*(cord_new_x - cord_temp_x)
							+(cord_new_y - cord_temp_y)*(cord_new_y - cord_temp_y) 
							<= ((this.Range*2)*Field.RADIUS)*((this.Range*2)*Field.RADIUS);
		
		if(logic_move) {
			position[0] = pos_x;
			position[1] = pos_y;
			this.coordinates[0] = cord_new_x;
			this.coordinates[1] = cord_new_y;
			return true;
		}
		else {
			this.coordinates[0] = cord_temp_x;
			this.coordinates[1] = cord_temp_y;
			return false;
		}
	}

}
