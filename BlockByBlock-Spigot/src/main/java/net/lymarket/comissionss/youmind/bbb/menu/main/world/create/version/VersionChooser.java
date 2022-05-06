package net.lymarket.comissionss.youmind.bbb.menu.main.world.create.version;

import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.create.WorldCreatorMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VersionChooser extends Menu {
    
    private final UUID targetUserUUID;
    
    private final Menu previousMenu;
    
    public VersionChooser( IPlayerMenuUtility playerMenuUtility , UUID targetUserUUID , Menu prevMenu ){
        super( playerMenuUtility );
        this.targetUserUUID = targetUserUUID;
        this.previousMenu = prevMenu;
    }
    
    @Override
    public String getMenuName( ){
        return "Elige una versión para el mundo";
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void setMenuItems( ){
        
        inventory.setItem( 11 , Items.BUILDER_1_12 );
        
        inventory.setItem( 13 , Items.BUILDER_1_16 );
        
        inventory.setItem( 15 , Items.BUILDER_1_18 );
        
        inventory.setItem( 18 , super.CLOSE_ITEM );
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        
        if ( NBTItem.hasTag( item , "server-version" ) ) {
            final String version = NBTItem.getTag( item , "server-version" );
            new WorldCreatorMenu( playerMenuUtility , version , targetUserUUID ).open( );
            
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            previousMenu.open( );
        }
    }
}
