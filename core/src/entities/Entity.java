package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import maps.GameMap;
import screens.MainMenuScreen;
import snapshot.EntitySnapshot;
import tiles.TileType;

public abstract class Entity{


    protected GameMap map;

    // Entity properties
    protected Vector2 pos;
    protected EntityType type;

    protected float health;
    protected float maxHealth;
    protected float energy;
    protected float maxEnergy;

    protected float density;
    protected float attackPoints;
    protected float defendPoints;
    protected float attackRange;

    protected boolean isInDefence;
    protected boolean isAttacking;


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
    public ClickListener attackListener;
    public ClickListener defendListener;


    // NPC attributes
    boolean xCollision;

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
        this.maxHealth = snapshot.getHealth();
        this.energy = snapshot.getEnergy();
        this.maxEnergy = snapshot.getEnergy();

        this.density = snapshot.getDensity();

        this.attackPoints = snapshot.getAttackPoints();
        this.defendPoints = snapshot.getDefendPoints();
        this.attackRange = snapshot.getAttackRange();

        this.type = type;
        this.map = map;


        setDefence(false);
        setAttacking(false);

        jumpTick = 0;

        xCollision = false;

        bouncing = false;
        sliding = false;
        floating = false;
        current_friction = INITIAL_FRICTION;

        maxForce = SPEED_STEP;

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

    /* MOVEMENT */
    //X-Movement
    public void moveToRight() {
        this.totalForceX += SPEED_STEP;
    }

    public void moveToLeft() {
        this.totalForceX += -SPEED_STEP;
    }

    public void moveToRight(float amount) {
        this.totalForceX += amount;
    }

    public void moveToLeft(float amount) {
        this.totalForceX += -amount;
    }

    // Y-Movement
    public void jump() {
        jumpTick ++;
        totalForceY += JUMP_STEP;
    }

    public void doubleJump(float energy) {
        if (takeEnergy(energy)){
            jumpTick ++;
            this.velY = getJumpVelocity() * getWeight(); // No force is applied in order to make mechanics not so realistic and easy to play
        }

    }

    public void ascend() {
        totalForceY += JUMP_STEP/10;
    }


    /* PHYSICS SECTION */
    // X-Physics
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

    // Y-Physics
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

    // Total Physics
    public void updatePhysics() {
        if (grounded && !floating) {
            updateFriction(current_friction);
        }

        if (bouncing) {
            bouncing = false;
            totalForceY += JUMP_STEP*1.8;
            jumpTick++;
        }

        if (floating) {
            floating = false;
            updateFloating(WATER_DENSITY, G);
            updateLiquidResistX(WATER_FRICTIONX);
            updateLiquidResistY(WATER_FRICTIONY);
        }

        if (sliding) {
            sliding = false;
            current_friction = ICE_FRICTION;

        }else {
            current_friction = INITIAL_FRICTION;
        }

        updateGravity(G);
    }


    /* Velocity and Collision handlers */
    // X-Axis Handlers
    public void checkHorizontalCollision(float newX) {
        int collideCode = map.getCollision(newX, pos.y, (int) getWidth(), (int) getHeight(), this);
        if (collideCode == -10) {
            this.pos.x = newX;
        } else {
            velX = 0;
            potentialForceX = -0.9f*totalForceX;
            xCollision = true;
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


        } else {
            this.velX = (float) ((int) (Math.abs(newVelX)/newVelX)) * MAX_SPEED;
        }

    }


    // Y-Axis Handlers
    public void checkVerticalCollision(float newY) {
        int collideCode = map.getCollision(pos.x, newY, (int) getWidth(), (int) getHeight(), this);

        if (collideCode != -10) {

            TileType adjacentTile = collideCode != -1? TileType.getTileTypeById(collideCode) : null;

            if (velY < 0) {
                pos.y = (float) Math.floor(pos.y);
                grounded = true;
                jumpTick = 0; }


            potentialVelocityY = this.velY;
            velY = 0;


            // Collision Handlers
            if (adjacentTile != null) {
                if (adjacentTile.getName() == "Jell") {


                    if (potentialVelocityY <= 0) {
                        bouncing = true;
                    }
                }

                if (adjacentTile.getName() == "Ice") {
                    sliding = true;
                }

            }

        } else {
            pos.y =  newY;
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

    // Total Velocity Handler
    public void updateVelocity(float forceX, float forceY, float deltatime) {
        updateVelocityX(forceX, deltatime);
        updateVelocityY(forceY, deltatime);
    }


    /* COMBAT */
    // Combat Handlers
    public void attack(float amount, Entity entity) {
        if (entity != null) {
            Gdx.app.log("Attacking "+amount, entity.getType().getId());
            entity.takeDamage(amount);

        }

    }

    public void setAttacking(boolean state) {
        //Gdx.app.log("Attacking", ""+state);
        isAttacking = state;
    }

    public void setDefence(boolean state) {
        //Gdx.app.log("Defending", state+"");
        isInDefence = state;
    }

    public Entity getClosest(float range) {
        Entity closest = null;
        for (Entity entity : map.getEntities()) {
            if (entity.getType().getId() != this.getType().getId()) {
                float r = getRadius(entity.getX(), entity.getY(), this.getX(), this.getY());
                if (r < range) {
                    if (closest != null) {
                        closest = r <  getRadius(closest.getX(), closest.getY(), this.getX(), this.getY()) ? entity : closest;
                    } else {
                        closest = entity;
                    }
                }
            }
        }
        return closest;
    }



    /* DEFAULT METHODS */
    // Player methods

    public void takeCoin(float amount) {
        coins += amount;
    }

    public void takeStar() {
        starCollected ++;
    }


    public void takeDamage(float amount) {
        health -= amount;
        if (health < 0) health = 0;
        if (health > maxHealth) health = maxHealth;
    }

    public boolean takeEnergy(float amount) {
        float newEnergy = energy - amount;

        if (newEnergy < 0) {
            Gdx.app.log("energy", "not enough energy to perform an action ");
            return false;
        }

        if (newEnergy > maxEnergy) energy = maxEnergy;
        else energy = newEnergy;

        return true;
    }

    // Setters
    public void setFloating(boolean floating) {
        this.floating = floating;
    }


    // Getters
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

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getEnergy() {
        return energy;
    }

    public float getMaxEnergy() {
        return maxEnergy;
    }

    public float getCoins() {
        return coins;
    }

    public float getStars() {
        return starCollected;
    }

    public static int getJumpVelocity() {
        return JUMP_VELOCITY;
    }
    public float getVolume() {
        return getWeight() / this.density;
    }

    // Other
    public int getSign(float n) {
        return (int) (Math.abs(n) / n);
    }

    public float getRadius(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
    }


}
