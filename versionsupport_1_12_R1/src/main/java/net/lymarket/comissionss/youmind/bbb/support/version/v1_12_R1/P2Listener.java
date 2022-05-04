package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.events.PlayerClaimPlotEvent;
import net.lymarket.comissionss.youmind.bbb.common.BBBApi;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.support.common.events.PlotCreateFailed;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class P2Listener implements Listener {
    
    // if you like the dependency-injection-like approach:
    
    
    private final BBBApi bbbApi;
    
    
    public P2Listener( BBBApi bbbApi ){
        
        this.bbbApi = bbbApi;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerEnterPlot( PlayerClaimPlotEvent e ){
        final Player p = e.getPlayer( );
        final Plot plot = e.getPlot( );
        final User user = bbbApi.getPlayers( ).getPlayer( p.getUniqueId( ) );
        if ( user == null ) {
            e.setCancelled( true );
            return;
        }
        boolean canCreate = true;
        switch ( user.getRank( ) ) {
            case ADMIN:
            case DEV:
                break;
            case BUILDER: {
                if ( plot.getWorldName( ).equals( "bbb-plot-101" ) ) {
                    if ( user.getPlots101( ).size( ) >= 10 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P101 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( "bbb-plot-501" ) ) {
                    if ( user.getPlots501( ).size( ) >= 3 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P501 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( "bbb-plot-1001" ) ) {
                    if ( user.getPlots1001( ).size( ) >= 1 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P1001 ) );
                        canCreate = false;
                        break;
                    }
                }
                break;
            }
            case VISITOR: {
                if ( plot.getWorldName( ).equals( "bbb-plot-101" ) ) {
                    if ( user.getPlots101( ).size( ) >= 5 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P101 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( "bbb-plot-501" ) ) {
                    if ( user.getPlots501( ).size( ) >= 2 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P501 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( "bbb-plot-1001" ) ) {
                    e.setCancelled( true );
                    Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P1001 ) );
                    canCreate = false;
                    break;
                }
            }
        }
        
        if ( canCreate ) {
            if ( plot.getWorldName( ).equals( "bbb-plot-101" ) ) {
                user.addPlot( (new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P101 , plot.getId( ).toString( ) )) );
            } else if ( plot.getWorldName( ).equals( "bbb-plot-501" ) ) {
                user.addPlot( new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P501 , plot.getId( ).toString( ) ) );
            } else if ( plot.getWorldName( ).equals( "bbb-plot-1001" ) ) {
                user.addPlot( new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P1001 , plot.getId( ).toString( ) ) );
            } else {
                e.setCancelled( true );
                return;
            }
            bbbApi.getPlayers( ).savePlayer( user );
        } else {
            e.setCancelled( true );
        }
        //final User user =
        
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract( PlayerInteractEvent event ){
        final Player player = event.getPlayer( );
        final Plot plot = new PlotAPI( ).getPlot( player.getLocation( ) );
        
        if ( plot == null ) {
            return;
        }
        
        
    }
    
}