package net.lymarket.comissionss.youmind.bbb.event;

import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class PrevCreateWorld extends Event implements Cancellable {
    
    private static final HandlerList handlerList = new HandlerList();
    private final UUID user;
    private boolean cancelled = false;
    private BWorld world;
    
    private Material material;
    
    public PrevCreateWorld(UUID user, BWorld world, Material material){
        super(true);
        this.user = user;
        this.world = world;
        this.material = material;
    }
    
    public static HandlerList getHandlerList( ){
        return handlerList;
    }
    
    /**
     * @return
     */
    @Override
    public boolean isCancelled( ){
        return cancelled;
    }
    
    /**
     * @param b
     */
    @Override
    public void setCancelled(boolean b){
        cancelled = b;
    }
    
    /**
     * @return
     */
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
    
    public void setWorld(BWorld world){
        this.world = world;
    }
    
    public Material getMaterial( ){
        return material;
    }
    
    public void setMaterial(Material material){
        this.material = material;
    }
}
