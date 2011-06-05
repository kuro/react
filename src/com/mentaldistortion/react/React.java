package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.math.Vector3;

public class React implements ApplicationListener
{
    final static String TAG = "React::React";

    GL10 gl;
    OrthographicCamera cam;
    FPSLogger fpsLogger;
    Maze maze;

    @Override
    public void create ()
    {
        cam = new OrthographicCamera();

        fpsLogger = new FPSLogger();
        gl = Gdx.graphics.getGL10();

        gl.glClearColor(0, 0, 0, 0);

        maze = new Maze(cam);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(maze);
        /// @bug Drag scrolling is really jerky
        inputMultiplexer.addProcessor(new InputAdapter() {
            Vector3 prevWorldPos;
            {
                prevWorldPos = null;
            }
            @Override
            public boolean scrolled (int amount)
            {
                cam.position.y += amount * 0.02;
                return true;
            }
            @Override
            public boolean touchDragged (int x, int y, int pointer)
            {
                if (prevWorldPos == null) {
                    return false;
                }
                Vector3 curWorldPos = new Vector3(x, y, 0);
                cam.unproject(curWorldPos);

                Vector3 deltaWorldPos = prevWorldPos.sub(curWorldPos);

                cam.position.add(deltaWorldPos);

                prevWorldPos = curWorldPos;
                return true;
            }
            @Override
            public boolean touchDown (int x, int y, int pointer, int button)
            {
                prevWorldPos = new Vector3(x, y, 0);
                cam.unproject(prevWorldPos);
                return true;
            }
            @Override
            public boolean touchUp (int x, int y, int pointer, int button)
            {
                prevWorldPos = null;
                return true;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void render ()
    {
        //fpsLogger.log();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        cam.update();
        cam.apply(gl);

        maze.update();
        maze.render();
    }

    @Override
    public void resize (int w, int h)
    {
        float aspect = ((float)h) / ((float)w);

        cam.viewportWidth  = maze.size.x * 2.0f;
        cam.viewportHeight = maze.size.x * 2.0f * aspect;
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
        maze.dispose();
    }

}
