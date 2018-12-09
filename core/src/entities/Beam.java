package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Beam {
    Texture image;
    Sprite beamSprite;

    Vector2 velocityVector;
    Vector2 pos;

    float velocity;

    private static final int WIDTH = 4;
    private static final int HEIGHT = 4;

    float angle;

    float life;
    float lifespan;

    public Beam(float x, float y, Vector2 targetDirection, float lifespan, float velocity, String path) {
        image = new Texture("entities/"+path+"/beam.png");
        beamSprite = new Sprite(image);

        angle = targetDirection.angle(new Vector2(1,0));
        this.velocity = velocity;
        velocityVector = targetDirection;

        pos = new Vector2(x, y);
        beamSprite.setCenter(x, y);
        beamSprite.rotate(-angle);
        life = 0;
        this.lifespan = lifespan;

    }

    public void update(float deltatime) {
        life += deltatime;
        velocityVector.setLength(velocity * deltatime);
        pos = pos.add(velocityVector);

        beamSprite.setCenter(pos.x, pos.y);

    }

    public void dispose() {
        image.dispose();
    }

    public void render(SpriteBatch batch) {
        beamSprite.draw(batch);
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return  pos.y;
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getWidth() {
        return  WIDTH;
    }
    public int getHeight() {
        return  HEIGHT;
    }

    public Vector2 getSize() {
        return new Vector2(getWidth(), getHeight());
    }

    public float getLifespan() {
        return lifespan;
    }
}
