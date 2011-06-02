package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

public class React implements ApplicationListener
{
    final static String TAG = "React::React";

    GL10 gl;
    OrthographicCamera cam;
    FPSLogger fpsLogger;
    Simulation sim;

    public void create ()
    {
        /// @todo find a proper scale
        float foo = 0.25f;
        cam = new OrthographicCamera((int)(9 * foo), (int)(16 * foo));

        fpsLogger = new FPSLogger();
        gl = Gdx.graphics.getGL10();

        gl.glClearColor(0, 0, 0, 0);

        sim = new Simulation(cam);

    }

    public void render ()
    {
        //fpsLogger.log();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        cam.update();
        cam.apply(gl);

        sim.update();
        sim.render();
    }

    public void resize (int w, int h)
    {
    }

    public void pause ()
    {
    }

    public void resume ()
    {
    }

    public void dispose ()
    {
        sim.dispose();
    }

}
