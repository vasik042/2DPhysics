package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entity.hitBoxes.BoxCollision;

public class Wall extends BoxCollision{

    public Wall(float x, float y, World world, float width, float height) {
        super(new Drawable(x, y, "0.png"));
        sprite.setSize(width, height);
        hitBoxHeight = height/2;
        hitBoxWidth = width/2;
        sprite.setOriginCenter();

        new ShadowBox(x, y, sprite.getWidth(), sprite.getHeight(), world);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
