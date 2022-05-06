package net.lymarket.comissionss.youmind.bbb.velocity.socketmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class ProxySocketServer implements Runnable {
    
    private final Socket socket;
    private Scanner scanner;
    private PrintWriter out;
    
    public ProxySocketServer( Socket socket ){
        this.socket = socket;
        try {
            this.out = new PrintWriter( socket.getOutputStream( ) , true );
            this.scanner = new Scanner( socket.getInputStream( ) );
        } catch ( IOException e ) {
            e.printStackTrace( );
        }
    }
    
    public void sendMessage( JsonObject message ){
        VMain.debug( "Sending message: \n" + message );
        out.println( message );
    }
    
    @Override
    public void run( ){
        while (ServerSocketTask.compute && socket.isConnected( )) {
            if ( scanner.hasNext( ) ) {
                String message = scanner.next( );
                if ( message.isEmpty( ) ) continue;
                final JsonObject json;
                try {
                    JsonElement jse = new JsonParser( ).parse( message );
                    if ( jse.isJsonNull( ) || !jse.isJsonObject( ) ) {
                        VMain.debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) + "\nMsg: " + message );
                        continue;
                    }
                    json = jse.getAsJsonObject( );
                } catch ( JsonSyntaxException e ) {
                    VMain.debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) + "\nMsg: " + message );
                    e.printStackTrace( );
                    continue;
                }
                
                if ( !json.has( "type" ) ) continue;
                VMain.debug( "Received message: \n" + json );
                switch ( json.get( "type" ).getAsString( ) ) {
                    case "CREATE_WORLD": {
                        if ( !json.has( "server_name" ) ) continue;
                        if ( !json.has( "world_name" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        if ( !json.has( "world_version" ) ) continue;
                        if ( !json.has( "world_server" ) ) continue;
                        if ( !json.has( "world_layer_material" ) ) continue;
                        final String serverName = json.get( "world_server" ).getAsString( );
                        
                        ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> {
                            VMain.debug( "Received CREATE_WORLD from: " + serverName );
                            json.remove( "type" );
                            json.addProperty( "type" , "INIT_CREATE_WORLD" );
                            socket.sendMessage( json );
                            
                        } );
                        
                    }
                    
                    case "UPDATE": {
                        if ( !json.has( "server_name" ) ) continue;
                        if ( json.get( "server_name" ).getAsString( ) == null ) continue;
                        
                        ServerSocketTask.otherTasks.add( VMain.getInstance( ).getProxy( ).getScheduler( ).buildTask( VMain.getInstance( ) , ( ) ->
                                ServerSocketManager.getInstance( ).registerServerSocket( json.get( "server_name" ).getAsString( ) , this )
                        ).schedule( ) );
                        
                    }
                    
                    case "WORLD_DELETE_PREV": {
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        
                        final String server_target = json.get( "server_target" ).getAsString( );
                        final String current_server = json.get( "current_server" ).getAsString( );
                        
                        json.addProperty( "same_server" , server_target.equalsIgnoreCase( current_server ) );
                        
                        if ( ServerSocketManager.getSocketByServer( server_target ).isPresent( ) ) {
                            final ProxySocketServer socket = ServerSocketManager.getSocketByServer( server_target ).get( );
                            VMain.debug( "Received WORLD_DELETE_INIT from: " + current_server );
                            json.remove( "type" );
                            json.addProperty( "type" , "WORLD_DELETE_INIT" );
                            socket.sendMessage( json );
                        } else {
                            VMain.debug( "Received WORLD_DELETE_INIT from: " + current_server + " but server is not online" );
                            json.remove( "type" );
                            json.addProperty( "type" , "ERROR" );
                            json.addProperty( "error" , "SERVER_NOT_ONLINE" );
                            this.sendMessage( json );
                            
                        }
                    }
                    case "INIT_CREATE_WORLD_SUCCESS": {
                        if ( !json.has( "server_name" ) ) continue;
                        if ( !json.has( "world_name" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        if ( !json.has( "world_version" ) ) continue;
                        if ( !json.has( "world_server" ) ) continue;
                        if ( !json.has( "world_layer_material" ) ) continue;
                        if ( !json.has( "teleport" ) ) continue;
                        final String server_name = json.get( "server_name" ).getAsString( );
                        
                        ServerSocketManager.getSocketByServer( server_name ).ifPresent( socket -> {
                            json.remove( "type" );
                            json.addProperty( "type" , "UPDATE_INV_POST_INIT_CREATE_WORLD_SUCCESS" );
                            socket.sendMessage( json );
                        } );
                    }
                    
                    case "WORLD_DELETE_SUCCESS": {
                        if ( !json.has( "current_server" ) ) continue;
                        final String current_server = json.get( "current_server" ).getAsString( );
                        ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> {
                            json.remove( "type" );
                            json.addProperty( "type" , "UPDATE_INV_WORLD_DELETE_SUCCESS" );
                            socket.sendMessage( json );
                        } );
                        
                    }
                    
                    case "SEND_MSG_TO_PLAYER": {
                        if ( !json.has( "target_uuid" ) ) continue;
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "key" ) ) continue;
                        final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                        
                        json.remove( "type" );
                        json.addProperty( "type" , "SEND_MSG_TO_PLAYER_POST" );
                        VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).flatMap( p -> p.getCurrentServer( ).flatMap( server -> ServerSocketManager.getSocketByServer( server.getServerInfo( ).getName( ) ) ) ).ifPresent( socket -> socket.sendMessage( json ) );
                        
                        /*ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> {
                            socket.sendMessage( json );
                        } );*/
                        
                    }
                    
                    case "JOIN_WORLD_REQUEST": {
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        if ( !json.has( "item_slot" ) ) continue;
                        final String server_target = json.get( "server_target" ).getAsString( );
                        json.remove( "type" );
                        json.addProperty( "type" , "JOIN_WORLD_REQUEST_PREV" );
                        ServerSocketManager.getSocketByServer( server_target ).ifPresent( socket -> socket.sendMessage( json ) );
                    }
                    
                    case "JOIN_WORLD_REQUEST_POST": {
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        if ( !json.has( "response" ) ) continue;
                        if ( !json.has( "item_slot" ) ) continue;
                        final String server_target = json.get( "server_target" ).getAsString( );
                        final String current_server = json.get( "current_server" ).getAsString( );
                        final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                        final boolean response = json.get( "response" ).getAsBoolean( );
                        final UUID world_uuid = UUID.fromString( json.get( "world_uuid" ).getAsString( ) );
                        
                        if ( response && VMain.getInstance( ).getWorldManager( ).addPlayerToWorldOnlineMembers( owner_uuid , world_uuid ) ) {
                            VMain.getInstance( ).getProxy( ).getPlayer( owner_uuid ).ifPresent( player -> VMain.getInstance( ).getProxy( ).getServer( server_target ).ifPresent( server -> player.createConnectionRequest( server ).fireAndForget( ) ) );
                        } else {
                            json.remove( "type" );
                            json.addProperty( "type" , "JOIN_WORLD_REQUEST_POST_DENY" );
                            ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> {
                                socket.sendMessage( json );
                            } );
                        }
                    }
                    
                    case "JOIN_PLOT_REQUEST": {
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "server_version" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "plot_id" ) ) continue;
                        if ( !json.has( "item_slot" ) ) continue;
                        if ( !json.has( "plot_type" ) ) continue;
                        final String server_version = json.get( "server_version" ).getAsString( );
                        json.remove( "type" );
                        json.addProperty( "type" , "JOIN_PLOT_REQUEST_PREV" );
                        String server_target = "PP-" + server_version.replace( "." , "" ) + "-1";
                        json.addProperty( "server_target" , server_target );
                        ServerSocketManager.getSocketByServer( server_target ).ifPresent( socket -> socket.sendMessage( json ) );
                    }
                    
                    case "JOIN_PLOT_REQUEST_POST": {
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "server_version" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "plot_id" ) ) continue;
                        if ( !json.has( "item_slot" ) ) continue;
                        if ( !json.has( "plot_type" ) ) continue;
                        final String server_target = json.get( "server_target" ).getAsString( );
                        final String current_server = json.get( "current_server" ).getAsString( );
                        final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                        if ( !current_server.equals( server_target ) )
                            VMain.getInstance( ).getProxy( ).getPlayer( owner_uuid ).ifPresent( player -> VMain.getInstance( ).getProxy( ).getServer( server_target ).ifPresent( server -> player.createConnectionRequest( server ).fireAndForget( ) ) );
                        
                        
                            /*json.remove( "type" );
                            json.addProperty( "type" , "JOIN_WORLD_REQUEST_POST_DENY" );
                            ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> {
                                socket.sendMessage( json );
                            } );*/
                        
                    }
                    
                    case "CONNECT_TO_SERVER": {
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "msg" ) ) continue;
                        final String serverName = json.get( "server_target" ).getAsString( );
                        final String currentServer = json.get( "current_server" ).getAsString( );
                        final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                        final String msg = json.get( "msg" ).getAsString( );
                        
                        if ( VMain.getInstance( ).getProxy( ).getPlayer( owner_uuid ).isPresent( ) ) {
                            final Player p = VMain.getInstance( ).getProxy( ).getPlayer( owner_uuid ).get( );
                            if ( currentServer.equalsIgnoreCase( serverName ) ) {
                                p.sendMessage( Utils.format( "&cYa estás conectado en el server " + serverName ) );
                                return;
                            }
                            VMain.getInstance( ).getProxy( ).getServer( serverName ).ifPresent( server -> p.createConnectionRequest( server ).fireAndForget( ) );
                            if ( !msg.equalsIgnoreCase( "EMPTY" ) ) {
                                p.sendMessage( Utils.format( msg.replace( "%player%" , p.getUsername( ) ) ) );
                            }
                        }
                        
                    }
                    
                    case "KICK_FROM_WORLD": {
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        if ( !json.has( "world_uuid" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "target_uuid" ) ) continue;
                        final String server_target = json.get( "server_target" ).getAsString( );
                        //final String currentServer = json.get( "current_server" ).getAsString( );
                        /*final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                        final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                        if ( !server_target.equalsIgnoreCase( currentServer ) ) {
                            if ( owner_uuid.equals( target_uuid ) ) {
                            
                            }
                            
                        }*/
                        json.remove( "type" );
                        json.addProperty( "type" , "KICK_FROM_WORLD_PREV" );
                        
                        ServerSocketManager.getSocketByServer( server_target ).ifPresent( socket -> socket.sendMessage( json ) );
                        
                        
                    }
                    
                    case "WORLD_KICK_SUCCESS": {
                        if ( !json.has( "server_target" ) ) continue;
                        if ( !json.has( "target_uuid" ) ) continue;
                        
                        final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                        VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).ifPresent( ( p ) -> VMain.getInstance( ).getProxy( ).getServer( "lobby" ).ifPresent( server -> p.createConnectionRequest( server ).fireAndForget( ) ) );
                        
                    }
                    
                    case "SEND_VISIT_REQUEST": {
                        if ( !json.has( "current_server" ) ) continue;
                        if ( !json.has( "owner_uuid" ) ) continue;
                        if ( !json.has( "target_uuid" ) ) continue;
                        final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                        if (VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).isPresent() ){
                            final Player p = VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).get( );
                            p.getCurrentServer().ifPresent( server -> {
                                final String serverName =server.getServerInfo( ).getName( );
    
                                json.remove( "type" );
                                if ( serverName.equals( "lobby" ) ) {
                                    json.addProperty( "type" , "VISIT_REQUEST_DENY" );
                                    json.addProperty( "reason" , "error.visit.cant-visit-lobby" );
                                    ServerSocketManager.getSocketByServer( json.get( "current_server" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                                }
                                json.addProperty( "type" , "VISIT_REQUEST_PREV" );
                                if ( serverName.startsWith( "PP-" ) ) {
                                    json.addProperty( "visit-type", "plot" );
                                    ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> socket.sendMessage( json ) );
                                } else if ( serverName.startsWith( "PW-" ) ) {
                                    json.addProperty( "visit-type", "world" );
                                    ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> socket.sendMessage( json ) );
                                } else {
                                    json.addProperty( "type" , "VISIT_REQUEST_DENY" );
                                    json.addProperty( "reason" , "error.visit.cant-visit-lobby" );
                                    ServerSocketManager.getSocketByServer( json.get( "current_server" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                                }
                                ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> socket.sendMessage( json ) );
        
                            } );
                        } else {
                            json.remove( "type" );
                            json.addProperty( "type" , "VISIT_REQUEST_DENY" );
                            json.addProperty( "reason" , "error.player.not-found" );
                            ServerSocketManager.getSocketByServer( json.get( "current_server" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                            continue;
                        }
    
                    }
                    
                    case "ERROR": {
                        if ( !json.has( "error" ) ) continue;
                        final String error = json.get( "error" ).getAsString( );
                        VMain.getInstance( ).getLogger( ).severe( error );
                        switch ( error ) {
                            case "WORLD_DELETE_FAILED": {
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                final String current_server = json.get( "current_server" ).getAsString( );
                                
                                ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> socket.sendMessage( json ) );
                                
                            }
                            case "SERVER_NOT_ONLINE": {
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "world_uuid" ) ) continue;
                                final String current_server = json.get( "current_server" ).getAsString( );
                                
                                ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> socket.sendMessage( json ) );
                            }
                        }
                    }
                }
            } else {
                try {
                    socket.close( );
                    VMain.getInstance( ).getLogger( ).info( "Socket closed: " + socket );
                } catch ( IOException e ) {
                    e.printStackTrace( );
                }
                
                Thread.currentThread( ).interrupt( );
                break;
            }
        }
    }
    
    public PrintWriter getOut( ){
        return out;
    }
}

