package net.lymarket.comissionss.youmind.bbb.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocket;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocketClient;
import net.lymarket.comissionss.youmind.bbb.event.PrevCreateWorld;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.create.WorldCreatorMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.edit.WorldEditorMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.add.AddPlayersToWorldMenuSelector;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class SpigotSocketClient implements ISocket {
    
    private ProxySocket mainSocket;
    
    
    public SpigotSocketClient( ){
    
    }
    
    /**
     * Format message to create World.
     */
    public static String formatCreateWorldMSG( PrevCreateWorld e ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "CREATE_WORLD" );
        js.addProperty( "server_name" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "world_name" , e.getWorld( ).getName( ) );
        js.addProperty( "owner_uuid" , e.getUser( ).toString( ) );
        js.addProperty( "world_uuid" , e.getWorld( ).getUUID( ).toString( ) );
        js.addProperty( "world_version" , e.getWorld( ).getVersion( ) );
        js.addProperty( "world_server" , e.getWorld( ).getServer( ) );
        js.addProperty( "world_is_public" , e.getWorld( ).isPublicWorld( ) );
        js.addProperty( "world_layer_material" , e.getMaterial( ).toString( ) );
        
        return js.toString( );
    }
    
    public static String formatJoinServer( UUID owner , String serverTarget ){
        return formatJoinServer( owner , serverTarget , "EMPTY" );
    }
    
    public static String formatJoinServer( UUID owner , String serverTarget , String msg ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "CONNECT_TO_SERVER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , serverTarget );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "msg" , msg );
        
        return js.toString( );
    }
    
    public static String formatKickFromWorld( UUID owner , BWorld world , UUID target ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "KICK_FROM_WORLD" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , world.getServer( ) );
        js.addProperty( "world_uuid" , world.getUUID( ).toString( ) );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "target_uuid" , target.toString( ) );
        
        return js.toString( );
    }
    
    public static String formatWorldDeleteRequest( Player owner , BWorld world ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "WORLD_DELETE_PREV" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , world.getServer( ) );
        js.addProperty( "owner_uuid" , owner.getUniqueId( ).toString( ) );
        js.addProperty( "world_uuid" , world.getUUID( ).toString( ) );
        js.addProperty( "has_permission" , owner.hasPermission( "blockbyblock.admin.world.delete.other" ) );
        
        return js.toString( );
    }
    
    public static String formatJoinWorldRequest( UUID owner , String serverTarget , UUID worldUUID , int item_slot ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "JOIN_WORLD_REQUEST" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , serverTarget );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "world_uuid" , worldUUID.toString( ) );
        js.addProperty( "item_slot" , item_slot );
        
        
        return js.toString( );
    }
    
    public static String formatSendMSGToPlayer( UUID target , String key ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "SEND_MSG_TO_PLAYER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "target_uuid" , target.toString( ) );
        js.addProperty( "key" , key );
        js.addProperty( "has-replacements" , false );
        return js.toString( );
    }
    
    public static String formatSendMSGToPlayer( UUID target , String key , String word , String replacement ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "SEND_MSG_TO_PLAYER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "target_uuid" , target.toString( ) );
        js.addProperty( "key" , key );
        js.addProperty( "has-replacements" , true );
        JsonObject replacements = new JsonObject( );
        replacements.addProperty( word , replacement );
        js.add( "replacements" , replacements );
        return js.toString( );
    }
    
    public static String formatSendMSGToPlayer( UUID target , String key , HashMap < String, String > replacementsMap ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "SEND_MSG_TO_PLAYER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "target_uuid" , target.toString( ) );
        js.addProperty( "key" , key );
        js.addProperty( "has-replacements" , true );
        JsonObject replacements = new JsonObject( );
        for ( String word : replacementsMap.keySet( ) ) {
            replacements.addProperty( word , replacementsMap.get( word ) );
        }
        js.add( "replacements" , replacements );
        return js.toString( );
    }
    
    /**
     * Format message to create World.
     */
    public static String formatUpdate( ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "UPDATE" );
        js.addProperty( "server_name" , Settings.PROXY_SERVER_NAME );
        
        return js.toString( );
    }
    
    public ISocketClient getSocket( ){
        return mainSocket;
    }
    
    public SpigotSocketClient init( ) throws IOException{
        Socket socket = new Socket( "localhost" , 5555 );
        mainSocket = new ProxySocket( socket );
        return this;
    }
    
    /**
     * Send arena data to the lobbies.
     */
    public void sendMessage( String message ){
        if ( message == null ) return;
        if ( message.isEmpty( ) ) return;
        
        if ( mainSocket == null ) {
            try {
                Socket socket = new Socket( "localhost" , 5555 );
                mainSocket = new ProxySocket( socket );
                mainSocket.sendMessage( message );
                
            } catch ( IOException ignored ) {
            }
        } else {
            mainSocket.sendMessage( message );
        }
    }
    
    /**
     * Send arena data to the lobbies.
     */
    public void sendMessage( JsonObject message ){
        sendMessage( message.toString( ) );
    }
    
    /**
     * Close active sockets.
     */
    public void disable( ){
        mainSocket.disable( "Close active sockets" );
    }
    
    private boolean isNumber( String s ){
        try {
            Double.parseDouble( s );
        } catch ( Exception e ) {
            try {
                Integer.parseInt( s );
            } catch ( Exception ex ) {
                try {
                    Long.parseLong( s );
                } catch ( Exception exx ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private class ProxySocket implements ISocketClient {
        private final Socket socket;
        private PrintWriter out;
        private Scanner in;
        private boolean compute = true;
        
        private ProxySocket( Socket socket ){
            this.socket = socket;
            try {
                out = new PrintWriter( socket.getOutputStream( ) , true );
            } catch ( IOException ignored ) {
                out = null;
                return;
            }
            
            try {
                in = new Scanner( socket.getInputStream( ) );
            } catch ( IOException ignored ) {
                return;
            }
            
            Bukkit.getScheduler( ).runTaskAsynchronously( Main.getInstance( ) , ( ) -> {
                while (compute) {
                    if ( in.hasNext( ) ) {
                        String msg = in.next( );
                        if ( msg.isEmpty( ) ) continue;
                        final JsonObject json;
                        try {
                            json = new JsonParser( ).parse( msg ).getAsJsonObject( );
                        } catch ( JsonSyntaxException e ) {
                            Main.debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) );
                            continue;
                        }
                        if ( json == null ) continue;
                        if ( !json.has( "type" ) ) continue;
                        Main.debug( "Received message: \n" + json );
                        switch ( json.get( "type" ).getAsString( ).toUpperCase( ) ) {
                            //pre load data
                            //pld,worldIdentifier,uuidUser,languageIso,uuidPartyOwner
                            case "INIT_CREATE_WORLD": {
                                if ( !json.has( "server_name" ) ) continue;
                                if ( !json.has( "world_name" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "world_version" ) ) continue;
                                if ( !json.has( "world_server" ) ) continue;
                                if ( !json.has( "world_is_public" ) ) continue;
                                if ( !json.has( "world_layer_material" ) ) continue;
                                
                                String world_name = json.get( "world_name" ).getAsString( );
                                UUID owner = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                UUID world_uuid = UUID.fromString( json.get( "world_uuid" ).getAsString( ) );
                                String world_version = json.get( "world_version" ).getAsString( );
                                String world_server = json.get( "world_server" ).getAsString( );
                                boolean world_is_public = json.get( "world_is_public" ).getAsBoolean( );
                                String world_layer_material = json.get( "world_layer_material" ).getAsString( );
                                Main.debug( "Received INIT_CREATE_WORLD request: world_name:" + world_name + " world_uuid:" + world_uuid + " world_version:" + world_version + " world_server:" + world_server + " world_is_public:" + world_is_public );
                                if ( Settings.PROXY_SERVER_NAME.equals( world_server ) ) {
                                    Main.debug( "[World Creation] [Phase 1/2] Creating World: " + world_name );
                                    BWorld world = new BWorld( owner , world_name , world_server , world_version , world_is_public , world_uuid );
                                    Main.getInstance( ).getWorlds( ).createCustomLayerWorld( world , world_layer_material );
                                    final World finalWorld = Bukkit.getWorld( world_uuid.toString( ) );
                                    Main.debug( "[World Creation] [Phase 2/2] Creating World: " + world_uuid.toString( ) );
                                    Main.getInstance( ).getWorlds( ).createWorld( world );
                                    final Player p = Bukkit.getPlayer( owner );
                                    final boolean teleport = p == null;
                                    Main.getInstance( ).getWorlds( ).addPlayerToTP( owner , world );
                                    if ( !teleport ) {
                                        for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ) {
                                            Bukkit.dispatchCommand( Bukkit.getConsoleSender( ) , "lp user " + p.getName( ) + " permission set " + perm + " " + true + " world=" + world_uuid.toString( ) + " server=" + world_server );
                                        }
                                        Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> p.teleport( finalWorld.getSpawnLocation( ) ) );
                                    }
                                    
                                    
                                    json.remove( "type" );
                                    json.addProperty( "type" , "INIT_CREATE_WORLD_SUCCESS" );
                                    json.addProperty( "teleport" , teleport );
                                    sendMessage( json );
                                    Main.debug( "[World Creation] Sending message to Proxy: " + json );
                                } else {
                                    Main.debug( "Received INIT_CREATE_WORLD but its meant for another server.\n " + json );
                                }
                            }
                            case "REMOVE_PLAYER_TO_TP_TO_WORLD": {
                                if ( !json.has( "uuid" ) ) continue;
                                UUID owner = UUID.fromString( json.get( "uuid" ).getAsString( ) );
                                try {
                                    Main.getInstance( ).getWorlds( ).removePlayerToTP( owner );
                                } catch ( Exception ignored ) {
                                }
                            }
                            case "UPDATE_INV_POST_INIT_CREATE_WORLD_SUCCESS": {
                                if ( !json.has( "server_name" ) ) continue;
                                if ( !json.has( "world_name" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "world_version" ) ) continue;
                                if ( !json.has( "world_server" ) ) continue;
                                if ( !json.has( "world_is_public" ) ) continue;
                                if ( !json.has( "world_layer_material" ) ) continue;
                                if ( !json.has( "teleport" ) ) continue;
                                final UUID owner = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final Player p = Bukkit.getPlayer( owner );
                                final boolean teleport = json.get( "teleport" ).getAsBoolean( );
                                final String world_uuid = json.get( "world_uuid" ).getAsString( );
                                final String world_server = json.get( "world_server" ).getAsString( );
                                if ( teleport ) {
                                    for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ) {
                                        Bukkit.dispatchCommand( Bukkit.getConsoleSender( ) , "lp user " + p.getName( ) + " permission set " + perm + " " + true + " world=" + world_uuid + " server=" + world_server );
                                    }
                                }
                                if ( p.getInventory( ).getHolder( ) instanceof WorldManagerMenu ) {
                                    WorldManagerMenu plotMenu = ( WorldManagerMenu ) p.getInventory( ).getHolder( );
                                    plotMenu.reOpen( );
                                }
                                if ( p.getInventory( ).getHolder( ) instanceof WorldCreatorMenu ) {
                                    WorldCreatorMenu plotMenu = ( WorldCreatorMenu ) p.getInventory( ).getHolder( );
                                    plotMenu.reOpen( );
                                }
                                if ( p.getInventory( ).getHolder( ) instanceof WorldEditorMenu ) {
                                    WorldEditorMenu plotMenu = ( WorldEditorMenu ) p.getInventory( ).getHolder( );
                                    plotMenu.reOpen( );
                                }
                                
                            }
                            case "UPDATE_INV_WORLD_DELETE_SUCCESS": {
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "same_server" ) ) continue;
                                if ( !json.has( "has_permission" ) ) continue;
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                try {
                                    final Player p = Bukkit.getPlayer( owner_uuid );
                                    
                                    if ( p.getInventory( ).getHolder( ) instanceof AddPlayersToWorldMenuSelector ) {
                                        AddPlayersToWorldMenuSelector plotMenu = ( AddPlayersToWorldMenuSelector ) p.getInventory( ).getHolder( );
                                        plotMenu.handleAccept( );
                                    }
                                    
                                } catch ( NullPointerException ignored ) {
                                }
                                
                                
                            }
                            case "JOIN_WORLD_REQUEST_PREV": {
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "item_slot" ) ) continue;
                                final String server_target = json.get( "server_target" ).getAsString( );
                                final String current_server = json.get( "current_server" ).getAsString( );
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final UUID world_uuid = UUID.fromString( json.get( "world_uuid" ).getAsString( ) );
                                final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
                                if ( server_target.equals( current_server ) ) {
                                    final Player p = Bukkit.getPlayer( owner_uuid );
                                    final World localWorld = Bukkit.getWorld( world.getUUID( ).toString( ) );
                                    if ( p != null && localWorld != null && (world.isPublicWorld( ) || world.getOwner( ).equals( owner_uuid ) || world.getMembers( ).contains( owner_uuid )) ) {
                                        Main.debug( "Teleporting " + p.getName( ) + " to " + localWorld.getName( ) );
                                        Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> p.teleport( localWorld.getSpawnLocation( ) ) );
                                        Main.getInstance( ).getWorlds( ).addPlayerToWorldOnlineMembers( owner_uuid , world_uuid );
                                    } else {
                                        Main.debug( "Teleporting " + owner_uuid + " to " + world.getUUID( ).toString( ) );
                                    }
                                    sendMessage( formatSendMSGToPlayer( owner_uuid , "world.join" , "world" , world.getName( ) ) );
                                    continue;
                                }
                                
                                json.remove( "type" );
                                json.addProperty( "type" , "JOIN_WORLD_REQUEST_POST" );
                                
                                if ( world.isPublicWorld( ) || world.getOwner( ).equals( owner_uuid ) || world.getMembers( ).contains( owner_uuid ) ) {
                                    Main.getInstance( ).getWorlds( ).addPlayerToTP( owner_uuid , world );
                                    json.addProperty( "response" , true );
                                } else {
                                    json.addProperty( "response" , false );
                                }
                                sendMessage( json );
                            }
                            case "JOIN_WORLD_REQUEST_POST_DENY": {
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "item_slot" ) ) continue;
                                final int item_slot = json.get( "item_slot" ).getAsInt( );
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                try {
                                    final Player p = Bukkit.getPlayer( owner_uuid );
                                    if ( p.getOpenInventory( ).getTopInventory( ).getHolder( ) instanceof WorldManagerMenu ) {
                                        WorldManagerMenu menu = ( WorldManagerMenu ) p.getOpenInventory( ).getTopInventory( ).getHolder( );
                                        menu.checkSomething( p , item_slot , menu.getInventory( ).getItem( item_slot ) , "&cSin permisos" , "&cNo tienes permisos" );
                                        sendMessage( formatSendMSGToPlayer( owner_uuid , "error.world.not-allowed-to-join-world" ) );
                                    }
                                } catch ( Exception ignored ) {
                                }
                            }
                            case "WORLD_DELETE_INIT": {
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "same_server" ) ) continue;
                                if ( !json.has( "has_permission" ) ) continue;
                                final boolean has_permission = json.get( "has_permission" ).getAsBoolean( );
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final UUID world_uuid = UUID.fromString( json.get( "world_uuid" ).getAsString( ) );
                                final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
                                final String server_target = json.get( "server_target" ).getAsString( );
                                json.remove( "type" );
                                
                                if ( world.getOwner( ).equals( owner_uuid ) || has_permission ) {
                                    Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> Main.getInstance( ).getWorlds( ).deleteWorldFromOutside( owner_uuid , world , server_target , json ) );
                                } else {
                                    json.addProperty( "type" , "ERROR" );
                                    json.addProperty( "error" , "WORLD_DELETE_FAILED" );
                                    sendMessage( json );
                                    continue;
                                }
                                
                                
                            }
                            case "KICK_FROM_WORLD_PREV": {
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "target_uuid" ) ) continue;
                                final String server_target = json.get( "server_target" ).getAsString( );
                                final String currentServer = json.get( "current_server" ).getAsString( );
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                if ( server_target.equalsIgnoreCase( currentServer ) ) {
                                    if ( owner_uuid.equals( target_uuid ) ) {
                                        sendMessage( formatSendMSGToPlayer( owner_uuid , "world.cant-kick-own" ) );
                                        continue;
                                    }
                                }
                                
                                json.remove( "type" );
                                json.addProperty( "type" , "WORLD_KICK_SUCCESS" );
                                sendMessage( json );
                            }
                            case "WORLD_KICK_SUCCESS": {
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "target_uuid" ) ) continue;
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                final UUID world_uuid = UUID.fromString( json.get( "world_uuid" ).getAsString( ) );
                                
                                try {
                                    final HashMap < String, String > replace = new HashMap <>( );
                                    replace.put( "world" , world_uuid.toString( ) );
                                    replace.put( "player" , Main.getInstance( ).getPlayers( ).getPlayer( target_uuid ).getName( ) );
                                    sendMessage( formatSendMSGToPlayer( owner_uuid , "world.kick-success" , replace ) );
                                } catch ( NullPointerException ignored ) {
                                }
                            }
                            case "SEND_MSG_TO_PLAYER_POST": {
                                if ( !json.has( "target_uuid" ) ) continue;
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "key" ) ) continue;
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                final String key = json.get( "key" ).getAsString( );
                                final boolean hasReplacements = json.get( "has-replacements" ).getAsBoolean( );
                                
                                try {
                                    final Player player = Bukkit.getPlayer( target_uuid );
                                    if ( player == null ) return;
                                    if ( hasReplacements ) {
                                        final JsonObject replacements = json.get( "replacements" ).getAsJsonObject( );
                                        final HashMap < String, String > replace = new HashMap <>( );
                                        
                                        for ( Map.Entry < String, JsonElement > entry : replacements.entrySet( ) ) {
                                            Main.debug( "Replacement: " + entry.getKey( ) + " = " + entry.getValue( ).getAsString( ) );
                                            replace.put( entry.getKey( ) , entry.getValue( ).getAsString( ) );
                                        }
                                        Main.getLang( ).sendMsg( player , key , replace );
                                    } else {
                                        player.sendMessage( Main.getLang( ).getMSG( key ) );
                                    }
                                    
                                } catch ( NullPointerException ignored ) {
                                    ignored.printStackTrace( );
                                }
                                
                                
                            }
                            case "ERROR": {
                                if ( !json.has( "error" ) ) continue;
                                final String error = json.get( "error" ).getAsString( );
                                Main.getInstance( ).getLogger( ).severe( error );
                                switch ( error ) {
                                    case "SERVER_NOT_ONLINE": {
                                        if ( !json.has( "owner_uuid" ) ) continue;
                                        if ( !json.has( "server_target" ) ) continue;
                                        final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                        final String server_target = json.get( "server_target" ).getAsString( );
                                        sendMessage( formatSendMSGToPlayer( owner_uuid , "error.server.not-online" , "server" , server_target ) );
                                    }
                                    case "WORLD_DELETE_FAILED": {
                                        if ( !json.has( "owner_uuid" ) ) continue;
                                        if ( !json.has( "current_server" ) ) continue;
                                        if ( !json.has( "server_target" ) ) continue;
                                        if ( !json.has( "world_uuid" ) ) continue;
                                        final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                        final String server_target = json.get( "server_target" ).getAsString( );
                                        final String world_uuid = json.get( "world_uuid" ).getAsString( );
                                        try {
                                            final HashMap < String, String > replace = new HashMap <>( );
                                            replace.put( "server" , server_target );
                                            replace.put( "world" , world_uuid );
                                            sendMessage( formatSendMSGToPlayer( owner_uuid , "error.world.delete-failed" , replace ) );
                                            
                                        } catch ( NullPointerException ignored ) {
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        disable( "in.hasNext()" );
                    }
                }
            } );
        }
        
        public boolean sendMessage( JsonObject message ){
            return sendMessage( message.toString( ) );
        }
        
        /**
         * Send a message to the given host with target port.
         *
         * @return true if message was sent successfully.
         */
        @SuppressWarnings("UnusedReturnValue")
        public boolean sendMessage( String message ){
            if ( socket == null ) {
                reconnect( "socket == null" );
                return false;
            }
            if ( !socket.isConnected( ) ) {
                reconnect( "!socket.isConnected( )" );
                return false;
            }
            if ( out == null ) {
                reconnect( "out == null" );
                return false;
            }
            if ( in == null ) {
                reconnect( "in == null" );
                return false;
            }
            if ( out.checkError( ) ) {
                reconnect( "out.checkError( )" );
                return false;
            }
            Main.debug( "Sending message: \n" + message );
            out.println( message );
            return true;
        }
        
        public void reconnect( String msg ){
            try {
                init( );
            } catch ( Exception e ) {
                disable( msg );
            }
        }
        
        public void disable( String reason ){
            compute = false;
            Main.debug( "Disabling socket: " + socket.toString( ) );
            Main.debug( "Disabling socket Reason: " + reason );
            try {
                socket.close( );
            } catch ( IOException e ) {
                e.printStackTrace( );
            }
            in = null;
            out = null;
            Bukkit.shutdown( );
        }
    }
}
