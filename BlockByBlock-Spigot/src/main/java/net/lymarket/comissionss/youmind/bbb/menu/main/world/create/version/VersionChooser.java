package net.lymarket.comissionss.youmind.bbb.menu.main.world.create.version;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.warp.WarpMenu;
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
    
    private final VersionChooseType versionChooseType;
    
    public VersionChooser(IPlayerMenuUtility playerMenuUtility, UUID targetUserUUID, Menu prevMenu, VersionChooseType versionChooseType){
        super(playerMenuUtility);
        this.targetUserUUID = targetUserUUID;
        this.previousMenu = prevMenu;
        this.versionChooseType = versionChooseType;
    }
    
    @Override
    public String getMenuName( ){
        if (versionChooseType.equals(VersionChooseType.WARP_CHOSE)){
            return "Selecciona una versión de warps";
        }
        return "Elige una versión para el mundo";
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void setMenuItems( ){
    
        inventory.setItem(11, Items.BUILDER_1_12);
    
        inventory.setItem(13, Items.BUILDER_1_16);
    
        inventory.setItem(15, Items.BUILDER_1_18);
    
        inventory.setItem(18, super.CLOSE_ITEM);
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        
        if (NBTItem.hasTag(item, "server-version")){
            final String version = NBTItem.getTag(item, "server-version");
            String serverName = "PW-112-1";
            if (version.equals("1.16")){
                serverName = "PW-116-1";
            } else if (version.equals("1.18")){
                serverName = "PW-118-1";
            }
            Main.getInstance().debug("Selected version: " + version);
            if (versionChooseType.equals(VersionChooseType.WARP_CHOSE)){
                new WarpMenu(playerMenuUtility, serverName).open();
                return;
            }
            new WorldCreatorMenu(playerMenuUtility, version, targetUserUUID).open();
    
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            if (previousMenu != null){
                previousMenu.open();
            } else {
                e.getWhoClicked().closeInventory();
            }
        }
    }
    
    public enum VersionChooseType {
        WORLD_CREATION,
        WARP_CHOSE
    }
}
