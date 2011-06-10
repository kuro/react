
package com.mentaldistortion.react.items;

public class Ball
    extends Item
{
    final static String TAG = "React::Item::Ball";

    public Ball (String name)
    {
        super(name);
    }

    /**
     * @warning This will explode if two balls collide.
     */
    public void contacted (Item other)
    {
        other.contacted(this);
    }
}
