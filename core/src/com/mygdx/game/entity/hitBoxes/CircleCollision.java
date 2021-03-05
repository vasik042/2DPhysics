package com.mygdx.game.entity.hitBoxes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entity.Drawable;

import java.util.ArrayList;

public class CircleCollision extends Collision {
    public float radius;

    public CircleCollision(Drawable obj) {
        super(obj.x, obj.y);
        radius = obj.sprite.getWidth()/2;
        collisionType = "CircleCollision";
    }
    public CircleCollision(float x, float y, float radius) {
        super(x, y);
        sprite.setTexture(new Texture("1.png"));
        sprite.setSize(radius*2, radius*2);
        sprite.setOriginCenter();
        this.radius = radius;
        collisionType = "CircleCollision";
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public boolean findPointCollision(float x1, float y1){
        return (x - x1) * (x - x1) + (y - y1) * (y - y1) <= radius * radius;
    }

    @Override
    public ArrayList<float[]> findLineCollisionPoints(float x1, float y1, float x2, float y2) {
        ArrayList<float[]> points = new ArrayList<>();

        if(x1 == x2){
            x1 += 0.01;
        }else if(y1 == y2){
            y1 += 0.01;
        }

        float znam = (x1-x2);
        if (znam == 0){
            znam = 0.000001f;
        }
        float pC = ((x1*y2-y1*x2) - (y1 - y2)*(-x))/znam;

        float c = pC - y;

        float a = - ((y1 - y2) / (x2 - x1));

        float sqrt = (float) Math.sqrt(radius * radius * (a * a + 1) - c * c);

        float pX1 = -((a*c - sqrt))/(a*a + 1) + x;
        float pY1 = ((c + a * sqrt))/(a*a + 1) + y;

        float pX2 = -((a*c + sqrt))/(a*a + 1) + x;
        float pY2 = ((c - a * sqrt))/(a*a + 1) + y;

        float first1 = (pY1-y1)/(y2-y1);
        float second1 = (pX1-x1)/(x2-x1);

        float first2 = (pY2-y1)/(y2-y1);
        float second2 = (pX2-x1)/(x2-x1);

        if(second1 > 0 && first1 > 0 && second1 < 1 && first1 < 1){
            points.add(new float[] {pX1, pY1});
        }
        if((second2 > 0 && first2 > 0 && second2 < 1 && first2 < 1)){
            points.add(new float[] {pX2, pY2});
        }

        return points;
    }

    @Override
    public ArrayList<float[]> findCircleCollisionPoints(float x1, float y1, float radius) {
        ArrayList<float[]> points = new ArrayList<>();

        float height = (float) Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));

        if(height <= radius + this.radius){
            float sin = (x1-x)/height;
            float cos = (y1-y)/height;

            points.add(new float[]{x + this.radius * sin, y + this.radius * cos});
        }

        return points;
    }

    @Override
    public ArrayList<float[]> findBoxCollisionPoints(float x1, float y1, float width, float height) {
        ArrayList<float[]> points = new ArrayList<>();

        if(x < x1 + width && x > x1 - width && y < y1 + height && y > y1 - height){
            points.add(new float[]{x, y});
            return points;
        }
        points.addAll(findLineCollisionPoints(x1 + width, y1 + height, x1 + width, y1 - height));
        points.addAll(findLineCollisionPoints(x1 - width, y1 + height, x1 - width, y1 - height));
        points.addAll(findLineCollisionPoints(x1 + width, y1 + height, x1 - width, y1 + height));
        points.addAll(findLineCollisionPoints(x1 + width, y1 - height, x1 - width, y1 - height));

        return points;
    }
}
