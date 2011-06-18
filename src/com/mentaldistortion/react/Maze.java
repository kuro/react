
package com.mentaldistortion.react;

import com.mentaldistortion.react.items.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.math.collision.Ray;

import com.badlogic.gdx.physics.box2d.*;

import java.io.InputStream;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.audio.*;

import java.util.Map;
import java.util.HashMap;

public class Maze
    extends Group
    implements Disposable
{
    final static String TAG = "React::Maze";

    float dt;

    Vector2 size;  ///< half extents
    World world;
    Box2DDebugRenderer debugRenderer;

    Map<String, Texture> textures;

    Actor currentActor;

    Vector2 prevPos;
    Vector2 curPos;

    boolean winnable;

    Sound winSound;

    {
        size = new Vector2();
        currentActor = null;
        textures = new HashMap<String, Texture>();

        prevPos = new Vector2();
        curPos = new Vector2();
    }

    Maze ()
    {
        super("Maze");

        Fixture f = null;

        dt = 1.0f / 60.0f;

        world = new World(new Vector2(0.0f, -9.8f), false);
        world.setContactListener(new ContactListener(this));
        debugRenderer = new Box2DDebugRenderer();

        try {
            FileHandle hdl = Gdx.files.internal("mazes/maze001.xml");
            MazeLoader loader = new MazeLoader(this, world);
            loader.parse(hdl.read());
        } catch (Exception e) {
            Gdx.app.log(TAG, "oops: ", e);
            throw new RuntimeException(e);
        }

        winSound = Gdx.audio.newSound(Gdx.files.internal("sfx/win.ogg"));
    }

    @Override
    public void dispose ()
    {
        debugRenderer.dispose();
        world.dispose();
        winSound.dispose();
    }

    void update (float dt)
    {
        world.step(dt, 3, 3);
        world.clearForces();
    }

    @Override
    protected void draw (SpriteBatch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);

        GL10 gl = Gdx.graphics.getGL10();
        gl.glDisable(gl.GL_TEXTURE_2D);

        /// @todo is it proper to apply my own transforms in this way?
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0.0f);

        debugRenderer.render(world);

        gl.glPopMatrix();
    }

    @Override
    protected boolean touchDown (float x, float y, int pointer)
    {
        boolean handled = super.touchDown(x, y, pointer);
        if (handled) {
            return handled;
        }

        currentActor = hit(x, y);

        if (currentActor != null) {
            handled = true;
            curPos.set(x, y);
            prevPos.set(x, y);
        }

        return handled;
    }

    @Override
    protected boolean touchDragged (float x, float y, int pointer)
    {
        boolean handled = super.touchDown(x, y, pointer);
        if (handled) {
            return handled;
        }

        if (currentActor != null) {
            handled = true;

            curPos.set(x, y);

            Vector2 delta = prevPos.sub(curPos);

            Body body = ((Item)currentActor).body;
            Vector2 p = body.getPosition();
            p = p.sub(delta);
            body.setTransform(p, body.getAngle());

            prevPos.set(curPos);
        }

        return handled;
    }

    @Override
    protected boolean touchUp (float x, float y, int pointer)
    {
        boolean handled = super.touchUp(x, y, pointer);
        if (handled) {
            return handled;
        }

        if (currentActor != null) {
            Gdx.app.log(TAG,
                        currentActor.name +
                        ": final position: " +
                        ((Item)currentActor).body.getPosition());
            handled = true;
            currentActor = null;
        }

        return handled;
    }

    public void reset ()
    {
        winnable = true;

        for (Actor child : getActors()) {
            if (child instanceof Item) {
                Item item = (Item)child;
                item.reset();
            }
        }
    }

    public void win ()
    {
        if (!winnable) {
            return;
        }
        winnable = false;

        Gdx.app.log(TAG, "WIN!!!");
        winSound.play();
    }

}
