package net.lymarket.comissionss.youmind.bbb.common;

import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IHomeManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.comissionss.youmind.bbb.common.db.IWarpManager;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocket;
import net.lymarket.lyapi.spigot.config.Config;

public interface BBBApi< S, U, H, W > {
    
    void error( String message );
    
    Config getConfig( );
    
    Config getItems( );
    
    String getVersion( );
    
    IPlayerRepository < U > getPlayers( );
    
    IBWorldManager < S > getWorlds( );
    
    IHomeManager < H > getHomes( );
    
    IWarpManager < W > getWarps( );
    
    ISocket < S, U, H, W > getSocket( );
    
    String getNMSVersion( );
    
    String getProxyServerName( );
    
    void debug( String message );
    
    ServerType getServerType( );
    
}
