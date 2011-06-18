package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actors.*;

import com.badlogic.gdx.math.Vector2;

public class React
    extends InputAdapter
    implements Screen
{
    final static String TAG = "React::React";

    FPSLogger fpsLogger;

    Stage stage;
    Maze maze;

    Stage ui;
    Group topBar;

    float dt;

    Vector2 prevWorldPos;

    {
        dt = 1.0f / 60.0f;
        prevWorldPos = null;
    }

    static float expMovAvg (float avg, float newValue, float n)
    {
        float alpha = 2.0f / (n + 1.0f);
        float retval = (newValue * alpha) + (avg * (1.0f - alpha));
        return retval;
    }

    @Override
    public void show ()
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
        gl.glClearColor(0.3f, 0.3f, 0.3f, 0);

        // input multiplexing
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        inputMultiplexer.addProcessor(ui);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);

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
                maze.reset();
            }
        };
        topBar.addActor(playButton);
        Gdx.app.log(TAG, playButton.toString());

        // maze
        maze = new Maze();
        stage.addActor(maze);

        fpsLogger = new FPSLogger();

    }

    @Override
    public void render (float delta)
    {
        GL10 gl = Gdx.graphics.getGL10();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        dt = expMovAvg(dt, Gdx.graphics.getDeltaTime(), 10);

        maze.update(dt);

        stage.act(dt);
        stage.draw();

        ui.act(dt);
        ui.draw();

        fpsLogger.log();
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

    @Override
    public void hide ()
    {
    }

    @Override
    public boolean scrolled (int amount)
    {
        maze.y += amount * 0.02;
        return true;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button)
    {
        prevWorldPos = new Vector2();
        stage.toStageCoordinates(x, y, prevWorldPos);
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer)
    {
        if (prevWorldPos == null) {
            return false;
        }

        Vector2 curWorldPos = new Vector2();
        stage.toStageCoordinates(x, y, curWorldPos);

        Vector2 deltaWorldPos = prevWorldPos.sub(curWorldPos);

        maze.x -= deltaWorldPos.x;
        maze.y -= deltaWorldPos.y;

        prevWorldPos = curWorldPos;
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button)
    {
        if (prevWorldPos == null) {
            return false;
        } else {
            prevWorldPos = null;
            return true;
        }
    }

}
