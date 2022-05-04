package net.lymarket.comissionss.youmind.bbb.velocity.listener;


import com.google.gson.JsonObject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerEvents {
    
    private final HashMap < UUID, Long > timeOnline = new HashMap <>( );
    
    public PlayerEvents( ){
        
        VMain.getInstance( ).getProxy( ).getScheduler( ).buildTask( VMain.getInstance( ) , ( ) -> {
            final long currentTime = System.currentTimeMillis( );
            VMain.getInstance( ).getProxy( ).getAllPlayers( ).forEach( player -> {
                final User user = VMain.getInstance( ).getPlayers( ).getPlayer( player.getUniqueId( ) );
                user.getStats( ).addTIME_PLAYED( currentTime - timeOnline.getOrDefault( player.getUniqueId( ) , 0L ) );
                VMain.getInstance( ).getPlayers( ).savePlayer( user );
                timeOnline.remove( player.getUniqueId( ) );
                timeOnline.put( player.getUniqueId( ) , currentTime );
            } );
            
        } ).repeat( 5 , TimeUnit.SECONDS ).schedule( );
        
    }
    
    @Subscribe
    public void onPostLoginEvent( PostLoginEvent e ){
        timeOnline.put( e.getPlayer( ).getUniqueId( ) , System.currentTimeMillis( ) );
    }
    
    @Subscribe
    public void onDisconnectEvent( DisconnectEvent e ){
        final long currentTime = System.currentTimeMillis( );
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        final User user = VMain.getInstance( ).getPlayers( ).getPlayer( uuid );
        user.getStats( ).addTIME_PLAYED( currentTime - timeOnline.get( uuid ) );
        VMain.getInstance( ).getPlayers( ).savePlayer( user );
        timeOnline.remove( uuid );
        VMain.getInstance( ).getWorldManager( ).getWorlds( ).forEach( world -> {
            world.removeOnlineMember( user.getUUID( ) );
            VMain.getInstance( ).getWorldManager( ).saveWorld( world );
        } );
    }
    
    @Subscribe
    public void onKickedFromServerEvent( KickedFromServerEvent e ){
        if ( e.getServer( ).ping( ).isDone( ) ) {
            VMain.getInstance( ).getWorldManager( ).getAllWorlds( ).forEach( world -> {
                if ( world.getServer( ).equals( e.getServer( ).getServerInfo( ).getName( ) ) ) {
                    world.getOnlineMembers( ).remove( e.getPlayer( ).getUniqueId( ) );
                    VMain.getInstance( ).getWorldManager( ).saveWorld( world );
                }
            } );
        }
        
        VMain.debug( "DisconnectEvent  " + e.getPlayer( ).getUsername( ) + "  " + e.getPlayer( ).getUniqueId( ) );
        for ( RegisteredServer server : VMain.getInstance( ).getProxy( ).getAllServers( ) ) {
            ServerSocketManager.getSocketByServer( server.getServerInfo( ).getName( ) ).ifPresent( socket -> {
                try {
                    final JsonObject json = new JsonObject( );
                    json.addProperty( "type" , "REMOVE_PLAYER_TO_TP_TO_WORLD" );
                    json.addProperty( "uuid" , e.getPlayer( ).getUniqueId( ).toString( ) );
                    socket.getOut( ).println( json );
                    
                } catch ( Exception ignored ) {
                }
            } );
        }
    }
    
}
