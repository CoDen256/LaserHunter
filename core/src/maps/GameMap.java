package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

import buttons.ButtonType;
import entities.Entity;
import snapshot.EntityLoader;
import tiles.TileType;

public abstract class GameMap {

    protected ArrayList<Entity> entities;

    protected TiledMap tiledMap;

    public GameMap() {

        entities = new ArrayList<Entity>();
        entities.addAll(EntityLoader.loadEntity("entities", this, entities));


    }

    public void create(ButtonType buttonLeft, ButtonType buttonRight) {
        if (buttonLeft != null && buttonRight != null) {
            // Applying Button Listeners to player (entities.get(0))
            buttonLeft.getButton().addListener(entities.get(0).movingLeftListener);
            buttonRight.getButton().addListener(entities.get(0).movingRightListener);
        }


    }

    public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {
        for (Entity entity : entities) {
            entity.render(batch, delta);
        }



    }

    public void update(OrthographicCamera camera, float delta) {

        for (Entity entity : entities) {

            entity.update(delta);
            if (entity.getType().getId() == "player") {
                followPlayer(entity, camera);
            }
        }


        // EntityLoader.saveEntities("entities", entities);



    }

    public void dispose(){
    }

    public void followPlayer(Entity player, OrthographicCamera camera) {


        if (player.getX() < getResX()/2) {  // w =  200
            camera.position.x = getResX()/2; // black background
        } else if (player.getX() > (getPixelWidth()) - getResX()/2) {
            camera.position.x = getPixelWidth() - getResX()/2;
        } else {
            camera.position.x = Math.round(player.getX() * TileType.TILE_SIZE) / TileType.TILE_SIZE;
        }

        if (player.getY() < getResY()/2) { // h= 150
            camera.position.y = getResY()/2; // black background
        } else if (player.getY() > (getPixelHeight()) - getResY()/2) {
            camera.position.y = getPixelHeight() - getResY()/2;
        } else {
            camera.position.y = player.getY(); // gaps
        }

    }



    public int getCollision(float x, float y, int width, int height) {
        // x + shiftLeft and x - shiftRight -  minimizing width from left and right on width*0.066f
        float shiftLeft = width*0.18f;
        float shiftRight = width*0.1f;
        float shiftUp = height*0.08f;
        if (x + shiftLeft < 0 || y < 0 || x - shiftRight + width   > getPixelWidth() || y - shiftUp + height > getPixelHeight()) {
            return -1;
        }
        for (int row = (int)(y / TileType.TILE_SIZE); row < Math.ceil((y - shiftUp + height) / TileType.TILE_SIZE); row++) {
            for (int col = (int) ((x + shiftLeft) / TileType.TILE_SIZE); col < Math.ceil(((x - shiftRight) + width) / TileType.TILE_SIZE); col++) {
                for (int layer = 0; layer < getLayers(); layer++) {

                    TileType type = getTileTypeByCoordinate(layer, col, row);

                    if (type != null) {
                        if (type.isForceDealer()) {

                            entities.get(0).setFloating(true);

                           // Gdx.app.log(type.getName(), "floating");
                        }
                        if (type.isDamageDealer()) {
                            entities.get(0).takeDamage(type.getDamage());
                            //Gdx.app.log(type.getName(), "taking damage = " + type.getDamage());
                        }

                        if (type.isCollectible()) {

                            Gdx.app.log(type.getName(), "collecting");

                            if (type.getName() == "HealthPotion") entities.get(0).takeDamage(-500);
                            else if (type.getName() == "EnergyPotion") entities.get(0).takeEnergy(-500);
                            else if (type.getName().contains("Coin")) {
                                entities.get(0).takeCoin((type.getId()-30)*5);
                            } else if (type.getName() == "Star") {
                                entities.get(0).takeStar();
                            }
                            ((TiledMapTileLayer)tiledMap.getLayers().get(layer)).getCell(col, row).setTile(null); // setCell
                        }

                        if (type.isCollidable()) {
                            return type.getId();
                        }


                    }
                }
            }
        }
        return -10;
    }

    public TileType getTileTypeByLocation(int layer, float x, float y) {
        return this.getTileTypeByCoordinate(layer, (int) x / TileType.TILE_SIZE, (int) y / TileType.TILE_SIZE);
    }



    public abstract TileType getTileTypeByCoordinate(int layer, int col, int row);


    // Width in Pixels
    public int getPixelWidth() {
        return this.getWidth() * TileType.TILE_SIZE;
    }

    // Height in Pixels
    public int getPixelHeight() {
        return this.getHeight() * TileType.TILE_SIZE;
    }

    public abstract int getResX(); // Pixels in Window width

    public abstract int getResY(); // Pixels in Window height

    public abstract int getWidth(); // Map width in tiles

    public abstract int getHeight(); // Map height in tiles

    public abstract int getLayers(); // number of Map layers



}
