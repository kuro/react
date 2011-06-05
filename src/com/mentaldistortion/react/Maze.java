
package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.math.collision.Ray;

import com.badlogic.gdx.physics.box2d.*;

import java.io.InputStream;

public class Maze implements InputProcessor, Disposable, QueryCallback
{
    final static String TAG = "React::Maze";

    Camera cam;

    float dt;

    Vector2 size;  ///< half extents
    World world;
    Box2DDebugRenderer debugRenderer;
    Body selectedBody;

    Vector2 prevWorldPos;

    {
        size = new Vector2();
        selectedBody = null;
    }

    Maze (Camera cam)
    {
        Fixture f = null;

        this.cam = cam;

        dt = 1.0f / 60.0f;

        world = new World(new Vector2(0.0f, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        try {
            FileHandle hdl = Gdx.files.classpath("mazes/maze001.xml");
            MazeLoader loader = new MazeLoader(this, world);
            loader.parse(hdl.read());
        } catch (Exception e) {
            Gdx.app.log(TAG, "oops: ", e);
            throw new RuntimeException(e);
        }
    }

    static float expMovAvg (float avg, float newValue, float n)
    {
        float alpha = 2.0f / (n + 1.0f);
        float retval = (newValue * alpha) + (avg * (1.0f - alpha));
        return retval;
    }    

    Vector2 lastAcc = new Vector2();

    void update ()
    {
        dt = expMovAvg(dt, Gdx.graphics.getDeltaTime(), 60);
        world.step(dt, 3, 3);
        world.clearForces();
    }

    void render ()
    {
        debugRenderer.render(world);
    }

    @Override
    public void dispose ()
    {
        debugRenderer.dispose();
        world.dispose();
    }

    @Override
    public boolean keyTyped (char character)
    {
        return false;
    }

    @Override
    public boolean keyDown (int keyCode)
    {
        return false;
    }

    @Override
    public boolean keyUp (int keyCode)
    {
        return false;
    }

    @Override
    public boolean scrolled (int amount)
    {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button)
    {
        boolean retval = false;

        Ray ray = cam.getPickRay(x, y);

        float delta = 0.0f;

        world.QueryAABB(this,
                      ray.origin.x - delta,
                      ray.origin.y - delta,
                      ray.origin.x + delta,
                      ray.origin.y + delta);

        // store prev
        Vector3 v = new Vector3(x, y, 0);
        cam.unproject(v);
        prevWorldPos = new Vector2(v.x, v.y);

        if (selectedBody != null) {
            retval = true;
        }

        return retval;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer)
    {
        boolean retval = false;

        // store prev
        Vector3 v = new Vector3(x, y, 0);
        cam.unproject(v);
        Vector2 curWorldPos = new Vector2(v.x, v.y);

        if (selectedBody != null) {
            Vector2 delta = prevWorldPos.sub(curWorldPos);
            Vector2 p = selectedBody.getPosition();
            p = p.sub(delta);
            selectedBody.setTransform(p, selectedBody.getAngle());
            retval = true;
        }

        // store prev
        prevWorldPos = curWorldPos;

        return retval;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button)
    {
        boolean retval = false;

        if (selectedBody != null) {
            selectedBody = null;
            retval = true;
        }

        // store prev
        prevWorldPos = null;

        return retval;
    }

    @Override
    public boolean touchMoved (int x, int y)
    {
        return false;
    }

    @Override
    public boolean reportFixture (Fixture fixture)
    {
        Gdx.app.log(TAG, fixture.toString() + ": " + fixture.getUserData());
        boolean toContinue = true;

        if (fixture.getBody().getType() == BodyDef.BodyType.StaticBody) {
            Gdx.app.log(TAG, "manipulate the above");
            toContinue = false;
            selectedBody = fixture.getBody();
        }

        return toContinue;
    }

}