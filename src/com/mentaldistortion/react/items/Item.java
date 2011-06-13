
package com.mentaldistortion.react.items;

import com.mentaldistortion.react.Maze;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.graphics.Texture;

/**
 * Associated with an instance.
 */

public class Item
    extends Actor
{
    final static String TAG = "React::Item";

    public String id;
    public String ref;

    public Maze maze;

    public Body body;
    public Fixture fixture;

    public final Vector2 initialPosition;
    public float initialAngle;
    public final Vector2 initialLinearVelocity;
    public float initialAngularVelocity;

    public Texture texture;

    /**
     * @todo Does it make since to have this, since it is basically the same as
     * touchable?
     */
    public boolean interactive;

    boolean pressed;
    int pointer;


    Vector2 curPos;
    Vector2 prevPos;
    Vector2 deltaPos;

    {
        initialPosition = new Vector2();
        initialAngle = 0.0f;
        initialLinearVelocity = new Vector2(0.0f, 0.0f);
        /// @todo undo me
        initialAngularVelocity = -1.0f;

        interactive = false;

        pressed = false;
        pointer = -1;

        curPos = new Vector2();
        prevPos = new Vector2();
        deltaPos = new Vector2();
    }

    public Item (String name)
    {
        super(name);
        Gdx.app.log(TAG, "created item: " + name);
    }

    @Override
    public Actor hit (float x, float y)
    {
        if (!touchable) {
            return null;
        }
        return x > 0 && y > 0 && x < width && y < height ? this : null;
    }

    @Override
    protected boolean touchDown (float x, float y, int pointer) 
    {
        return false;
    }

    @Override
    protected boolean touchDragged (float x, float y, int pointer) 
    {
        return false;
    }

    @Override
    protected boolean touchUp (float x, float y, int pointer) 
    {
        return false;
    }

    @Override
    protected void draw (SpriteBatch batch, float parentAlpha)
    {
        // update with physics info
        update();

        // draw (if given a texture)
        if (texture == null) {
            return;
        }

        batch.draw(
            texture,
            x, y,                                       // x, y
            originX, originY,                           // origin x, y
            width, height,                              // w, h in pixels
            scaleX, scaleY,                             // scale x, y
            rotation,                                   // rotation

            0, 0,                                       // src x, y
            texture.getWidth(), texture.getHeight(),    // src w, h

            false, false                                // flip x, y

            );

    }

    /**
     * Call after setting fields.
     */
    public void initialize ()
    {
        originX = 0.5f * width;
        originY = 0.5f * height;

        touchable = interactive;
    }

    /**
     * Update with physics info.
     *
     * Called before rendering.
     */
    private void update ()
    {
        x = body.getPosition().x - originX;
        y = body.getPosition().y - originY;

        rotation = (180.0f / (float)Math.PI) * body.getAngle();
    }

    /**
     * @todo Should this ignore interactive items, or should initial parameters
     * be reset earlier?
     */
    public void reset ()
    {
        if (!interactive) {
            body.setTransform(initialPosition, initialAngle);
        }
        body.setLinearVelocity(initialLinearVelocity);
        body.setAngularVelocity(initialAngularVelocity);
    }

    public void contacted (Item other)
    {
        Gdx.app.log(TAG, id + " contacted " + other.id);
    }
}
