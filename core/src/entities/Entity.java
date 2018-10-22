package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import maps.GameMap;
import snapshot.EntitySnapshot;

public abstract class Entity{

    protected Vector2 pos;
    protected EntityType type;
    protected float velocityY = 0;
    protected GameMap map;
    protected boolean grounded = false;
    protected boolean movingRight;
    protected boolean movingLeft;

    private static final int JUMP_HEIGHT = 50;
    private static final float g = 2000;
    private static final int JUMP_VELOCITY = 10;


    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        this.pos = new Vector2(snapshot.getX(), snapshot.getY());
        this.type = type;
        this.map = map;
        this.movingRight = false;
        this.movingLeft = false;
    }

    public void update(float deltatime, float gravity) {

        float newY = pos.y;
        this.velocityY += gravity * deltatime;
        newY += this.velocityY * deltatime;

        if (map.doesRectCollideWithMap(pos.x, newY, (int) getWidth(), (int) getHeight())) {
            if (velocityY < 0) {
                this.pos.y = (float) Math.floor(pos.y);
                grounded = true;
            }
            this.velocityY = 0;
        } else {
            this.pos.y =  newY;
            grounded = false;
        }

    }

    public abstract void render(SpriteBatch batch, float delta);

    public void moveX(float amount) {
        float newX = pos.x + amount;
        if (!map.doesRectCollideWithMap(newX, pos.y, (int) getWidth(), (int) getHeight())) {
            this.pos.x = newX;
        }
    }

    public EntitySnapshot getSaveSnapshot() {
        return new EntitySnapshot(type.getId(), pos.x, pos.y);
    }
    public Vector2 getPos() {
        return pos;
    }

    public EntityType getType() {
        return type;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public float getWidth() {
        return type.getWidth();
    }

    public float getHeight() {
        return type.getHeight();
    }

    public float getWeight() {
        return type.getWeight();
    }
    public static float getG() {
        return g;
    }

    public static int getJumpHeight() {
        return JUMP_HEIGHT;
    }

    public static int getJumpVelocity() {
        return JUMP_VELOCITY;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }


    public final ClickListener movingLeftListener = (new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("buttonLeft", "moving left");
            setMovingLeft(true);
            return true;

        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("buttonLeft", "moving left");
            setMovingLeft(false);

        }
    });
    public final ClickListener movingRightListener = (new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("buttonLeft", "moving right");
            setMovingRight(true);
            return true;

        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("buttonLeft", "moving right");
            setMovingRight(false);

        }
    });
}
