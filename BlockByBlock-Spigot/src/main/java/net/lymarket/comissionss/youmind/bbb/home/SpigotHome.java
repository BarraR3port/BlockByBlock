package net.lymarket.comissionss.youmind.bbb.home;

import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class SpigotHome extends Home {
    
    public SpigotHome(UUID owner, String name, Loc location, String version){
        super(owner, name, location, version);
    }
    
    public SpigotHome(String uuid, UUID owner, String name, Loc location, String version){
        super(uuid, owner, name, location, version);
    }
    
    public Location getBukkitLocation( ){
        return new Location(Bukkit.getWorld(this.getLocation().getWorld()), this.getLocation().getX(), this.getLocation().getY(), this.getLocation().getZ());
    }
}
