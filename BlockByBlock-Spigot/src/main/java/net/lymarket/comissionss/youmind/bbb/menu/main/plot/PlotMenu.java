package net.lymarket.comissionss.youmind.bbb.menu.main.plot;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.Stats;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.warp.WarpMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlotMenu extends Menu {
    
    private final String serverVersion;
    private final User user;
    
    public PlotMenu(IPlayerMenuUtility playerMenuUtility, String serverVersion, UUID targetUserUUID){
        super(playerMenuUtility);
        this.serverVersion = serverVersion;
        this.user = Main.getInstance().getPlayers().getUpdatedPlayer(targetUserUUID);
    }
    
    @Override
    public String getMenuName( ){
        return "Plots - &a" + serverVersion;
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void setMenuItems( ){
        final Stats stats = user.getStats();
    
        inventory.setItem(19, Items.PLOT_31_BASE);
    
        inventory.setItem(21, Items.PLOT_101_BASE);
    
        inventory.setItem(23, Items.PLOT_501_BASE);
    
        if (user.getRank().equals(Rank.VISITOR)){
            inventory.setItem(25, new ItemBuilder(Items.PLOT_1001_BASE.clone()).addLoreLine("&cNO TIENES RANGO PARA UNIRTE").build());
        } else {
            inventory.setItem(25, Items.PLOT_1001_BASE);
        }
    
        inventory.setItem(40, new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .setHeadSkin(user.getSkin())
                .setDisplayName("&b&lStats")
                .addLoreLine("")
                .addLoreLine("&aTiempo Jugado: " + stats.getFormattedTimePlayed())
                .addLoreLine("&aBloques destruidos: " + stats.getBLOCKS_BROKEN())
                .addLoreLine("&aBloques Colocados: " + stats.getBLOCKS_PLACED())
                .addLoreLine("&aElo: " + (stats.getELO() > 0 ? "&a" + stats.getELO() : "&c" + stats.getELO()))
                .addTag("stats", "stats")
                .build());
    
        inventory.setItem(45, super.CLOSE_ITEM);
    
        inventory.setItem(53, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem())
                .setDisplayName("&bWarps")
                .addTag("type", "warps")
                .build());
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        
        if (NBTItem.hasTag(item, "type")){
            new WarpMenu(playerMenuUtility, serverVersion).open();
        } else if (NBTItem.hasTag(item, "plot-type")){
            final PlotType plotType = PlotType.valueOf(NBTItem.getTag(item, "plot-type"));
            if (plotType.equals(PlotType.P1001)){
                if (user.getRank().equals(Rank.VISITOR)){
                    Main.getLang().sendErrorMsg(e.getWhoClicked(), "plot.not-allowed-to-join-1001");
                    return;
                }
            }
            Main.getInstance().getSocket().sendJoinPlotRequest(getOwner().getUniqueId(), serverVersion, null, plotType, e.getSlot());
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new MainMenu(playerMenuUtility).open();
        }
    }
    
}
