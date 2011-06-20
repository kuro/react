package com.mentaldistortion.react.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;

public abstract class ReactScreen
    implements Screen, InputProcessor
{
    Game game;

    public ReactScreen (Game game)
    {
        this.game = game;
    }

	@Override
    public void resize (int width, int height)
    {
	}

	@Override
    public void pause ()
    {
	}

	@Override
    public void resume ()
    {
	}

	@Override
    public void dispose ()
    {
        System.out.println("dispose");
	}





    @Override
    public boolean keyDown (int keyCode)
    {
        return false;
    }

    @Override
    public boolean keyTyped (char character)
    {
        return false;
    }

    @Override
    public boolean keyUp (int keyCode)
    {
        return false;
    }

    @Override
    public boolean scrolled (int amount)
    {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer)
    {
        return false;
    }

    @Override
    public boolean touchMoved (int x, int y)
    {
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button)
    {
        return false;
    }
}
