package screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.laserhunter.LaserHunterGame;

import java.util.ArrayList;

import buttons.ButtonType;
import buttons.StageManager;
import entities.Entity;
import entities.EntityType;
import entities.RedLaser;
import snapshot.EntitySnapshot;

public class MainMenuScreen implements Screen {

    LaserHunterGame game;
    BitmapFont font;
    StageManager manager;

    Texture backgorund, logo;
    SpriteBatch bgBatch;

    ButtonType startButton;
    ButtonType quitButton;
    ButtonType loadButton;

    ArrayList<RedLaser> lasers;

    public MainMenuScreen(LaserHunterGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("screens", "MainMenuScreen is created");
        font = new BitmapFont();

        lasers = new ArrayList<RedLaser>();
        manager = new StageManager();

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();


        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            startButton = new ButtonType(manager.getStage(), "buttons/androidStart.atlas", "", w/2-120, 2*h/5, 2f, 3f);
            quitButton = new ButtonType(manager.getStage(), "buttons/androidStart.atlas", "", w/2-120, h/5, 2f, 3f);

        } else {
            startButton = new ButtonType(manager.getStage(), "buttons/desktopStart.atlas", "", w/2-120, 2.5f*h/5 ,1f);
            quitButton = new ButtonType(manager.getStage(), "buttons/desktopQuit.atlas", "", w/2-120, h/5,1f );
        }

        RedLaser laser1 = new RedLaser();
        RedLaser laser2 = new RedLaser();
        EntitySnapshot laserSnapshot = new EntitySnapshot("RedLaser", w/5, 40);

        laserSnapshot.beamRate = 10;
        laserSnapshot.beamLifeSpan = 5.0f;
        laserSnapshot.beamVelocity = 400;
        laserSnapshot.staticPosition = 1;
        laserSnapshot.initialAngle = 0;

        laser1.create(laserSnapshot, EntityType.REDLASER, null);
        lasers.add(laser1);

        laserSnapshot.setX(4 * w / 5);
        laser2.create(laserSnapshot, EntityType.REDLASER, null);
        lasers.add(laser2);


        startButton.create();
        quitButton.create();


        startButton.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }});

        quitButton.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }});

        bgBatch = new SpriteBatch();
        backgorund = new Texture("background.png");
        logo = new Texture("game.png");


    }

    @Override
    public void render(float delta) {

        clearScreen();
        update();

        bgBatch.begin();
        bgBatch.draw(backgorund, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (RedLaser laser : lasers) {
            laser.render(bgBatch, delta);
        }


        bgBatch.draw(logo, Gdx.graphics.getWidth()/2 - 275, Gdx.graphics.getHeight()*4/5);

        bgBatch.end();


        this.game.batch.begin();

        manager.render();

        this.game.batch.end();
    }

    private void update() {

        for (RedLaser laser : lasers) {
            laser.update(Gdx.graphics.getDeltaTime(), true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.justTouched()) {
           // this.game.setScreen(new GameScreen(this.game));
        }

    }


    public void clearScreen(){
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


}
