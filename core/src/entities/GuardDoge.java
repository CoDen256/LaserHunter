package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class GuardDoge extends Entity {
    Texture image;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);

        image = new Texture(Gdx.files.internal("GuardDoge.png"));

        map.addHealthBar(this);

    }

    @Override
    public void update(float deltatime) {
        totalForceX = potentialForceX;
        potentialForceX = 0;

        totalForceY = potentialForceY;
        potentialForceY = 0;

        Entity closest = getClosest(attackRange * TileType.TILE_SIZE, EntityType.PLAYER);
        if (closest != null) {
            //Gdx.app.log(getType().getId()+" can see", closest.getType().getId());
            go();
        }



        updatePhysics(deltatime); // Applying physics to entity and adding forces

        updateVelocity(totalForceX, totalForceY, deltatime); // updating Velocity



        takeDamage(-10*deltatime);

        combatUpdate(deltatime); // Combat handling

        super.update(deltatime);


    }

    public void go() {
        if (grounded || jumpTick == 1) {
            moveToRight(SPEED_STEP/3);
        }


        if (xCollision && grounded) {
            jump();
            xCollision = false;
        }
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
