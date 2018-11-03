package screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.laserhunter.LaserHunterGame;

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

    StageManager manager;




    public GameScreen(LaserHunterGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        int w = 400;
        int h = 300;

        w = 1000;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);

        Gdx.app.log("screens", "GameScreen is created");

        gameMap = new StartMap(w,h);

        manager = new StageManager();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            buttonLeft = new ButtonType(manager.getStage(), "buttons/leftButton.atlas", Gdx.graphics.getWidth() / 30, h / 5);
            buttonRight = new ButtonType(manager.getStage(), "buttons/rightButton.atlas", Gdx.graphics.getHeight() / 3, h / 5);

            buttonLeft.create();
            buttonRight.create();
        } else {
            buttonRight = buttonLeft = null;
        }

        gameMap.create(buttonLeft, buttonRight);



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
        game.setScreen(new PauseScreen(this.game));

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
