
package com.mentaldistortion.react.items;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class Launcher
    extends Item
{
    final static String TAG = "React::Item::Launcher";

    public Launcher (String name)
    {
        super(name);
    }

    public void contacted (Item other)
    {
        if (other instanceof Ball) {
            Gdx.app.log(TAG, "apply impulse");
            Body ball = other.body;

            Vector2 impulse = new Vector2(0.0f, 0.5f);
            //ball.applyForce(impulse, ball.getWorldCenter());
            ball.applyLinearImpulse(impulse, ball.getWorldCenter());
            //ball.setLinearVelocity(impulse);
        }
    }
}
