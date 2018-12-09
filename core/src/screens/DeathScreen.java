package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.laserhunter.LaserHunterGame;

import buttons.ButtonType;
import buttons.StageManager;

public class DeathScreen implements Screen {

    SpriteBatch deathBatch;
    LaserHunterGame game;
    Texture cat, message, bg;

    ButtonType againButton;
    ButtonType mainMenuButton;

    StageManager manager;


    public DeathScreen(final LaserHunterGame game) {
        Gdx.app.log("screens", "DeathScreen is created");
        this.game = game;
        deathBatch = new SpriteBatch();

        bg = new Texture("background.png");
        cat = new Texture("dead.png");
        message = new Texture("death.png");

        manager = new StageManager();

        againButton = new ButtonType(manager.getStage(), "buttons/again.atlas", Gdx.graphics.getWidth()/ 2 -120, Gdx.graphics.getHeight()*2.2f/5);
        mainMenuButton = new ButtonType(manager.getStage(), "buttons/mm.atlas", Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()*1/5);

        againButton.create();
        mainMenuButton.create();

        againButton.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }});

        mainMenuButton.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }});

    }

    @Override
    public void show() {
        Gdx.app.log("screens", "DeathScreen is showed");


    }

    @Override
    public void render(float delta) {

        clearScreen();
        update();

        deathBatch.begin();

        deathBatch.draw(bg, 0, 0);

        deathBatch.draw(message,Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()*4.2f/5);
        deathBatch.draw(cat, Gdx.graphics.getWidth()/2-55, Gdx.graphics.getHeight()*3.3f/5, 110, 75);

        deathBatch.end();

        manager.render();

    }

    private void update() {


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
