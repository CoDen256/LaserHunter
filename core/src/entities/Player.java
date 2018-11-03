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

    Texture image;

    protected int movingDirection;

    // float spawnRadius;
    // 29х50


    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);


        // spawnRadius = snapshot.getFloat("spawnRadius", 50);

        this.image = new Texture(Gdx.files.internal("cat.png"));

        this.jumpTick = 0;
        this.movingDirection = 0;

        this.movingLeftListener = LListener;
        this.movingRightListener = RListener;

        bouncing = false;
        sliding = false;
        floating = false;
        current_friction = INITIAL_FRICTION;

        maxForce = SPEED_STEP;

    }

    @Override
    public void update(float deltatime) {
        totalForceX = potentialForceX;
        potentialForceX = 0;

        totalForceY = potentialForceY;
        potentialForceY = 0;
        //Gdx.app.log(starCollected + "", coins+"");
        //Gdx.app.log("health", health + "");

        // Y-movement in liquids
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && (floating)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){

                totalForceY += JUMP_STEP/10;

            }else if (Gdx.input.isTouched(1)) {

                if (Gdx.input.getX(1) > Gdx.graphics.getWidth() / 2) {

                    totalForceY += JUMP_STEP/10;
                }
            }else if (!Gdx.input.isTouched(1)){
                if (Gdx.input.getX(0) > Gdx.graphics.getWidth() / 2){

                    totalForceY += JUMP_STEP/10;
                }
            }}


        // Y-movement
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE)|| Gdx.input.isTouched()) && grounded && !floating && jumpTick == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

                this.jumpTick ++;
                totalForceY += JUMP_STEP;

            }else if (Gdx.input.isTouched(1)) {

                if (Gdx.input.getX(1) > Gdx.graphics.getWidth() / 2) {
                    this.jumpTick ++;
                    totalForceY += JUMP_STEP;
                }
            }else if (!Gdx.input.isTouched(1)){
                if (Gdx.input.getX(0) > Gdx.graphics.getWidth() / 2){
                    this.jumpTick ++;
                    totalForceY += JUMP_STEP;
                }
            }


            // Double Jump
        } else if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE)|| Gdx.input.isTouched()) && !grounded && jumpTick == 1 &&
                10*getJumpVelocity() > velY && velY > -60*getJumpVelocity() && !floating) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

                this.jumpTick ++;
                doubleJump();
            }else if (Gdx.input.isTouched(1)) {

                if (Gdx.input.getX(1) > Gdx.graphics.getWidth() / 2) {
                    this.jumpTick ++;
                    doubleJump();
                }
            }else if (!Gdx.input.isTouched(1)){
                if (Gdx.input.getX(0) > Gdx.graphics.getWidth() / 2){
                    this.jumpTick ++;
                    doubleJump();
                }
            }



        }

        // Left movement
        if (Gdx.input.isKeyPressed(Input.Keys.A) || this.movingDirection == -1) {
            totalForceX += -SPEED_STEP;

        }
        // Right movement
        if (Gdx.input.isKeyPressed(Input.Keys.D) || this.movingDirection == 1) {
            totalForceX += SPEED_STEP;
        }


        if (grounded && !floating) {
            updateFriction(current_friction);
        }

        if (bouncing) {
            bouncing = false;
            totalForceY += JUMP_STEP*1.8;
            jumpTick++;
        }

        if (floating) {
            floating = false;
            updateFloating(WATER_DENSITY, G);
            updateLiquidResistX(WATER_FRICTIONX);
            updateLiquidResistY(WATER_FRICTIONY);
        }

        if (sliding) {
            sliding = false;
            current_friction = ICE_FRICTION;

        }else {
            current_friction = INITIAL_FRICTION;
        }


        updateGravity(G);

        updateVelocityX(totalForceX, deltatime);
        updateVelocityY(totalForceY, deltatime);


        super.update(deltatime);


    }



    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(this.image, this.getX(), this.getY());
    }

    public void setMovingDirection(int direction) {
        this.movingDirection = direction;
    }

    // SnapShot Handler
    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        //snapshot.putFloat("spawnRadius", 50);
        return snapshot;
    }

    // Button Listeners

    public ClickListener LListener = (new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("buttonLeft", "moving left");
            setMovingDirection(-1);
            return true;

        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log("buttonLeft", "moving left");
            setMovingDirection(0);

        }
    });
    public ClickListener RListener = (new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            // Gdx.app.log("buttonLeft", "moving right");
            setMovingDirection(1);
            return true;

        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            //Gdx.app.log("buttonLeft", "moving right");
            setMovingDirection(0);

        }
    });

}
