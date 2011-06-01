
package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.physics.box2d.*;

public class Simulation implements Disposable
{
    final static String TAG = "React::Simulation";

    float dt;

    World world;
    Box2DDebugRenderer debugRenderer;

    Body ball;

    Simulation ()
    {
        dt = 1.0f / 60.0f;

        world = new World(new Vector2(0.0f, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        // ball
        createBall(21.8f * 0.01f * 0.5f);

        Vector2 c0 = new Vector2(-2.5f, -5.0f);
        Vector2 c1 = new Vector2( 2.5f, -5.0f);
        Vector2 c2 = new Vector2( 2.5f,  5.0f);
        Vector2 c3 = new Vector2(-2.5f,  5.0f);

        // left
        createWall(c0, c3);
        // right
        createWall(c1, c2);
        // bottom
        createWall(c0, c1.add(new Vector2(-1.0f, 0.0f)));
    }

    Body createBall (float radius)
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
        ball.createFixture(fixtureDef);

        shape.dispose();

        return ball;
    }

    Body createWall (Vector2 v0, Vector2 v1)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsEdge(v0, v1);

        BodyDef bodyDef = new BodyDef();
        Body wall = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.9f;
        wall.createFixture(fixtureDef);

        shape.dispose();

        return wall;
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
}
