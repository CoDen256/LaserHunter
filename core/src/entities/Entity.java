package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public abstract class Entity{


    protected GameMap map;

    // Entity properties
    protected Vector2 pos;
    protected EntityType type;

    protected float health;
    protected float energy;
    protected float density;


    // Movement
    protected float velX = 0;
    protected float totalForceX;
    protected float potentialForceX = 0;


    protected float velY = 0;
    protected float potentialVelocityY;
    protected float totalForceY;
    protected float potentialForceY;


    protected float current_friction;
    protected float maxForce;


    // Player attributes

    protected int starCollected = 0;
    protected int coins = 0;


    protected int jumpTick;

    public ClickListener movingRightListener;
    public ClickListener movingLeftListener;

    // Tile Collision
    protected boolean grounded = false;

    protected boolean bouncing;
    protected boolean sliding;
    protected boolean floating;




    // Constants
    protected static final float G = 2000;
    protected static final int JUMP_VELOCITY = 10;
    protected static final float MAX_SPEED = 235;

    protected static final int SPEED_STEP = 6500*49;
    protected static final int JUMP_STEP = 10*49*49*59; // vel * weight * weight * 1/ dt

    protected static final float ICE_FRICTION = 0.005f;
    protected static final float INITIAL_FRICTION = 1.2f; // 0.8f and not grounded

    protected static final float WATER_FRICTIONX = 0.0007f;
    protected static final float WATER_FRICTIONY = 0.001f;

    protected static final float WATER_DENSITY = 10f;



    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        this.pos = new Vector2(snapshot.getX(), snapshot.getY());
        this.health = snapshot.getHealth();
        this.energy = snapshot.getEnergy();
        this.density = snapshot.getDensity();
        this.type = type;
        this.map = map;

    }

    public void update(float deltatime) {

        float newX = pos.x;
        newX += this.velX * deltatime;

        checkHorizontalCollision(newX);


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
            velX = 0;
            potentialForceX = -0.9f*totalForceX;
        }
    }

    public void updateVelocityX(float force, float deltatime) {
        float newVelX = this.velX;
        newVelX =  true ? (newVelX +  force / getWeight() * deltatime) : 0 ;
        if (Math.abs(newVelX) <= MAX_SPEED) {

            if (newVelX * this.velX < 0) {
                this.velX = 0;
            } else {
                this.velX = newVelX;
            }
            //this.velX = Math.abs(newVelX) > 50 ? newVelX : 0;


        } else {
            this.velX = (float) ((int) (Math.abs(newVelX)/newVelX)) * MAX_SPEED;
        }

    }

    public void updateFriction(float friction){
        float frictionForce = getSign(this.velX) * -G*getWeight()*friction;

        if (Math.abs(frictionForce) > maxForce) Gdx.app.log("frictionForce is more than maxForce:", frictionForce+"", new Exception());

        if ((Math.abs(totalForceX) < Math.abs(frictionForce)) && this.velX == 0) {
            frictionForce = -totalForceX;
        }
        totalForceX += frictionForce;
    }

    public void updateLiquidResistX(float friction) {
        float newForce = totalForceX;
        newForce = this.velX != 0? newForce + friction * -G*getWeight() * velX  * getHeight() : newForce;
        totalForceX = newForce;
    }

    // Y-Axis Handlers
    public void checkVerticalCollision(float newY) {
        int collideCode = map.getCollision(pos.x, newY, (int) getWidth(), (int) getHeight());

        if (collideCode != -10) {

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

    // No force is applied in order to make mechanics not so realistic and easy to play
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
        newForce = this.velY > 0? newForce + friction * -G*getWidth() * velY*getWidth() : newForce;
        totalForceY = newForce;
    }

    public EntitySnapshot getSaveSnapshot() {
        return new EntitySnapshot(type.getId(), pos.x, pos.y);
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


    public float getWidth() {
        return type.getWidth();
    }

    public float getHeight() {
        return type.getHeight();
    }

    public float getWeight() {
        return type.getWeight();
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

    public void takeDamage(float amount) {
        Gdx.app.log("Taking damage", amount + "");
        this.health -= amount;
    }

    public void takeEnergy(float amount) {
        Gdx.app.log("Taking energy", amount + "");
        this.energy -= amount;
    }

    public int getSign(float n) {
        return (int) (Math.abs(n) / n);
    }

    public void takeCoin(float amount) {
        coins += amount;
    }

    public void takeStar() {
        starCollected ++;
    }

}
