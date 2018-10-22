package snapshot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import entities.Entity;
import entities.EntityType;
import maps.GameMap;


public class EntityLoader {
    private static Json json = new Json();
    public static ArrayList<Entity> loadEntity (String name, GameMap map, ArrayList<Entity> currentEntities) {

        FileHandle file = Gdx.files.internal( "entities/" + name + ".json");

        if (file.exists()) {
            EntitySnapshot[] snapshots = json.fromJson(EntitySnapshot[].class, file.readString());
            ArrayList<Entity> entities = new ArrayList<Entity>();
            for (EntitySnapshot snapshot : snapshots) {
                entities.add(EntityType.createEntityBySnapshot(snapshot, map));
            }
            return entities;
        } else {
            Gdx.app.error("EntityLoader", "could not load entities");
            saveEntities(name, currentEntities);
            return currentEntities;

        }

    }

    public static void saveEntities(String name, ArrayList<Entity> entities) {
        ArrayList<EntitySnapshot> snapshots = new ArrayList<EntitySnapshot>();
        for (Entity entity : entities) {
            snapshots.add(entity.getSaveSnapshot());
        }

        FileHandle file = Gdx.files.local("entities/" + name + ".json");
        file.writeString(json.prettyPrint(snapshots), false);

    }
}
