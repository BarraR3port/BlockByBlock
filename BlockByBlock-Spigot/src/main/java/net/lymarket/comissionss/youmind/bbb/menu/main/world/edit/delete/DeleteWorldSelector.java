package net.lymarket.comissionss.youmind.bbb.menu.main.world.edit.delete;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.menu.MenuSelector;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class DeleteWorldSelector extends MenuSelector {
    
    private final Menu lastMenu;
    
    private final UUID target_uuid;
    
    private final BWorld world;
    
    
    public DeleteWorldSelector(IPlayerMenuUtility playerMenuUtility, UUID world_uuid, Menu lastMenu, UUID target_uuid){
        super(playerMenuUtility);
        this.lastMenu = lastMenu;
        super.ACCEPT = new ItemBuilder(super.ACCEPT.clone())
                .addLoreLine("&7Click para &c&lBORRAR EL MUNDO")
                .build();
        
        this.target_uuid = target_uuid;
        
        world = Main.getInstance().getWorlds().getWorld(world_uuid);
    }
    
    public void setSubMenuItems( ){
    
    }
    
    public void handleSubMenu(InventoryClickEvent e){
    
    }
    
    public Menu getAcceptManu( ){
        return new WorldManagerMenu(playerMenuUtility, target_uuid, 10);
    }
    
    public Menu getDenyManu( ){
        return lastMenu;
    }
    
    public Menu getPrevMenu( ){
        return lastMenu;
    }
    
    public boolean handleAccept( ){
        if (world.getOwner().equals(target_uuid) || Main.getInstance().getPlayers().getPlayer(target_uuid).getRank() == Rank.ADMIN){
            Main.getInstance().getSocket().sendWorldDeleteRequest(getOwner(), world);
            return true;
        }
        return false;
    }
    
    public boolean handleDeny( ){
        return true;
    }
}
