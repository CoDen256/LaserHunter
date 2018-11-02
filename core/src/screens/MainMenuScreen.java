package screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.laserhunter.LaserHunterGame;

import buttons.ButtonType;
import buttons.StageManager;

public class MainMenuScreen implements Screen {

    LaserHunterGame game;
    BitmapFont font;
    StageManager manager;

    ButtonType startButton;
    ButtonType quitButton;

    public MainMenuScreen(LaserHunterGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("screens", "MainMenuScreen is created");
        font = new BitmapFont();

        manager = new StageManager();

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();


        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            startButton = new ButtonType(manager.getStage(), "buttons/androidStart.atlas", "Start", w/2-150, 2*h/3, 2f, 3f);
            quitButton = new ButtonType(manager.getStage(), "buttons/androidStart.atlas", "Quit", w/2-150, h/3, 2f, 3f);

        } else {
            startButton = new ButtonType(manager.getStage(), "buttons/desktopStart.atlas", "Start", w/2-150, 2*h/3 ,1f);
            quitButton = new ButtonType(manager.getStage(), "buttons/desktopStart.atlas", "Quit", w/2-150, h/3,1f );
        }



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


    }

    @Override
    public void render(float delta) {



        clearScreen();
        update();

        this.game.batch.begin();
        manager.render();
        font.draw(this.game.batch, "Main Menu", Gdx.graphics.getWidth()/3, 3*Gdx.graphics.getHeight()/4);
        this.game.batch.end();
    }

    private void update() {

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
