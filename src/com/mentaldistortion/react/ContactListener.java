
package com.mentaldistortion.react;

import com.badlogic.gdx.physics.box2d.*;

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
        System.out.println(contact);
    }

    public void endContact (Contact contact)
    {
    }
}
