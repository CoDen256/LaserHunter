package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.HashMap;

import maps.GameMap;
import snapshot.EntitySnapshot;


public enum EntityType {

    PLAYER("player", 29, 55, 49, 1000, 1000, 10.4f, 400, 300, 3,Player.class),
    GUARDCAT("GuardCat", 29, 55, 49, 500, 500, 10.4f, 400, 300, 2, GuardCat.class),
    REDLASER("RedLaser", 21, 10, 20, 8, RedLaser.class);

    private String id;
    private int width, height;
    private float weight;
    private float health, energy;
    private float density;
    private float attackPoints, defendPoints;
    private float attackRange;

    private Class loaderClass;

    EntityType(String id, int width, int height, float weight, float health, float energy, float density, float attackPoints, float defendPoints, float attackRange,Class loaderClass) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.weight = weight;

        this.health = health;
        this.energy = energy;
        this.density = density;

        this.attackPoints = attackPoints;
        this.defendPoints = defendPoints;
        this.attackRange = attackRange;

        this.loaderClass = loaderClass;
    }

    EntityType(String id, int width, int height, float attackPoints, float attackRange,Class loaderClass) {
        this.id = id;
        this.width = width;
        this.height = height;

        this.attackPoints = attackPoints;
        this.attackRange = attackRange;

        this.loaderClass = loaderClass;
    }



    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public float getHealth() {
        return health;
    }

    public float getEnergy() {
        return energy;
    }

    public float getDensity() {
        return density;
    }

    public float getAttackPoints() {
        return attackPoints;
    }

    public float getDefendPoints() {
        return defendPoints;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public static Entity createEntityBySnapshot(EntitySnapshot snapshot, GameMap map) {
        EntityType type = entityTypes.get(snapshot.type) ;
        try {
            Entity entity = (Entity) ClassReflection.newInstance(type.loaderClass);
            entity.create(snapshot, type, map);
            return entity;
        } catch (ReflectionException e) {
            Gdx.app.error("Entity loader", "Could not load entity of type" + type.id);
            return null;
        }
    }

    private static HashMap<String, EntityType> entityTypes;

    static {
        entityTypes = new HashMap<String, EntityType>();
        for (EntityType type : EntityType.values()) {
            entityTypes.put(type.id, type);
        }
    }

}
