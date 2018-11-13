package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;

import entities.Entity;
import entities.Player;
import hud.Bar;
import hud.TextRegion;
import tiles.TileType;

public class StartMap extends GameMap {

    ShapeRenderer HUDRenderer;
    ShapeRenderer mapHUDRenderer;

    SpriteBatch HUDBatch;
    SpriteBatch mapHUDBatch;

    TextRegion coinsCollected;
    TextRegion starsCollected;

    Bar playerBar;

    static ArrayList<TextRegion> messages = new ArrayList<TextRegion>();
    static ArrayList<Bar> bars =  new ArrayList<Bar>();


    OrthogonalTiledMapRenderer renderer;

    float rateX;
    float rateY;
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




        // HUD render

        HUDBatch = new SpriteBatch(); // For HUD on the window with static postion
        mapHUDBatch = new SpriteBatch(); // For HUD on the map depending on circumstances

        HUDRenderer = new ShapeRenderer();
        mapHUDRenderer = new ShapeRenderer();

        coinsCollected = new TextRegion("", rateX, rateY, 50, 0);
        starsCollected = new TextRegion("",rateX, rateY, 50, 0);

        playerBar = Bar.HUD_BAR.apply(getPlayer(), rateX, rateY);


    }

    public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {


        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.render(camera, batch, delta);

        batch.end();

        drawMapHUD(camera);
        drawHUD();


    }



    public void drawMapHUD(OrthographicCamera camera) {
        mapHUDRenderer.setProjectionMatrix(camera.combined);
        mapHUDBatch.setProjectionMatrix(camera.combined);


        mapHUDRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Bar bar : getBars()) {
            bar.fill(mapHUDRenderer, 0);
        }
        mapHUDRenderer.end();


        mapHUDBatch.begin();

        for (Bar bar : getBars()) {
            bar.draw(mapHUDBatch);
        }

        for (TextRegion message : messages) {
            if (message.getTick() > message.getDelay()) {
                message.render(mapHUDBatch);
            }

        }

        mapHUDBatch.end();


    }

    public void drawHUD() {

        HUDBatch.begin();


        playerBar.draw(HUDBatch, BARX, BARY1);
        playerBar.draw(HUDBatch, BARX, BARY2);


        coinsCollected.render(HUDBatch, Gdx.graphics.getWidth()*95/100, Gdx.graphics.getHeight()*29/30);
        starsCollected.render(HUDBatch, Gdx.graphics.getWidth()*95/100, Gdx.graphics.getHeight()*27/30);

        HUDBatch.end();


        HUDRenderer.begin(ShapeRenderer.ShapeType.Filled);

        playerBar.fill(HUDRenderer, 0,BARX, BARY1);
        playerBar.fill(HUDRenderer, 1, BARX, BARY2);


        HUDRenderer.end();


    }

    @Override
    public void update(OrthographicCamera camera, float delta) {
        super.update(camera, delta);

        coinsCollected.updateText((int)getPlayer().getCoins()+"");
        starsCollected.updateText((int)getPlayer().getStars()+"");


        for (TextRegion message : messages) {
            message.update(delta);

            if (message.getTick() > message.getLifespan()) {
                message.dispose();
                messages.remove(message);
                if (messages.isEmpty()) break;

            }
        }
    }

    @Override
    public void dispose() {
        HUDBatch.dispose();
        mapHUDBatch.dispose();

        HUDRenderer.dispose();
        mapHUDRenderer.dispose();

        tiledMap.dispose();
        super.dispose();
    }


    @Override
    public void addHealthBar(Entity entity) {
        Bar newBar = Bar.ENTITY_HEALTH_BAR.apply(entity, 0.075f, 0.075f);
        bars.add(newBar);
    }
    @Override
    public ArrayList<Bar> getBars() {
        return bars;
    }

    @Override
    public void addMessage(int id, int pid, String text,  Entity target, float width, float height, float lifespan, float delay) {
        for (TextRegion message : messages) {
            if (message.getPid() == pid) {
                if (message.getId() == id) {
                    return;
                } else {
                    Gdx.app.log("addMessage", "removing old message");
                    messages.remove(message);
                    break;
                }

            }
        }
        Gdx.app.log("addMessage", "adding new message");

        TextRegion newMessage = new TextRegion(id, pid, text, target, 1f, 1f, width, height, lifespan, delay);
        messages.add(newMessage);


    }

    @Override
    public ArrayList<TextRegion> getMessages() {
        return messages;
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