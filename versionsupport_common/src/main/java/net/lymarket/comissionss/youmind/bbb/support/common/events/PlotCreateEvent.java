package net.lymarket.comissionss.youmind.bbb.support.common.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlotCreateEvent extends Event implements Cancellable {
    
    
    private final HandlerList handlers;
    private boolean canceled;
    
    public PlotCreateEvent( ){
        handlers = new HandlerList();
        canceled = false;
    }
    
    public HandlerList getHandlers( ){
        return handlers;
    }
    
    
    /**
     * @return canceled
     */
    @Override
    public boolean isCancelled( ){
        return canceled;
    }
    
    /**
     * @param b canceled
     */
    @Override
    public void setCancelled(boolean b){
        canceled = b;
    }
}
