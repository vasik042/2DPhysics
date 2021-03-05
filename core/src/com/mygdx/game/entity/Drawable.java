package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drawable extends GameObject{
    public Sprite sprite;

    public Drawable(float x, float y, String path) {
        super(x, y);
        this.sprite = new Sprite(new Texture(path));
        sprite.setOriginCenter();
    }

    public void draw(SpriteBatch batch){
        sprite.setOriginBasedPosition(x, y);
        sprite.draw(batch);
    }
}
