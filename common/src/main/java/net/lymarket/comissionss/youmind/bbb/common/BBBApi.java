package net.lymarket.comissionss.youmind.bbb.common;

import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IHomeManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.comissionss.youmind.bbb.common.db.IWarpManager;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocket;
import net.lymarket.lyapi.spigot.config.Config;

public interface BBBApi {
    
    Config getConfig( );
    
    Config getItems( );
    
    String getVersion( );
    
    IPlayerRepository getPlayers( );
    
    IBWorldManager getWorlds( );
    
    IHomeManager getHomes( );
    
    IWarpManager getWarps( );
    
    ISocket getSocket( );
    
    String getProxyServerName( );
    
    void debug( String message );
    
}
