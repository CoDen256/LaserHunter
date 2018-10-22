package buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StageManager {
    Stage stage;

    public StageManager() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }


    public void render() {
        stage.act();
        stage.draw();

    }

    public void add(Actor actor) {
        stage.addActor(actor);
    }

    public Stage getStage() {
        return stage;
    }
}
