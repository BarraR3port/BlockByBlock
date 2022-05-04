package net.lymarket.comissionss.youmind.bbb.velocity.utils;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.server.ServerPing;

public class Serializer {
    
    
    @Subscribe(order = PostOrder.LAST)
    public void onProxyPing( ProxyPingEvent event ){
        ServerPing prev = event.getPing( );
        //System.out.println(prev.getVersion( ).getProtocol( ) );
        //ServerPing copy = new Serializer.LunarClientServerPing( prev.getVersion( ) , prev.getPlayers( ).get() , prev.getDescriptionComponent( ) , prev.getFavicon( ).get() );
        if ( prev.getPlayers( ).isPresent( ) && prev.getFavicon( ).isPresent( ) ) {
            if ( prev.getModinfo( ).isPresent( ) ) {
                
                if ( prev.getVersion( ).getProtocol( ) > 757 || prev.getVersion( ).getProtocol( ) < 46 ) {
                    prev.asBuilder( ).version( new ServerPing.Version( ProtocolVersion.MINECRAFT_1_18.getProtocol( ) , "Lydark 1.8.x - 1.18.x " ) );
                } else {
                    prev.asBuilder( ).version( new ServerPing.Version( prev.getVersion( ).getProtocol( ) , "Lydark 1.8.x - 1.18.x " ) );
                }
            }
            event.setPing( prev.asBuilder( ).build( ) );
        }
        
    }
    
    
    
    /*
    LUNAR CLIENT OLD BUNGEE SERVER PING STAR
    
    static class LunarClientServerPing extends ServerPing {
        private final String lcServer = "sanity";
        
        LunarClientServerPing( ServerPing.Version version , ServerPing.Players players , Component description , Favicon favicon ){
            super( version , players , description , favicon );
        }
    }*/
}
