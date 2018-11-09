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
import hud.TextRegion;
import tiles.TileType;

public class StartMap extends GameMap {

    ShapeRenderer shapeRenderer;
    ShapeRenderer shapeBarRenderer;

    SpriteBatch HUDBatch;
    SpriteBatch hudBarBatch;

    TextRegion coinsCollected;
    TextRegion starsCollected;

    Bar playerBar;

    float rateX;
    float rateY;

    OrthogonalTiledMapRenderer renderer;

    private int w,h;

    private static final float BARX = Gdx.graphics.getWidth() * 1/100;
    private static final float BARY1 = Gdx.graphics.getHeight() * 28/30;
    private static final float BARY2 = Gdx.graphics.getHeight() * 26/30;


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

        HUDBatch = new SpriteBatch();
        hudBarBatch = new SpriteBatch();

        //font = new BitmapFont();
        //font.getData().setScale(rateX, rateY);
        coinsCollected = new TextRegion("", 1, 1, 50, 0);
        starsCollected = new TextRegion("",1, 1, 50, 0);

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

        HUDBatch.begin();


        playerBar.drawBar(HUDBatch, BARX, BARY1);
        playerBar.drawBar(HUDBatch, BARX, BARY2);


        coinsCollected.render(HUDBatch, Gdx.graphics.getWidth()*95/100, Gdx.graphics.getHeight()*29/30);
        starsCollected.render(HUDBatch, Gdx.graphics.getWidth()*95/100, Gdx.graphics.getHeight()*27/30);

        HUDBatch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        playerBar.fillBar(shapeRenderer, 0,BARX, BARY1);
        playerBar.fillBar(shapeRenderer, 1, BARX, BARY2);


        shapeRenderer.end();
        

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

        coinsCollected.updateText((int)getPlayer().getCoins()+"");
        starsCollected.updateText((int)getPlayer().getStars()+"");


        super.update(camera, delta);
    }

    @Override
    public void dispose() {
        HUDBatch.dispose();
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