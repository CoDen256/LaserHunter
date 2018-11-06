package entities;

import com.badlogic.gdx.Application;
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

    protected static final int xLow = Gdx.graphics.getWidth()/2;
    protected static final int yHigh = Gdx.graphics.getHeight()*2/3;

    // float spawnRadius;
    // 29х50


    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);


        // spawnRadius = snapshot.getFloat("spawnRadius", 50);

        image = new Texture(Gdx.files.internal("cat.png"));

        jumpTick = 0;
        movingDirection = 0;

        movingLeftListener = LListener;
        movingRightListener = RListener;
        attackListener = AListener;
        defendListener = DListener;

        setDefence(false);
        setAttacking(false);

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

        Entity closestAttack = getClosest(this.attackRange*TileType.TILE_SIZE);


        updateInput(); // Handling all the input
        updatePhysics(); // Applying physics to entity and adding forces

        updateVelocity(totalForceX, totalForceY, deltatime);

        combatUpdate(deltatime, closestAttack); // Combat handling

        super.update(deltatime);


    }

    public void combatUpdate(float deltatime, Entity closest) {
        if (isInDefence) {

        }

        if (isAttacking) {

            attack(attackPoints, closest);

            setAttacking(false);

        }
    }


    public void updateInput() {
        // Y-movement in liquids
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && (floating)) {
            if (isSecondTap(xLow, yHigh) && !isInDefence && !isAttacking){

                ascend();


            }}


        // Y-movement
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)|| Gdx.input.isTouched()) && grounded && !bouncing && !floating && jumpTick == 0) {

            if (isSecondTap(xLow, yHigh) && !isInDefence && !isAttacking){
                jumpTick ++;

                jump();

            }

            // Double Jump
        } else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)|| Gdx.input.isTouched()) && !grounded && !floating && jumpTick == 1
                && 10*getJumpVelocity() > velY && velY > -60*getJumpVelocity()) {

            if (isSecondTap(xLow, yHigh) && !isInDefence && !isAttacking) {
                jumpTick ++;
                if (energy > 0) {
                    doubleJump(50);
                }


            }


        }

        // Left movement
        if (Gdx.input.isKeyPressed(Input.Keys.A) || this.movingDirection == -1) {
            moveToLeft();

        }
        // Right movement
        if (Gdx.input.isKeyPressed(Input.Keys.D) || this.movingDirection == 1) {
            moveToRight();
        }

        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
            if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) setAttacking(true);
            if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) setDefence(true);
        }
    }




    public boolean isSecondTap(float xLow, float yHigh) {
        return ( (Gdx.input.isTouched(1) && (Gdx.input.getX(1) > xLow) && (Gdx.input.getY(1) < yHigh) )
                || (!Gdx.input.isTouched(1) && (Gdx.input.getX(0) > xLow) && (Gdx.input.getY(0) < yHigh) )
                || (Gdx.input.isKeyPressed(Input.Keys.SPACE)) );
    }


    public void setMovingDirection(int direction) {
        movingDirection = direction;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(image, getX(), getY());
    }

    // SnapShot Handler
    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        //snapshot.putFloat("spawnRadius", 50);
        return snapshot;
    }


    // Button Listeners

    // Moving Left button
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
    // Moving Right button
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

    // Attack button
    public ClickListener AListener = (new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            // Gdx.app.log("buttonLeft", "moving right");
            setAttacking(true);
            return true;

        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            //Gdx.app.log("buttonLeft", "moving right");
            setAttacking(false);

        }
    });

    // Defence button
    public ClickListener DListener = (new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            // Gdx.app.log("buttonLeft", "moving right");
            setDefence(true);
            return true;

        }
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            //Gdx.app.log("buttonLeft", "moving right");
            setDefence(false);

        }
    });



}
