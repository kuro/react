
package com.mentaldistortion.react.items;

import com.badlogic.gdx.Gdx;

public class Goal
    extends Item
{
    final static String TAG = "React::Item::Goal";

    public Goal (String name)
    {
        super(name);
    }

    public void contacted (Item other)
    {
        Gdx.app.log(TAG, "WIN!!!");
    }
}
