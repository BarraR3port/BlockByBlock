package net.lymarket.comissionss.youmind.bbb.velocity.socketmanager;

import com.google.gson.*;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.LobbyMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.PlotMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.WorldMsg;
import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;
import net.lymarket.comissionss.youmind.bbb.velocity.user.VelocityUser;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProxySocketServer implements Runnable {
    
    private final Socket socket;
    private final Gson gson = new GsonBuilder( ).setPrettyPrinting( ).create( );
    private Scanner in;
    private PrintWriter out;
    private String name;
    
    public ProxySocketServer( Socket socket ){
        this.socket = socket;
        try {
            this.out = new PrintWriter( socket.getOutputStream( ) , true );
            this.in = new Scanner( socket.getInputStream( ) );
        } catch ( IOException e ) {
            e.printStackTrace( );
        }
    }
    
    public void closeConnections( ){
        try {
            socket.close( );
        } catch ( IOException ignored ) {
        }
    }
    
    public void sendMessage( JsonObject message ){
        if ( socket == null ) {
            return;
        }
        if ( !socket.isConnected( ) ) {
            return;
        }
        if ( out == null ) {
            return;
        }
        if ( in == null ) {
            return;
        }
        if ( out.checkError( ) ) {
            return;
        }
        VMain.debug( "Sending message to " + name + ": \n" + gson.toJson( message ) );
    
        out.println( encrypt( gson.toJson( message ) ) );
    }
    
    @Override
    public void run( ){
        while (ServerSocketTask.compute && socket.isConnected( )) {
            try {
                if ( in.hasNext( ) ) {
                    final String encrypted = in.nextLine( );
                    String decryptedMessage = decrypt( encrypted );
                    if ( decryptedMessage == null || decryptedMessage.isEmpty( ) ) {
                        VMain.debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) + "\nMsg: " + encrypted );
                        continue;
                    }
                    final JsonObject json;
                    try {
                
                        JsonElement jse = new JsonParser( ).parse( decryptedMessage );
                        if ( jse.isJsonNull( ) || !jse.isJsonObject( ) ) {
                            VMain.debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) + "\nMsg: " + decryptedMessage );
                            continue;
                        }
                        json = jse.getAsJsonObject( );
                    } catch ( JsonSyntaxException e ) {
                        VMain.debug( "Received bad data from: " + socket.getInetAddress( ).toString( ) + "\nMsg: " + decryptedMessage );
                        e.printStackTrace( );
                        continue;
                    }
            
                    if ( !json.has( "type" ) ) continue;
                    VMain.debug( "Received message from " + name + ": \n" + gson.toJson( json ) );
                    try {
                        switch ( json.get( "type" ).getAsString( ) ) {
                            case "PLAYER_SENT_MSG": {
                                if ( !json.has( "msg_type" ) ) continue;
                        
                                final String msg_type = json.get( "msg_type" ).getAsString( );
                                if ( msg_type.equals( "PlotMsg" ) ) {
                                    final PlotMsg plotMsg = gson.fromJson( json.getAsJsonObject( "msg" ) , PlotMsg.class );
                                    final VelocityUser user = VMain.getInstance( ).getPlayers( ).getPlayer( plotMsg.getOwner( ) );
                                    Component server = LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&7[&ePlots&7] " )
                                            //? Hover Message
                                            .hoverEvent( LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&bPlots Info: \n&7Versión: &e" + plotMsg.getVersion( ) + "\n&7Tipo: &e" + plotMsg.getPlot( ).getType( ).getFormattedName( ) ) );
                                    //? [Rank]
                                    //TextComponent rank = LegacyComponentSerializer.legacyAmpersand( ).deserialize( user.getRank( ).getTabPrefix( ) );
                                    //? [Name]
                                    TextComponent name = LegacyComponentSerializer.legacyAmpersand( ).deserialize( user.getRank( ).getTabPrefix( ) + " " + user.getName( ) + "&7: " ).hoverEvent( LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&7Click para visitar a este jugador" ) )
                                            .clickEvent( ClickEvent.suggestCommand( "/visit " + user.getName( ) ) );
                                    //? [MSG]
                                    Component msg = LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&f" + plotMsg.getMsg( ) )
                                            .hoverEvent( null )
                                            .clickEvent( null );
                                    //? [FINAL MSG]
                                    Component finalMsg = server.append( name ).append( msg ).asComponent( );
                                    for ( Player player : VMain.getInstance( ).getProxy( ).getAllPlayers( ) ) {
                                        player.sendMessage( finalMsg );
                                    }
                                } else if ( msg_type.equals( "WorldMsg" ) ) {
                                    final WorldMsg worldMsg = gson.fromJson( json.getAsJsonObject( "msg" ) , WorldMsg.class );
                                    final VelocityUser user = VMain.getInstance( ).getPlayers( ).getPlayer( worldMsg.getOwner( ) );
                                    Component server = LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&7[&eWorlds&7] " )
                                            //? Hover Message
                                            .hoverEvent( LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&bWorld Info: "/*\n&7Name: &e" + worldMsg.getWorld( ).getName( )*/ + "\n&7Versión: &e" + worldMsg.getVersion( ) + "\n&7Online Members: &e" + worldMsg.getWorld( ).getOnlineMembers( ).size( ) ) );
                                    //? [Rank]
                                    //TextComponent rank = LegacyComponentSerializer.legacyAmpersand( ).deserialize( user.getRank( ).getTabPrefix( ) );
                                    //? [Name]
                                    TextComponent name = LegacyComponentSerializer.legacyAmpersand( ).deserialize( user.getRank( ).getTabPrefix( ) + " " + user.getName( ) + "&7: " )
                                            .hoverEvent( LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&7Click para visitar a este jugador" ) )
                                            .clickEvent( ClickEvent.suggestCommand( "/visit " + user.getName( ) ) );
                                    //? [MSG]
                                    Component msg = LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&f" + worldMsg.getMsg( ) )
                                            .hoverEvent( null )
                                            .clickEvent( null );
                                    //? [FINAL MSG]
                                    Component finalMsg = server.append( name ).append( msg ).asComponent( );
                                    for ( Player player : VMain.getInstance( ).getProxy( ).getAllPlayers( ) ) {
                                        player.sendMessage( finalMsg );
                                    }
                            
                                } else {
                                    final LobbyMsg lobbyMsg = gson.fromJson( json.getAsJsonObject( "msg" ) , LobbyMsg.class );
                                    final VelocityUser user = VMain.getInstance( ).getPlayers( ).getPlayer( lobbyMsg.getOwner( ) );
                                    TextComponent server = LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&7[&eLobby&7] " )
                                            //? Hover Message
                                            .hoverEvent( LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&bThis player is in the &eLobby." ) );
                                    //? [Rank]
                                    //TextComponent rank = LegacyComponentSerializer.legacyAmpersand( ).deserialize( user.getRank( ).getTabPrefix( ) );
                                    //? [Name]
                                    TextComponent name = LegacyComponentSerializer.legacyAmpersand( ).deserialize( user.getRank( ).getTabPrefix( ) + " " + user.getName( ) + "&7: " )
                                            .hoverEvent( null )
                                            .clickEvent( null );
                                    //? [MSG]
                                    TextComponent msg = LegacyComponentSerializer.legacyAmpersand( ).deserialize( "&f" + lobbyMsg.getMsg( ) )
                                            .hoverEvent( null )
                                            .clickEvent( null );
                                    //? [FINAL MSG]
                                    Component finalMsg = server.append( name ).append( msg );
                                    for ( Player player : VMain.getInstance( ).getProxy( ).getAllPlayers( ) ) {
                                        player.sendMessage( finalMsg );
                                    }
                                }
                            }
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
                                continue;
                        
                            }
                    
                            case "UPDATE": {
                                if ( !json.has( "server_name" ) ) continue;
                                if ( json.get( "server_name" ).getAsString( ) == null ) continue;
                                final String server_name = json.get( "server_name" ).getAsString( );
                                ServerSocketTask.otherTasks.add( VMain.getInstance( ).getProxy( ).getScheduler( ).buildTask( VMain.getInstance( ) , ( ) -> {
                                            name = server_name;
                                            ServerSocketManager.getInstance( ).registerServerSocket( server_name , this );
                                        }
                                ).schedule( ) );
                                continue;
                        
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
                                continue;
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
                                continue;
                            }
                    
                            case "WORLD_DELETE_SUCCESS": {
                                if ( !json.has( "current_server" ) ) continue;
                                final String current_server = json.get( "current_server" ).getAsString( );
                                ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> {
                                    json.remove( "type" );
                                    json.addProperty( "type" , "UPDATE_INV_WORLD_DELETE_SUCCESS" );
                                    socket.sendMessage( json );
                                } );
                                continue;
                        
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
                                continue;
                        
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
                                continue;
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
                                    VMain.getInstance( ).getProxy( ).getPlayer( owner_uuid ).ifPresent( player -> VMain.getInstance( ).getProxy( ).getServer( server_target ).ifPresent( server -> {
                                        try {
                                            ConnectionRequestBuilder.Result result = player.createConnectionRequest( server ).connect( ).get( 2 , TimeUnit.SECONDS );
                                            if ( !result.getStatus( ).equals( ConnectionRequestBuilder.Status.SUCCESS ) ) {
                                                player.sendMessage( Utils.format( "&cError al conectar." ) );
                                            }
                                        } catch ( InterruptedException | ExecutionException | TimeoutException e ) {
                                            throw new RuntimeException( e );
                                        }
                                    } ) );
                                } else {
                                    json.remove( "type" );
                                    json.addProperty( "type" , "JOIN_WORLD_REQUEST_POST_DENY" );
                                    ServerSocketManager.getSocketByServer( current_server ).ifPresent( socket -> {
                                        socket.sendMessage( json );
                                    } );
                                }
                                continue;
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
                                continue;
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
                        
                                continue;
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
                        
                                continue;
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
                        
                                continue;
                            }
                    
                            case "WORLD_KICK_SUCCESS": {
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "target_uuid" ) ) continue;
                        
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).ifPresent( ( p ) -> VMain.getInstance( ).getProxy( ).getServer( "lobby" ).ifPresent( server -> p.createConnectionRequest( server ).fireAndForget( ) ) );
                                continue;
                            }
                    
                            case "SEND_VISIT_REQUEST": {
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                if ( !json.has( "target_uuid" ) ) continue;
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                if ( VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).isPresent( ) ) {
                                    final Player p = VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).get( );
                                    p.getCurrentServer( ).ifPresent( server -> {
                                        final String serverName = server.getServerInfo( ).getName( );
                                        json.remove( "type" );
                                        if ( serverName.equals( "lobby" ) ) {
                                            json.addProperty( "type" , "VISIT_REQUEST_DENY" );
                                            json.addProperty( "reason" , "visit.cant-visit-lobby" );
                                            ServerSocketManager.getSocketByServer( json.get( "current_server" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                                            return;
                                        }
                                        json.addProperty( "type" , "VISIT_REQUEST_PREV" );
                                        if ( serverName.startsWith( "PP-" ) ) {
                                            json.addProperty( "visit-type" , "plot" );
                                            ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> socket.sendMessage( json ) );
                                            return;
                                        } else if ( serverName.startsWith( "PW-" ) ) {
                                            json.addProperty( "visit-type" , "world" );
                                            ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> socket.sendMessage( json ) );
                                            return;
                                        } else {
                                            json.addProperty( "type" , "VISIT_REQUEST_DENY" );
                                            json.addProperty( "reason" , "visit.cant-visit-lobby" );
                                            ServerSocketManager.getSocketByServer( json.get( "current_server" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                                        }
                                        ServerSocketManager.getSocketByServer( serverName ).ifPresent( socket -> socket.sendMessage( json ) );
                                
                                    } );
                                } else {
                                    json.remove( "type" );
                                    json.addProperty( "type" , "VISIT_REQUEST_DENY" );
                                    json.addProperty( "reason" , "player.not-found" );
                                    ServerSocketManager.getSocketByServer( json.get( "current_server" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                                    continue;
                                }
                                continue;
                            }
                            case "JOIN_VISIT_PLOT_REQUEST_POST": {
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "target_uuid" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                final String server_target = json.get( "server_target" ).getAsString( );
                                final UUID target_uuid = UUID.fromString( json.get( "target_uuid" ).getAsString( ) );
                                VMain.getInstance( ).getProxy( ).getPlayer( target_uuid ).ifPresent( ( p ) -> VMain.getInstance( ).getProxy( ).getServer( server_target ).ifPresent( server -> p.createConnectionRequest( server ).fireAndForget( ) ) );
                                continue;
                            }
                            case "VISIT_WORLD_REQUEST_POST": {
                                if ( !json.has( "guest_server" ) ) continue;
                                if ( !json.has( "response" ) ) continue;
                                final boolean response = json.get( "response" ).getAsBoolean( );
                                if ( response ) {
                                    final String target_server = json.get( "target_server" ).getAsString( );
                                    final String guest_server = json.get( "guest_server" ).getAsString( );
                                    final UUID guest = UUID.fromString( json.get( "guest" ).getAsString( ) );
                                    if ( !target_server.equals( guest_server ) ) {
                                        VMain.getInstance( ).getProxy( ).getPlayer( guest ).ifPresent( ( p ) -> VMain.getInstance( ).getProxy( ).getServer( target_server ).ifPresent( server -> p.createConnectionRequest( server ).fireAndForget( ) ) );
                                    }
                                } else {
                                    //todo this
                                }
                                continue;
                            }
                    
                            case "JOIN_HOME": {
                                if ( !json.has( "current_server" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "home_uuid" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                json.remove( "type" );
                                json.addProperty( "type" , "JOIN_HOME_PREV" );
                                ServerSocketManager.getSocketByServer( json.get( "server_target" ).getAsString( ) ).ifPresent( socket -> socket.sendMessage( json ) );
                                continue;
                            }
                            case "JOIN_HOME_POST": {
                                if ( !json.has( "tp" ) ) continue;
                                if ( !json.has( "server_target" ) ) continue;
                                if ( !json.has( "owner_uuid" ) ) continue;
                                final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
                                final String server_target = json.get( "server_target" ).getAsString( );
                                boolean tp = json.get( "tp" ).getAsBoolean( );
                                if ( tp ) {
                                    VMain.debug( "Sending " + owner_uuid + " to " + server_target );
                                    VMain.getInstance( ).getProxy( ).getPlayer( owner_uuid ).ifPresent( ( p ) -> VMain.getInstance( ).getProxy( ).getServer( server_target ).ifPresent( server -> p.createConnectionRequest( server ).fireAndForget( ) ) );
                                }
                                continue;
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
                    } catch ( Exception e ) {
                        e.printStackTrace( );
                        continue;
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
            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }
    }
    
    public PrintWriter getOut( ){
        return out;
    }
    
    public String encrypt( String msg ){
        msg = msg.replace( "\\n" , "" );
        msg = msg.replace( "\n" , "" );
        msg = msg.replace( "\\\"" , "\"" );
        msg = msg.replace( "\"{" , "{" );
        msg = msg.replace( "}\"" , "}" );
        return Base64.getEncoder( ).encodeToString( msg.getBytes( ) );
    }
    
    public String decrypt( String data ){
        return new String( Base64.getDecoder( ).decode( data.getBytes( ) ) );
    }
}

