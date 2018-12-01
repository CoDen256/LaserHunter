package hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import entities.Entity;

public class Bar {

    Texture image;
    private Entity entity;


    float width, height;
    float barWidth, barHeight;
    float xShift, yShift;

    // BarType by default
    public Bar(float width, float height, float barWidth, float barHeight, float xShift, float yShift, float rateX, float rateY, String path, Entity entity) {
        this.width = width*rateX;
        this.height = height*rateY;

        this.barWidth = barWidth*rateX;
        this.barHeight = barHeight*rateY;

        this.xShift = xShift*rateX;
        this.yShift = yShift*rateY;

        load(path);

        this.entity = entity;
    }

    public Bar(Entity entity, float rateX, float rateY, BarType defaultBar) {
        this(defaultBar.width, defaultBar.height, defaultBar.barWidth, defaultBar.barHeight, defaultBar.xShift, defaultBar.yShift, rateX, rateY, defaultBar.path, entity);
    }



    // bar:
    // 0 - HealthBar
    // 1 - EnergyBar

    public void fill(ShapeRenderer renderer, int bar) {

        float x = entity.getX() - (width-entity.getWidth())/2;
        float y = entity.getY() + entity.getHeight() + 1 ;

        fill(renderer, bar, x, y);
    }

    public void fill(ShapeRenderer renderer, int bar, float x, float y) {

        float rate = 0;
        if (bar == 0) {
            rate = entity.getHealth()/entity.getMaxHealth();
            renderer.setColor(1f, 0.3f, 0.3f, 0);
        } else if (bar == 1) {
            rate = entity.getEnergy()/entity.getMaxEnergy();
            renderer.setColor(0.4f, 0.4f, 1f, 0);
        }


        renderer.rect(x+xShift,y+yShift,barWidth*rate, barHeight);
    }


    public void draw(SpriteBatch batch) {

        draw(batch, entity.getX() - (width-entity.getWidth())/2, entity.getY() + entity.getHeight() + 1);
    }

    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(image, x, y, width, height);
    }

    public void load(String path) {
        image = new Texture(path);
    }

    public Entity getEntity() {
        return this.entity;
    }

    public float getWidth() {
        return width;
    }

}
