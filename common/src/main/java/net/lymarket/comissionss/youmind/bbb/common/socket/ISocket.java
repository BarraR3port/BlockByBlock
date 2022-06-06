package net.lymarket.comissionss.youmind.bbb.common.socket;

import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.Msg;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.data.world.WorldVisitRequest;
import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IHomeManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.comissionss.youmind.bbb.common.db.IWarpManager;

import java.util.UUID;

public abstract class ISocket< V, U, H, W > implements SocketMSG {
    
    private final IBWorldManager < V > worlds;
    
    private final IPlayerRepository < U > players;
    
    private final IHomeManager < H > homes;
    
    private final IWarpManager < W > warps;
    
    public ISocket(IPlayerRepository < U > players, IBWorldManager < V > worlds, IHomeManager < H > homes, IWarpManager < W > warps){
        this.worlds = worlds;
        this.players = players;
        this.homes = homes;
        this.warps = warps;
    }
    
    protected IBWorldManager < V > getWorlds( ){
        return worlds;
    }
    
    protected IPlayerRepository < U > getPlayers( ){
        return players;
    }
    
    public IHomeManager < H > getHomes( ){
        return homes;
    }
    
    public IWarpManager < W > getWarps( ){
        return warps;
    }
    
    public abstract ISocketClient getSocket( );
    
    public abstract void sendJoinHome(UUID owner, Home home);
    
    public abstract void sendJoinWarp(UUID owner, Warp warp);
    
    public abstract void sendWorldVisitResponse(WorldVisitRequest request);
    
    public abstract void sendMsgFromPlayer(Msg msg);
    
    
}
