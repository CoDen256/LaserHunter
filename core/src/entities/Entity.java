package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public abstract class Entity{

    protected Vector2 pos;
    protected EntityType type;
    protected GameMap map;
    protected float health;


    // Movement
    protected float velX = 0;
    protected float totalForceX;
    protected float potentialForceX = 0;


    protected float velY = 0;
    protected float potentialVelocityY;
    protected float totalForceY;
    protected float potentialForceY;


    protected float density;

    protected float current_friction;


    // Player attributes
    public ClickListener movingRightListener;
    public ClickListener movingLeftListener;

    protected int jumpTick;

    protected boolean grounded = false;

    protected boolean bouncing;
    protected boolean sliding;
    protected boolean floating;



    private static final int JUMP_HEIGHT = 50;
    private static final float g = 2000;
    private static final int JUMP_VELOCITY = 10;

    private static final float MAX_SPEED = 235;


    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        this.pos = new Vector2(snapshot.getX(), snapshot.getY());
        this.health = snapshot.getHealth();
        this.type = type;
        this.map = map;

        this.density = 10.4f;
    }

    public void update(float deltatime) {

        float newX = pos.x;
        newX += this.velX * deltatime;

        checkHorizontalCollision(newX);


        //Gdx.app.log("velY", velY+"");

        float newY = pos.y;
        newY += this.velY * deltatime;

        checkVerticalCollision(newY);


    }

    public abstract void render(SpriteBatch batch, float delta);

    // X-Axis Handlers
    public void checkHorizontalCollision(float newX) {
        int collideCode = map.getCollision(newX, pos.y, (int) getWidth(), (int) getHeight());
        if (collideCode == -10) {
            this.pos.x = newX;
        } else {
            //Gdx.app.log("CollidingX", 0 + "");
            velX = 0;
            potentialForceX = -0.9f*totalForceX;
        }
    }

    public void updateVelocityX(float force, float deltatime) {
        float newVelX = this.velX;
        newVelX =  true ? (newVelX +  force / getWeight() * deltatime) : 0 ;
        if (Math.abs(newVelX) <= MAX_SPEED) {
            this.velX = Math.abs(newVelX) > 50 ? newVelX : 0;
        } else {
            this.velX = (float) ((int) (Math.abs(newVelX)/newVelX)) * MAX_SPEED;
        }

    }

    public void updateFriction(float friction){
        float newForce = totalForceX;
        newForce =  this.velX != 0 ? newForce + (float) ((int) (Math.abs(this.velX)/this.velX)) * -getG()*getWeight()*friction : newForce;
        totalForceX = newForce;

    }

    public void updateLiquidResistX(float friction) {
        float newForce = totalForceX;
        newForce = this.velX != 0? newForce + friction * -getG()*getWeight() * velX  * getHeight() : newForce;
        totalForceX = newForce;
    }

    // Y-Axis Handlers
    public void checkVerticalCollision(float newY) {
        int collideCode = map.getCollision(pos.x, newY, (int) getWidth(), (int) getHeight());

        if (collideCode != -10) {
            //Gdx.app.log("CollidingY", ""+collideCode);

            TileType adjacentTile = collideCode != -1? TileType.getTileTypeById(collideCode) : null;

            if (velY < 0) {
                this.pos.y = (float) Math.floor(pos.y);
                grounded = true;
                jumpTick = 0; }


            potentialVelocityY = this.velY;
            this.velY = 0;


            // Collision Handlers
            if (adjacentTile != null) {
                if (adjacentTile.getName() == "Jell") {


                    if (potentialVelocityY <= 0) {
                        Gdx.app.log("Jell", "bouncing");
                        bouncing = true;
                    }
                }

                if (adjacentTile.getName() == "Ice") {
                    Gdx.app.log("Ice", "sliding");
                    sliding = true;
                }

                }

        } else {
            this.pos.y =  newY;
            grounded = false;
        }
    }


    public void updateVelocityY(float force, float deltatime) {
        float newVelY = this.velY;
        newVelY = newVelY + force/getWeight()*deltatime;
        if (floating) {
            this.velY = Math.abs(newVelY) > 50 ? newVelY : 0;
        } else {
            this.velY = newVelY;
        }

    }

    // No force applied in order to make mechanics not so realistic and easy to play
    public void doubleJump() {
        this.velY = getJumpVelocity() * getWeight();
    }

    public void updateGravity(float a) {
        totalForceY += -a * getWeight();
    }

    public void updateFloating(float liquidDensity, float a) {
        float newForce = liquidDensity * a * getVolume();
        totalForceY += newForce;
    }

    public void updateLiquidResistY(float friction) {

        float newForce = totalForceY;
        newForce = this.velY > 0? newForce + friction * -getG()*getWidth() * velY*getWidth() : newForce;
        totalForceY = newForce;
    }

    public EntitySnapshot getSaveSnapshot() {
        return new EntitySnapshot(type.getId(), pos.x, pos.y);
    }
    public Vector2 getPos() {
        return pos;
    }

    public EntityType getType() {
        return type;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public float getWidth() {
        return type.getWidth();
    }

    public float getHeight() {
        return type.getHeight();
    }

    public float getWeight() {
        return type.getWeight();
    }
    public static float getG() {
        return g;
    }

    public static int getJumpHeight() {
        return JUMP_HEIGHT;
    }

    public static int getJumpVelocity() {
        return JUMP_VELOCITY;
    }
    public float getVolume() {
        return getWeight() / this.density;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }
}
