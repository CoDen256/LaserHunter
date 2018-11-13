package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import entities.Entity;

public enum Bar {
    ENTITY_HEALTH_BAR(450, 120, 360, 80, 70, 20, "HUD/hpbar.png"), // Entity Health Bar
    HUD_BAR(142, 20, 132, 9, 5, 5,"HUD/bar4.png") // Health- and Energy- Bar for HUD
    ;


    Texture image;
    Entity entity;


    float width, height;
    float barWidth, barHeight;
    float xShift, yShift;


    // Bar by default
    Bar(float width, float height, float barWidth, float barHeight, float xShift, float yShift, String path) {
        this.width = width;
        this.height = height;

        this.barWidth = barWidth;
        this.barHeight = barHeight;

        this.xShift = xShift;
        this.yShift = yShift;

        load(path);
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

    public Bar apply(Entity entity, float rateX, float rateY) {

        width *= rateX;
        height *= rateY;

        barWidth *= rateX;
        barHeight *= rateY;

        xShift *= rateX;
        yShift *= rateY;

        this.entity = entity;

        return this;
    }

}
