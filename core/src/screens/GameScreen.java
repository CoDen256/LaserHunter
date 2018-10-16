package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.laserhunter.LaserHunterGame;

import maps.GameMap;
import maps.GameMap1;

public class GameScreen implements Screen {

    LaserHunterGame game;
    OrthographicCamera camera;
    GameMap gameMap;

    public GameScreen(LaserHunterGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        gameMap = new GameMap1();
        int w = 900;
        int h = 450;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);

    }

    @Override
    public void render(float delta) {

        clearScreen();
        update();

        gameMap.render(camera, this.game.batch, Gdx.graphics.getDeltaTime());




    }

    private void update() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pause();
        }

        if (Gdx.input.isTouched()) {
            camera.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
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
