package net.lymarket.comissionss.youmind.bbb.event;

import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class PlayerLeaveBWorld extends Event implements Cancellable {
    
    private static final HandlerList handlerList = new HandlerList( );
    private final UUID user;
    private boolean cancelled = false;
    private BWorld world;
    
    public PlayerLeaveBWorld( UUID user , BWorld world ){
        super( true );
        this.user = user;
        this.world = world;
    }
    
    public static HandlerList getHandlerList( ){
        return handlerList;
    }
    
    @Override
    public boolean isCancelled( ){
        return cancelled;
    }
    
    
    @Override
    public void setCancelled( boolean b ){
        cancelled = b;
    }
    
    @Override
    public HandlerList getHandlers( ){
        return handlerList;
    }
    
    public UUID getUser( ){
        return user;
    }
    
    public BWorld getWorld( ){
        return world;
    }
    
    public void setWorld( BWorld world ){
        this.world = world;
    }
}
