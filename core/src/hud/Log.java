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
            shift += message.getHeight() + 2;
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

    public void add(String text, boolean permanent) {
        add(text, 1, permanent);
    }

    public void add(String text, int lines, boolean permanent) {
        if (logMessages.size() < maxAmount) {
            if (permanent) {
                for (TextRegion message : logMessages) {

                    if (message.getText().contains(text) || text.contains(message.getText())) {
                        return;
                    }
                }
            }

        } else {
            logMessages.remove(0);
        }
        logMessages.add(new TextRegion(text, scaleX, scaleY, width, lines * HEIGHT * scaleX, LIFESPAN, DELAY));
    }
}
