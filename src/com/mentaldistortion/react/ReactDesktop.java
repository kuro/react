
package com.mentaldistortion.react;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.backends.jogl.JoglApplication;

import com.mentaldistortion.react.screens.SelectScreen;

public class ReactDesktop
    extends Game
{
    public static void main (String[] args)
    {
        // FWVGA (854x480)
        new JoglApplication(new ReactDesktop(), "React", 480, 854, false);
    }

    @Override
    public void create ()
    {
        setScreen(new SelectScreen(this));
    }
}
