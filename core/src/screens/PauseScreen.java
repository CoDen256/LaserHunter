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

public class PauseScreen implements Screen {

    LaserHunterGame game;
    Screen previousScreen;
    SpriteBatch pauseBatch;
    Texture message,bg,cat;

    ButtonType resumeButton;
    ButtonType mainMenuButton;

    StageManager manager;



    public PauseScreen(final LaserHunterGame game, final GameScreen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
        pauseBatch = new SpriteBatch();

        Gdx.app.log("screens", "PauseScreen is created");


        manager = new StageManager();

        resumeButton = new ButtonType(manager.getStage(), "buttons/resume.atlas", Gdx.graphics.getWidth()/ 2 -120, Gdx.graphics.getHeight()*2.2f/5);
        mainMenuButton = new ButtonType(manager.getStage(), "buttons/mm.atlas", Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()*1/5);

        resumeButton.create();
        mainMenuButton.create();

        resumeButton.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousScreen);
            }});

        mainMenuButton.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousScreen.getGameMap().clearSprites();
                game.setScreen(new MainMenuScreen(game));
            }});

        message = new Texture("pausemessage.png");
        bg = new Texture("background.png");
        cat = new Texture("cat.png");
    }

    @Override
    public void show() {
        Gdx.app.log("screens", "PauseScreen is showed");

    }

    @Override
    public void render(float delta) {

        clearScreen();
        update();

        pauseBatch.begin();

        pauseBatch.draw(bg, 0, 0);
        pauseBatch.draw(message, Gdx.graphics.getWidth()/2-179, Gdx.graphics.getHeight()*4/5);
        pauseBatch.draw(cat, Gdx.graphics.getWidth()/2-55, Gdx.graphics.getHeight()*3.2f/5);

        pauseBatch.end();

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
