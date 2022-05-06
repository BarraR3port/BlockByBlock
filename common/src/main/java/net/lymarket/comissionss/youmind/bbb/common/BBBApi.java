package net.lymarket.comissionss.youmind.bbb.common;

import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocket;
import net.lymarket.lyapi.spigot.config.Config;

public interface BBBApi {
    
    Config getConfig( );
    
    Config getItems( );
    
    String getVersion( );
    
    IPlayerRepository getPlayers( );
    
    IBWorldManager getWorlds( );
    
    ISocket getSocket( );
    
    String getProxyServerName( );
    
    void debug( String message );
    
}
