package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class Player extends Entity {

    private static final int SPEED = 175;
    boolean doubleJumped;


    // float spawnRadius;
    // 29х50


    Texture image;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);
        doubleJumped = true;
        // spawnRadius = snapshot.getFloat("spawnRadius", 50);


        image = new Texture(Gdx.files.internal("cat.png"));


    }

    @Override
    public void update(float deltatime, float gravity) {


        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)|| Gdx.input.justTouched()) && grounded ) {
            if (Gdx.input.justTouched()) {
                if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
                    this.velocityY += getJumpVelocity() * getWeight();
                    Gdx.app.log("update", "jumping");
                    doubleJumped = true;
                }
            } else {
                doubleJumped = true;
                this.velocityY += getJumpVelocity() * getWeight();
            }




        } else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)|| Gdx.input.justTouched()) && !grounded && Math.abs(this.velocityY)<200 && !doubleJumped) {
            if (Gdx.input.justTouched()) {
                if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
                    doubleJumped = true;
                    this.velocityY += getJumpVelocity() * getWeight()*1.2;
                }

            } else {
                doubleJumped = true;
                this.velocityY += getJumpVelocity() * getWeight()*1.2;
            }



        }

        super.update(deltatime, gravity); // Apply the gravity

        if (Gdx.input.isKeyPressed(Input.Keys.A) || this.movingLeft) {
            moveX(-SPEED * deltatime);

        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || this.movingRight) {
            moveX(SPEED * deltatime);
        } else {
        }
        //moveX(SPEED*deltatime);

    }


    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(image, this.getX(), this.getY());
    }

    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        //snapshot.putFloat("spawnRadius", 50);
        return snapshot;
    }


}
