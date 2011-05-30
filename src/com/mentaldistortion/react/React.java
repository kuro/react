package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

public class React implements ApplicationListener
{
    GL10 gl;
    FPSLogger fpsLogger;

    final static String TAG = "React";

    public void create ()
    {
        Gdx.app.log(TAG, "create");
        fpsLogger = new FPSLogger();
        gl = Gdx.graphics.getGL10();

        gl.glClearColor(0, 0, 0, 0);
    }

    public void render ()
    {
        fpsLogger.log();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void resize (int w, int h)
    {
        Gdx.app.log(TAG, "resize");
    }

    public void pause ()
    {
        Gdx.app.log(TAG, "paused");
    }

    public void resume ()
    {
        Gdx.app.log(TAG, "resumed");
    }

    public void dispose ()
    {
        Gdx.app.log(TAG, "disposed");
    }

}
