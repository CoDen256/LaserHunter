package tiles;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public enum TileType {

    // Obstacles and solid blocks
    DIRT(0, true,   "Dirt", 0, false,false, false),
    GRASS(1, true,  "Grass", 0, false,false, false),
    ROCK(2, true, "Rock", 0, false,false, false),
    SLOPELEFT(3, true, "SlopeLeft", 0, false,false, false),
    SLOPERIGHT(4, true, "SlopeRight", 0, false,false, false),
    Stone1(5, true, "Stone1", 0, false,false, false),
    Stone2(6, true, "Stone2", 0, false,false, false),


    //  Spikes: damage 100
    SPIKESLEFT(7, true, "SpikesLeft", 100, false,false, false),
    SPIKESRIGHT(8, true, "SpikesRight", 100, false,false, false),
    SPIKESUP(9, true, "SpikesUp", 100, false,false, false),
    SPIKESDOWN(10, true, "SpikesDown", 100, false,false, false),

    // Liquid ForceDealers (Uncollidable)
    LAVA(11, false, "Lava", 10, false,true, false), // damage 10
    WATER(12, false, "Water", 0, false,true, false),
    HEALINGLAKE(15, false, "HealingLake", -10, false,true, false), // heal 10

    // Solid ForceDealers
    ICE(13, true, "Ice", 0, false,false, false), // making friction less
    JELL(14, true, "Jell", 0, false,false, false), // making jump higher


    //Uncollidalble Visual Background
    SKY(16, false, "Sky", 0, false, false, false),
    SKYNIGHT(17, false, "SkyNight", 0, false,false, false),
    SKYSTAR1(18, false, "SkyStar1", 0, false,false, false),
    SKYSTAR2(19, false, "SkyStar2", 0, false,false, false),
    SKYSTAR3(20, false, "SkyStar3", 0, false,false, false),
    CLOUD(21, false, "Cloud", 0, false,false, false),
    DARKCLOUD(22, false, "DarkCloud", 0, false,false, false),
    CAVE(23, false, "Cave", 0, false,false, false),

    // Uncollidable Visual Foreground
    TREEUP(24, false, "TreeUp", 0, false,false, false),
    TREEDOWN(25, false, "TreeDown", 0, false,false, false),
    BUSH(26, false, "Bush", 0, false,false, false),


    // Moving Platforms (ForceDealers)
    HORIZONTALPLATFORM(27, true, "HorizontalPlatform", 0, true,false, false),
    VERTICALPLATFORM(28, true, "VerticalPlatform", 0, true,false, false),

    //Collectible
    SapphireCoin(29, false, "SapphireCoin", 0, false,false, true),
    GoldCoin(30, false, "GoldCoin", 0, false,false, true),
    DiamondCoin(31, false, "DiamondCoin", 0, false,false, true),
    PlatinumCoin(32, false, "PlatinumCoin", 0, false,false, true),
    Star(33, false, "Star", 0, false,false, true),
    HealthPotion(34, false, "HealthPotion", 0, false,false, true),
    EnergyPotion(35, false, "EnergyPotion", 0, false,false, true),

    ;

    private int id;
    private boolean collidable;
    private String name;
    private float damage;
    private boolean dynamic;
    private boolean forceDealer;
    private boolean collectible;

    public static  final int TILE_SIZE = 25;

    TileType(int id, boolean collidable, String name, float damage, boolean dynamic, boolean forceDealer, boolean collectible) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.damage = damage;
        this.dynamic = dynamic;
        this.forceDealer = forceDealer;
        this.collectible = collectible;
    }


    public int getId() {
        return id;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public String getName() {
        return name;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public boolean isForceDealer() {
        return forceDealer;
    }

    public boolean isCollectible() {
        return collectible;
    }

    private static HashMap<Integer, TileType> tileMap;

    static {
        tileMap = new HashMap <Integer, TileType> ();

        for (TileType tileType : TileType.values()) {
            tileMap.put(tileType.getId(), tileType);
        }
    }

    public static TileType getTileTypeById(int id) {
        return tileMap.get(id);

    }
}
