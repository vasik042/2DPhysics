package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entity.hitBoxes.*;
import com.mygdx.game.entity.hitBoxes.Movable.Movable;

import java.util.ArrayList;

public class Player extends Movable{
    boolean isMoving = false;

    LineCollision l;

    public Player(Collision b) {
        super(b);
        l = new LineCollision(x, y, x+82, y);
    }

    public void live(SpriteBatch batch, ArrayList<Collision> collisions, ArrayList<Movable> movables) throws NoSuchFieldException, IllegalAccessException {
        control();
        setPos();
        collision(batch, collisions, movables);

        l.x = x;
        l.y= y;
        l.rotation = rotation;
        l.sprite.setRotation(-rotation);
        l.draw(batch);
        l.sprite.setColor(1, 1, 1, 1);
        sprite.setColor(1, 1, 1, 1);

        draw(batch);
    }

    private void collision(SpriteBatch batch, ArrayList<Collision> collisions, ArrayList<Movable> movables) throws NoSuchFieldException, IllegalAccessException {
        for (Movable m: movables) {
            collision(m);
            ArrayList<float[]> collisionPoints = collision.findCollisionPoints(m.collision);
            if(!collisionPoints.isEmpty()){
                for (float[] f:collisionPoints) {
                    Sprite s = new Sprite(new Texture("1.png"));
                    s.setSize(10, 10);
                    s.setColor(1, 0, 0, 1);
                    s.setOriginCenter();
                    s.setOriginBasedPosition(f[0], f[1]);
                    s.draw(batch);
                }
            }
        }

        for (Collision c: collisions) {
            collision(c);
            ArrayList<float[]> collisionPoints = collision.findCollisionPoints(c);
            if(!collisionPoints.isEmpty()){
                for (float[] f:collisionPoints) {
                    Sprite s = new Sprite(new Texture("1.png"));
                    s.setSize(10, 10);
                    s.setColor(1, 0, 0, 1);
                    s.setOriginCenter();
                    s.setOriginBasedPosition(f[0], f[1]);
                    s.draw(batch);
                }
            }
        }
    }

    public void control(){

//        changeSpeed(180, 0.5f);

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            changeSpeed(270, 1);

            isMoving = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            changeSpeed(90, 1);

            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            changeSpeed(180, 1);

            isMoving = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            changeSpeed(0, 1);

            isMoving = true;
        }
        if(speed > 8){
            speed = 8;
        }
        if(speed < -8){
            speed = -8;
        }

        if(speed > 0.2){
            speed -= 0.2f;
        }else if (speed < -0.2){
            speed += 0.2f;
        }else {
            speed = 0.0001f;
        }
    }
}
