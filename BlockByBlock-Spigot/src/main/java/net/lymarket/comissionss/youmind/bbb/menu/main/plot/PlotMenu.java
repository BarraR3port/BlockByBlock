package net.lymarket.comissionss.youmind.bbb.menu.main.plot;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlotMenu extends Menu {
    
    private final String serverVersion;
    private final UUID targetUserUUID;
    
    public PlotMenu( IPlayerMenuUtility playerMenuUtility , String serverVersion , UUID targetUserUUID ){
        super( playerMenuUtility );
        this.serverVersion = serverVersion;
        this.targetUserUUID = targetUserUUID;
    }
    
    @Override
    public String getMenuName( ){
        return "Plots y Mundos - &a" + serverVersion;
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void setMenuItems( ){
        
        
        inventory.setItem( 20 , Items.PLOT_101_BASE );
        
        inventory.setItem( 22 , Items.PLOT_501_BASE );
        
        inventory.setItem( 24 , Items.PLOT_1001_BASE );
        
        inventory.setItem( 40 , new ItemBuilder( Items.WORLDS.clone( ) )
                .addLoreLine( "&7Mundos: &a" + Main.getInstance( ).getWorlds( ).getWorldsByUser( targetUserUUID ).size( ) )
                .addLoreLine( "" )
                .build( ) );
        
        inventory.setItem( 45 , super.CLOSE_ITEM );
        
        inventory.setItem( 53 , new ItemBuilder( XMaterial.ENDER_PEARL.parseItem( ) )
                .setDisplayName( "&0Coming soon" )
                .build( ) );
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        
        
        if ( NBTItem.hasTag( item , "world" ) ) {
            new WorldManagerMenu( playerMenuUtility , serverVersion , targetUserUUID ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            new MainMenu( playerMenuUtility ).open( );
        }
        
        
    }
    
    @Override
    public void handleDragEvent( InventoryDragEvent e ){
    
    }
    
    @Override
    public void handleClose( InventoryCloseEvent e ){
    
    }
    
    @Override
    public void handleMove( InventoryMoveItemEvent e ){
    
    }
    
    @Override
    public void handlePickUp( InventoryPickupItemEvent e ){
    
    }
    
    
}
