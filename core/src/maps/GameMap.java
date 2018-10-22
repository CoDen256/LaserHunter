package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

import buttons.ButtonType;
import buttons.StageManager;
import entities.Entity;
import snapshot.EntityLoader;
import tiles.TileType;

public abstract class GameMap {

    protected ArrayList<Entity> entities;


    public GameMap() {

        entities = new ArrayList<Entity>();
        entities.addAll(EntityLoader.loadEntity("entities", this, entities));


    }

    public void create(ButtonType buttonLeft, ButtonType buttonRight) {
        buttonLeft.getButton().addListener(entities.get(0).movingLeftListener);
        buttonRight.getButton().addListener(entities.get(0).movingRightListener);

    }

    public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {
        for (Entity entity : entities) {
            entity.render(batch, delta);
        }



    }

    public void update(OrthographicCamera camera, float delta) {

        for (Entity entity : entities) {


            if (entity.getType().getId() == "player") {
                entity.update(delta, -entity.getG());
                camera.position.x = Math.round(entity.getX() * 16) / 16; // gaps
                camera.position.y = entity.getY();
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            EntityLoader.saveEntities("entities", entities);
        }


    }

    public void dispose(){
    }

    public TileType getTileTypeByLocation(int layer, float x, float y) {
        return this.getTileTypeByCoordinate(layer, (int) x / TileType.TILE_SIZE, (int) y / TileType.TILE_SIZE);
    }

    public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
        if (x < 0 || y < 0 || x + width > getPixelWidth() || y + height > getPixelHeight()) {
            return true;
        }
        for (int row = (int)(y / TileType.TILE_SIZE); row < Math.ceil((y + height) / TileType.TILE_SIZE); row++) {
            for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x + width) / TileType.TILE_SIZE); col++) {
                for (int layer = 0; layer < getLayers(); layer++) {

                    TileType type = getTileTypeByCoordinate(layer, col, row);

                    if (type != null && type.isCollidable()) {

                        return true;
                    }
                }
            }
        }
        return false;
    }

    public abstract TileType getTileTypeByCoordinate(int layer, int col, int row);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getLayers();

    public int getPixelWidth() {
        return this.getWidth() * TileType.TILE_SIZE;
    }

    public int getPixelHeight() {
        return this.getHeight() * TileType.TILE_SIZE;
    }

}
