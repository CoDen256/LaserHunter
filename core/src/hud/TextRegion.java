package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Entity;

public class TextRegion{

    int id;
    float width;
    float height;

    float rateX, rateY;
    float lifespan;
    float delay;
    float tick;

    String text;
    Texture surface;
    BitmapFont font;

    Entity target;



    public TextRegion(int id, String text, Entity target, float textScaleX, float textScaleY, float width, float height, float lifespan, float delay) {
        this.id = id;
        this.text = text;
        this.target = target;

        this.width = width;
        this.height = height;

        this.lifespan = lifespan;
        this.delay = delay;
        this.tick = 0;


        this.font = new BitmapFont();
        this.font.getData().setScale(textScaleX, textScaleY);



    }

    public void load(String path) {
        this.surface = new Texture(path);

        this.rateX = this.surface.getWidth()/width;
        this.rateY = this.surface.getHeight()/height;
    }

    public void render(SpriteBatch batch, Entity entity) {

        float x = entity.getX() ;
        float y = entity.getY() + entity.getHeight();

        float shiftX = -width/5;
        float shiftY = this.height;

        this.font.draw(batch, this.text, x+shiftX, y+shiftY, this.width, 10, true);
    }

    public void update(float delta) {

        this.tick += delta;


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

    public Texture getSurface() {
        return surface;
    }

    public float getTick() {
        return tick;
    }

    public int getId() {
        return id;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRateX() {
        return rateX;
    }

    public float getRateY() {
        return rateY;
    }

    public String getText() {
        return text;
    }

    public BitmapFont getFont() {
        return font;
    }
}
