package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

public class React implements ApplicationListener
{
    final static String TAG = "React::React";

    GL10 gl;
    OrthographicCamera cam;
    FPSLogger fpsLogger;
    Stage stage;

    @Override
    public void create ()
    {
        cam = new OrthographicCamera();

        fpsLogger = new FPSLogger();
        gl = Gdx.graphics.getGL10();

        gl.glClearColor(0, 0, 0, 0);

        stage = new Stage(cam);

    }

    @Override
    public void render ()
    {
        //fpsLogger.log();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        cam.update();
        cam.apply(gl);

        stage.update();
        stage.render();
    }

    @Override
    public void resize (int w, int h)
    {
        float aspect = ((float)h) / ((float)w);

        cam.viewportWidth  = stage.width;
        cam.viewportHeight = stage.width * aspect;
    }

    @Override
    public void pause ()
    {
    }

    @Override
    public void resume ()
    {
    }

    @Override
    public void dispose ()
    {
        stage.dispose();
    }

}
