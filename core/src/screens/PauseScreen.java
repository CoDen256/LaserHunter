package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.laserhunter.LaserHunterGame;

public class PauseScreen implements Screen {

    LaserHunterGame game;

    BitmapFont font;

    public PauseScreen(LaserHunterGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("screens", "PauseScreen is created");
        font = new BitmapFont();


    }

    @Override
    public void render(float delta) {

        clearScreen();
        update();

        this.game.batch.begin();
        font.draw(this.game.batch, "Game is paused", Gdx.graphics.getWidth()/3, 3*Gdx.graphics.getHeight()/4);
        this.game.batch.end();

    }

    private void update() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.justTouched()) {
            this.game.setScreen(new GameScreen(this.game));
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            this.game.setScreen(new MainMenuScreen(this.game));
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
