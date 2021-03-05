package com.mygdx.game.entity.hitBoxes;

import com.mygdx.game.entity.Drawable;

import java.util.ArrayList;

public class BoxCollision extends Collision {
    public float hitBoxHeight;
    public float hitBoxWidth;

    public BoxCollision(Drawable obj){
        super(obj.x, obj.y);
        hitBoxHeight = obj.sprite.getHeight()/ 2;
        hitBoxWidth = obj.sprite.getWidth()/ 2;
        sprite.setSize(hitBoxWidth * 2, hitBoxHeight * 2);
        sprite.setColor(1, 0, 1, 0.1f);
        sprite.setOriginCenter();

        collisionType = "BoxCollision";
    }

    @Override
    public boolean findBoxCollision(float x1, float y1, float width, float height) {
        return ((x + hitBoxWidth <= x1 + width && x + hitBoxWidth >= x1 - width) || (x - hitBoxWidth <= x1 + width && x - hitBoxWidth >= x1 - width)) &&
                ((y + hitBoxHeight <= y1 + height && y + hitBoxHeight >= y1 - height) || (y - hitBoxHeight <= y1 + height && y - hitBoxHeight >= y1 - height));
    }

    @Override
    public boolean findPointCollision(float x1, float y1){
        return x1 <= x + hitBoxWidth && x1 >= x - hitBoxWidth && y1 <= y + hitBoxHeight && y1 >= y - hitBoxHeight;
    }

    @Override
    public ArrayList<float[]> findLineCollisionPoints(float x1, float y1, float x2, float y2) {
        ArrayList<float[]> points = new ArrayList<>();

        float x3 = x + hitBoxWidth;
        float y3 = y + hitBoxHeight;
        float x4 = x + hitBoxWidth;
        float y4 = y - hitBoxHeight;

        float first;
        float second;

        float[] lineCollisionPoint = findLineCollisionPoint(x1, y1, x2, y2, x3, y3, x4, y4);

        if(y1 == y2) {
            first = (lineCollisionPoint[1] - y3) / (y4 - y3);
            second = (lineCollisionPoint[0] - x1) / (x2 - x1);
        }else {
            first = (lineCollisionPoint[1]-y3)/(y4-y3);
            second = (lineCollisionPoint[1]-y1)/(y2-y1);
        }

        if(first >= 0 && first <= 1 && second >= 0 && second <= 1){
            points.add(lineCollisionPoint);
        }

        x4 -= hitBoxWidth*2;
        y4 += hitBoxHeight*2;

        lineCollisionPoint = findLineCollisionPoint(x1, y1, x2, y2, x3, y3, x4, y4);
        if(x1 == x2) {
            first = (lineCollisionPoint[0] - x3) / (x4 - x3);
            second = (lineCollisionPoint[1] - y1) / (y2 - y1);
        }else {
            first = (lineCollisionPoint[0]-x3)/(x4-x3);
            second = (lineCollisionPoint[0]-x1)/(x2-x1);
        }


        if(first >= 0 && first <= 1 && second >= 0 && second <= 1){
            points.add(lineCollisionPoint);
        }

        x3 -= hitBoxWidth*2;
        y3 -= hitBoxHeight*2;

        lineCollisionPoint = findLineCollisionPoint(x1, y1, x2, y2, x3, y3, x4, y4);

        if(y1 == y2) {
            first = (lineCollisionPoint[1] - y3) / (y4 - y3);
            second = (lineCollisionPoint[0] - x1) / (x2 - x1);
        }else {
            first = (lineCollisionPoint[1]-y3)/(y4-y3);
            second = (lineCollisionPoint[1]-y1)/(y2-y1);
        }

        if(first >= 0 && first <= 1 && second >= 0 && second <= 1){
            points.add(lineCollisionPoint);
        }

        x4 += hitBoxWidth*2;
        y4 -= hitBoxHeight*2;

        lineCollisionPoint = findLineCollisionPoint(x1, y1, x2, y2, x3, y3, x4, y4);
        if(x1 == x2) {
            first = (lineCollisionPoint[0] - x3) / (x4 - x3);
            second = (lineCollisionPoint[1] - y1) / (y2 - y1);
        }else {
            first = (lineCollisionPoint[0]-x3)/(x4-x3);
            second = (lineCollisionPoint[0]-x1)/(x2-x1);
        }

        if(first >= 0 && first <= 1 && second >= 0 && second <= 1){
            points.add(lineCollisionPoint);
        }

        return points;
    }

    @Override
    public ArrayList<float[]> findBoxCollisionPoints(float x1, float y1, float width, float height) {

        //todo box in box
        ArrayList<float[]> points = new ArrayList<>();

        points.addAll(findLineCollisionPoints(x1 - width, y1 + height, x1 + width, y1 + height));
        points.addAll(findLineCollisionPoints(x1 - width, y1 - height, x1 + width, y1 - height));
        points.addAll(findLineCollisionPoints(x1 + width, y1 + height, x1 + width, y1 - height));
        points.addAll(findLineCollisionPoints(x1 - width, y1 + height, x1 - width, y1 - height));

        return points;
    }
}
