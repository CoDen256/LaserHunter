package entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hud.AnimationHandler;
import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class Player extends Entity {

    protected int movingDirection;
    protected int lastMovingDirection = 1 ; // 0 - left, 1 - right

    protected static final int xLow = Gdx.graphics.getWidth()/2;
    protected static final int yHigh = Gdx.graphics.getHeight()*2/3;

    AnimationHandler handler;

    // float spawnRadius;
    // 29х50


    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);

        handler = new AnimationHandler();

        handler.initialize(0, "player/static.png", 29, 55);
        handler.initialize(1, "player/run.png", 29, 55);
        handler.initialize(3, "player/jump.png", 49, 55);
        handler.initialize(2, "player/fall.png", 29, 55);
        handler.initialize(4, "player/attack.png", 37, 55);


        // spawnRadius = snapshot.getFloat("spawnRadius", 50);

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
        handler.setAnim(0);
        totalForceX = potentialForceX;
        potentialForceX = 0;

        totalForceY = potentialForceY;
        potentialForceY = 0;

        Entity closestAttack = getClosest(this.attackRange*TileType.TILE_SIZE, null);



        updateInput(); // Handling all the input
        updatePhysics(deltatime); // Applying physics to entity and adding forces

        if (velY > 0) {
            handler.setAnim(3);
        } else if (velY < 0) {
            handler.setAnim(2);
        }

        updateVelocity(totalForceX, totalForceY, deltatime);



        takeEnergy(RESTORE_ENERGY*deltatime); // restoring energy
        combatUpdate(deltatime, closestAttack); // Combat handling

        super.update(deltatime);


        if (sloping != 0) {
            updateSloping();
        }

    }

    public void combatUpdate(float deltatime, Entity closest) {
        if (isInDefence) {
            if (takeEnergy(DEFENCE_ENERGY * deltatime)) {

            } else {
                setDefence(false);
            }
        }

        if (isAttacking) {

            attack(attackPoints, closest);
            setAttacking(false);

        }
    }


    public void updateInput() {
        // Y-movement in liquids
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && (floating)) {
            if (isSecondTap(xLow, yHigh)){

                handler.setAnim(3);
                ascend();


            }}

        // Y-movement
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)|| Gdx.input.isTouched()) && grounded && !bouncing && !floating && jumpTick == 0) {

            if (isSecondTap(xLow, yHigh) && !isInDefence){
                jump();

            }

            // Double Jump
        } else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)|| Gdx.input.isTouched()) && !grounded && !floating && jumpTick == 1
                && 10*getJumpVelocity() > velY && velY > -60*getJumpVelocity()) {

            if (isSecondTap(xLow, yHigh)&& !isInDefence) {
                doubleJump(DOUBLE_JUMP_ENERGY);


            }


        }



        // Left movement
        if (Gdx.input.isKeyPressed(Input.Keys.A) || this.movingDirection == -1) {
            if (!isInDefence) {
                moveToLeft();
            } else {
                moveToLeft(SPEED_STEP/2.85f);
            }
            handler.setAnim(1);
            lastMovingDirection = 0;


        }
        // Right movement
        if (Gdx.input.isKeyPressed(Input.Keys.D) || this.movingDirection == 1) {
            if (!isInDefence) {

                moveToRight();
            } else {
                moveToRight(SPEED_STEP/2.85f);
            }
            handler.setAnim(1);
            lastMovingDirection = 1;

        }

        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
            if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
                setAttacking(true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) setDefence(true); else setDefence(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            takeDamage(-1000);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            takeEnergy(-1000);
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

        handler.increaseStateTime(delta) ;



        if (handler.getCurrent_animation() == 3) {
            batch.draw((TextureRegion) handler.getCurrentAnim()[lastMovingDirection].getKeyFrame(handler.getStateTime(), true), pos.x + 6 * (-1), pos.y);
            if (isInDefence) {
                batch.draw(shield, pos.x -12, pos.y);
            }}
        else {


            batch.draw((TextureRegion) handler.getCurrentAnim()[lastMovingDirection].getKeyFrame(handler.getStateTime(), true), pos.x, pos.y);
            if (isInDefence) {
                batch.draw(shield, pos.x - 12, pos.y);
            }
        }






        //batch.draw(image, getX(), getY());
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
