package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1;

import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


@SuppressWarnings("unused")
public class v1_12_R1 extends VersionSupport {
    
    private static v1_12_R1 instance;
    
    public v1_12_R1( JavaPlugin plugin ){
        super( plugin );
        instance = this;
    }
    
    public static v1_12_R1 getInstance( ){
        return instance;
    }
    
    public void registerPlotEvents( ){
        
        Bukkit.getServer( ).getPluginManager( ).registerEvents( new P2Listener( bbbApi ) , plugin );
    }
}
