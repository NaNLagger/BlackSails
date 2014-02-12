package com.example.blacksails;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class MainActivity extends Activity implements OnTouchListener {
	MyRenderer r;
	int width,height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		WindowManager w = getWindowManager();
	    Display d = w.getDefaultDisplay();
	    width = d.getWidth();
	    height = d.getHeight();
	    
		GLSurfaceView g=new GLSurfaceView(this);
		g.setRenderer(r = new MyRenderer(this,width,height));
        g.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        g.setOnTouchListener(this);
        setContentView(g);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		float x = event.getX();
		float y = event.getY();
		Log.v("Touch", x + " | " + y);
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				r.Touch(x, y);
				r.FlagDown = true;
				r.start_x = x;
				r.start_y = y;
			} break;
			case MotionEvent.ACTION_UP: {
				r.FlagDown = false;
			} break;
			case MotionEvent.ACTION_CANCEL: {
			} break;
			case MotionEvent.ACTION_MOVE: {
				r.Touch(x, y);
			} break;
		}
		return true;
	}

}
