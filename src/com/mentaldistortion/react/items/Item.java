
package com.mentaldistortion.react.items;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.physics.box2d.*;

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

    public Body body;
    public Fixture fixture;

    public final Vector2 initialPosition;
    public float initialAngle;
    public final Vector2 initialLinearVelocity;
    public float initialAngularVelocity;

    public Texture texture;

    boolean pressed;
    int pointer;

    {
        initialPosition = new Vector2();
        initialAngle = 0.0f;
        initialLinearVelocity = new Vector2(0.0f, 0.0f);
        /// @todo undo me
        initialAngularVelocity = -1.0f;

        pressed = false;
        pointer = -1;
    }

    public Item (String name)
    {
        super(name);
        Gdx.app.log(TAG, "created item: " + name);
    }

    @Override
    public Actor hit (float x, float y)
    {
        return x > 0 && y > 0 && x < width && y < height ? this : null;
    }

    @Override
    protected boolean touchDown (float x, float y, int pointer) 
    {
        pressed = x > 0 && y > 0 && x < width && y < height;

        if (pressed) {
            parent.focus(this, pointer);
            this.pointer = pointer;
        }

        Gdx.app.log(TAG, name + ": touch down: " + x + ", " + y);
        return pressed;
    }

    @Override
    protected boolean touchDragged (float x, float y, int pointer) 
    {
        if (!pressed) {
            return false;
        }

        Gdx.app.log(TAG, name + ": touch dragged: " + x + ", " + y);
        return pressed;
    }

    @Override
    protected boolean touchUp (float x, float y, int pointer) 
    {
        if (!pressed) {
            return false;
        }

        Gdx.app.log(TAG, name + ": touch up: " + x + ", " + y);

        if (this.pointer == pointer) {
            parent.focus(null, pointer);
        }

        pressed = false;

        return true;
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

        rotation = (180.0f / (float)Math.PI) * body.getAngle() ;
    }

    public void reset ()
    {
        body.setTransform(initialPosition, initialAngle);
        body.setLinearVelocity(initialLinearVelocity);
        body.setAngularVelocity(initialAngularVelocity);
    }

    public void contacted (Item other)
    {
        Gdx.app.log(TAG, id + " contacted " + other.id);
    }
}
