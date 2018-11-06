package hud;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Entity;

public class TextRegion{

    int id,pid;
    float width,height;

    float lifespan,delay;
    float tick;

    String text;
    BitmapFont font;

    Entity target;



    public TextRegion(int id, int pid, String text, Entity target, float textScaleX, float textScaleY, float width, float height, float lifespan, float delay) {
        this.id = id;
        this.pid = pid;
        this.text = " " + text;
        this.target = target;

        this.width = width;
        this.height = height;

        this.lifespan = lifespan;
        this.delay = delay;
        this.tick = 0;


        this.font = new BitmapFont();
        this.font.getData().setScale(textScaleX, textScaleY);



    }


    public void render(SpriteBatch batch, Entity entity) {

        float x = entity.getX() ;
        float y = entity.getY() + entity.getHeight();

        float shiftX = -width/5;
        float shiftY = this.height;

        font.draw(batch, this.text, x+shiftX, y+shiftY, this.width, 10, true);
    }

    public void update(float delta) {

        tick += delta;


    }

    public void dispose() {
        font.dispose();
    }

    public Entity getTarget() {
        return target;
    }

    public float getLifespan() {
        return lifespan;
    }

    public float getDelay() {
        return delay;
    }


    public float getTick() {
        return tick;
    }

    public int getId() {
        return id;
    }
    public int getPid() {
        return pid;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public BitmapFont getFont() {
        return font;
    }
}
