package com.example.projectkim;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class PKGameView extends GLSurfaceView
{
	public PKGameView(Context context)
	{
		super(context);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	}
}
