package net.lymarket.comissionss.youmind.bbb.menu.settings;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class Settings extends UpdatableMenu {
    
    private final UUID targetUUID;
    
    public Settings(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.targetUUID = getOwner().getUniqueId();
        
    }
    
    public Settings(IPlayerMenuUtility playerMenuUtility, UUID target){
        super(playerMenuUtility);
        this.targetUUID = target;
    }
    
    @Override
    public String getMenuName( ){
        return targetUUID.equals(getOwner().getUniqueId()) ? "Ajustes" : "Ajustes de " + Main.getInstance().getPlayers().getPlayer(targetUUID).getName();
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void setMenuItems( ){
    
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
    
    }
}
