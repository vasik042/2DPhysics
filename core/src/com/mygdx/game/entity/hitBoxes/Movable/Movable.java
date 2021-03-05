package com.mygdx.game.entity.hitBoxes.Movable;

import com.mygdx.game.entity.Drawable;
import com.mygdx.game.entity.hitBoxes.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Movable extends Drawable {
    public Collision collision;
    public float speed;
    public float elasticity = 1f;
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

    private void boxCollision(Collision collision1, Movable movable) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> collisionPoints = collision.findCollisionPoints(collision1);

        if(!collisionPoints.isEmpty()){
            switch (collision1.collisionType){
                case "BoxCollision":
                    if(collisionPoints.size() > 1){
                        Field hitBoxWidthField = BoxCollision.class.getDeclaredField("hitBoxWidth");
                        Field hitBoxHeightField = BoxCollision.class.getDeclaredField("hitBoxHeight");

                        float hitBoxWidth1 = (float) hitBoxWidthField.get(collision);
                        float hitBoxHeight1 = (float) hitBoxHeightField.get(collision);

                        float hitBoxWidth2 = (float) hitBoxWidthField.get(collision1);
                        float hitBoxHeight2 = (float) hitBoxHeightField.get(collision1);

                        float rotation1;

                        if(collisionPoints.get(0)[0] == collisionPoints.get(1)[0]) {
                            if(collisionPoints.get(0)[0] > x){
                                x = collision1.x - hitBoxWidth1 - hitBoxWidth2;
                                rotation1 = 270;
                            }else {
                                x = collision1.x + hitBoxWidth1 + hitBoxWidth2;
                                rotation1 = 90;
                            }
                        }else if(collisionPoints.get(0)[1] == collisionPoints.get(1)[1]) {
                            if(collisionPoints.get(0)[1] > y){
                                y = collision1.y - hitBoxHeight1 - hitBoxHeight2;
                                rotation1 = 180;
                            }else {
                                y = collision1.y + hitBoxHeight1 + hitBoxHeight2;
                                rotation1 = 0;
                            }
                        }else {
                            float side11 = (collisionPoints.get(0)[0] - collision1.x);
                            float side12 = (collisionPoints.get(0)[1] - collision1.y);
                            float side21 = (collisionPoints.get(1)[0] - collision1.x);
                            float side22 = (collisionPoints.get(1)[1] - collision1.y);
                            if(side11 < 0){
                                side11 *= -1;
                            }
                            if(side12 < 0){
                                side12 *= -1;
                            }
                            if(side21 < 0){
                                side21 *= -1;
                            }
                            if(side22 < 0){
                                side22 *= -1;
                            }

                            side11 += side12;
                            side21 += side22;

                            side11 -= (hitBoxWidth2 + hitBoxHeight2);
                            side21 -= (hitBoxWidth2 + hitBoxHeight2);

                            float correlation = side11/side21;

                            if(correlation < 1){
                                if(x > collision1.x){
                                    x = collision1.x + hitBoxWidth1 + hitBoxWidth2;
                                    rotation1 = 90;
                                }else {
                                    x = collision1.x - hitBoxWidth1 - hitBoxWidth2;
                                    rotation1 = 270;
                                }
                            }else {
                                if(y < collision1.y){
                                    y = collision1.y - hitBoxHeight1 - hitBoxHeight2;
                                    rotation1 = 180;
                                }else {
                                    y = collision1.y + hitBoxHeight1 + hitBoxHeight2;
                                    rotation1 = 0;
                                }
                            }
                        }
                        if(movable != null){
                            movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                            changeSpeed(rotation1, (movable.mass/mass)*speed);
                        }else {
                            changeSpeed(rotation1);
                        }
                    }
                    break;
                case "LineCollision":
                    float hitBoxWidth2 = (float) BoxCollision.class.getDeclaredField("hitBoxWidth").get(collision);
                    float hitBoxHeight2 = (float) BoxCollision.class.getDeclaredField("hitBoxHeight").get(collision);

                    float rotation1 = (float) LineCollision.class.getDeclaredField("rotation").get(collision1);

                    if(collisionPoints.size() == 1) {
                        float height = (float) LineCollision.class.getDeclaredField("height").get(collision1);

                        float deltaSin = (float) (Math.sin((rotation1*0.0174532925)));
                        float deltaCos = (float) (Math.cos((rotation1*0.0174532925)));

                        float x2 = collision1.x + deltaSin * height;
                        float y2 = collision1.y + deltaCos * height;

                        float x1;
                        float y1;
                        if(collision.findPointCollision(collision1.x, collision1.y)){
                            x1 = collision1.x;
                            y1 = collision1.y;
                        }else if(collision.findPointCollision(x2, y2)){
                            x1 = x2;
                            y1 = y2;
                        }else {
                            x1 = collisionPoints.get(0)[0];
                            y1 = collisionPoints.get(0)[1];
                        }

                        x2 = x1 - x;
                        y2 = y1 - y;
                        if(x2 < 0){
                            x2 *= -1;
                        }
                        if(y2 < 0){
                            y2 *= -1;
                        }
                        x2 -= hitBoxWidth2;
                        y2 -= hitBoxHeight2;
                        if(x2 >= y2){
                            if(x > x1){
                                x = x1 + hitBoxWidth2;
                                rotation1 = 90;
                            }else {
                                x = x1 - hitBoxWidth2;
                                rotation1 = 270;
                            }
                        }else {
                            if(y > y1){
                                y = y1 + hitBoxHeight2;
                                rotation1 = 0;
                            }else {
                                y = y1 - hitBoxHeight2;
                                rotation1 = 180;
                            }
                        }
                    }else {
                        if(!(collisionPoints.get(0)[1] - collisionPoints.get(1)[1] > -0.0001f && collisionPoints.get(0)[1] - collisionPoints.get(1)[1] < 0.0001f)){
                            if (collisionPoints.get(0)[0] > x) {
                                if(x - collisionPoints.get(0)[0] > x - collisionPoints.get(1)[0]){
                                    x = collisionPoints.get(0)[0] - hitBoxWidth2;
                                }else {
                                    x = collisionPoints.get(1)[0] - hitBoxWidth2;
                                }
                            } else {
                                if(x - collisionPoints.get(0)[0] < x - collisionPoints.get(1)[0]){
                                    x = collisionPoints.get(0)[0] + hitBoxWidth2;
                                }else {
                                    x = collisionPoints.get(1)[0] + hitBoxWidth2;
                                }
                            }
                        }
                        if(!(collisionPoints.get(0)[0] - collisionPoints.get(1)[0] > -0.0001f && collisionPoints.get(0)[0] - collisionPoints.get(1)[0] < 0.0001f)){
                            if (collisionPoints.get(0)[1] > y) {
                                if(y - collisionPoints.get(0)[1] < y - collisionPoints.get(1)[1]){
                                    y = collisionPoints.get(0)[1] - hitBoxHeight2;
                                }else {
                                    y = collisionPoints.get(1)[1] - hitBoxHeight2;
                                }
                            } else {
                                if(y - collisionPoints.get(0)[1] < y - collisionPoints.get(1)[1]){
                                    y = collisionPoints.get(0)[1] + hitBoxHeight2;
                                }else {
                                    y = collisionPoints.get(1)[1] + hitBoxHeight2;
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

                        if(y > collisionPoints.get(0)[1]){
                            rotation1 += 180;
                        }
                        if(x > collisionPoints.get(0)[0] && collisionPoints.get(0)[0] == collisionPoints.get(1)[0]){
                            rotation1 += 180;
                        }
                    }
                    if(movable != null){
                        movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation1, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation1);
                    }
                    break;
                case "CircleCollision":
                    float x1;
                    float y1;
                    float rotation2 = 0;

                    float hitBoxWidth = (float) BoxCollision.class.getDeclaredField("hitBoxWidth").get(collision);
                    float hitBoxHeight = (float) BoxCollision.class.getDeclaredField("hitBoxHeight").get(collision);

                    float radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision1);

                    if(collision1.y < y + hitBoxHeight && collision1.y > y - hitBoxHeight) {
                        if(collision1.x > x){
                            x = collision1.x - hitBoxWidth - radius;
                            rotation2 = 270;
                        }else {
                            x = collision1.x + hitBoxWidth + radius;
                            rotation2 = 90;
                        }
                    }else if(collision1.x < x + hitBoxWidth && collision1.x > x - hitBoxWidth) {
                        if(collision1.y > y){
                            y = collision1.y - hitBoxHeight - radius;
                            rotation2 = 180;
                        }else {
                            y = collision1.y + hitBoxHeight + radius;
                            rotation2 = 0;
                        }
                    }else {
                        if(x > collision1.x){
                            x1 = x - hitBoxWidth;
                        }else {
                            x1 = x + hitBoxWidth;
                        }
                        if(y > collision1.y){
                            y1 = y - hitBoxHeight;
                        }else {
                            y1 = y + hitBoxHeight;
                        }

                        float height2 = (float) Math.sqrt((x1-collision1.x)*(x1-collision1.x) + (y1-collision1.y)*(y1-collision1.y));
                        rotation2 = (float) (Math.asin((collision1.x - x1) / height2)/0.0174532925)+ 180;
                        if(y > y1){
                            rotation2 = (float) (Math.asin((x1 - collision1.x) / height2)/0.0174532925);
                        }
                        float x3 = (float) (Math.sin(Math.toRadians(rotation2)) * radius);
                        float y3 = (float) (Math.cos(Math.toRadians(rotation2)) * radius);

                        x = collision1.x + x3 + x - x1;
                        y = collision1.y + y3 + y - y1;
                    }
                    if(movable != null){
                        movable.changeSpeed(rotation2+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation2, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation2);
                    }
                    break;
                case "PolygonalCollision":
                    ArrayList<LineCollision> lines = (ArrayList<LineCollision>) PolygonalCollision.class.getDeclaredField("lines").get(collision1);
                    for (LineCollision l : lines) {
                        boxCollision(l, null);
                    }
                    break;
            }
        }
    }

    private void circleCollision(Collision collision1, Movable movable) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> collisionPoints = collision.findCollisionPoints(collision1);
        float rotation2 = 0;

        if(!collisionPoints.isEmpty()){
            switch (collision1.collisionType){
                case "BoxCollision":
                    float x1;
                    float y1;

                    float hitBoxWidth = (float) BoxCollision.class.getDeclaredField("hitBoxWidth").get(collision1);
                    float hitBoxHeight = (float) BoxCollision.class.getDeclaredField("hitBoxHeight").get(collision1);

                    float radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision);

                    if(collision1.y < y + hitBoxHeight && collision1.y > y - hitBoxHeight) {
                        if(collision1.x > x){
                            x = collision1.x - hitBoxWidth - radius;
                            rotation2 = 270;
                        }else {
                            x = collision1.x + hitBoxWidth + radius;
                            rotation2 = 90;
                        }
                    }else if(collision1.x < x + hitBoxWidth && collision1.x > x - hitBoxWidth) {
                        if(collision1.y > y){
                            y = collision1.y - hitBoxHeight - radius;
                            rotation2 = 180;
                        }else {
                            y = collision1.y + hitBoxHeight + radius;
                            rotation2 = 0;
                        }
                    }else {
                        if(x > collision1.x){
                            x1 = x - hitBoxWidth;
                        }else {
                            x1 = x + hitBoxWidth;
                        }
                        if(y > collision1.y){
                            y1 = y - hitBoxHeight;
                        }else {
                            y1 = y + hitBoxHeight;
                        }

                        float height2 = (float) Math.sqrt((x1-collision1.x)*(x1-collision1.x) + (y1-collision1.y)*(y1-collision1.y));
                        rotation2 = (float) (Math.asin((collision1.x - x1) / height2)/0.0174532925)+ 180;
                        if(y > y1){
                            rotation2 = (float) (Math.asin((x1 - collision1.x) / height2)/0.0174532925);
                        }
                        float x3 = (float) (Math.sin(Math.toRadians(rotation2)) * radius);
                        float y3 = (float) (Math.cos(Math.toRadians(rotation2)) * radius);

                        x = collision1.x + x3 + x - x1;
                        y = collision1.y + y3 + y - y1;
                    }

                    if(movable != null){
                        movable.changeSpeed(rotation2+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation2, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation2);
                    }
                    break;
                case "LineCollision":
                    radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision);
                    float rotation1 = (float) LineCollision.class.getDeclaredField("rotation").get(collision1);

                    if(collisionPoints.size() > 1) {
                        x1 = (collisionPoints.get(0)[0] + collisionPoints.get(1)[0]) / 2;
                        y1 = (collisionPoints.get(0)[1] + collisionPoints.get(1)[1]) / 2;
                    }else {
                        float height = (float) LineCollision.class.getDeclaredField("height").get(collision1);

                        float x2 = (float) (collision1.x + height*Math.sin((rotation1*0.0174532925)));
                        float y2 = (float) (collision1.y + height*Math.cos((rotation1*0.0174532925)));

                        if(collision.findPointCollision(collision1.x, collision1.y)){
                            x1 = collision1.x;
                            y1 = collision1.y;
                        }else if(collision.findPointCollision(x2, y2)){
                            x1 = x2;
                            y1 = y2;
                        }else {
                            x1 = collisionPoints.get(0)[0];
                            y1 = collisionPoints.get(0)[1];
                        }

                        if((x < collision1.x && x > x2) || (x > collision1.x && x < x2)){
                            x1 = (x1 + collisionPoints.get(0)[0])/2;
                        }
                        if((y < collision1.y && y > y2) || (y > collision1.y && y < y2)){
                            y1 = (y1 + collisionPoints.get(0)[1])/2;
                        }
                    }
                    float height = (float) Math.sqrt((x1-x)*(x1-x) + (y1-y)*(y1-y));
                    rotation2 = (float) (Math.asin((x1 - x) / height)/0.0174532925) + 180;
                    if(y > y1){
                        rotation2 = (float) (Math.asin((x - x1) / height)/0.0174532925);
                    }

                    float modSin = (float) Math.sin(Math.toRadians(rotation2));
                    float modCos = (float) Math.cos(Math.toRadians(rotation2));

                    float x3 = modSin * radius;
                    float y3 = modCos * radius;

                    x = x1 + x3;
                    y = y1 + y3;

                    if(movable != null){
                        movable.changeSpeed(rotation2+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation2, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation2);
                    }
                    break;
                case "CircleCollision":
                    radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision);
                    float radius1 = (float) CircleCollision.class.getDeclaredField("radius").get(collision1);

                    x1 = collisionPoints.get(0)[0];
                    y1 = collisionPoints.get(0)[1];

                    height = (float) Math.sqrt((x1-x)*(x1-x) + (y1-y)*(y1-y));
                    rotation2 = (float) (Math.asin((x - x1) / height)/0.0174532925);
                    if(y < y1){
                        rotation2 = (float) (Math.asin((x1 - x) / height)/0.0174532925) + 180;
                    }
                    modSin = (float) Math.sin(Math.toRadians(rotation2));
                    modCos = (float) Math.cos(Math.toRadians(rotation2));

                    x = collision1.x + modSin * (radius + radius1);
                    y = collision1.y + modCos * (radius + radius1);

                    if(movable != null){
                        movable.changeSpeed(rotation2+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation2, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation2);
                    }
                    break;
                case "PolygonalCollision":
                    ArrayList<LineCollision> lines = (ArrayList<LineCollision>) PolygonalCollision.class.getDeclaredField("lines").get(collision1);
                    for (LineCollision l : lines) {
                        circleCollision(l, null);
                    }
                    break;
            }
        }
    }

    private void lineCollision(Collision collision1, Movable movable) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<float[]> collisionPoints = collision.findCollisionPoints(collision1);
        float rotation2 = 0;

        if(!collisionPoints.isEmpty()){
            switch (collision1.collisionType){
                case "BoxCollision":
                    float hitBoxWidth2 = (float) BoxCollision.class.getDeclaredField("hitBoxWidth").get(collision);
                    float hitBoxHeight2 = (float) BoxCollision.class.getDeclaredField("hitBoxHeight").get(collision);

                    float rotation1 = (float) LineCollision.class.getDeclaredField("rotation").get(collision1);

                    if(collisionPoints.size() == 1) {
                        float height = (float) LineCollision.class.getDeclaredField("height").get(collision1);

                        float deltaSin = (float) (Math.sin((rotation1*0.0174532925)));
                        float deltaCos = (float) (Math.cos((rotation1*0.0174532925)));

                        float x2 = collision1.x + deltaSin * height;
                        float y2 = collision1.y + deltaCos * height;

                        float x1;
                        float y1;
                        if(collision.findPointCollision(collision1.x, collision1.y)){
                            x1 = collision1.x;
                            y1 = collision1.y;
                        }else if(collision.findPointCollision(x2, y2)){
                            x1 = x2;
                            y1 = y2;
                        }else {
                            x1 = collisionPoints.get(0)[0];
                            y1 = collisionPoints.get(0)[1];
                        }

                        x2 = x1 - x;
                        y2 = y1 - y;
                        if(x2 < 0){
                            x2 *= -1;
                        }
                        if(y2 < 0){
                            y2 *= -1;
                        }
                        x2 -= hitBoxWidth2;
                        y2 -= hitBoxHeight2;
                        if(x2 >= y2){
                            if(x > x1){
                                x = x1 + hitBoxWidth2;
                                rotation1 = 90;
                            }else {
                                x = x1 - hitBoxWidth2;
                                rotation1 = 270;
                            }
                        }else {
                            if(y > y1){
                                y = y1 + hitBoxHeight2;
                                rotation1 = 0;
                            }else {
                                y = y1 - hitBoxHeight2;
                                rotation1 = 180;
                            }
                        }
                    }else {
                        if(!(collisionPoints.get(0)[1] - collisionPoints.get(1)[1] > -0.0001f && collisionPoints.get(0)[1] - collisionPoints.get(1)[1] < 0.0001f)){
                            if (collisionPoints.get(0)[0] > x) {
                                if(x - collisionPoints.get(0)[0] > x - collisionPoints.get(1)[0]){
                                    x = collisionPoints.get(0)[0] - hitBoxWidth2;
                                }else {
                                    x = collisionPoints.get(1)[0] - hitBoxWidth2;
                                }
                            } else {
                                if(x - collisionPoints.get(0)[0] < x - collisionPoints.get(1)[0]){
                                    x = collisionPoints.get(0)[0] + hitBoxWidth2;
                                }else {
                                    x = collisionPoints.get(1)[0] + hitBoxWidth2;
                                }
                            }
                        }
                        if(!(collisionPoints.get(0)[0] - collisionPoints.get(1)[0] > -0.0001f && collisionPoints.get(0)[0] - collisionPoints.get(1)[0] < 0.0001f)){
                            if (collisionPoints.get(0)[1] > y) {
                                if(y - collisionPoints.get(0)[1] < y - collisionPoints.get(1)[1]){
                                    y = collisionPoints.get(0)[1] - hitBoxHeight2;
                                }else {
                                    y = collisionPoints.get(1)[1] - hitBoxHeight2;
                                }
                            } else {
                                if(y - collisionPoints.get(0)[1] < y - collisionPoints.get(1)[1]){
                                    y = collisionPoints.get(0)[1] + hitBoxHeight2;
                                }else {
                                    y = collisionPoints.get(1)[1] + hitBoxHeight2;
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

                        if(y > collisionPoints.get(0)[1]){
                            rotation1 += 180;
                        }
                        if(x > collisionPoints.get(0)[0] && collisionPoints.get(0)[0] == collisionPoints.get(1)[0]){
                            rotation1 += 180;
                        }
                    }
                    if(movable != null){
                        movable.changeSpeed(rotation1+180, (mass/movable.mass)*speed);
                        changeSpeed(rotation1, (movable.mass/mass)*speed);
                    }else {
                        changeSpeed(rotation1);
                    }
                    break;
                case "LineCollision":
                    break;
                case "CircleCollision":
                    float radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision1);
                    rotation1 = (float) LineCollision.class.getDeclaredField("rotation").get(collision);

                    float x1 = 0;
                    float y1 = 0;

                    if(collisionPoints.size() > 1) {
                        x1 = (collisionPoints.get(0)[0] + collisionPoints.get(1)[0]) / 2;
                        y1 = (collisionPoints.get(0)[1] + collisionPoints.get(1)[1]) / 2;
                    }else {
//                        float height = (float) LineCollision.class.getDeclaredField("height").get(collision1);
//
//                        float x2 = (float) (collision1.x + height*Math.sin((rotation1*0.0174532925)));
//                        float y2 = (float) (collision1.y + height*Math.cos((rotation1*0.0174532925)));
//
//                        if(collision.findPointCollision(collision1.x, collision1.y)){
//                            x1 = collision1.x;
//                            y1 = collision1.y;
//                        }else if(collision.findPointCollision(x2, y2)){
//                            x1 = x2;
//                            y1 = y2;
//                        }else {
//                            x1 = collisionPoints.get(0)[0];
//                            y1 = collisionPoints.get(0)[1];
//                        }
//
//                        if((x < collision1.x && x > x2) || (x > collision1.x && x < x2)){
//                            x1 = (x1 + collisionPoints.get(0)[0])/2;
//                        }
//                        if((y < collision1.y && y > y2) || (y > collision1.y && y < y2)){
//                            y1 = (y1 + collisionPoints.get(0)[1])/2;
//                        }
                    }
                    float height = (float) Math.sqrt((x1-collision1.x)*(x1-collision1.x) + (y1-collision1.y)*(y1-collision1.y));

                    if(collision1.y > y1){
                        rotation2 = (float) (Math.asin((collision1.x - x1) / height)/0.0174532925) + 180;
                    }else {
                        rotation2 = (float) (Math.asin((x1 - collision1.x) / height)/0.0174532925);
                    }

                    float modSin = (float) Math.sin(Math.toRadians(rotation2));
                    float modCos = (float) Math.cos(Math.toRadians(rotation2));

                    float x3 = modSin * radius;
                    float y3 = modCos * radius;

                    x = x1 + x3;
                    y = y1 + y3;

//                    if(movable != null){
//                        movable.changeSpeed(rotation2+180, (mass/movable.mass)*speed);
//                        changeSpeed(rotation2, (movable.mass/mass)*speed);
//                    }else {
//                        changeSpeed(rotation2);
//                    }
//                    radius = (float) CircleCollision.class.getDeclaredField("radius").get(collision);
//                    float rotation1 = (float) LineCollision.class.getDeclaredField("rotation").get(collision1);
//
//                    if(collisionPoints.size() > 1) {
//                        x1 = (collisionPoints.get(0)[0] + collisionPoints.get(1)[0]) / 2;
//                        y1 = (collisionPoints.get(0)[1] + collisionPoints.get(1)[1]) / 2;
//                    }else {
//                        float height = (float) LineCollision.class.getDeclaredField("height").get(collision1);
//
//                        float x2 = (float) (collision1.x + height*Math.sin((rotation1*0.0174532925)));
//                        float y2 = (float) (collision1.y + height*Math.cos((rotation1*0.0174532925)));
//
//                        if(collision.findPointCollision(collision1.x, collision1.y)){
//                            x1 = collision1.x;
//                            y1 = collision1.y;
//                        }else if(collision.findPointCollision(x2, y2)){
//                            x1 = x2;
//                            y1 = y2;
//                        }else {
//                            x1 = collisionPoints.get(0)[0];
//                            y1 = collisionPoints.get(0)[1];
//                        }
//
//                        if((x < collision1.x && x > x2) || (x > collision1.x && x < x2)){
//                            x1 = (x1 + collisionPoints.get(0)[0])/2;
//                        }
//                        if((y < collision1.y && y > y2) || (y > collision1.y && y < y2)){
//                            y1 = (y1 + collisionPoints.get(0)[1])/2;
//                        }
//                    }
//                    float height = (float) Math.sqrt((x1-x)*(x1-x) + (y1-y)*(y1-y));
//                    rotation2 = (float) (Math.asin((x1 - x) / height)/0.0174532925) + 180;
//                    if(y > y1){
//                        rotation2 = (float) (Math.asin((x - x1) / height)/0.0174532925);
//                    }
//
//                    float modSin = (float) Math.sin(Math.toRadians(rotation2));
//                    float modCos = (float) Math.cos(Math.toRadians(rotation2));
//
//                    float x3 = modSin * radius;
//                    float y3 = modCos * radius;
//
//                    x = x1 + x3;
//                    y = y1 + y3;
//
//                    if(movable != null){
//                        movable.changeSpeed(rotation2+180, (mass/movable.mass)*speed);
//                        changeSpeed(rotation2, (movable.mass/mass)*speed);
//                    }else {
//                        changeSpeed(rotation2);
//                    }
                    break;
                case "PolygonalCollision":

                    break;
            }
        }
    }

    public void collision(Collision collision1) throws NoSuchFieldException, IllegalAccessException {
        switch (collision.collisionType){
            case "BoxCollision":
                boxCollision(collision1, null);
                break;
            case "CircleCollision":
                circleCollision(collision1, null);
                break;
            case "LineCollision":
                lineCollision(collision1, null);
                break;
        }
        collision.x = x;
        collision.y = y;
    }

    public void collision(Movable movable) throws NoSuchFieldException, IllegalAccessException {
        switch (collision.collisionType){
            case "BoxCollision":
                boxCollision(movable.collision, movable);
                break;
            case "CircleCollision":
                circleCollision(movable.collision, movable);
                break;
            case "LineCollision":
                lineCollision(movable.collision, movable);
                break;
        }

        collision.x = x;
        collision.y = y;
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

    public void  correctSpeed(){
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
