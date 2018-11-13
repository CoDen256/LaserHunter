package tiles;

import java.util.HashMap;

public enum TileType {

    // Obstacles and solid blocks
    DIRT(0, true,   "Dirt", 0,false, false),
    GRASS(1, true,  "Grass", 0,false, false),
    ROCK(2, true, "Rock", 0, false, false),
    SLOPELEFT(3, false, "SlopeLeft", 0,false, false),
    SLOPERIGHT(4, false, "SlopeRight", 0,false, false),
    Stone1(5, true, "Stone1", 0,false, false),
    Stone2(6, true, "Stone2", 0,false, false),


    //  Spikes: damage 100
    SPIKESLEFT(7, true, "SpikesLeft", 100,false, false),
    SPIKESRIGHT(8, true, "SpikesRight", 100,false, false),
    SPIKESUP(9, true, "SpikesUp", 100,false, false),
    SPIKESDOWN(10, true, "SpikesDown", 100,false, false),

    // Liquid ForceDealers (Uncollidable)
    LAVA(11, false, "Lava", 4,true, false), // damage 2
    WATER(12, false, "Water", 0, true, false),
    HEALINGLAKE(15, false, "HealingLake", -3,true, false), // heal 2
    SLOWINGDIRT(29, false, "SlowingDirt", 0, true, false), // making liquid friction more

    // Solid ForceDealers
    ICE(13, true, "Ice", 0, false, false), // making friction less
    JELL(14, true, "Jell", 0, false, false), // making jump higher


    //Visual Background
    SKY(16, "Sky"),
    SKYNIGHT(17, "SkyNight"),
    SKYSTAR1(18, "SkyStar1"),
    SKYSTAR2(19, "SkyStar2"),
    SKYSTAR3(20, "SkyStar3"),
    CLOUD(21, "Cloud"),
    DARKCLOUD(22,  "DarkCloud"),
    CAVE(23, "Cave"),

    // Uncollidable Visual Foreground
    TREEUP(24,"TreeUp"),
    TREEDOWN(25, "TreeDown"),
    BUSH(26, "Bush"),


    // Moving Platforms
    HORIZONTALPLATFORM(27, true, "HorizontalPlatform", true),
    VERTICALPLATFORM(28, true, "VerticalPlatform", true),

    //Collectible
    SapphireCoin(32,"SapphireCoin", 10, 0, 0),
    GoldCoin(33, "GoldCoin", 20, 0, 0),
    DiamondCoin(34,"DiamondCoin", 30, 0, 0),
    PlatinumCoin(35,"PlatinumCoin", 40, 0, 0),
    Star(36,"Star", 0, 0, 0),
    HealthPotion(37, "HealthPotion", 0, -500, 0),
    EnergyPotion(38, "EnergyPotion", 0, 0, -500),

    ;

    private int id;
    private boolean collidable;
    private String name;
    private float damage;
    private boolean liquid;
    private boolean collectible;
    private boolean movable;

    private int coins;
    private float health;
    private float energy;

    public static  final int TILE_SIZE = 25;


    TileType(int id, boolean collidable, String name, float damage, boolean liquid, boolean collectible) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;

        this.damage = damage;

        this.liquid = liquid;
        this.collectible = collectible;
        this.movable = false;
    }

    // Movable
    TileType(int id, boolean collidable, String name, boolean movable) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;

        this.damage = 0;

        this.liquid = false;
        this.collectible = false;
        this.movable = movable;
    }

    // Visuals
    TileType(int id, String name) {
        this.id = id;
        this.collidable = false;
        this.name = name;

        this.damage = 0;

        this.liquid = false;
        this.collectible = false;
        this.movable = false;
    }

    // Collectibles
    TileType(int id, String name, int coins, float health, float energy ) {
        this.id = id;
        this.collidable = false;
        this.name = name;

        this.damage = 0;

        this.coins = coins;
        this.health = health;
        this.energy = energy;

        this.liquid = false;
        this.collectible = true;
        this.movable = false;
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

    public boolean isLiquid() {
        return liquid;
    }

    public boolean isMovable() {
        return movable;
    }

    public int getCoins() {
        return coins;
    }

    public float getHealth() {
        return health;
    }

    public float getEnergy() {
        return energy;
    }

    public boolean isCollectible() {
        return collectible;
    }

    public boolean isDamageDealer() {
        if (damage != 0) {
            return  true;
        }
        return false;
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
