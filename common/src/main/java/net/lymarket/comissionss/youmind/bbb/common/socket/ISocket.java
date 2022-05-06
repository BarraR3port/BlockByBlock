package net.lymarket.comissionss.youmind.bbb.common.socket;

import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;

import java.util.HashMap;
import java.util.UUID;

public abstract class ISocket {
    
    private final IBWorldManager worlds;
    
    private final IPlayerRepository players;
    
    public ISocket( IPlayerRepository players , IBWorldManager worlds ){
        this.worlds = worlds;
        this.players = players;
    }
    
    protected IBWorldManager getWorlds( ){
        return worlds;
    }
    
    protected IPlayerRepository getPlayers( ){
        return players;
    }
    
    public abstract ISocketClient getSocket( );
    
    public abstract void sendFormattedCreateWorldMSG( Object event );
    
    public abstract void sendFormattedJoinServer( UUID owner , String serverTarget );
    
    public abstract void sendFormattedJoinServer( UUID owner , String serverTarget , String msg );
    
    public abstract void sendFormattedKickFromWorld( UUID owner , BWorld world , UUID target );
    
    public abstract void sendFormattedKickFromWorld( UUID owner , String world_uuid , String server , UUID target );
    
    public abstract void sendFormattedWorldDeleteRequest( Object owner , BWorld world );
    
    public abstract void sendFormattedJoinWorldRequest( UUID owner , String serverTarget , UUID worldUUID , int item_slot );
    
    public abstract void sendFormattedJoinPlotRequest( UUID owner , String server_version , String plotID , PlotType plotType , int item_slot );
    
    public abstract void sendFormattedSendVisitRequest( UUID owner_uuid , UUID target );
    
    public abstract void sendFormattedSendMSGToPlayer( UUID target , String key );
    
    public abstract void sendFormattedSendMSGToPlayer( UUID target , String key , String word , String replacement );
    
    public abstract void sendFormattedSendMSGToPlayer( UUID target , String key , HashMap < String, String > replacementsMap );
    
    public abstract void sendFormattedUpdate( );
    
    
}
