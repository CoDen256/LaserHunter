package hud;

import com.badlogic.gdx.graphics.Texture;
public enum BarType{
    ENTITY_HEALTH_BAR(450, 120, 360, 80, 70, 20, "HUD/hpbar.png"), // Entity Health BarType
    HUD_BAR(142, 20, 132, 9, 5, 5,"HUD/bar4.png") // Health- and Energy- BarType for HUD
    ;

    float width, height;
    float barWidth, barHeight;
    float xShift, yShift;
    String path;

    BarType(float width, float height, float barWidth, float barHeight, float xShift, float yShift, String path) {
        this.width = width;
        this.height = height;

        this.barWidth = barWidth;
        this.barHeight = barHeight;

        this.xShift = xShift;
        this.yShift = yShift;

        this.path = path;
    }


}
