package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class Log{

    float x,y;
    float scaleX, scaleY;
    int maxAmount;
    int width;
    int last = 0;

    ArrayList<TextRegion> logMessages;

    private static final int LIFESPAN = 5;
    private static final int DELAY = 0;
    private static final int HEIGHT = 16;


    public Log(float x, float y, int maxAmount, int width, float scaleX, float scaleY) {
        this.x = x;
        this.y = y;
        this.maxAmount = maxAmount;
        this.width = width;

        this.scaleX = scaleX;
        this.scaleY = scaleY;

        logMessages = new ArrayList<TextRegion>();
    }

    public void render(SpriteBatch batch) {
        int shift = 0;
        for (TextRegion message : logMessages) {
            message.render(batch, x, y-shift);
            shift += message.getHeight() + 1;
        }
    }

    public void update(float deltatime) {
        Iterator iterator = logMessages.iterator();

        while (iterator.hasNext()) {
            TextRegion message = (TextRegion) iterator.next();

            message.update(deltatime);

            if (message.getTick() > message.getLifespan()) {
                message.dispose();
                iterator.remove();

            }
        }
    }

    public ArrayList<TextRegion> getLogMessages() {
        return logMessages;
    }

    public void add(String text) {
        add(text, 1);
    }

    public void add(String text, int lines) {
        if (logMessages.size() < maxAmount) {
            for (TextRegion message : logMessages) {

                if (message.getText() == text) {
                    return;
                }
            }
        } else {
            logMessages.remove(0);
        }
        logMessages.add(new TextRegion(text, scaleX, scaleY, width, lines * HEIGHT * scaleX, LIFESPAN, DELAY));
    }
}
