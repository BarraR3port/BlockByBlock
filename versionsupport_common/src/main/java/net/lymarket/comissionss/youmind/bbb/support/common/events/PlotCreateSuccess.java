package net.lymarket.comissionss.youmind.bbb.support.common.events;

import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlotCreateSuccess extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final PlotType plotType;
    private final Player player;
    
    private final User user;
    
    public PlotCreateSuccess(final Player player, final User user, final PlotType plotType){
        this.player = player;
        this.plotType = plotType;
        this.user = user;
    }
    
    public static HandlerList getHandlerList( ){
        return handlers;
    }
    
    public User getUser( ){
        return user;
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
