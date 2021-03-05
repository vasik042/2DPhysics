package com.mygdx.game.entity.hitBoxes;

import java.util.ArrayList;

public class LineCollision extends Collision {
    public float height;
    public float rotation;

    public LineCollision(float x1, float y1, float x2, float y2) {
        super(x1, y1);

        height = (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
        rotation = (float) (Math.asin((x2 - x1) / height)/0.0174532925);
        if(y2 < y1){
            rotation = (float) (Math.asin((x1 - x2) / height)/0.0174532925) + 180;
        }

        sprite.setSize(1, height);
        sprite.setOrigin(0, 0);
        sprite.setRotation(-rotation);
        collisionType = "LineCollision";
    }

    @Override
    public ArrayList<float[]> findLineCollisionPoints(float x1, float y1, float x2, float y2) {
        ArrayList<float[]> points = new ArrayList<>();

        float x4 = (float) (x + height*Math.sin((rotation*0.0174532925)));
        float y4 = (float) (y + height*Math.cos((rotation*0.0174532925)));

        float[] lineCollisionPoint = findLineCollisionPoint(x1, y1, x2, y2, x, y, x4, y4);

        float first;
        float second;

        if (y != y4 && y1 != y2){
            first = (lineCollisionPoint[1]-y)/(y4-y);
            second = (lineCollisionPoint[1]-y1)/(y2-y1);
        }else if(y1 == y2){
            first = (lineCollisionPoint[1]-y)/(y4-y);
            second = (lineCollisionPoint[0]-x1)/(x2-x1);
        }else {
            first = (lineCollisionPoint[0]-x)/(x4-x);
            second = (lineCollisionPoint[1]-y1)/(y2-y1);
        }

        if(first >= 0 && first <= 1 && second >= 0 && second <= 1 ){
            points.add(lineCollisionPoint);
        }

        return points;
    }

    @Override
    public ArrayList<float[]> findCircleCollisionPoints(float x3, float y3, float radius) {
        ArrayList<float[]> points = new ArrayList<>();

        float x1 = x;
        float x2 = (float) (x + height*Math.sin((rotation*0.0174532925)));
        float y1 = y;
        float y2 = (float) (y + height*Math.cos((rotation*0.0174532925)));

        if(x1 == x2){
            x1 += 0.0001;
        }else if(y1 == y2){
            y1 += 0.0001;
        }

        float znam = (x1-x2);
        if (znam == 0){
            znam = 0.000001f;
        }
        float pC = ((x1*y2-y1*x2) - (y1 - y2)*(-x3))/znam;

        float c = pC - y3;

        float a = - ((y1 - y2) / (x2 - x1));

        float sqrt = (float) Math.sqrt(radius * radius * (a * a + 1) - c * c);

        float pX1 = -((a*c - sqrt))/(a*a + 1) + x3;
        float pY1 = ((c + a * sqrt))/(a*a + 1) + y3;

        float pX2 = -((a*c + sqrt))/(a*a + 1) + x3;
        float pY2 = ((c - a * sqrt))/(a*a + 1) + y3;

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
}
