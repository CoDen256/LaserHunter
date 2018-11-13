package maps;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

import buttons.ButtonType;
import entities.Entity;
import hud.Bar;
import hud.TextRegion;
import snapshot.EntityLoader;
import tiles.TileType;

public abstract class GameMap {

    protected ArrayList<Entity> entities;


    protected TiledMap tiledMap;


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
            return -1;
        }
        for (int row = (int)(y / TileType.TILE_SIZE); row < Math.ceil((y - shiftUp + height) / TileType.TILE_SIZE); row++) {
            for (int col = (int) ((x + shiftLeft) / TileType.TILE_SIZE); col < Math.ceil(((x - shiftRight) + width) / TileType.TILE_SIZE); col++) {
                for (int layer = 0; layer < getLayers(); layer++) {

                    TileType type = getTileTypeByCoordinate(layer, col, row);

                    if (type != null) {

                        handleCollisions(type, layer, col, row, entity);

                        if (type.isCollidable()) {
                            return type.getId();
                        }


                    }
                }
            }
        }
        return -10;
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
                if (entity.getType().getId() == "player") {
                    addMessage(0,0,"Ouch", entity, 50, 25, 2, 0);
                }


            }

            entity.takeDamage(tile.getDamage());
        }

        if (tile.isCollectible()) {

            Gdx.app.log(tile.getName(), "collecting");

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

}