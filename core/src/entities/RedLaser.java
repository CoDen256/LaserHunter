package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public class RedLaser extends Entity{
    Texture image;
    Sprite laserSprite;

    Vector2 followingVector;

    float angle;

    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {

        super.create(snapshot, type, map);
        image = new Texture("RedLaser.png");
        laserSprite = new Sprite(image);
        laserSprite.setPosition(getX(), getY());

        angle = 0;


    }

    public void update(float deltatime) {


        Entity closest = getClosest(attackRange*TileType.TILE_SIZE, EntityType.PLAYER);


        if (closest != null) {
            //Gdx.app.log("I see you" + attackRange, closest.getType().getId());
            //flyOver(closest);
            follow(closest);

        }

        laserSprite.setPosition(getX(), getY());

    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        laserSprite.draw(batch);
    }

    public void flyOver(Entity entity) {
        pos.y = entity.getY() + 150;
    }

    public void follow(Entity entity) {
        followingVector = new Vector2(-centerX() + entity.centerX(), -centerY() + entity.centerY());
        float newAngle = followingVector.angle(Vector2.X);
        rotate(angle-newAngle);
        angle = newAngle;
    }

    public void rotate(float angle) {
        laserSprite.rotate(angle);
    }
}
