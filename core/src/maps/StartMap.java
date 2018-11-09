package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import hud.Bar;
import tiles.TileType;

public class StartMap extends GameMap {

    ShapeRenderer shapeRenderer;
    ShapeRenderer shapeBarRenderer;
    SpriteBatch hudBatch;
    SpriteBatch hudBarBatch;
    Texture bar;
    BitmapFont font;

    Bar playerBar;
    Bar playerEnergyBar;

    float rateX;
    float rateY;

    OrthogonalTiledMapRenderer renderer;

    private int w,h;


    public StartMap(int w, int h) {

        this.w = w; // MapPixels per window
        this.h = h;

        rateX = Gdx.graphics.getWidth()/640; //Ration of normal Pixels per Window
        rateY = Gdx.graphics.getHeight()/480;


        tiledMap = new TmxMapLoader().load("map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Hud render
        shapeRenderer = new ShapeRenderer();
        shapeBarRenderer = new ShapeRenderer();
        hudBatch = new SpriteBatch();
        hudBarBatch = new SpriteBatch();

        //font = new BitmapFont();
        //font.getData().setScale(rateX, rateY);

        playerBar = Bar.HUD_BAR.apply(getPlayer(), rateX, rateY);
    }

    public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {


        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.render(camera, batch, delta);

        batch.end();

        drawEntityHealthBar(camera);
        drawHUD();





    }

    public void drawHUD() {

        hudBatch.begin();



        //hudBatch.draw(bar, xBar, yBar1, bar.getWidth()*rateX, bar.getHeight()*rateY);
        //hudBatch.draw(bar, xBar, yBar2, bar.getWidth()*rateX, bar.getHeight()*rateY);

        playerBar.drawBar(hudBatch, Gdx.graphics.getWidth()* 1/100, Gdx.graphics.getHeight()* 28/30);
        playerBar.drawBar(hudBatch, Gdx.graphics.getWidth()* 1/100, Gdx.graphics.getHeight()* 26/30);
       // playerEnergyBar.drawBar(hudBatch);

        //font.draw(hudBatch, (int)getPlayer().getCoins()+"", Gdx.graphics.getWidth()*96/100, Gdx.graphics.getHeight()*29/30);
        //font.draw(hudBatch, (int)getPlayer().getStars()+"", Gdx.graphics.getWidth()*96/100, Gdx.graphics.getHeight()*27/30);



        hudBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        playerBar.fillBar(shapeRenderer, 0,Gdx.graphics.getWidth()* 1/100, Gdx.graphics.getHeight()* 28/30);
        playerBar.fillBar(shapeRenderer, 1, Gdx.graphics.getWidth()* 1/100, Gdx.graphics.getHeight()* 26/30);
        //.drawEnergyBar(shapeRenderer);


        shapeRenderer.end();



    }
    public void drawHealthBar(float x, float y, ShapeRenderer shapeRenderer, float width, float height) {
        float rate = this.getPlayer().getHealth() / getPlayer().getMaxHealth();
        shapeRenderer.setColor(1f, 0.4f, 0.4f, 0);
        shapeRenderer.rect(x,y,rate*width,height);

    }

    public void drawEnergyBar(float x, float y, ShapeRenderer shapeRenderer, float width, float height){
        float rate = this.getPlayer().getEnergy() / getPlayer().getMaxEnergy();

        shapeRenderer.setColor(0.4f, 0.4f, 1f, 0);
        shapeRenderer.rect(x,y,rate*width,height);
    }

    public void drawEntityHealthBar(OrthographicCamera camera) {
        shapeBarRenderer.setProjectionMatrix(camera.combined);

        shapeBarRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Bar bar : getBars()) {
            bar.fillBar(shapeBarRenderer, 0);
        }

        shapeBarRenderer.end();

        hudBarBatch.setProjectionMatrix(camera.combined);

        hudBarBatch.begin();

        for (Bar bar : getBars()) {
            bar.drawBar(hudBarBatch);
        }

        hudBarBatch.end();


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