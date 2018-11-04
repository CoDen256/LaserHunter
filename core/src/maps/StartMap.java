package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

import tiles.TileType;

public class StartMap extends GameMap {


    private HashMap<TiledMapTileLayer.Cell, Vector2> movableMap;

    float tileVelX;
    float current_shift = -1;
    float speed = 0.5f;
    float tick = 1;
    int direction = 1;


    ShapeRenderer shapeRenderer;
    SpriteBatch hudBatch;
    Texture bar;
    BitmapFont font;

    float rateX;
    float rateY;

    float playerMaxHealth;
    float playerMaxEnergy;



    OrthogonalTiledMapRenderer renderer;

    private int w,h;


    public StartMap(int w, int h) {

        this.w = w;
        this.h = h;

        tiledMap = new TmxMapLoader().load("map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);


        movableMap = generateMovableMap();

        // Hud render
        shapeRenderer = new ShapeRenderer();
        hudBatch = new SpriteBatch();

        playerMaxHealth = this.getPlayer().getMaxHealth();
        playerMaxEnergy = this.getPlayer().getMaxEnergy();

        bar = new Texture("HUD/bar4.png");

        rateX = Gdx.graphics.getWidth()/640;
        rateY = Gdx.graphics.getHeight()/480;

        font = new BitmapFont();
        font.getData().setScale(rateX, rateY);

    }

    public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {


        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.render(camera, batch, delta);

        //updateMovableTiles();
        //movableMap = generateMovableMap();

        batch.end();

        drawHUD();




    }

    public void drawHUD() {
        float xBar = Gdx.graphics.getWidth()* 1/100;
        float yBar1 = Gdx.graphics.getHeight()* 28/30;
        float yBar2 = Gdx.graphics.getHeight()* 26/30;

        float shiftX = 5 * rateX;
        float shiftY = 5 * rateY;

        float width = 132*rateX;
        float height = 9*rateY;

        hudBatch.begin();

        hudBatch.draw(bar, xBar, yBar1, bar.getWidth()*rateX, bar.getHeight()*rateY);
        hudBatch.draw(bar, xBar, yBar2, bar.getWidth()*rateX, bar.getHeight()*rateY);
        font.draw(hudBatch, (int)getPlayer().getCoins()+"", Gdx.graphics.getWidth()*96/100, Gdx.graphics.getHeight()*29/30);
        font.draw(hudBatch, (int)getPlayer().getStars()+"", Gdx.graphics.getWidth()*96/100, Gdx.graphics.getHeight()*27/30);

        hudBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        drawHealthBar(xBar+shiftX, yBar1+shiftY,shapeRenderer, width, height);
        drawEnergyBar(xBar+shiftX,yBar2+shiftY, shapeRenderer, width, height);

        shapeRenderer.end();



    }
    public void drawHealthBar(float x, float y, ShapeRenderer shapeRenderer, float width, float height) {
        float rate = this.getPlayer().getHealth() / playerMaxHealth;
        shapeRenderer.setColor(1f, 0.4f, 0.4f, 0);
        shapeRenderer.rect(x,y,rate*width,height);

    }

    public void drawEnergyBar(float x, float y, ShapeRenderer shapeRenderer, float width, float height){
        float rate = this.getPlayer().getEnergy() / playerMaxHealth;

        shapeRenderer.setColor(0.4f, 0.4f, 1f, 0);
        shapeRenderer.rect(x,y,rate*width,height);
    }


    public void updateMovableTiles() {
        current_shift +=speed;
        if (current_shift > 1) {
            current_shift = -1;
        }

        tick += 1;



        tileVelX = Interpolation.fade.apply(current_shift);
        if (current_shift < 0) {
            tileVelX = Interpolation.fade.apply(-current_shift); }

        if (true) {
            tileVelX = (tick % 2) / 2 + 0.5f;
        } else {
            tileVelX = -((tick % 2) / 2 + 0.5f);
        }


        Gdx.app.log(direction+" "+tileVelX, current_shift+" " + tick);

        for (TiledMapTileLayer.Cell cell : movableMap.keySet()) {

            Gdx.app.log(cell.getTile().getId()-1+"", "");
            float newX = movableMap.get(cell).x;
            float newY = movableMap.get(cell).y;

            TileType tile = getTileTypeByCoordinate(3, (int) newX, (int) newY) ;

            //if (tile.getName() == "VerticalPlatform") dx = 0;
            //if (tile.getName() == "HorizontalPlatform") dy = 0;

            //Gdx.app.log(dx + " " + dy + " " + d, tile.getName());
            if (tile.getName() == "HorizontalPlatform") {
                //Gdx.app.log(newY + "", dx + "");
                //changeMovableCell(newX + dx, newY, cell);

                //cell.setTile(null);
            } else {
               // Gdx.app.log(newX+"", tileVelX + "");
                if (direction > 0) {
                    changeMovableCell(newX + tileVelX, newY, cell);
                } else {
                    changeMovableCell(newX + tileVelX, newY, cell);
                }

            }



        }
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
        TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
        newCell.setTile(cell.getTile());
        cell.setTile(null);
        ((TiledMapTileLayer)tiledMap.getLayers().get(3)).setCell((int)x, (int)y, newCell);

    }

    @Override
    public void update(OrthographicCamera camera, float delta) {
        super.update(camera, delta);
    }

    @Override
    public void dispose() {
        hudBatch.dispose();
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
    public float getRateX() {
        return rateX;
    }

    @Override
    public float getRateY() {
        return rateY;
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
