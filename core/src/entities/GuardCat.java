package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class GuardCat extends Entity {

    Texture image;

    float initial_posX, initial_posY;
    int current_direction;

    boolean healthAdded;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);


        // spawnRadius = snapshot.getFloat("spawnRadius", 50);

        this.image = new Texture(Gdx.files.internal("GuardCat.png"));

        this.jumpTick = 0;

        this.setDefence(false);
        this.setAttacking(false);

        this.bouncing = false;
        this.sliding = false;
        this.floating = false;
        this.current_friction = INITIAL_FRICTION;

        this.maxForce = SPEED_STEP;

       initial_posX = this.getX();
       initial_posY = this.getY();
       current_direction = 0;
       healthAdded = false;

    }

    @Override
    public void update(float deltatime) {
        totalForceX = potentialForceX;
        potentialForceX = 0;

        totalForceY = potentialForceY;
        potentialForceY = 0;


        updateDecision(2);

        Entity e5 = getClosest(5*TileType.TILE_SIZE);
        Entity e1 = getClosest(2*TileType.TILE_SIZE);


        if (e5 != null) {
            if (e5.getType().getId() == "player" && !healthAdded) {
                current_direction = 0;
                this.map.addMessage(3,"Hello fellow Player, come here ill give you some health",this, 150, 100, 5, 0);
            }
            if (healthAdded && e5.getType().getId()=="player" && e1 == null) {
                this.map.addMessage(3,"You have already taken the heal",this, 125, 75, 3, 0);
            }
        }


        if (e1 != null) {

            if (e1.getType().getId() == "player" && !healthAdded) {
                this.map.addMessage(3,"Take it",this, 75, 30, 2, 0);
                e1.takeDamage(-50);
                current_direction = 1;
                healthAdded = true;
            }
        }



        updateMovement();
        updatePhysics(); // Applying physics to entity and adding forces

        updateVelocityX(totalForceX, deltatime); // Applying forces
        updateVelocityY(totalForceY, deltatime);

        combatUpdate(deltatime); // Combat handling

        super.update(deltatime);


    }

    public void updateDecision(int range) {
        if (getX() <= initial_posX && current_direction != 1) {
            current_direction = 1;
        } else if (getX() >= initial_posX + range*TileType.TILE_SIZE && current_direction != -1) {
            current_direction = -1;
        }


    }

    public void updateMovement() {
        if (current_direction == 1) {
            moveToRight(SPEED_STEP/2.5f);
        } else if (current_direction == -1) {
            moveToLeft(SPEED_STEP/2.5f);
        }
    }

    public void updatePhysics() {
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
    }

    public void combatUpdate(float deltatime) {
        if (isInDefence) {

        }

        if (isAttacking) {

        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(this.image, this.getX(), this.getY());
    }
}
