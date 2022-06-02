package net.lymarket.comissionss.youmind.bbb.users;

import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.TestOnly;

import java.util.UUID;

public class SpigotUser extends User {
    
    @TestOnly
    public SpigotUser( ){
    }
    
    public SpigotUser(String name, UUID uuid){
        super(name, uuid);
    }
    
    public Location getBukkitLocation( ){
        return new Location(Bukkit.getWorld(this.getLastLocation().getWorld()), this.getLastLocation().getX(), this.getLastLocation().getY(), this.getLastLocation().getZ());
    }
}
