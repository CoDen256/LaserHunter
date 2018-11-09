package buttons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonType extends Game {
    Stage stage;
    Button button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    private String path;
    private String text;

    private float x,y;

    public ButtonType(Stage stage, String path, float x, float y) {
        this(stage, path, "", x, y, 1, 1);
    }
    public ButtonType(Stage stage, String path, String text, float x, float y, float scale) {
        this(stage, path, text, x, y, scale, scale);
    }

    public ButtonType(Stage stage, String path, String text, float x, float y, float scaleX, float scaleY) {
        this.stage = stage;
        this.path = path;
        this.text = text;
        this.x = x;
        this.y = y;

        this.font = new BitmapFont();
        this.font.getData().setScale(scaleX, scaleY);
    }

    @Override
    public void create() {
        skin = new Skin();

        buttonAtlas = new TextureAtlas(Gdx.files.internal(this.path));

        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = this.font;
        textButtonStyle.up = skin.getDrawable("buttonUp");
        textButtonStyle.down = skin.getDrawable("buttonDown");
        textButtonStyle.over = skin.getDrawable("buttonOver");

        if (text == "") {
            button = new Button(textButtonStyle);

        } else {
            button = new TextButton(text, textButtonStyle);

        }
        stage.addActor(button);

        button.setX(x);
        button.setY(y);

    }

    @Override
    public void render() {
        super.render();
    }

    public Button getButton() {
        return button;
    }
}
