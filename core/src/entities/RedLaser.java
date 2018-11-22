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
import java.util.Set;

import maps.GameMap;
import screens.MainMenuScreen;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class RedLaser extends Entity {
    Texture image;
    Sprite laserSprite;

    Vector2 followingVector;

    ArrayList<Beam> beams;
    float tick;

    float angle;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {

        super.create(snapshot, type, map);
        image = new Texture("RedLaser.png");
        laserSprite = new Sprite(image);
        laserSprite.setPosition(getX(), getY());

        angle = 0;

        tick = 0;

        beams = new ArrayList<Beam>();

    }

    public void update(float deltatime) {


        tick += 10*deltatime;
        Entity closest = getClosest(attackRange * TileType.TILE_SIZE, EntityType.PLAYER);


        if (closest != null) {
            //Gdx.app.log("I see you" + attackRange, closest.getType().getId());
            //flyOver(closest);
            follow(closest);

            if (Math.sin(tick) > 0.99f) {
                shootBeam(centerX(), centerY(), followingVector, 1.5f, 250, 1);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                shootBeam(centerX(), centerY(), followingVector, 1.5f, 250, 1);
            }


            Iterator iterator = beams.iterator();
            while (iterator.hasNext()) {
                Beam beam = (Beam) iterator.next();



                beam.update(deltatime);

                int collideCode = map.getCollision(beam.getX(), beam.getY(), beam.getWidth(), beam.getWidth(), null);
                boolean isColl = false;

                if (closest != null) {
                    isColl = map.getCollision(beam.pos, closest.getPos(), beam.getSize(), closest.getSize());
                }
                if (isColl) {
                    closest.takeDamage(attackPoints);
                }
                if (collideCode != -10 || beam.life > beam.lifespan || isColl) {
                    beam.dispose();
                    iterator.remove();
                }
            }




        }


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
        pos.y = entity.getY() + 150;
    }

    public void follow(Entity entity) {
        followingVector = new Vector2(-centerX() + entity.centerX(), -centerY() + entity.centerY());
        float newAngle = followingVector.angle(Vector2.X);
        rotate(angle - newAngle);
        angle = newAngle;
    }

    public void rotate(float angle) {
        laserSprite.rotate(angle);
    }

    public void shootBeam(float x, float y, Vector2 target, float lifespan, float velocity, int amount) {
        for (int i = 0; i < amount; i++) {
            beams.add(new Beam(x, y, target, lifespan, velocity));
        }

    }
}
