package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1.plot;

import com.intellectualcrafters.plot.generator.HybridPlotWorld;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.worlds.SingleWorldGenerator;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import org.junit.Test;

import java.util.UUID;

public class P2ListenerTest {
    
    @Test()
    public void onPlayerEnterPlot( ){
        User user = new User( "BarraR3port" , UUID.fromString( "bc7d7eb8-cb64-4002-8ee7-6bd68e04d789" ) );
        user.setOption( "changed-plots" , true );
        final Plot plot = new Plot( new HybridPlotWorld( "bbb-plot-31" , "12" , new SingleWorldGenerator( ) , new PlotId( 25 , 25 ) , new PlotId( 25 , 26 ) ) , new PlotId( 25 , 25 ) );
        boolean changed = user.getOption( "changed-plots" );
        user.setRank( Rank.BUILDER );
        for ( int i = 1; i <= 10; i++ ) {
            user.addPlot( new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot( PlotType.P31 , String.valueOf( i ) ) );
        }
        System.out.println( user.getPlots31( ).size( ) );
        user.getStats( ).removeMAX_PLOTS_31( 5 );
        switch ( user.getRank( ) ) {
            case ADMIN:
            case DEV:
                break;
            case BUILDER: {
                if ( plot.getWorldName( ).equals( PlotType.P31.getWorldName( ) ) ) {
                    if ( user.getPlots31( ).size( ) >= (changed ? 15 + user.getStats( ).getMAX_PLOTS_31( ) : 15) ) {
                        
                        //Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P31 ) );
                        
                        System.out.println( PlotType.P31 );
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P101.getWorldName( ) ) ) {
                    if ( user.getPlots101( ).size( ) >= 10 ) {
                        
                        //Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P101 ) );
                        
                        System.out.println( PlotType.P101 );
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P501.getWorldName( ) ) ) {
                    if ( user.getPlots501( ).size( ) >= 3 ) {
                        
                        //Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P501 ) );
                        
                        System.out.println( PlotType.P501 );
                        
                        break;
                    }
                } else if ( plot.getWorldName( ).equals( PlotType.P1001.getWorldName( ) ) ) {
                    if ( user.getPlots1001( ).size( ) >= 1 ) {
                        
                        //Bukkit.getServer( ).getPluginManager( ).callEvent( new PlotCreateFailed( p , PlotType.P1001 ) );
                        System.out.println( PlotType.P1001 );
                        break;
                    }
                }
                break;
            }
            
        }
    }
}