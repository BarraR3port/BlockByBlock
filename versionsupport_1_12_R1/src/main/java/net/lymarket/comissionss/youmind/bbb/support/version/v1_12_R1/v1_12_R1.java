package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1;

import com.intellectualcrafters.plot.object.Plot;
import net.lymarket.comissionss.youmind.bbb.support.common.plot.IPlotManager;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1.plot.P2Listener;
import net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1.plot.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


@SuppressWarnings("all")
public class v1_12_R1 extends VersionSupport {
    
    private IPlotManager < Plot > plotManager;
    
    public v1_12_R1( JavaPlugin plugin ){
        super( plugin );
    }
    
    public void registerPlotEvents( ){
        
        Bukkit.getServer( ).getPluginManager( ).registerEvents( new P2Listener( this ) , plugin );
        plotManager = new PlotManager( plugin , this );
    }
    
    public IPlotManager getPlotManager( ){
        return plotManager;
    }
}
