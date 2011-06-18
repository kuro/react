package com.mentaldistortion.react.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class ReactScreen
    implements Screen
{
    Game game;

    public ReactScreen (Game game)
    {
        this.game = game;
    }

	@Override
    public void resize (int width, int height) {
	}

	@Override
    public void pause () {
	}

	@Override
    public void resume () {
	}

	@Override
    public void dispose () {
	}
    
}
