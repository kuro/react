
package com.mentaldistortion.react;

import com.mentaldistortion.react.items.*;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Gdx;

class ContactListener
    implements com.badlogic.gdx.physics.box2d.ContactListener
{
    final static String TAG = "React::ContactListener";

    Maze maze;

    ContactListener (Maze maze)
    {
        this.maze = maze;
    }

    public void beginContact (Contact contact)
    {
        if (!contact.isTouching()) {
            return;
        }

        Fixture fixture0 = contact.getFixtureA();
        Fixture fixture1 = contact.getFixtureB();

        Item item0 = (Item)fixture0.getUserData();
        Item item1 = (Item)fixture1.getUserData();

        item0.contacted(item1);
    }

    public void endContact (Contact contact)
    {
    }

    public void preSolve(Contact contact, Manifold oldManifold)
    {
    }

    public void postSolve(Contact contact, ContactImpulse impulse)
    {
    }

}
