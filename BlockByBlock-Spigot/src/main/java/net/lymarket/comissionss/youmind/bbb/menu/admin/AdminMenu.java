package net.lymarket.comissionss.youmind.bbb.menu.admin;

import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import org.bukkit.event.inventory.*;

public class AdminMenu extends Menu {
    
    private final User target;
    
    public AdminMenu( IPlayerMenuUtility playerMenuUtility , User target ){
        super( playerMenuUtility );
        this.target = target;
    }
    
    public String getMenuName( ){
        return "Administrar a: " + target.getName( );
    }
    
    public int getSlots( ){
        return 54;
    }
    
    public void setMenuItems( ){
    
    }
    
    public void handleMenu( InventoryClickEvent e ){
    
    }
    
    public void handleDragEvent( InventoryDragEvent e ){
    
    }
    
    public void handleClose( InventoryCloseEvent e ){
    
    }
    
    public void handleMove( InventoryMoveItemEvent e ){
    
    }
    
    public void handlePickUp( InventoryPickupItemEvent e ){
    
    }
    
}
