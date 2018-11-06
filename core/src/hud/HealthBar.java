package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import entities.Entity;

public class HealthBar {
    Texture image;
    Entity entity;


    float width, height;
    float barWidth, barHeight;
    float xShift, yShift;
    float x,y;

    public HealthBar(Entity entity, float rateX, float rateY) {
        width = 450 * rateX;
        height = 120 * rateY;

        barWidth = 360 * rateX;
        barHeight = 80 * rateY;

        xShift = 70 * rateX;
        yShift = 20 * rateY;

        this.entity = entity;

        image = new Texture("HUD/hpbar.png");
    }

    public void draw(ShapeRenderer renderer) {

        x = entity.getX() - (width-entity.getWidth())/2;
        y = entity.getY() + entity.getHeight() + 1;

        float hpRate = entity.getHealth()/entity.getMaxHealth();

        renderer.setColor(1f, 0.3f, 0.3f, -1f);
        renderer.rect(x+xShift,y+yShift,barWidth*hpRate, barHeight);
        //renderer.rect(50,50,width*hpRate, height);
    }
    public void draw(SpriteBatch batch) {

        x = entity.getX() - (width-entity.getWidth())/2;
        y = entity.getY() + entity.getHeight() + 1;

        batch.draw(image, x, y, width, height);
    }
}
