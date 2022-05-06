package net.lymarket.comissionss.youmind.bbb.settings;

import net.lymarket.lyapi.spigot.config.Config;
import org.bukkit.Location;

import java.util.ArrayList;

public class Settings {
    
    
    public static boolean BREAK_BLOCKS = false;
    public static boolean PLACE_BLOCKS = false;
    
    public static boolean PLAYER_INTERACT_AT_ENTITY = false;
    public static boolean PLAYER_INTERACT_ENTITY = false;
    
    public static boolean PLAYER_BED_EVENTS = false;
    
    public static boolean PLAYER_DROP_ITEMS = false;
    
    public static boolean PLAYER_PICKUP_ITEMS = false;
    
    public static boolean CONSUME_ITEMS = false;
    
    public static Location SPAWN_LOCATION;
    
    public static String PROXY_SERVER_NAME;
    
    public static boolean DEBUG;
    
    public static ArrayList < String > PERMS_WHEN_CREATING_WORLD = new ArrayList <>( );
    
    public static ArrayList < String > PERMS_WHEN_JOINING_WORLD = new ArrayList <>( );
    
    public static ServerType SERVER_TYPE;
    
    
    public Settings( ){
    
    }
    
    public static void init( Config config ){
        BREAK_BLOCKS = config.getBoolean( "actions.break-blocks" );
        PLACE_BLOCKS = config.getBoolean( "actions.place-blocks" );
        PLAYER_INTERACT_AT_ENTITY = config.getBoolean( "actions.player-interact-at-entity" );
        PLAYER_INTERACT_ENTITY = config.getBoolean( "actions.player-interact-entity" );
        PLAYER_BED_EVENTS = config.getBoolean( "actions.player-bed-events" );
        PLAYER_DROP_ITEMS = config.getBoolean( "actions.player-drop-items" );
        PLAYER_PICKUP_ITEMS = config.getBoolean( "actions.player-pickup-items" );
        CONSUME_ITEMS = config.getBoolean( "actions.consume-items" );
        try {
            SPAWN_LOCATION = ( Location ) config.get( "spawn.location" , Location.class );
        } catch ( NullPointerException | ClassCastException ignored ) {
        }
        PROXY_SERVER_NAME = config.getString( "global.proxy-server-name" );
        DEBUG = config.getBoolean( "global.debug" );
        PERMS_WHEN_CREATING_WORLD = new ArrayList <>( config.getStringList( "perms.when-creating-world" ) );
        PERMS_WHEN_JOINING_WORLD = new ArrayList <>( config.getStringList( "perms.when-joining-world" ) );
        SERVER_TYPE = ServerType.valueOf( config.getString( "global.server-type" ) );
    }
    
    
}
