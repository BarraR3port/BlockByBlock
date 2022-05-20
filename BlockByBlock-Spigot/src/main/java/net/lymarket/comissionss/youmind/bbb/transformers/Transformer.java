package net.lymarket.comissionss.youmind.bbb.transformers;

import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Transformer {
    
    
    public static Location toLocation( Loc loc ){
        return new Location( Bukkit.getWorld( loc.getWorld( ) ) , loc.getX( ) , loc.getY( ) , loc.getZ( ) );
    }
    
    public static Loc toLoc( Location location , UUID world , String plot ){
        if ( world != null ) {
            return new Loc( Settings.SERVER_NAME , location.getWorld( ).getName( ) , location.getX( ) , location.getY( ) , location.getZ( ) , world );
        } else if ( plot != null ) {
            return new Loc( Settings.SERVER_NAME , location.getWorld( ).getName( ) , location.getX( ) , location.getY( ) , location.getZ( ) , plot );
        } else {
            return new Loc( Settings.SERVER_NAME , location.getWorld( ).getName( ) , location.getX( ) , location.getY( ) , location.getZ( ) );
        }
        
    }
}
