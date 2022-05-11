package net.lymarket.comissionss.youmind.bbb.common.socket;

import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;

import java.util.HashMap;
import java.util.UUID;

public interface SocketMSG {
    
    void sendCreateWorldMSG( Object event );
    
    void sendJoinServer( UUID owner , String serverTarget );
    
    void sendJoinServer( UUID owner , String serverTarget , String msg );
    
    void sendKickFromWorld( UUID owner , BWorld world , UUID target );
    
    void sendKickFromWorld( UUID owner , String world_uuid , String server , UUID target );
    
    void sendWorldDeleteRequest( Object owner , BWorld world );
    
    void sendJoinWorldRequest( UUID owner , String serverTarget , UUID worldUUID , int item_slot );
    
    void sendJoinPlotRequest( UUID owner , String server_version , String plotID , PlotType plotType , int item_slot );
    
    void sendVisitRequest( UUID owner_uuid , UUID target );
    
    void sendMSGToPlayer( UUID target , String key );
    
    void sendMSGToPlayer( UUID target , String key , String word , String replacement );
    
    void sendMSGToPlayer( UUID target , String key , HashMap < String, String > replacementsMap );
    
    void sendUpdate( );
}
