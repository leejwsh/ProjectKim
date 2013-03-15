package com.example.projectkim;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class PKGameView extends GLSurfaceView
{
	public PKGameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// For certain models only.
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	}
}
