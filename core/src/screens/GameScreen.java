package screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.laserhunter.LaserHunterGame;

import java.awt.Button;

import buttons.ButtonType;
import buttons.StageManager;
import maps.GameMap;
import maps.StartMap;
import tiles.TileType;

public class GameScreen implements Screen {

    LaserHunterGame game;
    OrthographicCamera camera;
    GameMap gameMap;

    ButtonType buttonLeft;
    ButtonType buttonRight;
    ButtonType attackButton;
    ButtonType defendButton;
    ButtonType pauseButton;

    StageManager manager;



    public GameScreen(LaserHunterGame game) {
        this.game = game;


        int w = 400;// Gdx.graphics.getWidth(); // Pixels per window
        int h = 300;// Gdx.graphics.getHeight(); // Pixels per window


        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);

        Gdx.app.log("screens", "GameScreen is created: " + Gdx.graphics.getWidth()+"x"+Gdx.graphics.getHeight());
        Gdx.app.log("screens", "GameScreen is created: " + w+"x"+h);

        gameMap = new StartMap(w,h);

        manager = new StageManager();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            buttonLeft = new ButtonType(manager.getStage(), "buttons/leftButton.atlas", Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 18);
            buttonRight = new ButtonType(manager.getStage(), "buttons/rightButton.atlas", Gdx.graphics.getWidth() / 6.5f, Gdx.graphics.getHeight() / 18);

            attackButton = new ButtonType(manager.getStage(), "buttons/attack.atlas", 7.8f*Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 25);
            defendButton = new ButtonType(manager.getStage(), "buttons/defence.atlas", 8.85f*Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 6);

            pauseButton = new ButtonType(manager.getStage(), "buttons/pause.atlas", Gdx.graphics.getWidth()*97.5f/100, Gdx.graphics.getHeight()*28.5f/30);

            pauseButton.create();
            buttonLeft.create();
            buttonRight.create();
            attackButton.create();
            defendButton.create();

            pauseButton.getButton().addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    pause();
                    return true;
                }});
        } else {
            buttonRight = buttonLeft = attackButton = defendButton = null;
        }

        gameMap.create(buttonLeft, buttonRight, attackButton, defendButton);




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        clearScreen();
        update();

        gameMap.render(camera, this.game.batch, Gdx.graphics.getDeltaTime());

        manager.render();

    }

    private void update() {


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pause();
        }


        camera.update();
        gameMap.update(camera, Gdx.graphics.getDeltaTime());

        if (gameMap.finish) {
            game.setScreen(new DeathScreen(this.game));
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
        game.setScreen(new PauseScreen(this.game, this));

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

