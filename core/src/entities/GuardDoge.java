package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import hud.AnimationHandler;
import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class GuardDoge extends Entity {

    int position;
    float initialX;
    float movingRange;
    float cooldownTick,defendTick;

    protected int lastMovingDirection = 1 ; // 0 - left, 1 - right
    AnimationHandler handler;

    int direction;

  public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);

        position = snapshot.isStaticPosition();
        movingRange = snapshot.getMovementRadius();

        initialX = snapshot.getX();
        direction = 0;
        cooldownTick = defendTick = 0;

        map.addHealthBar(this);

        handler = new AnimationHandler();

        handler.initialize(0, "dogo/static.png", 42, 30);
        handler.initialize(1, "dogo/run.png", 42, 30);

    }

    @Override
    public void update(float deltatime) {
        totalForceX = potentialForceX;
        potentialForceX = 0;

        totalForceY = potentialForceY;
        potentialForceY = 0;


        Entity closestTarget = getClosest(attackRange * TileType.TILE_SIZE, EntityType.PLAYER);
        if (closestTarget != null) {
            if (cooldownTick <= 0) {
                setAttacking(true);
                cooldownTick = cooldown;

            }

        }

        Entity closesInSight = getClosest(attackRange * 5 * TileType.TILE_SIZE, EntityType.PLAYER);
        if (closesInSight != null) {
            defend(deltatime, 2);

        } else {
            setDefence(false);
            defendTick = 0;
        }

        cooldownTick = cooldownTick < 0 ? 0 : cooldownTick-deltatime;



        if (position == 1) { // dynamic
            if (closestTarget != null) {
                map.addMessage(0, gameId, "Take it, stupid cat",this,150,20,2,0);
            }

            updateMovingLeftRight(movingRange*TileType.TILE_SIZE);

        } else if (position < 0) { // guarding left and right
            updateGuarding(position, (movingRange)*TileType.TILE_SIZE);
        }

        updateXCollision();

        updatePhysics(deltatime); // Applying physics to entity and adding forces

        updateVelocity(totalForceX, totalForceY, deltatime); // updating Velocity


        takeDamage(-10*deltatime);
        takeEnergy(-100*deltatime);

        combatUpdate(deltatime, closestTarget); // Combat handling

        super.update(deltatime);


    }


    public void defend(float deltatime, float lifespan) {
        if (defendTick == 0) {
            if (((int)(Math.random() * 100) % 99) == 0) {

                defendTick += deltatime;
                setDefence(true);
            }
        } else {
            if (defendTick < lifespan) {
                defendTick += deltatime;
                setDefence(true);
            } else {
                defendTick = 0;
                setDefence(false);
            }

        }



    }

    public void updateMovingLeftRight(float movingRange) {
        if ((getX() - initialX) > movingRange) {
            direction = -1;
        }else if ((-getX() + initialX) > movingRange){
            direction = 1;
        }


        if (direction == 0) {
            direction = (int) (Math.random() * 10) % 2 - 1;
        } else if (direction == 1) {
            moveToRight(SPEED_STEP/2);
            lastMovingDirection = 1;
        } else if (direction == -1) {
            moveToLeft(SPEED_STEP/2);
            lastMovingDirection = 0;
        }


    }

    public void updateGuarding(int direction, float range) {
      // direction = -1 - right
      // direction = -2 - left

        Entity closest = getClosest(range * TileType.TILE_SIZE, EntityType.PLAYER);
        if (closest != null) {
            if (Math.abs(closest.getX() - getX()) > 5*TileType.TILE_SIZE) {
                map.addMessage(0, gameId, "Go away, stupid cat. You are not allowed here",this,150,30,2,0);
            }

            if (direction == -1) {
                if (closest.getX() > initialX && Math.abs(closest.getX()-initialX) < range){
                    follow(closest.getPos());
                }

            } else if (direction == -2) {
                if (closest.getX() < initialX && Math.abs(closest.getX()-initialX) < range){
                    follow(closest.getPos());
                }
            }
        }



    }

    public void follow(Vector2 target) {
        if (Math.abs(getX()-target.x) > 1 * TileType.TILE_SIZE) {
            if (getX() < target.x) {
                moveToRight(SPEED_STEP);
                lastMovingDirection = 1;
            } else if (getX() > target.x) {
                moveToLeft(SPEED_STEP);
                lastMovingDirection = 0;
            }

        }
    }

    public void updateXCollision() {
        if (xCollision && grounded && jumpTick == 0) {
            jump();
            xCollision = false;
        }
    }

    public void combatUpdate(float deltatime, Entity closest) {
        if (isInDefence) {
            //setDefence(false);
        }

        if (isAttacking) {
            attack(attackPoints, closest);
            setAttacking(false);
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        handler.increaseStateTime(delta) ;
        batch.draw((TextureRegion) handler.getCurrentAnim()[lastMovingDirection].getKeyFrame(handler.getStateTime(), true), pos.x , pos.y);
        //batch.draw(this.image, this.getX(), this.getY());
    }

}
