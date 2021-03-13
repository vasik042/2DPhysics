package com.mygdx.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.entity.Drawable;
import com.mygdx.game.entity.hitBoxes.*;
import com.mygdx.game.entity.Player;
import com.mygdx.game.entity.Wall;
import com.mygdx.game.entity.hitBoxes.Movable.Movable;

import javax.sound.sampled.Line;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private World world;
	private Stage stage;
	private RayHandler rayHandler;

	PointLight pl;

	Player player;
	private Movable circle;
	ArrayList<Collision> collisions;
	ArrayList<Movable> movables;

	@Override
	public void create () {
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();

		collisions = new ArrayList<>();
		movables = new ArrayList<>();

		stage = new Stage(new ScreenViewport());
		stage.getCamera().position.set((float)(Gdx.graphics.getWidth()/2), (float)(Gdx.graphics.getHeight()/2),0);
		stage.getCamera().viewportWidth = (float)Gdx.graphics.getWidth();
		stage.getCamera().viewportHeight = (float)Gdx.graphics.getHeight();


		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.4f);
		rayHandler.setBlurNum(3);
//		rayHandler.resizeFBO(2000, 2000);
		rayHandler.setCulling(false);

		pl = new PointLight(rayHandler, 1000, new Color(1f,1,0,0.5f), 500,0,0);
		pl.setSoftnessLength(0);

		rayHandler.setShadows(true);
		pl.setStaticLight(false);
		pl.setSoft(true);

		batch = new SpriteBatch();

		float[][] arr = {{10,5}, {0,250}, {50,240}, {80,80}, {70,80}, {70,75}, {165,75}, {165,80}, {160,80}, {170,250}, {240,240}, {230,0}};
		Drawable d = new Drawable(400, 100, "1.png");
		d.sprite.setOriginCenter();
		CircleCollision b = new CircleCollision(400, 100, 32);
//		LineCollision b = new LineCollision(300, 100, 400, 200);
//		BoxCollision b = new BoxCollision(new Drawable(400, 100, "0.png"));
//		PolygonalCollision b = new PolygonalCollision(400, 100, arr);
		player = new Player(b);

		collisions.add(new Wall(750,300, world, 20, 20));
		collisions.add(new Wall(860,300, world, 100, 200));
		collisions.add(new Wall(750,0, world, 1500, 50));


		LineCollision line = new LineCollision(500, 100, 300, 100);
		Movable line2 = new Movable(new LineCollision(300, 500, 500, 500));
		LineCollision line3 = new LineCollision(300, 500, 300, 300);
		Movable line4 = new Movable(new LineCollision(500, 300, 500, 500));
		LineCollision line5 = new LineCollision(500, 300, 300, 500);
		Movable line6 = new Movable(new LineCollision(500, 300, 530, 700));


		circle = new Movable(new CircleCollision(200, 200, 50));

		movables.add(circle);
		collisions.add(line);
		movables.add(line2);
		collisions.add(line3);
		movables.add(line4);
		collisions.add(line5);
		movables.add(line6);


//		float[][] array = {{0,0}, {-300,75}, {0,150},{75,450}, {150,150}, {450,75}, {150,0}, {75,-300}};
//		float[][] array = {{0,0}, {0,150}, {150,150}, {150,0}};

		float[][] array1 = {{-1*20,3*20}, {-1*20,4*20}, {1*20,4*20}, {1*20,3*20}};
		float[][] array2 = {{3*20,1*20}, {4*20,1*20}, {4*20,-1*20}, {3*20,-1*20}};
		float[][] array3 = {{1*20,-3*20}, {1*20,-4*20}, {-1*20,-4*20}, {-1*20,-3*20}};
		float[][] array4 = {{-3*20,-1*20}, {-4*20,-1*20}, {-4*20,1*20}, {-3*20,1*20}};
		float[][] array = {{-1*20,3*20}, {1*20,3*20}, {3*20,1*20}, {3*20,-1*20}, {1*20,-3*20}, {-1*20,-3*20}, {-3*20,-1*20}, {-3*20,1*20}};

//		float[][] array = {{-1*20,3*20}, {-1*20,4*20}, {1*20,4*20}, {1*20,3*20}, {3*20,1*20}, {4*20,1*20}, {4*20,-1*20}, {3*20,-1*20}, {1*20,-3*20}, {1*20,-4*20}, {-1*20,-4*20}, {-1*20,-3*20}, {-3*20,-1*20}, {-4*20,-1*20}, {-4*20,1*20}, {-3*20,1*20}};
		collisions.add(new PolygonalCollision(200, 400, array1));
		collisions.add(new PolygonalCollision(200, 400, array2));
		collisions.add(new PolygonalCollision(200, 400, array3));
		collisions.add(new PolygonalCollision(200, 400, array4));
		collisions.add(new PolygonalCollision(200, 400, array));


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			CircleCollision b = new CircleCollision(400, 100, 32);
			player = new Player(b);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			LineCollision b = new LineCollision(300, 100, 400, 200);
			player = new Player(b);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
			BoxCollision b = new BoxCollision(new Drawable(400, 100, "0.png"));
			player = new Player(b);
		}


//		if(Gdx.input.isKeyPressed(Input.Keys.A)){
//			walls.get(0).x-=3;
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.D)){
//			walls.get(0).x+=3;
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.W)){
//			walls.get(0).y+=3;
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.S)){
//			walls.get(0).y-=3;
//		}

		for (Movable m : movables) {
			m.live();
		}

		for (Collision c : collisions) {
			c.draw(batch);
			for (Movable m : movables) {
				try {
					m.collision(c);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < movables.size() - 1; i++) {
			Movable m1 = movables.get(i);

			for (int j = i+1; j < movables.size(); j++) {
				Movable m2 = movables.get(j);

				try {
					m1.collision(m2);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				m2.draw(batch);
			}
			m1.draw(batch);
		}
		collisions.get(0).sprite.setColor(0.1f, 0.55f, 0.2f, 1);
		collisions.get(1).sprite.setColor(0.1f, 0.55f, 0.2f, 1);


		try {
			player.live(batch, collisions, movables);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		pl.setPosition(player.x, player.y);



		batch.end();
		stage.draw();


		rayHandler.setCombinedMatrix(stage.getCamera().combined,0,0,1,1);
		rayHandler.updateAndRender();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
