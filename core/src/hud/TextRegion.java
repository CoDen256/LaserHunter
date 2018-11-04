package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Entity;

public class TextRegion{

    float width;
    float height;

    float rateX, rateY;

    String text;
    public Texture surface;
    BitmapFont font;

    public Entity target;

    public TextRegion(String text,float textScaleX, float textScaleY, float width, float height, String path, Entity target) {
        this.text = text;
        this.width = width;
        this.height = height;

        this.target = target;

        font = new BitmapFont();
        font.getData().setScale(textScaleX, textScaleY);

        this.surface = new Texture(path);

        rateX = this.surface.getWidth()/width;
        rateY = this.surface.getHeight()/height;
    }

    public void render(SpriteBatch batch, Entity entity) {

        float x = entity.getX() + 1*entity.getWidth()/9;
        float y = entity.getY() + 9.5f*entity.getHeight()/10;

        float shiftX = width/10;
        float shiftY = height*8.75f/10;

        batch.draw(this.surface, x, y, width, height);
        this.font.draw(batch, this.text, x+shiftX, y+shiftY, width-shiftX, 10, true);
    }

}
