package com.fourtress.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

public class Level {
	
	private TiledMap tiledMap;
	private Box2dModel model;
	
	public Level(TiledMap tiledMap, Box2dModel model) {
		this.tiledMap = tiledMap;
		this.model = model;
		createWalls();
		createInteractionPoints();
		setPlayerSpawn();
	}

	private void createWalls() {
		 MapObjects objects = tiledMap.getLayers().get("Object Layer").getObjects();
		 for(MapObject object : objects) {

	            if (object instanceof TextureMapObject) {
	                continue;
	            }

	            Shape shape;

	            if (object instanceof RectangleMapObject) {
	                shape = BodyFactory.getInstance(model.world).getRectangle((RectangleMapObject)object);
	            } else {
	            	continue;
	            }
	            
	            BodyDef bd = new BodyDef();
	            bd.type = BodyType.StaticBody;
	            Body body = model.world.createBody(bd);
	            body.createFixture(shape, 1);

	            shape.dispose();
		 }
	}
	
	private void createInteractionPoints() {
		MapObjects objects = tiledMap.getLayers().get("Interaction Layer").getObjects();
		for(MapObject object : objects) {
			if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof EllipseMapObject) {
                shape = BodyFactory.getInstance(model.world).getCircle((EllipseMapObject) object);
            } else {
            	continue;
            }
            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = model.world.createBody(bd);
            body.createFixture(shape, 1);
            BodyFactory.getInstance(model.world).makeBodySensor(body, (String) object.getProperties().get("Message"));
            shape.dispose();
		}
	}
	
	private void setPlayerSpawn() {
		MapObjects objects = tiledMap.getLayers().get("Spawn Layer").getObjects();
		MapObject spawn = objects.get("Player Spawn");
		if (spawn instanceof EllipseMapObject) {
			model.setSpawn(((EllipseMapObject) spawn).getEllipse());
		}
	}
	
	public TiledMap getTiledMap() {
		return tiledMap;
	}
}