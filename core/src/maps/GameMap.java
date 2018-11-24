package maps;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import buttons.ButtonType;
import entities.Entity;
import hud.Bar;
import hud.Log;
import hud.TextRegion;
import snapshot.EntityLoader;
import tiles.TileType;

public abstract class GameMap {

    protected ArrayList<Entity> entities;


    protected TiledMap tiledMap;

    public static final int NO_COLLISION = -10;
    public static final int BOUNDARY_COLLISION = -1;

    public GameMap() {

        entities = new ArrayList<Entity>();
        entities.addAll(EntityLoader.loadEntity("entities", this, entities));


    }

    public void create(ButtonType buttonLeft, ButtonType buttonRight, ButtonType attackButton, ButtonType defendButton) {
        if (buttonLeft != null && buttonRight != null) {
            // Applying Button Listeners to player (entities.get(0))
            buttonLeft.getButton().addListener(getPlayer().movingLeftListener);
            buttonRight.getButton().addListener(getPlayer().movingRightListener);
            attackButton.getButton().addListener(getPlayer().attackListener);
            defendButton.getButton().addListener(getPlayer().defendListener);

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

        }

        followPlayer(getPlayer(), camera);


    }

    public void dispose(){
        this.dispose();
    }

    public abstract void addHealthBar(Entity entity);

    public abstract ArrayList<Bar> getBars();

    public abstract void addMessage(int id, int pid, String text,  Entity target, float width, float height, float lifespan, float delay);

    public abstract ArrayList<TextRegion> getMessages();


    public int getCollision(float x, float y, int width, int height, Entity entity) {
        // x + shiftLeft and x - shiftRight -  minimizing width from left and right on width*0.066f
        float shiftLeft = width*0.18f;
        float shiftRight = width*0.1f;
        float shiftUp = height*0.08f;

        if (x + shiftLeft < 0 || y < 0 || x - shiftRight + width   > getPixelWidth() || y - shiftUp + height > getPixelHeight()) {
            return BOUNDARY_COLLISION;
        }
        for (int row = (int)(y / TileType.TILE_SIZE); row < Math.ceil((y - shiftUp + height) / TileType.TILE_SIZE); row++) {
            for (int col = (int) ((x + shiftLeft) / TileType.TILE_SIZE); col < Math.ceil(((x - shiftRight) + width) / TileType.TILE_SIZE); col++) {
                for (int layer = 0; layer < getLayers(); layer++) {

                    TileType type = getTileTypeByCoordinate(layer, col, row);

                    if (type != null) {

                        if (entity != null) {
                            handleCollisions(type, layer, col, row, entity);
                        }


                        if (type.isCollidable()) {
                            return type.getId();
                        }


                    }
                }
            }
        }
        return NO_COLLISION;
    }

    public boolean getCollision(Vector2 pos1, Vector2 pos2, Vector2 size1, Vector2 size2) {
        if (pos1.x < pos2.x + size2.x && pos1.x + size1.x > pos2.x
                && pos1.y < pos2.y + size2.y && pos1.y + size1.y > pos2.y) {
            return true;
        }
        return false;
    }

    public Entity getCollisionWithEntities(Vector2 pos, Vector2 size, Entity exception) {

        for (Entity entity : getEntities()) {
            boolean collision = getCollision(pos, entity.getPos(), size, entity.getSize());
            if (collision == true) {
                if (entity != exception) {
                    return entity;
                }

            }
        }
        return null;
    }


    public void handleCollisions(TileType tile, int layer, int col, int row, Entity entity) {


        if (tile.getId() == 3) {

            if (entity.getX() < col * TileType.TILE_SIZE) {
                entity.setSloping(-1);
                entity.setGrounded(true);
            }

        }
        if (tile.getId() == 4) {
            if (entity.getX()+entity.getWidth() > col * TileType.TILE_SIZE) {
                entity.setSloping(1);
                entity.setGrounded(true);
            }
        }

        if (tile.isLiquid()) {

            entity.setFloating(true);

        }
        if (tile.isDamageDealer()) {
            if (tile.getDamage() > 0) {
                if (entity.getId() == "player") {
                    addMessage(0,0,"Ouch", entity, 50, 25, 2, 0);
                }


            }

            entity.takeDamage(tile.getDamage());
        }

        if (tile.isCollectible()) {

            getLog().add(tile.getName() + " collected");

            if (tile.getName() == "Star") {
                entity.takeStar();
            }else {
                entity.takeCoin(tile.getCoins());
                entity.takeDamage(tile.getHealth());
                entity.takeEnergy(tile.getEnergy());
            }

            ((TiledMapTileLayer)tiledMap.getLayers().get(layer)).getCell(col, row).setTile(null); // deleting collectible
        }

    }


    public void followPlayer(Entity player, OrthographicCamera camera) {


        if (player.getX() < getResX()/2) {  // w =  200
            camera.position.x = getResX()/2; // black background
        } else if (player.getX() > (getPixelWidth()) - getResX()/2) {
            camera.position.x = getPixelWidth() - getResX()/2;
        } else {
            camera.position.x = Math.round(player.getX() * TileType.TILE_SIZE) / TileType.TILE_SIZE;
        }

        if (player.getY()+25 < getResY()/2) { // h= 150
            camera.position.y = getResY()/2; // black background
        } else if (player.getY() > (getPixelHeight()) - getResY()/2) {
            camera.position.y = getPixelHeight() - getResY()/2;
        } else {
            camera.position.y = Math.round((player.getY()+25) * TileType.TILE_SIZE) / TileType.TILE_SIZE; // gaps
        }

    }

    public Entity getPlayer() {
        return entities.get(0);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
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

    public abstract Log getLog();
}