package net.lymarket.comissionss.youmind.bbb.support.common.version;

import net.lymarket.comissionss.youmind.bbb.common.BBBApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VersionSupport {
    
    protected final JavaPlugin plugin;
    protected final BBBApi bbbApi;
    
    public VersionSupport( JavaPlugin plugin ){
        this.plugin = plugin;
        this.bbbApi = ( BBBApi ) plugin;
        if ( Bukkit.getPluginManager( ).getPlugin( "PlotSquared" ) != null ) {
            registerPlotEvents( );
        }
        
    }
    
    public BBBApi getBbbApi( ){
        return bbbApi;
    }
    
    public abstract void registerPlotEvents( );
}
