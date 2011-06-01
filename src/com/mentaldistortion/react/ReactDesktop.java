
package com.mentaldistortion.react;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class ReactDesktop
{
    public static void main (String[] args)
    {
        // FWVGA (854x480)
        new JoglApplication(new React(), "React", 480, 854, false);
    }
}
