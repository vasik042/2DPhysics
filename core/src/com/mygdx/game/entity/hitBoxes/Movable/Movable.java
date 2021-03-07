package com.mygdx.game.entity.hitBoxes.Movable;

import com.mygdx.game.entity.Drawable;
import com.mygdx.game.entity.hitBoxes.*;

import java.util.ArrayList;

public class Movable extends Drawable {
    public Collision collision;
    public float speed;
    public float elasticity = 0f;
    public float mass = 1f;

    public float rotation;
    public float sin = 1;
    public float cos = 1;

    public Movable(Collision collision) {
        super(collision.x, collision.y, "0.png");

        sprite = collision.sprite;
        sprite.setColor(1, 1, 0, 1);
        this.collision = collision;
    }
    public void live(){
        correctSpeed();
        setPos();
    }
    private void changeSpeed (Movable movable, float rotation){
        movable.changeSpeed(rotation+180, (mass/movable.mass)*speed);
        changeSpeed(rotation, (movable.mass/mass)*speed);
    }

    private float correctPosition(Collision collision1){
        return 0.1f;
    }


    private float boxToBox(BoxCollision collision1, BoxCollision collision2){
        float rotation1;
        boolean invert = false;

        if(collision1.hitBoxWidth/2 >= collision2.hitBoxWidth || collision1.hitBoxHeight/2 >= collision2.hitBoxHeight){
            BoxCollision collision3 = collision1;
            collision1 = collision2;
            collision2 = collision3;
            invert = true;
        }

        float height = (float) Math.sqrt((collision2.hitBoxWidth) * (collision2.hitBoxWidth) + (collision2.hitBoxHeight) * (collision2.hitBoxHeight));
        rotation1 = (float) Math.toDegrees(Math.asin((collision2.hitBoxWidth)/height));

        float x3;
        float y3;

        if(collision1.x - collision1.hitBoxWidth/2 > collision2.x){
            x3 = collision1.x - collision1.hitBoxWidth;
        }else if (collision1.x + collision1.hitBoxWidth/2 < collision2.x){
            x3 = collision1.x + collision1.hitBoxWidth;
        }else {
            x3 = collision1.x;
        }
        if(collision1.y - collision1.hitBoxHeight/2 > collision2.y){
            y3 = collision1.y - collision1.hitBoxHeight;
        }else if(collision1.y + collision1.hitBoxHeight/2 < collision2.y){
            y3 = collision1.y + collision1.hitBoxHeight;
        }else {
            y3 = collision1.y;
        }

        height = (float) Math.sqrt((collision2.x - x3) * (collision2.x - x3) + (collision2.y - y3) * (collision2.y - y3));
        float rotation2 = (float) Math.toDegrees(Math.asin((x3 - collision2.x)/height));
        if(y3 < collision2.y){
            rotation2 = (float) Math.toDegrees(Math.asin((collision2.x - x3)/height)) + 180;
        }

        if(invert){
            BoxCollision collision3 = collision1;
            collision1 = collision2;
            collision2 = collision3;
            rotation2 += 180;
        }

        if(rotation2 < 0){
            rotation2 += 360;
        }else if(rotation2 >= 360){
            rotation2 -= 360;
        }

        if(rotation2 < rotation1 || rotation2 > 360 - rotation1){
            collision1.y = collision2.y + collision1.hitBoxHeight + collision2.hitBoxHeight;
            rotation1 = 0;
        }else if(rotation2 < 180 - rotation1){
            collision1.x = collision2.x + collision1.hitBoxWidth + collision2.hitBoxWidth;
            rotation1 = 90;
        }else if(rotation2 < 180 + rotation1){
            collision1.y = collision2.y - collision1.hitBoxHeight - collision2.hitBoxHeight;
            rotation1 = 180;
        }else {
            collision1.x = collision2.x - collision1.hitBoxWidth - collision2.hitBoxWidth;
            rotation1 = 270;
        }

        return rotation1;
    }

    private float circleToCircle(CircleCollision collision1, CircleCollision collision2, ArrayList<float[]> collisionPoints){
        float rotation1;

        float x1 = collisionPoints.get(0)[0];
        float y1 = collisionPoints.get(0)[1];

        float height = (float) Math.sqrt((x1-collision1.x)*(x1-collision1.x) + (y1-collision1.y)*(y1-collision1.y));
        rotation1 = (float) (Math.asin((collision1.x - x1) / height)/0.0174532925);
        if(collision1.y < y1){
            rotation1 = (float) (Math.asin((x1 - collision1.x) / height)/0.0174532925) + 180;
        }
        float modSin = (float) Math.sin(Math.toRadians(rotation1));
        float modCos = (float) Math.cos(Math.toRadians(rotation1));

        collision1.x = collision2.x + modSin * (collision1.radius + collision2.radius);
        collision1.y = collision2.y + modCos * (collision1.radius + collision2.radius);

        return rotation1;
    }

//    private float lineToLine(LineCollision collision1, LineCollision collision2, ArrayList<float[]> collisionPoints){}


    private float circleToBox(CircleCollision collision1, BoxCollision collision2, boolean inverted){
        float rotation1;

        float x1;
        float y1;

        if(collision2.y < collision1.y + collision2.hitBoxHeight && collision2.y > collision1.y - collision2.hitBoxHeight) {
            if(collision2.x > collision1.x){
                if(inverted){
                    collision2.x = collision1.x + collision2.hitBoxWidth + collision1.radius;
                    rotation1 = 90;
                }else {
                    collision1.x = collision2.x - collision2.hitBoxWidth - collision1.radius;
                    rotation1 = 270;
                }
            }else {
                if(inverted){
                    collision2.x = collision1.x - collision2.hitBoxWidth - collision1.radius;
                    rotation1 = 270;
                }else {
                    collision1.x = collision2.x + collision2.hitBoxWidth + collision1.radius;
                    rotation1 = 90;
                }
            }
        }else if(collision2.x < collision1.x + collision2.hitBoxWidth && collision2.x > collision1.x - collision2.hitBoxWidth) {
            if(collision2.y > collision1.y){
                if(inverted){
                    collision2.y = collision1.y + collision2.hitBoxHeight + collision1.radius;
                    rotation1 = 0;
                }else {
                    collision1.y = collision2.y - collision2.hitBoxHeight - collision1.radius;
                    rotation1 = 180;
                }
            }else {
                if(inverted){
                    collision2.y = collision1.y - collision2.hitBoxHeight - collision1.radius;
                    rotation1 = 180;
                }else {
                    collision1.y = collision2.y + collision2.hitBoxHeight + collision1.radius;
                    rotation1 = 0;
                }
            }
        }else {
            if (inverted){
                if(collision2.x > collision1.x){
                    x1 = collision2.x - collision2.hitBoxWidth;
                }else {
                    x1 = collision2.x + collision2.hitBoxWidth;
                }
                if(collision2.y > collision1.y){
                    y1 = collision2.y - collision2.hitBoxHeight;
                }else {
                    y1 = collision2.y + collision2.hitBoxHeight;
                }
            }else {
                if(collision1.x > collision2.x){
                    x1 = collision1.x - collision2.hitBoxWidth;
                }else {
                    x1 = collision1.x + collision2.hitBoxWidth;
                }
                if(collision1.y > collision2.y){
                    y1 = collision1.y - collision2.hitBoxHeight;
                }else {
                    y1 = collision1.y + collision2.hitBoxHeight;
                }
            }

            float height2;

            if(inverted){
                height2 = (float) Math.sqrt((x1-collision1.x)*(x1-collision1.x) + (y1-collision1.y)*(y1-collision1.y));

                rotation1 = (float) (Math.asin((collision1.x - x1) / height2)/0.0174532925)+ 180;
                if(collision2.y > y1){
                    rotation1 = (float) (Math.asin((x1 - collision1.x) / height2)/0.0174532925);
                }
            }else {
                height2 = (float) Math.sqrt((x1-collision2.x)*(x1-collision2.x) + (y1-collision2.y)*(y1-collision2.y));

                rotation1 = (float) (Math.asin((collision2.x - x1) / height2)/0.0174532925)+ 180;
                if(collision1.y > y1){
                    rotation1 = (float) (Math.asin((x1 - collision2.x) / height2)/0.0174532925);
                }
            }

            float x3 = (float) (Math.sin(Math.toRadians(rotation1)) * collision1.radius);
            float y3 = (float) (Math.cos(Math.toRadians(rotation1)) * collision1.radius);

            if(inverted){
                collision2.x = collision2.x + x3 + collision1.x - x1;
                collision2.y = collision2.y + y3 + collision1.y - y1;
            }else {
                collision1.x = collision2.x + x3 + collision1.x - x1;
                collision1.y = collision2.y + y3 + collision1.y - y1;
            }
        }
        return rotation1;
    }

    private float circleToLine(CircleCollision collision1, LineCollision collision2, ArrayList<float[]> collisionPoints, boolean inverted){
        float rotation1 = collision2.rotation;

        float x1;
        float y1;

        if(collisionPoints.size() > 1) {
            x1 = (collisionPoints.get(0)[0] + collisionPoints.get(1)[0]) / 2;
            y1 = (collisionPoints.get(0)[1] + collisionPoints.get(1)[1]) / 2;
        }else {
            float x2 = (float) (collision2.x + collision2.height*Math.sin((rotation1*0.0174532925)));
            float y2 = (float) (collision2.y + collision2.height*Math.cos((rotation1*0.0174532925)));

            if(collision1.findPointCollision(collision2.x, collision2.y)){
                x1 = collision2.x;
                y1 = collision2.y;
            }else if(collision1.findPointCollision(x2, y2)){
                x1 = x2;
                y1 = y2;
            }else {
                x1 = collisionPoints.get(0)[0];
                y1 = collisionPoints.get(0)[1];
            }

            if((collision1.x < collision2.x && collision1.x > x2) || (x > collision2.x && collision1.x < x2)){
                x1 = (x1 + collisionPoints.get(0)[0])/2;
            }
            if((collision1.y < collision2.y && collision1.y > y2) || (y > collision2.y && collision1.y < y2)){
                y1 = (y1 + collisionPoints.get(0)[1])/2;
            }
        }




        float height = (float) Math.sqrt((x1-collision1.x)*(x1-collision1.x) + (y1-collision1.y)*(y1-collision1.y));
        rotation1 = (float) (Math.asin((x1 - collision1.x) / height)/0.0174532925) + 180;
        if(collision1.y > y1){
            rotation1 = (float) (Math.asin((collision1.x - x1) / height)/0.0174532925);
        }
        if(inverted){
            rotation1 += 180;
        }

        float modSin = (float) Math.sin(Math.toRadians(rotation1));
        float modCos = (float) Math.cos(Math.toRadians(rotation1));

        float x3 = modSin * collision1.radius;
        float y3 = modCos * collision1.radius;

        if(inverted){
            collision2.x = collision1.x + x3 + (collision2.x - x1);
            collision2.y = collision1.y + y3 + (collision2.y - y1);
        }else {
            collision1.x = x1 + x3;
            collision1.y = y1 + y3;
        }

        return rotation1;
    }


    private float boxToLine(BoxCollision collision1, LineCollision collision2, ArrayList<float[]> collisionPoints, boolean inverted){
        float rotation1 = collision2.rotation;

        float x1 = 0;
        float y1 = 0;

        if(collisionPoints.size() == 1) {
            float deltaSin = (float) (Math.sin((rotation1*0.0174532925)));
            float deltaCos = (float) (Math.cos((rotation1*0.0174532925)));

            float x2 = collision2.x + deltaSin * collision2.height;
            float y2 = collision2.y + deltaCos * collision2.height;

            if(collision1.findPointCollision(collision2.x + deltaSin, collision2.y + deltaCos)){
                x1 = collision2.x + deltaSin;
                y1 = collision2.y + deltaCos;
            }else if(collision1.findPointCollision(x2 - deltaSin, y2 - deltaCos)){
                x1 = x2 - deltaSin;
                y1 = y2 - deltaCos;
            }else {
                x1 = collisionPoints.get(0)[0];
                y1 = collisionPoints.get(0)[1];
            }

            x2 = x1 - collision1.x;
            y2 = y1 - collision1.y;
            if(x2 < 0){
                x2 *= -1;
            }
            if(y2 < 0){
                y2 *= -1;
            }
            x2 -= collision1.hitBoxWidth;
            y2 -= collision1.hitBoxHeight;

            if(inverted){
                if(x2 >= y2){
                    if(collision1.x < x1){
                        collision2.x = collision1.x + collision1.hitBoxWidth + (collision2.x - x1);
                        rotation1 = 90;
                    }else {
                        collision2.x = collision1.x - collision1.hitBoxWidth + (collision2.x - x1);
                        rotation1 = 270;
                    }
                }else {
                    if(collision1.y < y1){
                        collision2.y = collision1.y + collision1.hitBoxHeight + (collision2.y - y1);
                        rotation1 = 0;
                    }else {
                        collision2.y = collision1.y - collision1.hitBoxHeight + (collision2.y - y1);
                        rotation1 = 180;
                    }
                }
            }else {
                if(x2 >= y2){
                    if(collision1.x > x1){
                        collision1.x = x1 + collision1.hitBoxWidth;
                        rotation1 = 90;
                    }else {
                        collision1.x = x1 - collision1.hitBoxWidth;
                        rotation1 = 270;
                    }
                }else {
                    if(collision1.y > y1){
                        collision1.y = y1 + collision1.hitBoxHeight;
                        rotation1 = 0;
                    }else {
                        collision1.y = y1 - collision1.hitBoxHeight;
                        rotation1 = 180;
                    }
                }
            }

        }else {
            if(!(collisionPoints.get(0)[1] - collisionPoints.get(1)[1] > -0.0001f && collisionPoints.get(0)[1] - collisionPoints.get(1)[1] < 0.0001f)){
                if (collisionPoints.get(0)[0] > collision1.x) {
                    if (inverted){
                        collision2.x = collision1.x + collision1.hitBoxWidth + (collision2.x - (collisionPoints.get(0)[0] + collisionPoints.get(1)[0]) / 2);
                    }else {
                        if(collision1.x - collisionPoints.get(0)[0] > collision1.x - collisionPoints.get(1)[0]){
                            collision1.x = collisionPoints.get(0)[0] - collision1.hitBoxWidth;
                        }else {
                            collision1.x = collisionPoints.get(1)[0] - collision1.hitBoxWidth;
                        }
                    }
                } else {
                    if (inverted){
                        collision2.x = collision1.x - collision1.hitBoxWidth + (collision2.x - (collisionPoints.get(0)[0] + collisionPoints.get(1)[0]) / 2);
                    }else{
                        if(collision1.x - collisionPoints.get(0)[0] < collision1.x - collisionPoints.get(1)[0]){
                            collision1.x = collisionPoints.get(0)[0] + collision1.hitBoxWidth;
                        }else {
                            collision1.x = collisionPoints.get(1)[0] + collision1.hitBoxWidth;
                        }
                    }
                }
            }
            if(!(collisionPoints.get(0)[0] - collisionPoints.get(1)[0] > -0.0001f && collisionPoints.get(0)[0] - collisionPoints.get(1)[0] < 0.0001f)){
                if (collisionPoints.get(0)[1] > collision1.y) {
                    if (inverted){
                        collision2.y = collision1.y + collision1.hitBoxHeight + (collision2.y - (collisionPoints.get(0)[1] + collisionPoints.get(1)[1]) / 2);
                    }else{
                        if(collision1.y - collisionPoints.get(0)[1] < collision1.y - collisionPoints.get(1)[1]){
                            collision1.y = collisionPoints.get(0)[1] - collision1.hitBoxHeight;
                        }else {
                            collision1.y = collisionPoints.get(1)[1] - collision1.hitBoxHeight;
                        }
                    }
                } else {
                    if (inverted){
                        collision2.y = collision1.y - collision1.hitBoxHeight + (collision2.y - (collisionPoints.get(0)[1] + collisionPoints.get(1)[1]) / 2);
                    }else{
                        if(collision1.y - collisionPoints.get(0)[1] < collision1.y - collisionPoints.get(1)[1]){
                            collision1.y = collisionPoints.get(0)[1] + collision1.hitBoxHeight;
                        }else {
                            collision1.y = collisionPoints.get(1)[1] + collision1.hitBoxHeight;
                        }
                    }
                }
            }

            if(rotation1 > 360){
                rotation1 -= 360;
            }else if(rotation1 < 0){
                rotation1 += 360;
            }

            if(rotation1 > 180 || rotation1 == 0){
                rotation1 -= 90;
            }else {
                rotation1 += 90;
            }

            if(collision1.y > collisionPoints.get(0)[1]){
                rotation1 += 180;
            }
            if(collision1.x > collisionPoints.get(0)[0] && collisionPoints.get(0)[0] == collisionPoints.get(1)[0]){
                rotation1 += 180;
            }
            if (inverted){
                rotation1 -= 180;
            }
        }
        return rotation1;
    }


    private void circleCollision(Collision collision1, Movable movable, ArrayList<float[]> collisionPoints) throws NoSuchFieldException, IllegalAccessException {
        float rotation1 = 0;
        boolean polygonal = false;

        switch (collision1.collisionType){
            case "BoxCollision":
                rotation1 = circleToBox((CircleCollision) collision, (BoxCollision) collision1, false);
                break;
            case "LineCollision":
                rotation1 = circleToLine((CircleCollision) collision, (LineCollision) collision1, collisionPoints, false);
                break;
            case "CircleCollision":
                rotation1 = circleToCircle((CircleCollision) collision, (CircleCollision) collision1, collisionPoints);
                break;
            case "PolygonalCollision":
                ArrayList<LineCollision> lines = (ArrayList<LineCollision>) PolygonalCollision.class.getDeclaredField("lines").get(collision1);
                for (LineCollision l : lines) {
                    collisionPoints = collision.findCollisionPoints(l);
                    if(!collisionPoints.isEmpty()){
                        rotation1 = circleToLine((CircleCollision) collision, l, collisionPoints, false);

                        x = collision.x;
                        y = collision.y;

                        if(movable != null){
                            movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                            changeSpeed(rotation1, (movable.mass/mass)*speed);
                        }else {
                            changeSpeed(rotation1);
                        }
                    }
                }
                polygonal = true;
                break;
        }

        if (!polygonal){
            x = collision.x;
            y = collision.y;
            if(movable != null){
                movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                changeSpeed(rotation1, (movable.mass/mass)*speed);
            }else {
                changeSpeed(rotation1);
            }
        }
    }

    private void boxCollision(Collision collision1, Movable movable, ArrayList<float[]> collisionPoints) throws NoSuchFieldException, IllegalAccessException {
    float rotation1 = 0;
    boolean polygonal = false;

    switch (collision1.collisionType){
        case "BoxCollision":
            rotation1 = boxToBox((BoxCollision) collision, (BoxCollision)  collision1);
            break;
        case "LineCollision":
            rotation1 = boxToLine((BoxCollision) collision, (LineCollision) collision1, collisionPoints, false);
            break;
        case "CircleCollision":
            rotation1 = circleToBox((CircleCollision) collision1, (BoxCollision) collision, true);
            break;
        case "PolygonalCollision":
            ArrayList<LineCollision> lines = (ArrayList<LineCollision>) PolygonalCollision.class.getDeclaredField("lines").get(collision1);
            for (LineCollision l : lines) {
                collisionPoints = collision.findCollisionPoints(l);
                if(!collisionPoints.isEmpty()){
                    rotation1 = boxToLine((BoxCollision) collision, l, collisionPoints, false);

                    x = collision.x;
                    y = collision.y;

                    if(movable != null){
                        movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation1, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation1);
                    }
                }
            }
            polygonal = true;
            break;
    }

    if (!polygonal){
        x = collision.x;
        y = collision.y;
        if(movable != null){
            movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
            changeSpeed(rotation1, (movable.mass/mass)*speed);
        }else {
            changeSpeed(rotation1);
        }
    }
}

    private void lineCollision(Collision collision1, Movable movable, ArrayList<float[]> collisionPoints){
        float rotation1 = 0;
        boolean polygonal = false;

        switch (collision1.collisionType){
            case "BoxCollision":
                rotation1 = boxToLine((BoxCollision)  collision1, (LineCollision) collision, collisionPoints, true);
                break;
            case "LineCollision":
                break;
            case "CircleCollision":
                rotation1 = circleToLine((CircleCollision) collision1, (LineCollision)  collision, collisionPoints, true);
                break;
            case "PolygonalCollision":
                polygonal = true;
                break;
        }

        if (!polygonal){
            x = collision.x;
            y = collision.y;
            if(movable != null){
                movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                changeSpeed(rotation1, (movable.mass/mass)*speed);
            }else {
                changeSpeed(rotation1);
            }
        }
    }


    public void collision(Collision collision1) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> collisionPoints = collision.findCollisionPoints(collision1);
        if(!collisionPoints.isEmpty()){
            switch (collision.collisionType){
                case "BoxCollision":
                    boxCollision(collision1, null, collisionPoints);
                    break;
                case "CircleCollision":
                    circleCollision(collision1, null, collisionPoints);
                    break;
                case "LineCollision":
                    lineCollision(collision1, null, collisionPoints);
                    break;
            }
        }
    }

    public void collision(Movable movable) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> collisionPoints = collision.findCollisionPoints(movable.collision);
        if(!collisionPoints.isEmpty()){
            switch (collision.collisionType){
                case "BoxCollision":
                    boxCollision(movable.collision, movable, collisionPoints);
                    break;
                case "CircleCollision":
                    circleCollision(movable.collision, movable, collisionPoints);
                    break;
                case "LineCollision":
                    lineCollision(movable.collision, movable, collisionPoints);
                    break;
            }
        }
    }


    private void changeSpeed(float rotation2){
        float modSin = (float) Math.sin(Math.toRadians(rotation - rotation2));
        float modCos = (float) Math.cos(Math.toRadians(rotation - rotation2));

        if(modSin < 0){
            modSin *= -1;
        }
        if(modCos < 0){
            modCos *= -1;
        }

        float s = speed;
        changeSpeed(rotation2, speed * modCos * (1 + elasticity));

        if(elasticity > 0){
            speed = s;
        }else {
            speed = s * modSin;
        }
    }

    public void changeSpeed(float rotation1, float speed1){
        float sin1 = (float) Math.sin(Math.toRadians(rotation1));
        float cos1 = (float) Math.cos(Math.toRadians(rotation1));

        if(speed < 0.001 && speed1 < 0.001){
            speed = 0.001f;
        }else if(speed < 0.001){
            speed = speed1;
            rotation = rotation1;
            sin = (float) Math.sin(Math.toRadians(rotation));
            cos = (float) Math.cos(Math.toRadians(rotation));
        }else if(speed1 != 0){
            float x1 = sin*speed;
            float y1 = cos*speed;

            float x2 = sin1*speed1;
            float y2 = cos1*speed1;

            float x3 = x1+x2;
            float y3 = y1+y2;

            speed = (float) Math.sqrt(x3*x3 + y3*y3);
            if(speed < 0.001){
                speed = 0.001f;
            }

            rotation = (float) (Math.asin((x3) / speed)/0.0174532925);
            if(y3 < 0){
                rotation = (float) (Math.asin((-x3) / speed)/0.0174532925) + 180;
            }

            sin = (float) Math.sin(Math.toRadians(rotation));
            cos = (float) Math.cos(Math.toRadians(rotation));
        }
    }


    public void correctSpeed(){
        if(speed > 0.2){
            speed -= 0.2f;
        }else if (speed < -0.2){
            speed += 0.2f;
        }else {
            speed = 0.0001f;
        }
        if(speed > 8){
            speed = 8;
        }
        if(speed < -8){
            speed = -8;
        }
    }

    public void setPos(){
        x+= speed*sin;
        y+= speed*cos;

        collision.x = x;
        collision.y = y;
    }
}
