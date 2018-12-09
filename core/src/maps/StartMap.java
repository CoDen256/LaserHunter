package maps;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.Text;

import entities.Entity;
import hud.Bar;
import hud.BarType;
import hud.Log;
import hud.TextRegion;
import tiles.TileType;

public class StartMap extends GameMap {

    ShapeRenderer HUDRenderer;
    ShapeRenderer mapHUDRenderer;

    SpriteBatch HUDBatch;
    SpriteBatch mapHUDBatch;

    TextRegion coinsCollected;
    TextRegion starsCollected;
    Texture life,coin,star;

    Bar playerBar;

    Log log;

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

        playerBar = new Bar(getPlayer(), rateX, rateY, BarType.HUD_BAR);
        life = new Texture("entities/player/life.png");
        log = new Log(5*rateX, Gdx.graphics.getHeight() * 5.5f/7, 5, 150*rateX, 1f*rateX, 1f*rateY);

        coin = new Texture("coin.png");
        star = new Texture("star.png");

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

        Iterator iterator = bars.iterator();

        while (iterator.hasNext()) {
            Bar bar = (Bar) iterator.next();

            bar.fill(mapHUDRenderer, 0);
            //Gdx.app.log(bar.getEntity().getType().getId(), bar.getEntity().getHealth()+"");
            if (bar.getEntity().getHealth() < 1) {
                iterator.remove();
            }
        }


        //mapHUDRenderer.line(getPlayer().centerX(), getPlayer().centerY(), entities.get(2).centerX(), entities.get(2).centerY());

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

        log.render(HUDBatch);


        HUDBatch.draw(coin, Gdx.graphics.getWidth()*92/100, Gdx.graphics.getHeight()*28/30 - 10*rateY + 10, 16*rateX, 16*rateY); // y+0 -> y+10
        HUDBatch.draw(star, Gdx.graphics.getWidth()*92/100, Gdx.graphics.getHeight()*27/30 - 10*rateY + 10, 16*rateX, 16*rateY);
        coinsCollected.render(HUDBatch, Gdx.graphics.getWidth()*95/100, Gdx.graphics.getHeight()*28.2f/30 + 10);
        starsCollected.render(HUDBatch, Gdx.graphics.getWidth()*95/100, Gdx.graphics.getHeight()*27/30 + 10);

        /*
        for (int i = 0; i < getPlayer().getLifes(); i++) {
            HUDBatch.draw(life, playerBar.getWidth() + BARX*rateX + 5 + i * 10*rateX/2, BARY1-1, 16*rateX, 16*rateY);
        }
        */

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

        log.update(delta);

        Iterator iterator = messages.iterator();

        while (iterator.hasNext()) {
            TextRegion message = (TextRegion) iterator.next();

            message.update(delta);

            if (message.getTick() > message.getLifespan()) {
                message.dispose();
                iterator.remove();

            }
        }


    }

    @Override
    public void dispose() {

        HUDBatch.dispose();
        mapHUDBatch.dispose();

        HUDRenderer.dispose();
        mapHUDRenderer.dispose();

        clearSprites();

        tiledMap.dispose();

        super.dispose();
    }

    public void clearSprites() {
        bars.clear();
        messages.clear();

    }


    @Override
    public void addHealthBar(Entity entity) {
        Gdx.app.log("HealthBar added", entity.getId());
        Bar newBar = new Bar(entity, 0.075f, 0.075f, BarType.ENTITY_HEALTH_BAR);
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
        //Gdx.app.log("addMessage", "adding new message");

        TextRegion newMessage;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            newMessage = new TextRegion(id, pid, text, target, 0.65f, 0.65f, width, height, lifespan, delay);
        } else {
            newMessage = new TextRegion(id, pid, text, target, 0.5f, 0.5f, width, height, lifespan, delay);
        }

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
    public Log getLog() {
        return log;
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