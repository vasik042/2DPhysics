package com.mygdx.game.entity.hitBoxes;

import com.mygdx.game.entity.Drawable;

import java.util.ArrayList;

public class Collision  extends Drawable {
    public String collisionType;

    public Collision(float x, float y) {
        super(x, y, "0.png");
    }

    public boolean findCollision(Collision collision) throws NoSuchFieldException, IllegalAccessException {
        return !findCollisionPoints(collision).isEmpty();
    }

    public ArrayList<float[]> findCollisionPoints(Collision collision) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> points = new ArrayList<>();

        switch (collision.collisionType){
            case "BoxCollision":
                if(collisionType.equals("LineCollision")){
                    float height = (float) LineCollision.class.getDeclaredField("height").get(this);
                    float rotation = (float) LineCollision.class.getDeclaredField("rotation").get(this);

                    points.addAll(collision.findLineCollisionPoints(
                            this.x,
                            this.y,
                            (float) (this.x + height*Math.sin((rotation*0.0174532925))),
                            (float) (this.y + height*Math.cos((rotation*0.0174532925)))));
                }else {
                    float hitBoxWidth = (float) BoxCollision.class.getDeclaredField("hitBoxWidth").get(collision);
                    float hitBoxHeight = (float) BoxCollision.class.getDeclaredField("hitBoxHeight").get(collision);

                    points.addAll(findBoxCollisionPoints(collision.x, collision.y, hitBoxWidth, hitBoxHeight));
                }

                break;
            case "LineCollision":
                float height = (float) LineCollision.class.getDeclaredField("height").get(collision);
                float rotation = (float) LineCollision.class.getDeclaredField("rotation").get(collision);

                points.addAll(findLineCollisionPoints(
                        collision.x,
                        collision.y,
                        (float) (collision.x + height*Math.sin((rotation*0.0174532925))),
                        (float) (collision.y + height*Math.cos((rotation*0.0174532925)))));
                break;
            case "CircleCollision":
                if(this.collisionType.equals("BoxCollision")){
                    float hitBoxWidth = (float) BoxCollision.class.getDeclaredField("hitBoxWidth").get(this);
                    float hitBoxHeight = (float) BoxCollision.class.getDeclaredField("hitBoxHeight").get(this);

                    points.addAll(collision.findBoxCollisionPoints(this.x, this.y, hitBoxWidth, hitBoxHeight));
                }else {
                    float radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision);

                    points.addAll(findCircleCollisionPoints(collision.x, collision.y, radius));
                }
                break;
            case "PolygonalCollision":
                points.addAll(collision.findCollisionPoints(this));
                break;
        }

        return points;
    }

    public boolean findLineCollision(float x1, float y1, float x2, float y2){
        return !findLineCollisionPoints(x1, y1, x2, y2).isEmpty();
    }

    public ArrayList<float[]> findLineCollisionPoints(float x1, float y1, float x2, float y2){
        return new ArrayList<>();
    }

    public boolean findPointCollision(float x1, float y1){
        return false;
    }

    public boolean findCircleCollision(float x1, float y1, float radius){
        return !findCircleCollisionPoints(x1, y1, radius).isEmpty();
    }

    public ArrayList<float[]> findCircleCollisionPoints(float x1, float y1, float radius){
        return new ArrayList<>();
    }

    public boolean findBoxCollision(float x1, float y1, float width, float height){
        return !findBoxCollisionPoints(x1, y1, width, height).isEmpty();
    }

    public ArrayList<float[]> findBoxCollisionPoints(float x1, float y1, float width, float height){
        return new ArrayList<>();
    }

    public float[] findLineCollisionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        float znam = ((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
        if (znam == 0){
            znam = 0.00001f;
        }

        float pX = ((x1*y2-y1*x2)*(x3-x4) - (x1 - x2)*(x3*y4 - y3*x4))/znam;
        float pY = ((x1*y2-y1*x2)*(y3-y4) - (y1 - y2)*(x3*y4 - y3*x4))/znam;

        return new float[] {pX, pY};
    }
}
