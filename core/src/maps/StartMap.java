package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import tiles.TileType;

public class StartMap extends GameMap {


    //private HashMap<Vector2, TileType> movableTiles;

    private HashMap<TiledMapTileLayer.Cell, Vector2> movableMap;

    OrthogonalTiledMapRenderer renderer;

    int dx = 0;
    float dy = 0;
    float d = 0;

    private int w,h;

    public StartMap(int w, int h) {

        this.w = w;
        this.h = h;

        tiledMap = new TmxMapLoader().load("map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);


        movableMap = generateMovableMap();

        //for (int col = 0; col < getWidth(); col ++) {
        //    for (int row = 0; row < getHeight(); row++) {
//
        //        TileType tile = getTileTypeByCoordinate(3, col, row);
        //        if (tile != null) {
        //            Gdx.app.log(tile.getName(), col+" "+row);
        //            movableMap.put(new Vector2(col, row), tile);
        //        }
//
        //    }
        //}



        //Gdx.app.log("tile 106, 119", "" + movableMap.get(new Vector2(106, 30)));

    }

    public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {
        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();


        //for (Vector2 key : movableMap.keySet()) {
//
        //    TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)tiledMap.getLayers().get(3)).getCell((int)key.x, (int)key.y);
        //    ((TiledMapTileLayer)tiledMap.getLayers().get(3)).setCell((int)key.x+1, (int)key.y, cell);
        //    movableMap.put(new Vector2(key.x+1, key.y+1), getTileTypeByCoordinate(3, (int)key.x+1, (int)key.y+1));
        //}

        for (TiledMapTileLayer.Cell cell : movableMap.keySet()) {
            float newX = movableMap.get(cell).x;
            float newY = movableMap.get(cell).y;

            TileType tile = getTileTypeByCoordinate(3, (int) newX, (int) newY) ;

            //if (tile.getName() == "VerticalPlatform") dx = 0;
            //if (tile.getName() == "HorizontalPlatform") dy = 0;

            //Gdx.app.log(dx + " " + dy + " " + d, tile.getName());
            if (tile.getName() == "VerticalPlatform") {
                changeMovableCell(newX+dx, newY+dy, cell);
            }

            cell.setTile(null);

        }

        movableMap = generateMovableMap();

        Gdx.app.log(dx +" "+ d, "");
        dx = (int) (2 * Math.sin(d));
        //dy = (float) (50 * Math.sin(d));
        d += 0.1;
        Gdx.app.log(dx +" "+ d, "");



        super.render(camera, batch, delta);
        batch.end();

    }

    public HashMap<TiledMapTileLayer.Cell, Vector2> generateMovableMap() {
        HashMap<TiledMapTileLayer.Cell, Vector2> map = new HashMap<TiledMapTileLayer.Cell, Vector2>();
        for (int col = 0; col < getWidth(); col ++) {
            for (int row = 0; row < getHeight(); row++) {

                TileType tile = getTileTypeByCoordinate(3, col, row);
                if (tile != null) {
                    TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)tiledMap.getLayers().get(3)).getCell(col, row);
                    map.put(cell, new Vector2(col, row));
                }

            }
        }
        return map;
    }

    public void changeMovableCell(float x, float y, TiledMapTileLayer.Cell cell) {
        ((TiledMapTileLayer)tiledMap.getLayers().get(3)).setCell((int)x, (int)y, cell);
    }

    @Override
    public void update(OrthographicCamera camera, float delta) {
        super.update(camera, delta);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        super.dispose();
    }

    @Override
    public TileType getTileTypeByCoordinate(int layer, int col, int row) {
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)tiledMap.getLayers().get(layer)).getCell(col, row);
        if (cell != null) {
            TiledMapTile tile = cell.getTile();
            if (tile != null) {
                int id = tile.getId();
                return TileType.getTileTypeById(id-1);
            }
        }
        return null;

    }


    @Override
    public int getResX(){
        return this.w;
    }
    @Override
    public int getResY(){
        return this.h;
    }

    @Override
    public int getWidth() {
        return ((TiledMapTileLayer)tiledMap.getLayers().get(0)).getWidth();
    }

    @Override
    public int getHeight() {

        return ((TiledMapTileLayer)tiledMap.getLayers().get(0)).getHeight();
    }

    @Override
    public int getLayers() {
        return tiledMap.getLayers().getCount();
    }



}
