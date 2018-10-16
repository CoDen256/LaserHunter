package tiles;

import java.util.HashMap;

public enum TileType {

    // Collidables
    // Visuals
    DRRT(0, true,   "Dirt", 0, false,false),
    GRASS(1, true,  "Grass", 0, false,false),
    STONE(2, true, "Stone", 0, false,false),
    SLOPELEFT(3, true, "SlopeLeft", 0, false,false),
    SLOPERIGHT(4, true, "SlopeRight", 0, false,false),
    SLOPELEFTREVERSED(5, true, "SlopeLeftReversed", 0, false,false),
    SLOPERIGHTREVERSED(6, true, "SlopeRightReversed", 0, false,false),


    // DamageDealers
    SPIKESLEFT(7, true, "SpikesLeft", 100, false,false),
    SPIKESRIGHT(8, true, "SpikesRight", 100, false,false),
    SPIKESUP(9, true, "SpikesUp", 100, false,false),
    SPIKESDOWN(10, true, "SpikesDown", 100, false,false),

    // Force+Damage Dealer
    LAVA(11, false, "Lava", 10, false,true),


    // ForceDealers && Uncollidable
    WATER(12, false, "Water", 0, false,true),
    ICE(13, false, "Ice", 0, false,true),
    JELL(14, false, "Jell", 0, false,true),
    HEALINGLAKE(15, false, "HealingLake", -10, false,true),


    //Uncollidalble
    SKY(16, false, "Sky", 0, false,false),
    SKYNIGHT(17, false, "SkyNight", 0, false,false),
    SKYSTAR1(18, false, "SkyStar1", 0, false,false),
    SKYSTAR2(19, false, "SkyStar2", 0, false,false),
    SKYSTAR3(20, false, "SkyStar3", 0, false,false),
    CLOUD(21, false, "Cloud", 0, false,false),
    DARKCLOUD(22, false, "DarkCloud", 0, false,false),
    CAVE(23, false, "Cave", 0, false,false),

    TREEUP(24, false, "TreeUp", 0, false,false),
    TREEDOWN(25, false, "TreeDown", 0, false,false),
    BUSH(26, false, "Bush", 0, false,false),


    //Dynamic
    HORIZONTALPLATFORM(27, true, "HorizontalPlatform", 0, true,false),
    VERTICALPLATFORM(28, true, "VerticalPlatform", 0, true,false),


    ;

    private int id;
    private boolean collidable;
    private String name;
    private float damage;
    private boolean dynamic;
    private boolean forceDealer;

    public static  final int TILE_SIZE = 25;

    TileType(int id, boolean collidable, String name, float damage, boolean dynamic, boolean forceDealer) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.damage = damage;
        this.dynamic = dynamic;
        this.forceDealer = forceDealer;
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
