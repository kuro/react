
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

    {
        initialPosition = new Vector2();
        initialAngle = 0.0f;
        initialLinearVelocity = new Vector2(0.0f, 0.0f);
        /// @todo undo me
        initialAngularVelocity = -1.0f;

    }

    public Item (String name)
    {
        super(name);
        System.out.println("created item: " + name);
    }

    @Override
    public Actor hit (float x, float y)
    {
        return null;
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
        if (texture == null) {
            return;
        }

        x = body.getPosition().x;
        y = body.getPosition().y;

        // center it
        float xOffset = x + (-0.5f * width);
        float yOffset = y + (-0.5f * height);

        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.draw(texture,
                   xOffset, yOffset,                        // x, y
                   width * 0.5f, height * 0.5f,             // origin x, y
                   width, height,                           // w, h in pixels
                   1.0f, 1.0f,                              // scale x, y
                   body.getAngle()*180.0f/(float)Math.PI,   // rotation

                   0, 0,                                    // src x, y
                   texture.getWidth(), texture.getHeight(), // src w, h

                   false, false                             // flip x, y

                  );
    }

    public void reset ()
    {
        body.setTransform(initialPosition, initialAngle);
        body.setLinearVelocity(initialLinearVelocity);
        body.setAngularVelocity(initialAngularVelocity);
    }

}
