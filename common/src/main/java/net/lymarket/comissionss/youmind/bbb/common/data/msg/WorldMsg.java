package net.lymarket.comissionss.youmind.bbb.common.data.msg;

import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;

import java.util.UUID;

public class WorldMsg extends Msg {
    
    private final BWorld world;
    
    public WorldMsg(UUID owner, String msg, String version, BWorld world){
        super(owner, msg, version);
        this.world = world;
    }
    
    public BWorld getWorld( ){
        return world;
    }
    
}
