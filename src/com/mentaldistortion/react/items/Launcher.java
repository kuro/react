
package com.mentaldistortion.react.items;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class Launcher
    extends Item
{
    final static String TAG = "React::Item::Launcher";

    public Vector2 impulse;

    {
        impulse = new Vector2();
    }

    public Launcher (String name)
    {
        super(name);
    }

    public void contacted (Item other)
    {
        if (other instanceof Ball) {
            Body ball = other.body;

            ball.applyLinearImpulse(impulse, ball.getWorldCenter());
        }
    }
}
