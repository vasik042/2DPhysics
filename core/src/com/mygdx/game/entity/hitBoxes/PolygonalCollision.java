package com.mygdx.game.entity.hitBoxes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class PolygonalCollision extends Collision {
    public ArrayList<LineCollision> lines;
    private float[][] array;
//    private ArrayList<Float> rotations;

    Sprite s = new Sprite(new Texture("1.png"));

    public PolygonalCollision(float x, float y, float[][] array) {
        super(x, y);

        s.setSize(10, 10);
        s.setColor(1, 0, 0, 1);
        s.setOriginCenter();

        lines = new ArrayList<>();
        this.array = array;
//        rotations = new ArrayList<>();
//
//        for (int i = 0; i < array.length; i++) {
//            float height = (float) Math.sqrt((array[i][0])*(array[i][0]) + (array[i][1])*(array[i][1]));
//            if(array[i][1] > 0){
//                rotations.add((float) Math.asin((array[i][0]/height)));
//            }else if (array[i][0] < 0){
//                rotations.add((float) -(Math.asin((array[i][0]/height))+3.14159));
//            }else {
//                rotations.add((float) Math.acos((array[i][1]/height)));
//            }
//
//        }

        for (int i = 0; i < array.length - 1; i++) {
            lines.add(new LineCollision(x + array[i][0], y + array[i][1], x + array[i + 1][0], y + array[i + 1][1]));
        }
        lines.add(new LineCollision(x + array[array.length - 1][0], y + array[array.length - 1][1], x + array[0][0], y + array[0][1]));

        collisionType = "PolygonalCollision";
    }

    @Override
    public boolean findCollision(Collision collision) throws NoSuchFieldException, IllegalAccessException {
        for (LineCollision l : lines) {
            if(collision.findCollision(l)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<float[]> findCollisionPoints(Collision collision) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> points = new ArrayList<>();

        for (LineCollision l : lines) {
            points.addAll(collision.findCollisionPoints(l));
        }

        return points;
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (LineCollision l : lines) {
            l.draw(batch);
        }
        s.draw(batch);
    }

    public boolean findPointCollision(float x1, float y1) {
        for (int i = 0; i < array.length; i++) {
            int collisions = findOnePointCollision(x + array[i][0], y + array[i][1], x1, y1, i);

            if(collisions % 2 == 1){
                return true;
            }
        }
        return false;
    }

    private int findOnePointCollision(float x1, float y1, float x2, float y2, int i) {
        int collisions = 0;

        for (int j = 0; j < lines.size() - 1; j++) {
            if (i == lines.size() - 1) {
                i = -1;
            }
            LineCollision l = lines.get(i + 1);
            i++;

            float x3 = l.x;
            float x4 = (float) (l.x + l.height * Math.sin((l.rotation * 0.0174532925)));

            float y3 = l.y;
            float y4 = (float) (l.y + l.height * Math.cos((l.rotation * 0.0174532925)));

            if (y4 == y3) {
                y4 += 0.001;
                y3 -= 0.001;
            }
            if (y2 == y1) {
                y2 += 0.001;
                y1 -= 0.001;
            }
            if (x1 == x2) {
                x2 += 0.001;
                x1 -= 0.001;
            }
            if (x3 == x4) {
                x4 += 0.001;
                x3 -= 0.001;
            }

            float znam = ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

            if (znam == 0) {
                znam = 0.001f;
            }

            float pX = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / znam;
            float pY = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / znam;

            float first = (y2 - pY) / (y1 - pY);
            float second = (pY - y3) / (y4 - y3);



            if (first >= 0 && first <= 1 && second >= 0 && second <= 1) {
                collisions++;
            }
        }

        return collisions;
    }

//    public void setRotation(float deltaRotation){
//        rotation += deltaRotation;
//        for (int i = 0; i < lines.size(); i++) {
//
//            lines.get(i).rotation += deltaRotation;
//            lines.get(i).sprite.setRotation(-lines.get(i).rotation);
//
//            double sin = Math.sin(Math.toRadians(rotation)+rotations.get(i));
//            double cos = Math.cos(Math.toRadians(rotation)+rotations.get(i));
//
//            float height = (float) Math.sqrt((array[i][0])*(array[i][0]) + (array[i][1])*(array[i][1]));
//
//            lines.get(i).x = x + (float) (height*sin);
//            lines.get(i).y = y + (float) (height*cos);
//
//            array[i][0] = x - lines.get(i).x;
//            array[i][1] = y - lines.get(i).y;
//        }
//    }

}
