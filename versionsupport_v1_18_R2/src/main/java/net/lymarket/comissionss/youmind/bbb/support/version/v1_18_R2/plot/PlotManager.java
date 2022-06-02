package net.lymarket.comissionss.youmind.bbb.support.version.v1_18_R2.plot;

import com.google.gson.JsonObject;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.support.common.plot.IPlotManager;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlotManager extends IPlotManager < Plot > {
    
    private final PlotAPI api = new PlotAPI( );
    private final VersionSupport vs;
    
    private final JavaPlugin plugin;
    
    
    public PlotManager( JavaPlugin plugin , VersionSupport vs ){
        this.vs = vs;
        this.plugin = plugin;
    }
    
    
    @Override
    public void manageJoinPlot( JsonObject json ){
        final String current_server = json.get( "current_server" ).getAsString( );
        final String server_target = json.get( "server_target" ).getAsString( );
        final UUID owner_uuid = UUID.fromString( json.get( "owner_uuid" ).getAsString( ) );
        final String plot_id = json.get( "plot_id" ).getAsString( );
        
        final Plot plot = api.getAllPlots( ).stream( ).filter( plotI -> plotI.getId( ).toString( ).equals( plot_id ) ).findFirst( ).orElse( null );
        
        final PlotType plot_type = PlotType.valueOf( json.get( "plot_type" ).getAsString( ) );
        if ( server_target.equals( current_server ) ) {
            final Player p = Bukkit.getPlayer( owner_uuid );
            final World world = Bukkit.getWorld( plot_type.getWorldName( ) );
            if ( p != null && world != null ) {
                vs.getBbbApi().debug("Teleporting " + p.getName() + " to " + world.getName().split("-")[0]);
                Bukkit.getScheduler().runTask((Plugin) vs.getBbbApi(), ( ) -> {
                    if (plot != null){
                        plot.teleportPlayer(api.wrapPlayer(p.getUniqueId()), (result) -> {
                            if (result){
                                for ( final Player players : Bukkit.getOnlinePlayers() ){
                                    p.showPlayer(plugin, players);
                                    players.showPlayer(plugin, p);
                                }
                            }
                        });
                    } else {
                        p.teleport( world.getSpawnLocation( ) );
                    }
                } );
            } else {
                vs.getBbbApi( ).debug( "Teleporting " + owner_uuid + " to " + plot_type.getWorldName( ) );
            }
            vs.getBbbApi( ).getSocket( ).sendMSGToPlayer( owner_uuid , "plot.join" , "plot" , plot_type.getWorldName( ) );
            return;
        } else {
            addWorldToTp( owner_uuid , Bukkit.getWorld( plot_type.getWorldName( ) ) );
        }
        if ( plot != null ) {
            super.addPlot( owner_uuid , plot );
        }
        json.remove( "type" );
        json.addProperty( "type" , "JOIN_PLOT_REQUEST_POST" );
        
        vs.getBbbApi( ).getSocket( ).getSocket( ).sendMessage( json );
        
    }
    
    @Override
    public void manageVisitJoinPlot( UUID owner_uuid , User targetUser , String fromServer , String currentServer ){
        final Plot plot = api.wrapPlayer( targetUser.getUUID( ) ).getCurrentPlot( );
        final JsonObject json = new JsonObject( );
        final User owner = ( User ) vs.getBbbApi( ).getPlayers( ).getPlayer( owner_uuid );
        if ( fromServer.equals( currentServer ) ) {
            final Player p = Bukkit.getPlayer( owner_uuid );
            if ( p != null ) {
                Bukkit.getScheduler( ).runTask( ( Plugin ) vs.getBbbApi( ) , ( ) -> {
                    if ( plot != null && (plot.getTrusted( ).contains( owner_uuid ) || owner.getRank( ).isAdmin( )) ) {
                        vs.getBbbApi( ).debug( "Teleporting " + p.getName( ) + " to " + plot.getWorldName( ) );
                        plot.teleportPlayer( api.wrapPlayer( p.getUniqueId( ) ) , ( result ) -> {
                            if ( result ) {
                                for ( final Player players : Bukkit.getOnlinePlayers( ) ) {
                                    p.showPlayer( plugin , players );
                                    players.showPlayer( plugin , p );
                                }
                            }
                        } );
                        
                    } else {
                        vs.getBbbApi( ).debug( "Error " + p.getName( ) + " is not trusted." );
                        vs.getBbbApi( ).getSocket( ).sendMSGToPlayer( owner_uuid , "error.visit.plot.not-trusted" , "player" , targetUser.getName( ) );
                    }
                } );
                return;
                
            }
            vs.getBbbApi( ).debug( "Error " + owner_uuid + " is not online." );
            vs.getBbbApi( ).getSocket( ).sendMSGToPlayer( owner_uuid , "error.player.not-online" , "player" , targetUser.getName( ) );
        } else {
            if ( plot != null && (plot.getTrusted( ).contains( owner_uuid ) || owner.getRank( ).isAdmin( )) ) {
                addPlot( owner_uuid , plot );
                json.addProperty( "type" , "JOIN_VISIT_PLOT_REQUEST_POST" );
                json.addProperty( "server_target" , currentServer );
                json.addProperty( "target_uuid" , owner_uuid.toString( ) );
                vs.getBbbApi( ).getSocket( ).getSocket( ).sendMessage( json );
            } else {
                vs.getBbbApi( ).getSocket( ).sendMSGToPlayer( owner_uuid , "error.visit.plot.not-trusted" , "player" , targetUser.getName( ) );
            }
        }
        
    }
}
