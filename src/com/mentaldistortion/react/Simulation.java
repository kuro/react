
package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.math.collision.Ray;

import com.badlogic.gdx.physics.box2d.*;

public class Simulation implements InputProcessor, Disposable, QueryCallback
{
    final static String TAG = "React::Simulation";

    Camera cam;

    float dt;

    World world;
    Box2DDebugRenderer debugRenderer;
    Body selectedBody;

    Vector2 prevWorldPos;

    {
        selectedBody = null;
    }

    Simulation (Camera cam)
    {
        Fixture f = null;

        this.cam = cam;

        dt = 1.0f / 60.0f;

        world = new World(new Vector2(0.0f, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        // ball
        f = createBall(21.8f * 0.01f * 0.5f);
        f.setUserData("ball");

        // left
        f = createWall(new Vector2(0.0f, -5.0f),
                       new Vector2(0.0f, 5.0f),
                       new Vector2(-2.5f, 0.0f));
        f.setUserData("left wall");
        // right
        f = createWall(new Vector2(0.0f, -5.0f),
                       new Vector2(0.0f, 5.0f),
                       new Vector2(2.5f, 0.0f));
        f.setUserData("right wall");
        // bottom
        f = createWall(new Vector2(-2.5f, 0.0f),
                       new Vector2(2.5f, 0.0f),
                       new Vector2(0.0f, -5.0f));
        f.setUserData("bottom wall");

        Gdx.input.setInputProcessor(this);
    }

    Fixture createBall (float radius)
    {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body ball = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.9f;
        Fixture fixture = ball.createFixture(fixtureDef);

        shape.dispose();

        return fixture;
    }

    Fixture createWall (Vector2 v0, Vector2 v1, Vector2 position)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsEdge(v0, v1);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        Body wall = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.9f;
        Fixture fixture = wall.createFixture(fixtureDef);

        shape.dispose();

        return fixture;
    }

    static float expMovAvg (float avg, float newValue, float n)
    {
        float alpha = 2.0f / (n + 1.0f);
        float retval = (newValue * alpha) + (avg * (1.0f - alpha));
        return retval;
    }    

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

    public void dispose ()
    {
        debugRenderer.dispose();
        world.dispose();
    }

    public boolean keyTyped (char character)
    {
        Gdx.app.log(TAG, "key: " + character);
        return false;
    }

    public boolean keyDown (int keyCode)
    {
        return false;
    }

    public boolean keyUp (int keyCode)
    {
        return false;
    }

    public boolean scrolled (int amount)
    {
        Gdx.app.log(TAG, "scrolled: " + amount);
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button)
    {
        Gdx.app.log(TAG, "touchDown: (" + x + ", " + y + "), " +
                    pointer + ", " + button);

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

        return false;
    }

    public boolean touchDragged (int x, int y, int pointer)
    {
        // store prev
        Vector3 v = new Vector3(x, y, 0);
        cam.unproject(v);
        Vector2 curWorldPos = new Vector2(v.x, v.y);

        if (selectedBody != null) {
            Vector2 delta = prevWorldPos.sub(curWorldPos);
            Vector2 p = selectedBody.getPosition();
            p = p.sub(delta);
            selectedBody.setTransform(p, selectedBody.getAngle());
        }

        // store prev
        prevWorldPos = curWorldPos;

        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button)
    {
        if (selectedBody != null) {
            selectedBody = null;
        }

        // store prev
        prevWorldPos = null;

        return false;
    }

    public boolean touchMoved (int x, int y)
    {
        return false;
    }

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
