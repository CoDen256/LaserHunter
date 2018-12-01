package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class GuardDoge extends Entity {

    int position;
    float initialX;
    float movingRange;
    float cooldownTick;

    int direction;

  public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);

        position = snapshot.isStaticPosition();
        movingRange = snapshot.getMovementRadius();

        initialX = snapshot.getX();
        direction = 0;
        cooldownTick = 0;

        map.addHealthBar(this);

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

        cooldownTick = cooldownTick < 0 ? 0 : cooldownTick-deltatime;



        if (position == 1) { // dynamic
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

    public void updateMovingLeftRight(float movingRange) {
        if ((getX() - initialX) > movingRange) {
            direction = -1;
        }else if ((-getX() + initialX) > movingRange){
            direction = 1;
        }

        Gdx.app.log(direction+" "+(getX()-initialX), movingRange+"");

        if (direction == 0) {
            direction = (int) (Math.random() * 10) % 2 - 1;
        } else if (direction == 1) {
            moveToRight(SPEED_STEP/2);
        } else if (direction == -1) {
            moveToLeft(SPEED_STEP/2);
        }


    }

    public void updateGuarding(int direction, float range) {
      // direction = -1 - right
      // direction = -2 - left

        Entity closest = getClosest(range * TileType.TILE_SIZE, EntityType.PLAYER);
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

    public void follow(Vector2 target) {
        if (Math.abs(getX()-target.x) > 1 * TileType.TILE_SIZE) {
            if (getX() < target.x) {
                moveToRight(SPEED_STEP);
            } else if (getX() > target.x) {
                moveToLeft(SPEED_STEP);
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
            setDefence(false);
        }

        if (isAttacking) {
            attack(attackPoints, closest);
            setAttacking(false);
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(this.image, this.getX(), this.getY());
    }

}
