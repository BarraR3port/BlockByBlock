package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1.plot;

import com.google.gson.JsonObject;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.support.common.plot.IPlotManager;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlotManager extends IPlotManager < Plot > {
    
    private final PlotAPI api = new PlotAPI( );
    private final VersionSupport vs;
    
    public PlotManager( VersionSupport vs ){
        this.vs = vs;
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
                vs.getBbbApi( ).debug( "Teleporting " + p.getName( ) + " to " + world.getName( ) );
                Bukkit.getScheduler( ).runTask( ( Plugin ) vs.getBbbApi( ) , ( ) -> {
                    if ( plot != null ) {
                        plot.teleportPlayer( api.wrapPlayer( p ) );
                    } else {
                        p.teleport( world.getSpawnLocation( ) );
                    }
                } );
            } else {
                vs.getBbbApi( ).debug( "Teleporting " + owner_uuid + " to " + plot_type.getWorldName( ) );
            }
            vs.getBbbApi( ).getSocket( ).sendFormattedSendMSGToPlayer( owner_uuid , "plot.join" , "plot" , plot_type.getWorldName( ) );
            return;
        }
        if ( plot != null ) {
            super.addPlot( owner_uuid , plot );
        }
        json.remove( "type" );
        json.addProperty( "type" , "JOIN_PLOT_REQUEST_POST" );
        
        vs.getBbbApi( ).getSocket( ).getSocket( ).sendMessage( json );
        
    }
}
