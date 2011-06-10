
package com.mentaldistortion.react;

import com.mentaldistortion.react.items.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Texture;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

class MazeLoader extends DefaultHandler implements Disposable
{
    final static String TAG = "React::MazeLoader";

    /**
     * Associated with a prototype.
     */
    class ShapeInfo
    {
        String id;
        String type; // ball|box
        Shape shape;
        float density;
        float restitution;
        String textureId;

        float width;
        float height;

        {
            density = 0.0f;
            restitution = 0.0f;
            textureId = null;
        }

        /**
         * Only for things that should not persist into the game.
         */
        void dispose ()
        {
            shape.dispose();
        }
    }

    Map<String, ShapeInfo> shapes;
    Maze maze;
    World world;
    StringBuilder builder;

    MazeLoader (Maze maze, World world)
    {
        this.maze = maze;
        this.world = world;
    }

    @Override
    public void dispose ()
    {
    }

    void parse (InputStream input)
        throws Exception
    {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(input, this);
    }

    Vector2 to_vec2 (String str)
    {
        Vector2 v = new Vector2();
        String[] ary = str.split(",[ ]*");
        v.set(Float.parseFloat(ary[0]), Float.parseFloat(ary[1]));
        return v;
    }

    Vector3 to_vec3 (String str)
    {
        Vector3 v = new Vector3();
        String[] ary = str.split(",[ ]*");
        v.set(Float.parseFloat(ary[0]),
              Float.parseFloat(ary[1]),
              Float.parseFloat(ary[2]));
        return v;
    }

    @Override
    public void startDocument ()
        throws SAXException
    {
        super.startDocument();
        shapes = new HashMap<String, ShapeInfo>();
        builder = new StringBuilder();
    }

    @Override
    public void endDocument ()
        throws SAXException
    {
        super.endDocument();
        for (ShapeInfo def : shapes.values()) {
            def.shape.dispose();
        }
    }

    @Override
    public void startElement (String uri, String localName,
                              String name, Attributes attributes)
        throws SAXException
    {
        super.startElement(uri, localName, name, attributes);
        String tmp;
        if (name.equals("maze")) {
            maze.size.set(to_vec2(attributes.getValue("size")));
        }
        if (name.equals("texture")) {
            String id = attributes.getValue("id");
            String path = attributes.getValue("path");
            Texture tex = new Texture(Gdx.files.internal(path));
            maze.textures.put(id, tex);
        }
        if (Pattern.matches("ball|box|goal", name)) {
            ShapeInfo def = new ShapeInfo();

            def.id = attributes.getValue("id");
            def.type = name;

            def.textureId = attributes.getValue("textureId");

            if (name.equals("ball")) {
                def.shape = new CircleShape();

                // radius
                float radius = Float.parseFloat(attributes.getValue("radius"));
                def.shape.setRadius(radius);
                def.width = def.height = 2.0f * radius;
            } else if (Pattern.matches("box|goal", name)) {
                def.shape = new PolygonShape();

                Vector2 halfSize = to_vec2(attributes.getValue("size"));

                ((PolygonShape)def.shape).setAsBox(halfSize.x, halfSize.y);
                def.width  = 2.0f * halfSize.x;
                def.height = 2.0f * halfSize.y;
            } else {
                throw new RuntimeException("oops");
            }

            // density
            tmp = attributes.getValue("density");
            if (tmp != null) {
                def.density = Float.parseFloat(tmp);
            }

            // restitution
            tmp = attributes.getValue("restitution");
            if (tmp != null) {
                def.restitution = Float.parseFloat(tmp);
            }

            shapes.put(def.id, def);
        }
        if (name.equals("instance")) {
            String id = attributes.getValue("id");
            String ref = attributes.getValue("ref");

            ShapeInfo shapeInfo = shapes.get(ref);

            BodyDef bd = new BodyDef();

            bd.allowSleep = false;

            if (shapeInfo.density > 0.0f) {
                bd.type = BodyDef.BodyType.DynamicBody;
            }

            bd.position.set(to_vec2(attributes.getValue("pos")));

            Body body = world.createBody(bd);

            FixtureDef fd = new FixtureDef();
            fd.shape = shapeInfo.shape;

            if (shapeInfo.density > 0.0f) {
                fd.density = shapeInfo.density;
            }

            if (shapeInfo.restitution > 0.0f) {
                fd.restitution = shapeInfo.restitution;
            }

            if (shapeInfo.type.equals("goal")) {
                fd.isSensor = true;
            }

            Fixture fixture = body.createFixture(fd);

            // item
            Item item = null;
            if (shapeInfo.type.equals("ball")) {
                item = new Ball(id);
            } else if (shapeInfo.type.equals("goal")) {
                item = new Goal(id);
            } else {
                item = new Item(id);
            }
            item.id = id;
            item.ref = ref;
            item.body = body;
            item.fixture = fixture;
            item.width = shapeInfo.width;
            item.height = shapeInfo.height;
            item.initialPosition.set(bd.position);
            if (shapeInfo.textureId != null) {
                item.texture = maze.textures.get(shapeInfo.textureId);
            }
            item.initialize();
            item.reset();

            body.setUserData(item);
            fixture.setUserData(item);

            maze.addActor(item);
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

        if (name.equals("title")) {
            String title = builder.toString();
            Gdx.app.log(TAG, "title: " + title);
        }
    }

}
