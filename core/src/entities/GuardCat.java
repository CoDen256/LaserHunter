package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class GuardCat extends Entity {

    float healPoints, visionRange, movementRadius;
    float cd;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);

        healPoints = snapshot.getHealPoints();
        visionRange = snapshot.getVisionRange();
        movementRadius = snapshot.getMovementRadius();

        cd = 0;
        map.addHealthBar(this);

    }

    @Override
    public void update(float deltatime) {
        totalForceX = potentialForceX;
        potentialForceX = 0;

        totalForceY = potentialForceY;
        potentialForceY = 0;

        Entity closest = getClosest(visionRange * TileType.TILE_SIZE, EntityType.PLAYER);
        if (closest != null) {
            if (Math.abs(getX() - closest.getX()) > 4 * TileType.TILE_SIZE) {
                map.addMessage(0, gameId, "Come here my little boy",this,125,20,2,0);
            }

            if (getRadius(getX(), getY(), closest.getX(), closest.getY()) < 30 && cd == 0) {
                closest.takeDamage(-healPoints);
                cd = cooldown;
            }
        }

        cd = cd < 0 ? 0 : cd - deltatime;



        updatePhysics(deltatime); // Applying physics to entity and adding forces

        updateVelocity(totalForceX, totalForceY, deltatime); // updating Velocity



        takeDamage(-10*deltatime);
        takeEnergy(-100*deltatime);

        combatUpdate(deltatime, closest); // Combat handling

        super.update(deltatime);


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
