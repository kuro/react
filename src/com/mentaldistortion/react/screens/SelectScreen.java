package com.mentaldistortion.react.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actors.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.*;

public class SelectScreen
    extends ReactScreen
{
    final static String TAG = "React::SelectScreen";

    SpriteBatch spriteBatch;
    BitmapFont font;
    Stage stage;

    LinearGroup linearGroup;

    Texture buttonTexture;

    class Level
    {
        String title;
        String file;

        public String toString ()
        {
            StringBuilder b = new StringBuilder();
            b.append("level " + title + " in " + file);
            return b.toString();
        }
    };

    abstract class CustomClickListener implements Button.ClickListener
    {
        int index;
        CustomClickListener (int index)
        {
            this.index = index;
        }
    };

    Vector<Level> levels;

    {
        levels = new Vector<Level>();
    }

    DefaultHandler handler = new DefaultHandler()
    {
        StringBuilder builder;

        @Override
        public void startDocument ()
            throws SAXException
        {
            super.startDocument();
            builder = new StringBuilder();
        }

        @Override
        public void endDocument ()
            throws SAXException
        {
            super.endDocument();
        }

        @Override
        public void startElement (String uri, String localName,
                                  String name, Attributes attributes)
            throws SAXException
        {
            super.startElement(uri, localName, name, attributes);
            if (name.equals("maze")) {
                Level level = new Level();
                level.title = attributes.getValue("title");
                level.file = attributes.getValue("file");
                levels.addElement(level);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            super.characters(ch, start, length);
            builder.append(ch, start, length);
        }

        @Override
        public void endElement (String uri, String localName, String name)
            throws SAXException
        {
            super.endElement(uri, localName, name);
            builder.setLength(0);
        }
    };

    public SelectScreen (Game game)
    {
        super(game);
    }

    void parse (InputStream input)
        throws Exception
    {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(input, handler);
    }

    @Override
    public void show ()
    {
        // parse manifest
        try {
            FileHandle hdl = Gdx.files.internal("mazes/manifest.xml");
            parse(hdl.read());
        } catch (Exception e) {
            Gdx.app.log(TAG, "oops: ", e);
            throw new RuntimeException(e);
        }

        /// @todo find a better font
        font = new BitmapFont();

        spriteBatch = new SpriteBatch();

        // create stage
        stage = new Stage(Gdx.graphics.getWidth(),
                          Gdx.graphics.getHeight(),
                          false);

        // create layout
        linearGroup = new LinearGroup("lin", 10, 10,
                                      LinearGroup.LinearGroupLayout.Vertical);
        stage.addActor(linearGroup);


        buttonTexture = new Texture(Gdx.files.internal("ui/level.png"));

        float w = buttonTexture.getWidth();
        float h = buttonTexture.getHeight();

        int i = 0;
        for (Level level : levels) {
            BoundGroup group = new BoundGroup("foo", w, h);

            Button button = new Button(level.title, buttonTexture);
            group.addActor(button);

            button.clickListener = new CustomClickListener(i)
            {
                @Override
                public void clicked (Button button)
                {
                    load(index);
                }
            };

            Label label = new Label(level.title, font);
            label.setText(level.title);
            label.color.set(Color.GREEN);
            label.touchable = false;
            group.addActor(label);

            label.x = (w - label.getPrefWidth() ) * 0.5f;
            label.y = (h - label.getPrefHeight()) * 0.5f;

            linearGroup.addActor(group);

            i++;
        }

        // center menu
        linearGroup.x = (Gdx.graphics.getWidth()  - w      ) * 0.5f;
        linearGroup.y = (Gdx.graphics.getHeight() - (h * i)) * 0.5f;

        // input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
    }

    @Override
    public void hide ()
    {
        stage.dispose();
        spriteBatch.dispose();
        font.dispose();
        buttonTexture.dispose();
    }

    @Override
    public void render (float dt)
    {
        GL10 gl = Gdx.graphics.getGL10();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    void load (int index)
    {
        Level level = levels.elementAt(index);
        game.setScreen(new MazeScreen(game, level.file));
    }

    @Override
    public boolean keyDown (int keyCode)
    {
        switch (keyCode)
        {
        case Input.Keys.ESCAPE:
        case Input.Keys.BACK:
            if (Gdx.app.getType() == Application.ApplicationType.Android) {
                Gdx.app.exit();
            }
            return true;
        default:
            return false;
        }
    }

}
