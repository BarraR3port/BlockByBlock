package net.lymarket.comissionss.youmind.bbb.common.socket;

import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IHomeManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.comissionss.youmind.bbb.common.db.IWarpManager;

import java.util.UUID;

public abstract class ISocket implements SocketMSG {
    
    private final IBWorldManager worlds;
    
    private final IPlayerRepository players;
    
    private final IHomeManager homes;
    
    private final IWarpManager warps;
    
    public ISocket( IPlayerRepository players , IBWorldManager worlds , IHomeManager homes , IWarpManager warps ){
        this.worlds = worlds;
        this.players = players;
        this.homes = homes;
        this.warps = warps;
    }
    
    protected IBWorldManager getWorlds( ){
        return worlds;
    }
    
    protected IPlayerRepository getPlayers( ){
        return players;
    }
    
    public IHomeManager getHomes( ){
        return homes;
    }
    
    public IWarpManager getWarps( ){
        return warps;
    }
    
    public abstract ISocketClient getSocket( );
    
    
    public abstract void sendJoinHome( UUID owner , Home home );
    
    public abstract void sendJoinWarp( UUID owner , Warp warp );
}
