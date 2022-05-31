package net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.remove;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.menu.MenuSelector;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class RemoveMemberFromWorld extends MenuSelector {
    
    private final Menu lastMenu;
    
    private final UUID world_uuid;
    
    private final UUID target_uuid;
    
    public RemoveMemberFromWorld( IPlayerMenuUtility playerMenuUtility , UUID world_uuid , Menu lastMenu , UUID target_uuid ){
        super( playerMenuUtility );
        this.lastMenu = lastMenu;
        super.ACCEPT = new ItemBuilder( super.ACCEPT.clone( ) )
                .addLoreLine( "&7Click para echar al jugador" )
                .addLoreLine( "&7del los miembros del mundo." )
                .build( );
        
        this.world_uuid = world_uuid;
        
        this.target_uuid = target_uuid;
    }
    
    @Override
    public String getMenuName( ){
        return "Eliminar de los miembros del mundo";
    }
    
    public void setSubMenuItems( ){
    
    }
    
    public void handleSubMenu( InventoryClickEvent e ){
    
    }
    
    public Menu getAcceptManu( ){
        return lastMenu;
    }
    
    public Menu getDenyManu( ){
        return lastMenu;
    }
    
    public Menu getPrevMenu( ){
        return lastMenu;
    }
    
    public boolean handleAccept( ){
        BWorld world = Main.getInstance( ).getWorlds( ).getWorld( this.world_uuid );
        world.removeMember( this.target_uuid );
        Main.getInstance( ).getWorlds( ).saveWorld( world );
        Main.getInstance( ).managePermissions( target_uuid , world.getUUID( ) , true );
        return true;
    }
    
    public boolean handleDeny( ){
        return true;
    }
}