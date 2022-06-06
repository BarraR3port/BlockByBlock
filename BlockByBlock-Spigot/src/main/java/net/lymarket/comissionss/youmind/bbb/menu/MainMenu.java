package net.lymarket.comissionss.youmind.bbb.menu;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ProxyStats;
import net.lymarket.comissionss.youmind.bbb.common.data.user.Stats;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.plot.PlotMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MainMenu extends Menu {
    
    private final UUID targetUserUUID;
    
    public MainMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.targetUserUUID = getOwner().getUniqueId();
    }
    
    public MainMenu(IPlayerMenuUtility playerMenuUtility, UUID targetUserUUID){
        super(playerMenuUtility);
        this.targetUserUUID = targetUserUUID;
    }
    
    @Override
    public String getMenuName( ){
        return "Block By Block Menu";
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "server-version")){
            final String version = NBTItem.getTag(item, "server-version");
            final Player p = (Player) e.getWhoClicked();
            switch(version){
                case "1.12":{
                    int playerVersion = Main.getInstance().getViaVersion().getPlayerVersion(p);
                    if (playerVersion >= 340){
                        new PlotMenu(playerMenuUtility, version, targetUserUUID).open();
                        return;
                    } else {
                        super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                    }
                }
                case "1.16":{
                    int playerVersion = Main.getInstance().getViaVersion().getPlayerVersion(p);
                    if (playerVersion >= 754){
                        new PlotMenu(playerMenuUtility, version, targetUserUUID).open();
                        return;
                    } else {
                        super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                    }
                }
                case "1.18":{
                    int playerVersion = Main.getInstance().getViaVersion().getPlayerVersion(p);
                    if (playerVersion >= 758){
                        new PlotMenu(playerMenuUtility, version, targetUserUUID).open();
                        return;
                    } else {
                        super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                    }
                }
                default:{
                    super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                }
            }
            super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
        } else if (NBTItem.hasTag(item, "world")){
            new WorldManagerMenu(playerMenuUtility, targetUserUUID).open();
        } else if (NBTItem.hasTag(item, "type")){
            Main.getInstance().getSocket().sendJoinServer(targetUserUUID, "lobby");
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            getOwner().closeInventory();
        }
    }
    
    
    @Override
    public void setMenuItems( ){
    
        SpigotUser user = Main.getInstance().getPlayers().getUpdatedPlayer(targetUserUUID);
        if (user == null){
            user = Main.getInstance().getPlayers().getPlayer(getOwner().getName());
            if (user == null){
                getOwner().closeInventory();
                return;
            }
            user.setUUID(getOwner().getUniqueId());
            Main.getInstance().getPlayers().savePlayer(user);
        }
        final Stats userStats = user.getStats();
        final ProxyStats proxyStats = Main.getInstance().getProxyStats();
        inventory.setItem(20, new ItemBuilder(Items.BUILDER_1_12.clone())
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getPlot_1_12_player_size())
                .build());
    
        inventory.setItem(22, new ItemBuilder(Items.BUILDER_1_16.clone())
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getPlot_1_16_player_size())
                .build());
    
        inventory.setItem(24, new ItemBuilder(Items.BUILDER_1_18.clone())
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getPlot_1_18_player_size())
                .build());
    
        inventory.setItem(40, new ItemBuilder(Items.WORLDS.clone())
                .addLoreLine("&7Mundos: &a" + Main.getInstance().getWorlds().getWorldsByUser(targetUserUUID).size())
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + (proxyStats.getWorld_1_12_player_size() + proxyStats.getWorld_1_16_player_size() + proxyStats.getWorld_1_18_player_size()))
                .build());
    
        inventory.setItem(53, new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .setHeadSkin(user.getSkin())
                .setDisplayName("&b&lStats")
                .addLoreLine("")
                .addLoreLine("&aTiempo Jugado: " + userStats.getFormattedTimePlayed())
                .addLoreLine("&aBloques destruidos: " + userStats.getBLOCKS_BROKEN())
                .addLoreLine("&aBloques Colocados: " + userStats.getBLOCKS_PLACED())
                .addLoreLine("&aElo: " + (userStats.getELO() > 0 ? "&a" + userStats.getELO() : "&c" + userStats.getELO()))
                .addLoreLine("&eLocation: " + (user.getLastLocation().getServer()))
                .addLoreLine("&e" + user.getLastLocation().getCurrentServerType().getName() + ":" + (user.getLastLocation().getCurrentServerTypeFormatted()))
                .addTag("stats", "stats")
                .build());
    
        inventory.setItem(26, new ItemBuilder(XMaterial.NETHER_STAR.parseItem())
                .setDisplayName("&bLobby")
                .addTag("type", "lobby")
                .build());
        inventory.setItem(45, super.CLOSE_ITEM);
    
    }
}
