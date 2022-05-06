package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1.plot;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.events.PlayerClaimPlotEvent;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.support.common.events.PlotCreateFailed;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class P2Listener implements Listener {
    
    private final VersionSupport vs;
    
    
    public P2Listener( VersionSupport vs ){
        this.vs = vs;
    }
    
    @EventHandler
    public void onPlayerJoinEvent( PlayerJoinEvent e ){
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        final boolean tp = vs.getPlotManager( ).hasPlot( uuid );
        if ( tp ) {
            final Plot plot = ( Plot ) vs.getPlotManager( ).getPlot( uuid );
            if ( plot.teleportPlayer( new PlotAPI( ).wrapPlayer( uuid ) ) ) {
                vs.getPlotManager( ).removePlot( uuid );
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerEnterPlot( PlayerClaimPlotEvent e ){
        final Player p = e.getPlayer( );
        final Plot plot = e.getPlot( );
        final User user = vs.getBbbApi( ).getPlayers( ).getPlayer( p.getUniqueId( ) );
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
                if ( plot.getWorldName( ).equals( PlotType.P31.getWorldName( ) ) ) {
                    if ( user.getPlots31( ).size( ) >= 10 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P31 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P101.getWorldName( ) ) ) {
                    if ( user.getPlots101( ).size( ) >= 10 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P101 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P501.getWorldName( ) ) ) {
                    if ( user.getPlots501( ).size( ) >= 3 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P501 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P1001.getWorldName( ) ) ) {
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
                if ( plot.getWorldName( ).equals( PlotType.P31.getWorldName( ) ) ) {
                    if ( user.getPlots31( ).size( ) >= 5 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P31 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P101.getWorldName( ) ) ) {
                    if ( user.getPlots101( ).size( ) >= 5 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P101 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P501.getWorldName( ) ) ) {
                    if ( user.getPlots501( ).size( ) >= 2 ) {
                        e.setCancelled( true );
                        Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P501 ) );
                        canCreate = false;
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P1001.getWorldName( ) ) ) {
                    e.setCancelled( true );
                    Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P1001 ) );
                    canCreate = false;
                    break;
                }
            }
        }
        
        if ( canCreate ) {
            if ( plot.getWorldName( ).equals( PlotType.P31.getWorldName( ) ) ) {
                user.addPlot( (new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P31 , plot.getId( ).toString( ) )) );
            } else if ( plot.getWorldName( ).equals( PlotType.P101.getWorldName( ) ) ) {
                user.addPlot( (new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P101 , plot.getId( ).toString( ) )) );
            } else if ( plot.getWorldName( ).equals( PlotType.P501.getWorldName( ) ) ) {
                user.addPlot( new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P501 , plot.getId( ).toString( ) ) );
            } else if ( plot.getWorldName( ).equals( PlotType.P1001.getWorldName( ) ) ) {
                user.addPlot( new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P1001 , plot.getId( ).toString( ) ) );
            } else {
                e.setCancelled( true );
                return;
            }
            vs.getBbbApi( ).getPlayers( ).savePlayer( user );
        } else {
            e.setCancelled( true );
        }
        //final User user =
        
    }
    
    /*@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract( PlayerInteractEvent e ){
        final Player player = e.getPlayer( );
        final Plot plot = new PlotAPI( ).getPlot( player.getLocation( ) );
        
        if ( plot == null ) {
            return;
        }
        
        
    }*/
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport( PlayerTeleportEvent e ){
        final World world = e.getTo( ).getWorld( );
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        
        final User user = vs.getBbbApi( ).getPlayers( ).getPlayer( playerUUID );
        final Location loc = e.getTo( );
        final Plot plot = new PlotAPI( ).getPlot( loc );
        final Loc location = new Loc( vs.getBbbApi( ).getProxyServerName( ) , world.getName( ) , loc.getX( ) , loc.getY( ) , loc.getZ( ) , plot == null ? null : plot.getId( ).toString( ) );
        user.setLastLocation( location );
        vs.getBbbApi( ).getPlayers( ).savePlayer( user );
    }
    
    @EventHandler
    public void onPlayerChangeWorld( PlayerChangedWorldEvent e ){
        final Player player = e.getPlayer( );
        final World world = player.getWorld( );
        final UUID playerUUID = player.getUniqueId( );
        
        final User user = vs.getBbbApi( ).getPlayers( ).getPlayer( playerUUID );
        if ( world.getName( ).equals( PlotType.P1001.getWorldName( ) ) ) {
            if ( user.getRank( ).equals( Rank.VISITOR ) ) {
                vs.getBbbApi( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" );
                vs.getBbbApi( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.plot.not-allowed-to-join-1001" );
            }
        }
        
    }
    
}