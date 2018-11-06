package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.HashMap;

import maps.GameMap;
import snapshot.EntitySnapshot;
import tiles.TileType;

public enum EntityType {

    PLAYER("player", 29, 55, 49, Player.class),
    GUARDCAT("GuardCat", 29, 55, 49, GuardCat.class);

    private String id;
    private int width, height;
    private float weight;

    private Class loaderClass;


    EntityType(String id, int width, int height, float weight, Class loaderClass) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.weight = weight;

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
