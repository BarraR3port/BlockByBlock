package net.lymarket.comissionss.youmind.bbb.support.common.version;

import net.lymarket.comissionss.youmind.bbb.common.BBBApi;
import net.lymarket.comissionss.youmind.bbb.support.common.plot.IPlotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VersionSupport< S, U, H, W > {
    
    protected final JavaPlugin plugin;
    protected final BBBApi < S, U, H, W > bbbApi;
    
    public VersionSupport(JavaPlugin plugin){
        this.plugin = plugin;
        this.bbbApi = (BBBApi) plugin;
        switch(bbbApi.getServerType()){
            case LOBBY:{
                break;
            }
            case PLOT:{
                if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null){
                    registerPlotEvents();
                }
                break;
            }
            case WORLDS:{
                registerWorldEvents();
                break;
            }
        
        }
        
        
    }
    
    public BBBApi < S, U, H, W > getBbbApi( ){
        return bbbApi;
    }
    
    public abstract void registerPlotEvents( );
    
    public abstract void registerWorldEvents( );
    
    public abstract IPlotManager getPlotManager( );
}
