package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class GuardCat extends Entity {

    Texture image;
    float healPoints, visionRange;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        super.create(snapshot, type, map);

        image = new Texture(Gdx.files.internal("GuardCat.png"));

        setDefence(false);
        setAttacking(false);

        healPoints = snapshot.getHealPoints();
        visionRange = snapshot.getVisionRange();

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

        Entity closest = getClosest(visionRange * TileType.TILE_SIZE);
        if (closest != null) {
            //Gdx.app.log(getType().getId()+" can see", closest.getType().getId());
        }

        updatePhysics(); // Applying physics to entity and adding forces

        updateVelocity(totalForceX, totalForceY, deltatime);

        combatUpdate(deltatime); // Combat handling

        super.update(deltatime);

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
