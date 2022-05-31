package net.lymarket.comissionss.youmind.bbb.common.data.world;

import java.util.UUID;

public class WorldVisitRequest {
    
    private final UUID guest;
    
    private final String guest_name;
    
    private final String guest_server;
    
    private final UUID target_uuid;
    
    private final String target_server;
    
    private boolean accepted = false;
    
    public WorldVisitRequest( UUID guest , UUID target_uuid , String guest_name , String guest_server , String target_server ){
        this.guest = guest;
        this.target_uuid = target_uuid;
        this.guest_name = guest_name;
        this.guest_server = guest_server;
        this.target_server = target_server;
    }
    
    public UUID getTarget_uuid( ){
        return target_uuid;
    }
    
    public UUID getGuest( ){
        return guest;
    }
    
    public String getGuest_name( ){
        return guest_name;
    }
    
    public String getGuest_server( ){
        return guest_server;
    }
    
    public String getTarget_server( ){
        return target_server;
    }
    
    public WorldVisitRequest accept( ){
        this.accepted = true;
        return this;
    }
    
    public boolean isAccepted( ){
        return accepted;
    }
}
