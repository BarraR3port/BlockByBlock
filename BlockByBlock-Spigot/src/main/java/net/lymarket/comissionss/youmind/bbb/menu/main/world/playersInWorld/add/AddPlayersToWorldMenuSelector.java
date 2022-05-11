package net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.add;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.menu.MenuSelector;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class AddPlayersToWorldMenuSelector extends MenuSelector {
    
    private final Menu lastMenu;
    
    private final UUID world_uuid;
    
    private final UUID target_uuid;
    
    public AddPlayersToWorldMenuSelector( IPlayerMenuUtility playerMenuUtility , UUID world_uuid , Menu lastMenu , UUID target_uuid ){
        super( playerMenuUtility );
        this.lastMenu = lastMenu;
        super.ACCEPT = new ItemBuilder( super.ACCEPT.clone( ) )
                .addLoreLine( "&7Click para añadir a la" )
                .addLoreLine( "&7lista de usuarios del mundo." )
                .build( );
        
        this.world_uuid = world_uuid;
        
        this.target_uuid = target_uuid;
    }
    
    @Override
    public String getMenuName( ){
        return "&7Añadir jugador a mundo";
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
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( this.world_uuid );
        if ( world.getOwner( ).equals( getOwner( ).getUniqueId( ) ) || getOwner( ).hasPermission( "blockbyblock.admin.world.members.add" ) || Main.getInstance( ).getPlayers( ).getPlayer( target_uuid ).getRank( ) == Rank.ADMIN ) {
            world.addMember( target_uuid );
            return Main.getInstance( ).getWorlds( ).saveWorld( world );
        }
        return false;
    }
    
    public boolean handleDeny( ){
        return true;
    }
}
