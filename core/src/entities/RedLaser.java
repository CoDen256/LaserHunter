package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class RedLaser extends Entity {
    Sprite laserSprite;

    Vector2 followingVector;

    ArrayList<Beam> beams;
    float tick;
    int ntick;

    int rate; //beams per second;
    float beamLifeSpan;
    float beamVelocity;

    int staticPostion;

    float angle;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {

        super.create(snapshot, type, map);

        rate = snapshot.getBeamRate();
        beamLifeSpan = snapshot.getBeamLifeSpan();
        beamVelocity = snapshot.getBeamVelocity();

        staticPostion = snapshot.isStaticPosition();
        laserSprite = new Sprite(image);
        laserSprite.setPosition(getX(), getY());

        angle = snapshot.getInitialAngle();

        tick = ntick = 0;

        beams = new ArrayList<Beam>();

        if (staticPostion == 0) {
            followingVector = Vector2.X.rotate(angle);
            rotate(angle);
        }

    }

    public void update(float deltatime) {




        Entity closest = getClosest(attackRange * TileType.TILE_SIZE, EntityType.PLAYER);


        if (closest != null) {
            //flyOver(closest);
            if (staticPostion != 0) {
                follow(closest);
                if (staticPostion == 2) {
                    flyOver(closest);
                }
            }


            if (ntick % (58/rate) == 0) {
                tick = 0;
                shootBeam(centerX(), centerY(), followingVector, beamLifeSpan, beamVelocity);
            }

        }

        updateTick(deltatime);

        updateBeams(deltatime);

        laserSprite.setPosition(getX(), getY());

    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        laserSprite.draw(batch);

        for (Beam beam : beams) {
            beam.render(batch);
        }
    }

    public void flyOver(Entity entity) {
        pos.y = entity.getY() +150;
    }

    public void follow(Entity entity) {
        followingVector = new Vector2(-centerX() + entity.centerX(), -centerY() + entity.centerY());
        float newAngle = followingVector.angle(Vector2.X);
        rotate(180+(angle - newAngle));
        angle = newAngle;
    }

    public void rotate(float angle) {
        laserSprite.rotate(angle);
    }

    public void updateTick(float deltatime) {
        tick += deltatime;


        if ((int)(tick - deltatime) == (int) tick) {
            ntick++;
        } else {
            ntick = 0;
        }

    }

    public void updateBeams(float deltatime) {
        Iterator iterator = beams.iterator();
        while (iterator.hasNext()) {
            Beam beam = (Beam) iterator.next();

            beam.update(deltatime);


            int mapCollideCode = map.getCollision(beam.getX(), beam.getY(), beam.getWidth(), beam.getHeight(), null);
            Entity adjacentEntity = map.getCollisionWithEntities(beam.getPos(), beam.getSize(), this);

            if (adjacentEntity != null) {
                adjacentEntity.takeDamage(attackPoints);
            }

            if (mapCollideCode != map.NO_COLLISION || beam.life > beam.lifespan || adjacentEntity != null) {
                beam.dispose();
                iterator.remove();
            }
        }
    }

    public void shootBeam(float x, float y, Vector2 target, float lifespan, float velocity) {

        beams.add(new Beam(x, y, target, lifespan, velocity));


    }
}
