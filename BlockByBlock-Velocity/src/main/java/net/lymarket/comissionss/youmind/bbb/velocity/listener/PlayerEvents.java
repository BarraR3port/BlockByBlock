package net.lymarket.comissionss.youmind.bbb.velocity.listener;


import com.google.gson.JsonObject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.Utils;

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
        VMain.getInstance( ).getPlayers( ).getOrCreatePlayer( e.getPlayer( ).getUsername( ) , e.getPlayer( ).getUniqueId( ) , String.valueOf( e.getPlayer( ).getRemoteAddress( ).getAddress( ) ).replace( "/" , "" ) );
        
        timeOnline.put( e.getPlayer( ).getUniqueId( ) , System.currentTimeMillis( ) );
    }
    
    @Subscribe
    public void onPlayerPreLogin( PreLoginEvent e ){
        if ( e.getUsername( ).contains( "McDown_pw_" ) || e.getUsername( ).contains( "McDown" ) ) {
            e.setResult( PreLoginEvent.PreLoginComponentResult.denied( Utils.format( "PENDEJO" ) ) );
        }
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
    
    @Subscribe(order = PostOrder.FIRST)
    public void onKickedFromServerEvent( KickedFromServerEvent e ){
        if ( e.getServer( ).ping( ).isDone( ) ) {
            VMain.getInstance( ).getWorldManager( ).getAllWorlds( ).forEach( world -> {
                if ( world.getServer( ).equals( e.getServer( ).getServerInfo( ).getName( ) ) ) {
                    world.getOnlineMembers( ).remove( e.getPlayer( ).getUniqueId( ) );
                    VMain.getInstance( ).getWorldManager( ).saveWorld( world );
                }
            } );
        }
        
        VMain.debug( "KickedFromServerEvent  " + e.getPlayer( ).getUsername( ) + "  " + e.getPlayer( ).getUniqueId( ) );
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
        
        if ( VMain.getInstance( ).getProxy( ).getServer( "lobby" ).isPresent( ) ) {
            e.setResult( KickedFromServerEvent.RedirectPlayer.create( VMain.getInstance( ).getProxy( ).getServer( "lobby" ).get( ) ) );
        } else {
            e.setResult( KickedFromServerEvent.DisconnectPlayer.create( Utils.format( "&cNo hay lobbys disponibles!" ) ) );
        }
    }
    
    @Subscribe(order = PostOrder.LAST)
    public void onProxyPing( ProxyPingEvent event ){
        ServerPing prev = event.getPing( );
        if ( prev.getPlayers( ).isPresent( ) && prev.getFavicon( ).isPresent( ) ) {
            if ( prev.getModinfo( ).isPresent( ) ) {
                
                if ( prev.getVersion( ).getProtocol( ) > 757 || prev.getVersion( ).getProtocol( ) < 46 ) {
                    prev.asBuilder( ).version( new ServerPing.Version( ProtocolVersion.MINECRAFT_1_18.getProtocol( ) , "BlockByBlock 1.8.x - 1.18.x " ) );
                } else {
                    prev.asBuilder( ).version( new ServerPing.Version( prev.getVersion( ).getProtocol( ) , "BlockByBlock 1.8.x - 1.18.x " ) );
                }
            }
            event.setPing( prev.asBuilder( ).build( ) );
        }
        
    }
    
}
