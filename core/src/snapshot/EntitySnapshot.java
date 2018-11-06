package snapshot;

import java.util.HashMap;

public class EntitySnapshot {

    public String type;
    public float x,y;
    public float health,energy;
    public float density;

    public float attackPoints, defendPoints;
    public float attackRange,visionRange;
    public float healPoints;
    public HashMap<String, String> data;

    public EntitySnapshot() {
    }

    public EntitySnapshot( String type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;

    }



    public void setType(String type) {
        this.type = type;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHealth(){return health;}
    public float getEnergy(){return energy;}
    public float getDensity(){return density;}
    public float getAttackPoints(){return attackPoints;}
    public float getDefendPoints(){return defendPoints;}
    public float getAttackRange(){return attackRange;}

    public String getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVisionRange() {
        return visionRange;
    }

    public float getHealPoints() {
        return healPoints;
    }

    public void putFloat(String key, float value) {
        data.put(key, "" + value);
    }

    public void putInt(String key, int value) {
        data.put(key, ""+ value);
    }

    public void putInt(String key, boolean value) {
        data.put(key, ""+ value);
    }
    public void putInt(String key, String value) {
        data.put(key, value);
    }

    public float getFloat(String key, float defaultValue) {
        if (data.containsKey(key)) {
            try {
                return Float.parseFloat(data.get(key));
            }catch (Exception e){
                return  defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    public int getInt(String key, int defaultValue) {
        if (data.containsKey(key)) {
            try {
                return Integer.parseInt(data.get(key));
            }catch (Exception e){
                return  defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    public boolean getBoolean(String key, boolean defaultValue) {
        if (data.containsKey(key)) {
            try {
                return Boolean.parseBoolean(data.get(key));
            }catch (Exception e){
                return  defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    public String getString(String key, String defaultValue) {
        if (data.containsKey(key)) {
            return data.get(key);
        } else {
            return defaultValue;
        }
    }
}