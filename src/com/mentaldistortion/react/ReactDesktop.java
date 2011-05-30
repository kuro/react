
package com.mentaldistortion.react;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class ReactDesktop
{
    public static void main (String[] args)
    {
        new JoglApplication(new React(), "React", 960, 600, false);
    }
}
