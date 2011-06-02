
package com.mentaldistortion.react;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import com.badlogic.gdx.physics.box2d.*;

import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

class StageLoader extends DefaultHandler implements Disposable
{
    final static String TAG = "React::StageLoader";

    class ShapeInfo
    {
        String id;
        String type; // ball|box
        Shape shape;
        float density;
        float restitution;

        {
            density = 0.0f;
            restitution = 0.0f;
        }

        void dispose ()
        {
            shape.dispose();
        }
    }

    Map<String, ShapeInfo> shapes;
    Stack<String> stack;

    World world;

    {
        shapes = new HashMap<String, ShapeInfo>();
        stack = new Stack<String>();
    }

    StageLoader (World world)
    {
        this.world = world;
    }

    public void dispose ()
    {
    }

    void parse (InputStream input) throws Exception
    {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(input, this);
    }

    public void startElement (String uri, String localName,
                              String name, Attributes attributes)
    {
        String tmp;
        stack.push(name);
        if (name.equals("box") || name.equals("ball")) {
            ShapeInfo def = new ShapeInfo();

            def.id = attributes.getValue("id");
            def.type = name;

            if (name.equals("ball")) {
                def.shape = new CircleShape();

                // radius
                String radius = attributes.getValue("radius");
                def.shape.setRadius(Float.parseFloat(radius));
            } else if (name.equals("box")) {
                def.shape = new PolygonShape();

                String[] size =
                    attributes.getValue("size").split(",[ ]*");

                float hw = Float.parseFloat(size[0]);
                float hh = Float.parseFloat(size[1]);
                ((PolygonShape)def.shape).setAsBox(hw, hh);
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

            if (shapeInfo.density > 0.0f) {
                bd.type = BodyDef.BodyType.DynamicBody;
            }

            String[] pos = attributes.getValue("pos").split(",[ ]*");
            bd.position.set(new Vector2(Float.parseFloat(pos[0]),
                                        Float.parseFloat(pos[1])));

            Body body = world.createBody(bd);

            FixtureDef fd = new FixtureDef();
            fd.shape = shapeInfo.shape;

            if (shapeInfo.density > 0.0f) {
                fd.density = shapeInfo.density;
            }

            if (shapeInfo.restitution > 0.0f) {
                fd.restitution = shapeInfo.restitution;
            }

            Fixture fixture = body.createFixture(fd);
            fixture.setUserData(id);
        }
    }
    public void endElement (String uri, String localName, String name)
    {
        stack.pop();
    }
    public void characters(char[] ch, int start, int length)
    {
        if (stack.isEmpty()) {
            return;
        }
        if (stack.peek().equals("title")) {
            String title = new String(ch, start, length);
            Gdx.app.log(TAG, "title: " + title);
        }
    }
}
