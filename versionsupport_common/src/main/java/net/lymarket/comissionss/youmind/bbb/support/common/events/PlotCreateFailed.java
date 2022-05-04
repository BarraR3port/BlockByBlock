package net.lymarket.comissionss.youmind.bbb.support.common.events;

import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlotCreateFailed extends Event {
    
    private static final HandlerList handlers = new HandlerList( );
    
    private final PlotType plotType;
    private final Player player;
    
    public PlotCreateFailed( final Player player , final PlotType plotType ){
        this.player = player;
        this.plotType = plotType;
    }
    
    public static HandlerList getHandlerList( ){
        return handlers;
    }
    
    public Player getPlayer( ){
        return player;
    }
    
    public PlotType getPlotType( ){
        return plotType;
    }
    
    @Override
    public HandlerList getHandlers( ){
        return handlers;
    }
}
