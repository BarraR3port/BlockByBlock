package net.lymarket.comissionss.youmind.bbb.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocket;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocketClient;
import net.lymarket.comissionss.youmind.bbb.event.PrevCreateWorld;
import net.lymarket.comissionss.youmind.bbb.home.HomeManager;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.add.AddPlayersToWorldMenuSelector;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.comissionss.youmind.bbb.transformers.Transformer;
import net.lymarket.comissionss.youmind.bbb.users.PlayersRepository;
import net.lymarket.comissionss.youmind.bbb.warp.WarpManager;
import net.lymarket.comissionss.youmind.bbb.world.WorldManager;
import net.lymarket.lyapi.spigot.menu.IUpdatableMenu;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class SpigotSocketClient extends ISocket {
    
    private final VersionSupport vs;
    private ProxySocket mainSocket;
    
    public SpigotSocketClient( PlayersRepository players , WorldManager worlds , HomeManager homes , WarpManager warps , VersionSupport plots ){
        super( players , worlds , homes , warps );
        this.vs = plots;
        
    }
    
    public ISocketClient getSocket( ){
        return mainSocket;
    }
    
    @Override
    public void sendCreateWorldMSG( Object event ){
        final PrevCreateWorld e = ( PrevCreateWorld ) event;
        final JsonObject js = new JsonObject( );
        js.addProperty( "type" , "CREATE_WORLD" );
        js.addProperty( "server_name" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "world_name" , e.getWorld( ).getName( ) );
        js.addProperty( "owner_uuid" , e.getUser( ).toString( ) );
        js.addProperty( "world_uuid" , e.getWorld( ).getUUID( ).toString( ) );
        js.addProperty( "world_version" , e.getWorld( ).getVersion( ) );
        js.addProperty( "world_server" , e.getWorld( ).getServer( ) );
        js.addProperty( "world_layer_material" , e.getMaterial( ).toString( ) );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendJoinServer( UUID owner , String serverTarget ){
        final JsonObject js = new JsonObject( );
        js.addProperty( "type" , "CONNECT_TO_SERVER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , serverTarget );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "msg" , "EMPTY" );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendJoinServer( UUID owner , String serverTarget , String msg ){
        final JsonObject js = new JsonObject( );
        js.addProperty( "type" , "CONNECT_TO_SERVER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , serverTarget );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "msg" , msg );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendKickFromWorld( UUID owner , BWorld world , UUID target ){
        final JsonObject js = new JsonObject( );
        js.addProperty( "type" , "KICK_FROM_WORLD" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , world.getServer( ) );
        js.addProperty( "world_uuid" , world.getUUID( ).toString( ) );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "target_uuid" , target.toString( ) );
        
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendKickFromWorld( UUID owner , String world_uuid , String server , UUID target ){
        final JsonObject js = new JsonObject( );
        js.addProperty( "type" , "KICK_FROM_WORLD" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , server );
        js.addProperty( "world_uuid" , world_uuid );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "target_uuid" , target.toString( ) );
        
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendWorldDeleteRequest( Object player , BWorld world ){
        final Player owner = ( Player ) player;
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "WORLD_DELETE_PREV" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , world.getServer( ) );
        js.addProperty( "owner_uuid" , owner.getUniqueId( ).toString( ) );
        js.addProperty( "world_uuid" , world.getUUID( ).toString( ) );
        js.addProperty( "has_permission" , owner.hasPermission( "blockbyblock.admin.world.delete.other" ) );
        
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendJoinWorldRequest( UUID owner , String serverTarget , UUID worldUUID , int item_slot ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "JOIN_WORLD_REQUEST" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , serverTarget );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "world_uuid" , worldUUID.toString( ) );
        js.addProperty( "item_slot" , item_slot );
        sendMessage( js.toString( ) );
    }
    
    
    @Override
    public void sendJoinPlotRequest( UUID owner , String server_version , String plotID , PlotType plotType , int item_slot ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "JOIN_PLOT_REQUEST" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_version" , server_version );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        js.addProperty( "plot_id" , plotID == null ? "NONE" : plotID );
        js.addProperty( "item_slot" , item_slot );
        js.addProperty( "plot_type" , plotType.toString( ) );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendVisitRequest( UUID owner_uuid , UUID target_uuid ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "SEND_VISIT_REQUEST" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "owner_uuid" , owner_uuid.toString( ) );
        js.addProperty( "target_uuid" , target_uuid.toString( ) );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendMSGToPlayer( UUID target , String key ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "SEND_MSG_TO_PLAYER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "target_uuid" , target.toString( ) );
        js.addProperty( "key" , key );
        js.addProperty( "has-replacements" , false );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendMSGToPlayer( UUID target , String key , String word , String replacement ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "SEND_MSG_TO_PLAYER" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "target_uuid" , target.toString( ) );
        js.addProperty( "key" , key );
        js.addProperty( "has-replacements" , true );
        JsonObject replacements = new JsonObject( );
        replacements.addProperty( word , replacement );
        js.add( "replacements" , replacements );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendMSGToPlayer( UUID target , String key , HashMap < String, String > replacementsMap ){
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
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendJoinHome( UUID owner , Home home ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "JOIN_HOME" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , home.getLocation( ).getServer( ) );
        js.addProperty( "home_uuid" , home.getUUID( ).toString( ) );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        sendMessage( js.toString( ) );
    }
    
    @Override
    public void sendJoinWarp( UUID owner , Warp warp ){
        JsonObject js = new JsonObject( );
        /*js.addProperty( "type" , "JOIN_HOME" );
        js.addProperty( "current_server" , Settings.PROXY_SERVER_NAME );
        js.addProperty( "server_target" , home.getLocation( ).getServer( ) );
        js.addProperty( "home_uuid" , home.getUUID( ).toString( ) );
        js.addProperty( "owner_uuid" , owner.toString( ) );
        sendMessage( js.toString( ) );*/
    }
    
    
    @Override
    public void sendUpdate( ){
        JsonObject js = new JsonObject( );
        js.addProperty( "type" , "UPDATE" );
        js.addProperty( "server_name" , Settings.PROXY_SERVER_NAME );
        sendMessage( js.toString( ) );
    }
    
    public SpigotSocketClient init( String host , int port ) throws IOException{
        Socket socket = new Socket( host , port );
        mainSocket = new ProxySocket( socket );
        return this;
    }
    
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
                            Main.getInstance( ).debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) );
                            continue;
                        }
                        if ( json == null ) continue;
                        if ( !json.has( "type" ) ) continue;
                        Main.getInstance( ).debug( "Received message: \n" + json );
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
                                if ( !json.has( "world_layer_material" ) ) continue;
                                
                                String world_name = json.get( "world_name" ).getAsString( );
                                UUID owner = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                UUID world_uuid = UUID.fromString( json.get( "world_uuid" ).getAsString( ) );
                                String world_version = json.get( "world_version" ).getAsString( );
                                String world_server = json.get( "world_server" ).getAsString( );
                                String world_layer_material = json.get( "world_layer_material" ).getAsString( );
                                Main.getInstance( ).debug( "Received INIT_CREATE_WORLD request: world_name:" + world_name + " world_uuid:" + world_uuid + " world_version:" + world_version + " world_server:" + world_server );
                                if ( Settings.PROXY_SERVER_NAME.equals( world_server ) ) {
                                    Main.getInstance( ).debug( "[World Creation] [Phase 1/2] Creating World: " + world_name );
                                    BWorld world = new BWorld( owner , world_name , world_server , world_version , world_uuid );
                                    getWorlds( ).createCustomLayerWorld( world , world_layer_material );
                                    Main.getInstance( ).debug( "[World Creation] [Phase 2/2] Creating World: " + world_uuid );
                                    getWorlds( ).createWorld( world );
                                    final Player p = Bukkit.getPlayer( owner );
                                    final boolean teleport = p == null;
                                    getWorlds( ).addPlayerToTP( owner , world );
                                    Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> {
                                        Main.getInstance( ).managePermissions( owner , world_uuid , false );
                                    } );
                                    
                                    json.remove( "type" );
                                    json.addProperty( "type" , "INIT_CREATE_WORLD_SUCCESS" );
                                    json.addProperty( "teleport" , teleport );
                                    sendMessage( json );
                                    Main.getInstance( ).debug( "[World Creation] Sending message to Proxy: " + json );
                                } else {
                                    Main.getInstance( ).debug( "Received INIT_CREATE_WORLD but its meant for another server.\n " + json );
                                }
                            }
                            case "REMOVE_PLAYER_TO_TP_TO_WORLD": {
                                if ( !json.has( "uuid" ) ) continue;
                                UUID owner = UUID.fromString( json.get( "uuid" ).getAsString( ) );
                                try {
                                    getWorlds( ).removePlayerToTP( owner );
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
                                if ( !json.has( "world_layer_material" ) ) continue;
                                if ( !json.has( "teleport" ) ) continue;
                                final UUID owner = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final Player p = Bukkit.getPlayer( owner );
                                if ( p.getInventory( ).getHolder( ) instanceof IUpdatableMenu ) {
                                    (( IUpdatableMenu ) p.getOpenInventory( ).getTopInventory( ).getHolder( )).reOpen( );
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
                                final BWorld world = getWorlds( ).getWorld( world_uuid );
                                if ( server_target.equals( current_server ) ) {
                                    final Player p = Bukkit.getPlayer( owner_uuid );
                                    final World localWorld = Bukkit.getWorld( world.getUUID( ).toString( ) );
                                    if ( p != null && localWorld != null && (world.getOwner( ).equals( owner_uuid ) || world.getMembers( ).contains( owner_uuid )) ) {
                                        Main.getInstance( ).debug( "Teleporting " + p.getName( ) + " to " + localWorld.getName( ) );
                                        Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> p.teleport( localWorld.getSpawnLocation( ) ) );
                                        getWorlds( ).addPlayerToWorldOnlineMembers( owner_uuid , world_uuid );
                                    } else {
                                        Main.getInstance( ).debug( "Teleporting " + owner_uuid + " to " + world.getUUID( ).toString( ) );
                                    }
                                    sendMSGToPlayer( owner_uuid , "world.join" , "world" , world.getName( ) );
                                    continue;
                                }
                                
                                json.remove( "type" );
                                json.addProperty( "type" , "JOIN_WORLD_REQUEST_POST" );
                                
                                if ( world.getOwner( ).equals( owner_uuid ) || world.getMembers( ).contains( owner_uuid ) ) {
                                    getWorlds( ).addPlayerToTP( owner_uuid , world );
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
                                        sendMSGToPlayer( owner_uuid , "error.world.not-allowed-to-join-world" );
                                    }
                                } catch ( Exception ignored ) {
                                }
                            }
                            case "JOIN_PLOT_REQUEST_PREV": {
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "server_version" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "plot_id" ) ) continue;
                                if ( !json.has( "item_slot" ) ) continue;
                                if ( !json.has( "plot_type" ) ) continue;
                                
                                vs.getPlotManager( ).manageJoinPlot( json );
                                
                            }
                            case "JOIN_PLOT_REQUEST_POST_DENY": {
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "item_slot" ) ) continue;
                                final int item_slot = json.get( "item_slot" ).getAsInt( );
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                try {
                                    final Player p = Bukkit.getPlayer( owner_uuid );
                                    if ( p.getOpenInventory( ).getTopInventory( ).getHolder( ) instanceof WorldManagerMenu ) {
                                        WorldManagerMenu menu = ( WorldManagerMenu ) p.getOpenInventory( ).getTopInventory( ).getHolder( );
                                        menu.checkSomething( p , item_slot , menu.getInventory( ).getItem( item_slot ) , "&cSin permisos" , "&cNo tienes permisos" );
                                        sendMSGToPlayer( owner_uuid , "error.world.not-allowed-to-join-world" );
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
                                final BWorld world = getWorlds( ).getWorld( world_uuid );
                                final String server_target = json.get( "server_target" ).getAsString( );
                                json.remove( "type" );
                                
                                if ( world.getOwner( ).equals( owner_uuid ) || has_permission ) {
                                    Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> getWorlds( ).deleteWorldFromOutside( owner_uuid , world , server_target , json ) );
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
                                        sendMSGToPlayer( owner_uuid , "world.cant-kick-own" );
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
                                    replace.put( "player" , getPlayers( ).getPlayer( target_uuid ).getName( ) );
                                    sendMSGToPlayer( owner_uuid , "world.kick-success" , replace );
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
                                    if ( player == null ) continue;
                                    if ( hasReplacements ) {
                                        final JsonObject replacements = json.get( "replacements" ).getAsJsonObject( );
                                        final HashMap < String, String > replace = new HashMap <>( );
                                        
                                        for ( Map.Entry < String, JsonElement > entry : replacements.entrySet( ) ) {
                                            replace.put( entry.getKey( ) , entry.getValue( ).getAsString( ) );
                                        }
                                        Main.getInstance( ).debug( "Replacements: " + replace );
                                        player.sendMessage( Main.getLang( ).getMSG( key , replace ) );
                                    } else {
                                        player.sendMessage( Main.getLang( ).getMSG( key ) );
                                    }
                                    
                                } catch ( NullPointerException e ) {
                                    e.printStackTrace( );
                                }
                                
                                
                            }
                            case "UPDATE_ONLINE_PLAYERS_IN_WORLDS": {
                                Bukkit.getScheduler( ).runTaskAsynchronously( Main.getInstance( ) , ( ) ->
                                        getWorlds( ).getWorldsByServer( ).forEach( world -> {
                                            boolean save = false;
                                            final ArrayList < UUID > playersToAdd = new ArrayList <>( );
                                            final World bukkitWorld = Bukkit.getWorld( world.getUUID( ).toString( ) );
                                            Main.getInstance( ).debug( "[UPDATE_ONLINE_PLAYERS_IN_WORLDS] Current world=" + world.getName( ) );
                                            if ( bukkitWorld == null ) {
                                                if ( world.getOnlineMembers( ).size( ) > 0 ) {
                                                    world.setOnlineMembers( new ArrayList <>( ) );
                                                    save = true;
                                                }
                                            } else {
                                                if ( bukkitWorld.getPlayers( ).size( ) != world.getOnlineMembers( ).size( ) ) {
                                                    playersToAdd.addAll( bukkitWorld.getPlayers( ).stream( ).map( Player::getUniqueId ).collect( Collectors.toList( ) ) );
                                                }
                                                if ( playersToAdd.size( ) > 0 ) {
                                                    world.setOnlineMembers( playersToAdd );
                                                    save = true;
                                                }
                                                
                                            }
                                            if ( save ) {
                                                getWorlds( ).saveWorld( world );
                                            }
                                            
                                        } ) );
                            }
                            case "VISIT_REQUEST_PREV": {
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "target_uuid" ) ) continue;
                                final String currentServer = json.get( "current_server" ).getAsString( );
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                
                                final User targetUser = getPlayers( ).getPlayer( target_uuid );
                                if ( targetUser == null ) {
                                    sendMSGToPlayer( owner_uuid , "error.player-not-found" );
                                    continue;
                                }
                                if ( !targetUser.getOption( "allow-visit-world-requests" ) && !targetUser.getOption( "allow-visit-plot-requests" ) ) {
                                    sendMSGToPlayer( owner_uuid , "error.player.not-available-to-be-visited" , "player" , targetUser.getName( ) );
                                    continue;
                                }
                                switch ( Settings.SERVER_TYPE ) {
                                    case LOBBY: {
                                        sendMSGToPlayer( owner_uuid , "error.player.in-lobby" , "player" , targetUser.getName( ) );
                                        continue;
                                    }
                                    case PLOT: {
                                        if ( !targetUser.getOption( "allow-visit-plot-requests" ) ) {
                                            sendMSGToPlayer( owner_uuid , "error.player.not-available-to-be-visited" , "player" , targetUser.getName( ) );
                                            continue;
                                        } else {
                                            vs.getPlotManager( ).manageVisitJoinPlot( owner_uuid , targetUser , currentServer , Settings.PROXY_SERVER_NAME );
                                        }
                                        continue;
                                    }
                                    case WORLDS: {
                                        //TODO VISIT_REQUEST_PREV - WORLDS
                                    }
                                }
                                
                                
                            }
                            
                            case "JOIN_HOME_PREV": {
                                
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "home_uuid" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                try {
                                    final String currentServer = json.get( "current_server" ).getAsString( );
                                    final String targetServer = json.get( "server_target" ).getAsString( );
                                    final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                    final Home home = getHomes( ).getHome( UUID.fromString( json.get( "home_uuid" ).getAsString( ) ) );
                                    if ( currentServer.equalsIgnoreCase( targetServer ) ) {
                                        Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> {
                                            final Loc homeLoc = home.getLocation( );
                                            final Player player = Bukkit.getPlayer( owner_uuid );
                                            player.teleport( Transformer.toLocation( homeLoc ) );
                                            Main.getLang( ).sendMsg( player , "home.tp-to-home" , "home" , home.getName( ) );
                                        } );
                                    } else {
                                        getHomes( ).addPlayerToTP( owner_uuid , home );
                                        json.remove( "type" );
                                        json.addProperty( "type" , "JOIN_HOME_POST" );
                                        json.addProperty( "tp" , true );
                                        sendMessage( json );
                                    }
                                    continue;
                                } catch ( Exception e ) {
                                    json.remove( "type" );
                                    json.addProperty( "type" , "JOIN_HOME_POST" );
                                    json.addProperty( "tp" , false );
                                    sendMessage( json );
                                    continue;
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
                                        sendMSGToPlayer( owner_uuid , "error.server.not-online" , "server" , server_target );
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
                                            sendMSGToPlayer( owner_uuid , "error.world.delete-failed" , replace );
                                            
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
            Main.getInstance( ).debug( "Sending message: \n" + message );
            out.println( message );
            return true;
        }
        
        public void reconnect( String msg ){
            try {
                init( "localhost" , 5555 );
            } catch ( Exception e ) {
                disable( msg );
            }
        }
        
        public void disable( String reason ){
            compute = false;
            Main.getInstance( ).debug( "Disabling socket: " + socket.toString( ) );
            Main.getInstance( ).debug( "Disabling socket Reason: " + reason );
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
