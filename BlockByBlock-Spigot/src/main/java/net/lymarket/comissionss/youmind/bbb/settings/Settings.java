package net.lymarket.comissionss.youmind.bbb.settings;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerName;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.lyapi.spigot.config.Config;
import org.bukkit.Location;

import java.util.ArrayList;

public class Settings {
    //Disable this in final releases
    public static boolean DEVELOPMENT_MODE = false;
    public static String SOCKET_IP = "localhost";
    public static int SOCKET_PORT = 5555;
    public static boolean BREAK_BLOCKS = false;
    public static boolean PLACE_BLOCKS = false;
    public static boolean PLAYER_INTERACT_AT_ENTITY = false;
    public static boolean PLAYER_INTERACT_ENTITY = false;
    public static boolean PLAYER_BED_EVENTS = false;
    public static boolean PLAYER_DROP_ITEMS = false;
    public static boolean PLAYER_PICKUP_ITEMS = false;
    public static boolean CONSUME_ITEMS = false;
    public static Location SPAWN_LOCATION;
    public static ServerName SERVER_NAME;
    public static boolean DEBUG;
    public static ArrayList<String> PERMS_WHEN_CREATING_WORLD = new ArrayList<>();
    public static ArrayList<String> PERMS_WHEN_JOINING_WORLD = new ArrayList<>();
    public static ServerType SERVER_TYPE;
    public static String VERSION;
    
    
    public Settings( ){
    
    }
    
    public static void init(Config config){
        BREAK_BLOCKS = config.getBoolean("actions.break-blocks");
        PLACE_BLOCKS = config.getBoolean("actions.place-blocks");
        PLAYER_INTERACT_AT_ENTITY = config.getBoolean("actions.player-interact-at-entity");
        PLAYER_INTERACT_ENTITY = config.getBoolean("actions.player-interact-entity");
        PLAYER_BED_EVENTS = config.getBoolean("actions.player-bed-events");
        PLAYER_DROP_ITEMS = config.getBoolean("actions.player-drop-items");
        PLAYER_PICKUP_ITEMS = config.getBoolean("actions.player-pickup-items");
        CONSUME_ITEMS = config.getBoolean("actions.consume-items");
        try {
            SPAWN_LOCATION = (Location) config.get("spawn.location", Location.class);
        } catch (NullPointerException | ClassCastException ignored) {
        }
        SERVER_NAME = ServerName.fromString(config.getString("global.proxy-server-name"));
        DEBUG = config.getBoolean("global.debug");
        PERMS_WHEN_CREATING_WORLD = new ArrayList<>(config.getStringList("perms.when-creating-world"));
        PERMS_WHEN_JOINING_WORLD = new ArrayList<>(config.getStringList("perms.when-joining-world"));
        SERVER_TYPE = ServerType.valueOf(config.getString("global.server-type"));
        switch(Main.getInstance().getNMSVersion()){
            case "v1_12_R1":{
                VERSION = "1.12";
                break;
            }
            case "v1_16_R3":{
                VERSION = "1.16";
                break;
            }
            default:{
                VERSION = "1.18";
                break;
            }
        }
    }
    
    
}
