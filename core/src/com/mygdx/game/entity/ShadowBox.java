package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ShadowBox  extends Image {

    public ShadowBox(float x, float y, float width, float height, World world) {
        super(new Texture("0.png"));
        this.setSize(width,height);
        this.setPosition(x, y);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);

        BodyDef bd = new BodyDef();
        bd.position.set(this.getX(),this.getY());
        bd.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bd);

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width/2, height/2, new Vector2(0, 0), 0);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;

        body.createFixture(fd);

        ps.dispose();
    }
}
