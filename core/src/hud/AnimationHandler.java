package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimationHandler {
    int current_animation;
    float stateTime = 0;
    // 0 - static
    // 1 - run
    // 3 - jump
    // 2 - fall
    // 4 - attack

    HashMap<Integer, Animation[]> animMap;

    public static final float STATIC = 0.2f;
    public static final float RUNNING = 0.1f;
    public static final float FALLING = 0.3f;
    public static final float JUMPING = 0.05f;
    public static final float ATTACKING = 0.1f;

    public AnimationHandler() {
        animMap = new HashMap<Integer, Animation[]>();
        stateTime = 0f;
    }

    public void initialize(int id, String path, int w, int h) {
        Animation[] rolls = new Animation[2];

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("entities/Animation/"+path), w,h);

        rolls[0] = new Animation(getAnimationSpeed(id), rollSpriteSheet[0]); // left
        rolls[1] = new Animation(getAnimationSpeed(id), rollSpriteSheet[1]); // right

        animMap.put(id, rolls);

    }

    public void setAnim(int current_animation) {
        this.current_animation = current_animation;


    }

    public void annulateStateTime() {
        stateTime = 0;
    }

    public float getAnimationSpeed(int id) {
        switch (id) {
            case 0:
                return STATIC;
            case 1:
                return RUNNING;

            case 2:
                return FALLING;

            case 3:
                return JUMPING;

            case 4:
                return ATTACKING;
        }
        return 0;
    }

    public int getCurrent_animation() {
        return current_animation;
    }

    public Animation[] getCurrentAnim() {
        return animMap.get(current_animation);
    }

    public void increaseStateTime(float delta) {
        stateTime += delta;
    }

    public float getStateTime() {
        return stateTime;
    }
}
