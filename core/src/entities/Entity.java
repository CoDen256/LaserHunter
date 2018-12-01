package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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

    protected String id;
    protected int gameId;

    protected Texture image;

    protected float health;
    protected float maxHealth;
    protected float energy;
    protected float maxEnergy;

    protected float density;
    protected float attackPoints;
    protected float defendPoints;
    protected float attackRange;
    protected float cooldown;

    protected boolean isInDefence;
    protected boolean isAttacking;

    protected int jumpTick;



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

    protected int lifes;
    protected int starCollected = 0;
    protected int coins = 0;



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

    protected int sloping;




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

    protected static final float FLOATING_ENERGY = 40;
    protected static final float DROWNING_HEALTH = 60;
    protected static final float RESTORE_ENERGY = -10;
    protected static final float DEFENCE_ENERGY = 50;
    protected static final float DOUBLE_JUMP_ENERGY = 30;



    public void create(EntitySnapshot snapshot, EntityType type, GameMap map) {
        pos = new Vector2(snapshot.getX(), snapshot.getY());

        image = new Texture(Gdx.files.internal("entities/"+type.getPath()+"/initial.png"));

        id = type.getId();
        gameId = snapshot.getId();

        health = type.getHealth();
        maxHealth = type.getHealth();
        energy = type.getEnergy();
        maxEnergy = type.getEnergy();

        density = type.getDensity();

        attackPoints = type.getAttackPoints();
        defendPoints = type.getDefendPoints();
        attackRange = type.getAttackRange();
        cooldown = type.getCooldown();

        this.type = type;
        this.map = map;


        setDefence(false);
        setAttacking(false);

        jumpTick = 0;

        xCollision = false;
        sloping = 0;

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
    public void updatePhysics(float deltatime) {
        if ((grounded && !floating)) {
            updateFriction(current_friction);
        }

        if (bouncing) {
            bouncing = false;
            totalForceY += JUMP_STEP*1.8;
            jumpTick++;
        }

        if (floating) {
            floating = false;
            if (takeEnergy(FLOATING_ENERGY*deltatime)) {

            } else {
                takeDamage(DROWNING_HEALTH*deltatime);
            }
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
        if (collideCode == map.NO_COLLISION) {
            this.pos.x = newX;
        } else{
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

        if (collideCode != map.NO_COLLISION) {

            TileType adjacentTile = collideCode != -1? TileType.getTileTypeById(collideCode) : null;

            if (velY < 0) {
                pos.y = (float) Math.floor(pos.y);
                grounded = true;
                velX = jumpTick != 0? 0.5f*velX : velX;
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

    public void updateSloping() {
        float xcolision;
        float ycolision = pos.y;
        if (sloping == -1) {
            xcolision = (getX()+getWidth()*0.9f) % TileType.TILE_SIZE;
            ycolision = (int)(getY()/TileType.TILE_SIZE)*TileType.TILE_SIZE + xcolision;


            if (xcolision > 24) {
                ycolision = (int)(getY()/TileType.TILE_SIZE)*TileType.TILE_SIZE + 25;
            } else if (xcolision < 1) {
                ycolision = (int)(getY()/TileType.TILE_SIZE)*TileType.TILE_SIZE;
            }

        } else if (sloping == 1) {
            xcolision = (getX()+getWidth()*0.18f) % TileType.TILE_SIZE;
            ycolision = ((int)(getY()/TileType.TILE_SIZE)+1)*TileType.TILE_SIZE - xcolision;


            if (xcolision > 23) {
                ycolision = ((int)(getY()/TileType.TILE_SIZE)+1)*TileType.TILE_SIZE - 25 ;
            } else if (xcolision < 2) {
                ycolision = ((int)(getY()/TileType.TILE_SIZE)+1)*TileType.TILE_SIZE + 0;
            }


        }
        pos.y = ycolision;
        setSloping(0);
    }


    /* COMBAT */
    // Combat Handlers
    public void attack(float amount, Entity entity) {
        if (entity != null) {
            map.getLog().add("Give to "+entity.getId()+" "+(int)amount+" damage",2, false);
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

    public Entity getClosest(float range, EntityType priority) {
        Entity closest = null;
        for (Entity entity : map.getEntities()) {
            if (entity.getId() != this.getId() && !entity.getId().contains("Laser")) {
                float r = getRadius(entity.getX(), entity.getY(), this.getX(), this.getY());
                if (r < range) {
                    if (closest != null) {
                        if (priority != null) {
                            if (closest.getId() == priority.getId()) {
                                return closest;
                            }
                        }
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

    public void takeLife() {
        if (lifes <= 8) {
            lifes++;
        }

    }


    public void takeDamage(float amount) {
        if (isInDefence) {
            amount = amount/defendPoints;
        }
        health -= amount;
        if (health < 0){
            health = 0;
            lifes--;
            map.getLog().add(this.getId()+" has died :c", 1, false );
        }

        if (health > maxHealth) health = maxHealth;
    }

    public boolean takeEnergy(float amount) {
        float newEnergy = energy - amount;

        if (newEnergy < 0) {
            if (getId() == "player") {
                map.getLog().add("Not enough energy to perform the action", 2, true);
            }

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

    public void setSloping(int state) {
        this.sloping = state;
    }

    public void setGrounded(boolean state) {
        this.grounded = state;
    }

    // Getters
    public EntitySnapshot getSaveSnapshot() {
        return new EntitySnapshot(type.getId(), pos.x, pos.y);
    }

    public EntityType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Vector2 getPos() {
        return pos;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public float centerX() {
        return pos.x + getWidth()/2;
    }

    public float centerY() {
        return pos.y + getHeight()/2;
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

    public Vector2 getSize() {
        return new Vector2(getWidth(), getHeight());
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

    public int getLifes() {
        return lifes;
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
