package net.lymarket.comissionss.youmind.bbb.menu.main.warp;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.WarpType;
import net.lymarket.comissionss.youmind.bbb.common.error.WarpNotFoundError;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.warp.SpigotWarp;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WarpMenu extends UpdatableMenu {
    
    private final String serverName;
    
    public WarpMenu(IPlayerMenuUtility playerMenuUtility, String serverName){
        super(playerMenuUtility);
        this.serverName = serverName;
    }
    
    @Override
    public String getMenuName( ){
        return "Warp Menu";
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void setMenuItems( ){
        inventory.setItem(11, Items.WARP_CASAS_BASE.clone());
        inventory.setItem(13, Items.WARP_ARBOLES_BASE.clone());
        inventory.setItem(15, Items.WARP_VARIOS_BASE.clone());
        inventory.setItem(18, super.CLOSE_ITEM);
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "warp")){
            try {
                final WarpType type = WarpType.valueOf(NBTItem.getTag(item, "warp"));
                final SpigotWarp warp = Main.getInstance().getWarps().getUserWarpByName(type, serverName);
                if (warp.getLocation().getServer().equals(Settings.SERVER_NAME)){
                    getOwner().teleport(warp.getBukkitLocation());
                } else {
                    Main.getInstance().getSocket().sendJoinWarp(getOwner().getUniqueId(), warp);
                }
                /*if (warp.isPublic() || warp.isMember(getOwner().getUniqueId()) || getOwner().hasPermission("blockbyblock.warp.goto") || user.getRank().isBuilder()){
                } else {
                    Main.getLang().sendErrorMsg(getOwner(), "warp.no-permission-to-go");
                }*/
            } catch (IllegalArgumentException error) {
                Main.getLang().sendErrorMsg(getOwner(), "warp.invalid-name");
            } catch (WarpNotFoundError error) {
                super.checkSomething(getOwner(), e.getSlot(), item, "&cWarp No encontrada", "", getMenuUUID());
            }
            
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new WorldManagerMenu(playerMenuUtility, getOwner().getUniqueId()).open();
        }
        
    }
}
