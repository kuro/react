package com.mentaldistortion.react.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.Vector;

public class SelectScreen
    extends ReactScreen
{
    final static String TAG = "React::SelectScreen";

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

    @Override
    public void show ()
    {
        try {
            FileHandle hdl = Gdx.files.internal("mazes/manifest.xml");
            parse(hdl.read());
        } catch (Exception e) {
            Gdx.app.log(TAG, "oops: ", e);
            throw new RuntimeException(e);
        }

        int i = 0;
        for (Level level : levels) {
            Gdx.app.log(TAG, "" + i + ": " + level.toString());
            i++;
        }
    }

    @Override
    public void render (float dt)
    {
    }

    @Override
    public void hide ()
    {
    }

    void parse (InputStream input)
        throws Exception
    {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(input, handler);
    }
}
