package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actors.*;

import com.badlogic.gdx.math.Vector3;

public class React
    implements ApplicationListener
{
    final static String TAG = "React::React";

    FPSLogger fpsLogger;

    Stage stage;
    Maze maze;

    Stage ui;
    Group topBar;

    float dt;

    {
        dt = 1.0f / 60.0f;
    }

    static float expMovAvg (float avg, float newValue, float n)
    {
        float alpha = 2.0f / (n + 1.0f);
        float retval = (newValue * alpha) + (avg * (1.0f - alpha));
        return retval;
    }

    @Override
    public void create ()
    {
        GL10 gl = Gdx.graphics.getGL10();

        stage = new Stage(Gdx.graphics.getWidth(),
                          Gdx.graphics.getHeight(),
                          false);

        // stage ui
        ui = new Stage(Gdx.graphics.getWidth(),
                       Gdx.graphics.getHeight(),
                       false);

        topBar = new Group("topBar");
        ui.addActor(topBar);

        // OpenGL
        gl.glClearColor(0, 0, 0, 0);

        // input multiplexing
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(ui);

        // button
        Button playButton = new Button(
            "playButton",
            new Texture(Gdx.files.internal("ui/play.png"))
            );
        playButton.clickListener = new Button.ClickListener()
        {
            @Override
            public void clicked (Button button)
            {
                Gdx.app.log(TAG, "play");
            }
        };
        topBar.addActor(playButton);
        Gdx.app.log(TAG, playButton.toString());

        // maze
        maze = new Maze();
        stage.addActor(maze);

//
//        fpsLogger = new FPSLogger();
//        gl = Gdx.graphics.getGL10();
//
//
//        maze = new Maze(cam);
//
//        /// @bug Drag scrolling is really jerky
//        inputMultiplexer.addProcessor(new InputAdapter() {
//            Vector3 prevWorldPos;
//            {
//                prevWorldPos = null;
//            }
//            @Override
//            public boolean scrolled (int amount)
//            {
//                cam.position.y += amount * 0.02;
//                return true;
//            }
//            @Override
//            public boolean touchDragged (int x, int y, int pointer)
//            {
//                if (prevWorldPos == null) {
//                    return false;
//                }
//                Vector3 curWorldPos = new Vector3(x, y, 0);
//                cam.unproject(curWorldPos);
//
//                Vector3 deltaWorldPos = prevWorldPos.sub(curWorldPos);
//
//                cam.position.add(deltaWorldPos);
//
//                prevWorldPos = curWorldPos;
//                return true;
//            }
//            @Override
//            public boolean touchDown (int x, int y, int pointer, int button)
//            {
//                prevWorldPos = new Vector3(x, y, 0);
//                cam.unproject(prevWorldPos);
//                return true;
//            }
//            @Override
//            public boolean touchUp (int x, int y, int pointer, int button)
//            {
//                prevWorldPos = null;
//                return true;
//            }
//        });

    }

    @Override
    public void render ()
    {
        GL10 gl = Gdx.graphics.getGL10();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        dt = expMovAvg(dt, Gdx.graphics.getDeltaTime(), 10);

        maze.update(dt);

        stage.act(dt);
        stage.draw();

        ui.act(dt);
        ui.draw();

        //fpsLogger.log();
    }

    @Override
    public void resize (int w, int h)
    {
        GL10 gl = Gdx.graphics.getGL10();

        gl.glViewport(0, 0, w, h);

        // stage
        float aspect = ((float)h) / ((float)w);
        stage.setViewport(maze.size.x * 2.0f,
                          maze.size.x * 2.0f * aspect,
                          false);

        /// @bug is there a better way to fix the camera position?
        Camera cam = stage.getCamera();
        cam.position.set(0, 0, 0);

        // ui
        ui.setViewport(w, h, false);

        // position bars
        /// @bug find a way around the hard coded values
        topBar.height = 64;
        topBar.y = h - topBar.height;
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
//        maze.dispose();
    }

}
