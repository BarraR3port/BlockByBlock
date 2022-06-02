package net.lymarket.comissionss.youmind.bbb.warp;

import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.WarpType;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class SpigotWarp extends Warp {
    
    public SpigotWarp(Loc location, String version, WarpType type){
        super(location, version, type);
    }
    
    public SpigotWarp(Loc location, String version, WarpType type, boolean isPublic){
        super(location, version, type, isPublic);
    }
    
    public SpigotWarp(String uuid, Loc location, String version, WarpType type, boolean isPublic, List < UUID > members){
        super(uuid, location, version, type, isPublic, members);
    }
    
    public Location getBukkitLocation( ){
        return new Location(Bukkit.getWorld(this.getLocation().getWorld()), this.getLocation().getX(), this.getLocation().getY(), this.getLocation().getZ());
    }
}
