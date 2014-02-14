package com.example.blacksails;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.util.Log;

public class MyRenderer implements Renderer {
	static final int SIZE_TEXTURES = 6;
	
	static public int[] texture_name={ 
		R.drawable.sea,
		R.drawable.riba,
		R.drawable.fish,
		R.drawable.land,
		R.drawable.ship_1,
		R.drawable.pirate_ship
		 }; 
	
	static public int[] textures = new int[SIZE_TEXTURES];
	
	Context con;
	float WinWid=1280,WinHei=800;
	float ScreenWidth,ScreenHeight;
	final int SIZE_FIELD = 20;
	
	Field GameField[][] = new Field[SIZE_FIELD][SIZE_FIELD];
	Rectangle Sea = new Rectangle(500, 500);
	boolean FlagDown = false;
	boolean FlagMove = false;
	float touch_x = 0, touch_y = 0; 
	float center_x = 0, center_y = 0;
	float start_x = 0, start_y = 0;
	int DownFrame = 0;
	Ship ship_one = new Ship(5,5, //Позиция корабля
							1, //Тип корабля
							"The Best", //Наименование типа
							2, //Радиус обзора
							2, //Очки передвижения
							2, //Урон
							10, //Жизни
							5); // id текстуры
	
	int frame = 0;
	int temp_fps;
	long start_time = System.currentTimeMillis();
	
	
	public MyRenderer(Context c) {
		con = c;
	}
	
	public void loadGLTexture(GL10 gl) { 
		 gl.glGenTextures(SIZE_TEXTURES,textures,0); 
	 	 
		 for(int i=0;i<SIZE_TEXTURES;i++) {
			 gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]); 
			 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		 	 InputStream is = con.getResources().openRawResource(texture_name[i]); 
		 	 Bitmap bitmap = BitmapFactory.decodeStream(is); 
		 	 GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0); 
		 	 bitmap.recycle(); 
		 }
	
	} 

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPushMatrix();
		gl.glTranslatef(center_x, center_y, 0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++) {
				gl.glPushMatrix();
				gl.glTranslatef(500*i, 500*j, -1);
				Sea.Draw(gl);
				gl.glPopMatrix();
			}
		
		for(int i=0;i<SIZE_FIELD;i++)
			for(int j=0;j<SIZE_FIELD;j++) {
				gl.glPushMatrix();
				gl.glTranslatef((3*Field.RADIUS/2)*j, 1.73205f*Field.RADIUS*i + (1.73205f*Field.RADIUS/2)*(j%2), 0);
				GameField[i][j].Draw(gl);
				gl.glPopMatrix();
			}
		
		gl.glPushMatrix();
		//gl.glTranslatef((3*Field.RADIUS/2)*ship_one.position[0], 1.73205f*Field.RADIUS*ship_one.position[1] + (1.73205f*Field.RADIUS/2)*(ship_one.position[0]%2), 1);
		gl.glTranslatef(ship_one.coordinates[0],ship_one.coordinates[1], 1);
		ship_one.Draw(gl);
		gl.glPopMatrix();
		
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_DEPTH_TEST);
		
		if(Math.abs(start_x - touch_x) < 10 && FlagDown) {
			DownFrame++;
		}
		else {
			DownFrame = 0;
		}
		
		if(DownFrame > temp_fps/2 && FlagMove != true) {
			Point p = ConvertToReal(start_x, start_y);
			boolean logic = ship_one.Check(p.x, p.y, 100);
			if(logic) {
				Log.v("Check", "Yes");
				FlagMove = true;
			}
			else Log.v("Check", "No");
		}
		
		FPS();
	}	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		ScreenWidth = (float)width;
		ScreenHeight = (float)height;
		Log.v("Screen", ScreenWidth + " | " + ScreenHeight);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		// TODO Auto-generated method stub
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-WinWid/2, WinWid/2, -WinHei/2, WinHei/2, -100, 100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		for(int i=0;i<SIZE_FIELD;i++)
			for(int j=0;j<SIZE_FIELD;j++) {
				GameField[i][j] = new Field();
			}
		loadGLTexture(gl);
	}
	
	public Point ConvertToReal(float x, float y) {
		float PX = WinWid/ScreenWidth;
		float PY = WinHei/ScreenHeight;
		float real_x = x*PX-WinWid/2-center_x;
		float real_y = -(y*PY-WinHei/2+center_y);
		Log.v("RealTouch", real_x + " | " + real_y);
		
		return(new Point(real_x,real_y));
	}
	
	public void Touch(float x, float y) {
		float PX = WinWid/ScreenWidth;
		float PY = WinHei/ScreenHeight;
		/*float real_x = x*PX-WinWid/2-center_x;
		float real_y = -(y*PY-WinHei/2+center_y);*/
		
		
		if(FlagDown && !FlagMove) {
			center_x -= (touch_x - x)*PX;
			center_y += (touch_y - y)*PY;
			touch_x = x; touch_y = y;

		}
		else {
			if(FlagMove && FlagDown) {
				ship_one.coordinates[0] -= (touch_x - x)*PX;
				ship_one.coordinates[1] += (touch_y - y)*PY;
				touch_x = x; touch_y = y;
			}
			else {
				if(FlagMove) {
					FlagMove = false;
					int j = (int)Math.round(ship_one.coordinates[0]/(3*Field.RADIUS/2));
					int i;
					if(j%2 == 0) {
						i = (int)Math.round(ship_one.coordinates[1]/(1.73205f*Field.RADIUS));
					}
					else {
						i = (int)Math.round((ship_one.coordinates[1]-1.73205f*Field.RADIUS/2)/(1.73205f*Field.RADIUS));
					}
					ship_one.Move(j, i);
				}
				touch_x = x; touch_y = y;
			}
		}
	}
	
	public void FPS() {
		long current_time = System.currentTimeMillis();
		frame++;
		if(current_time - start_time >= 1000) {
			Log.v("FPS", frame + "");
			start_time = current_time;
			temp_fps = frame;
			frame = 0;
		}
		
	}

}
