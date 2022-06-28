package net.lymarket.comissionss.youmind.bbb.menu.main.world;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.warp.WarpMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.create.version.VersionChooser;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.PlayersInWorldMenu;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class MemberWorlds extends UpdatableMenu {
    
    private final UUID targetUserUUID;
    private final UUID ownerUUID;
    
    public MemberWorlds(IPlayerMenuUtility playerMenuUtility, UUID targetUserUUID){
        super(playerMenuUtility);
        this.targetUserUUID = targetUserUUID;
        this.ownerUUID = getOwner().getUniqueId();
    }
    
    @Override
    public String getMenuName( ){
        return "Mundos en los que eres miembro.";
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        final Player p = (Player) e.getWhoClicked();
        if (NBTItem.hasTag(item, "type")){
            if (Settings.SERVER_TYPE.equals(ServerType.WORLDS)){
                new WarpMenu(playerMenuUtility, Settings.SERVER_NAME).open();
                return;
            }
            new VersionChooser(playerMenuUtility, targetUserUUID, this, VersionChooser.VersionChooseType.WARP_CHOSE).open();
            
        } else if (NBTItem.hasTag(item, "world-uuid")){
            final String server_target = NBTItem.getTag(item, "world-server");
            final UUID world_uuid = UUID.fromString(NBTItem.getTag(item, "world-uuid"));
            final BWorld world = Main.getInstance().getWorlds().getWorld(world_uuid);
            final String version = world.getVersion();
            //int playerVersion = Main.getInstance().getViaVersion().getPlayerVersion(p);
            if (e.getClick().equals(ClickType.LEFT)){
                switch(version){
                    case "1.12":{
                        if (Main.getInstance().getProxyStats().isWorld_1_12_online()){
                            //if (playerVersion >= 340){
                            Main.getInstance().getSocket().sendJoinWorldRequest(ownerUUID, server_target, world_uuid, e.getSlot());
                            return;
                            /*} else {
                                super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                            }*/
                        } else {
                            super.checkSomething(p, e.getSlot(), item, "&cServer Offline.", "", this.getMenuUUID());
                        }
                        
                    }
                    case "1.16":{
                        if (Main.getInstance().getProxyStats().isWorld_1_16_online()){
                            //if (playerVersion >= 754){
                            Main.getInstance().getSocket().sendJoinWorldRequest(ownerUUID, server_target, world_uuid, e.getSlot());
                            return;
                            /*} else {
                                super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                            }*/
                        } else {
                            super.checkSomething(p, e.getSlot(), item, "&cServer Offline.", "", this.getMenuUUID());
                        }
                    }
                    case "1.18":{
                        if (Main.getInstance().getProxyStats().isWorld_1_18_online()){
                            //if (playerVersion >= 758){
                            Main.getInstance().getSocket().sendJoinWorldRequest(ownerUUID, server_target, world_uuid, e.getSlot());
                            return;
                            /*} else {
                                super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                            }*/
                        } else {
                            super.checkSomething(p, e.getSlot(), item, "&cServer Offline.", "", this.getMenuUUID());
                        }
                    }
                    default:{
                        super.checkSomething(p, e.getSlot(), item, "&cVersión incompatible con tu cliente.", "", this.getMenuUUID());
                    }
                }
            } else if (e.getClick().equals(ClickType.RIGHT)){
                new PlayersInWorldMenu(playerMenuUtility, world_uuid, false, this).open();
            }
        } else if (NBTItem.hasTag(item, "world-available") && p.getUniqueId().equals(targetUserUUID)){
            super.checkSomething(p, e.getSlot(), item, "&cNo puedes crear un mundo desde aquí.", "", this.getMenuUUID());
            
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new MainMenu(playerMenuUtility, targetUserUUID).open();
        }
    }
    
    @Override
    public void setMenuItems( ){
        
        int available = 5;
        int start = 11;
        
        for ( int i = start; i < (start + available); i++ ){
            inventory.setItem(i, Items.AVAILABLE_WORLD);
        }
        
        final ArrayList < BWorld > worlds = Main.getInstance().getWorlds().getWorldWherePlayerIsMember(targetUserUUID);
        
        final ArrayList < ItemStack > finalWorlds = new ArrayList <>();
        
        for ( BWorld world : worlds ){
            boolean serverStatus = false;
            switch(world.getVersion()){
                case "1.12":{
                    serverStatus = Main.getInstance().getProxyStats().isWorld_1_12_online();
                    break;
                }
                case "1.16":{
                    serverStatus = Main.getInstance().getProxyStats().isWorld_1_16_online();
                    break;
                }
                case "1.18":{
                    serverStatus = Main.getInstance().getProxyStats().isWorld_1_18_online();
                    break;
                }
            }
            finalWorlds.add(new ItemBuilder(Items.CREATED_WORLD_BASE.clone())
                    .setDisplayName("&bMundo: " + world.getName().split("-")[0])
                    .addLoreLine("&7Click para ir al mundo.")
                    .addLoreLine(" ")
                    .addLoreLine("&7Eres miembro de este mundo, por lo que")
                    .addLoreLine("&7puedes entrar a y editarlo.")
                    .addLoreLine(" ")
                    .addLoreLine("&bInfo:")
                    .addLoreLine(" &b> &7Versión: &a" + world.getVersion())
                    .addLoreLine(" &b> &7Server: &a" + world.getServer())
                    .addLoreLine(" &b> &7Usuarios dentro: &a" + world.getOnlineMembers().size())
                    .addLoreLine(" &b> &7ID: &a" + world.getUUID().toString().split("-")[0])
                    .addLoreLine(" &b> &7Estado del server: &a" + (serverStatus ? "&aACTIVO" : "&cCERRADO"))
                    .addLoreLine(" &b> &7Dueño: &a" + Main.getInstance().getPlayers().getPlayer(world.getOwner()).getName())
                    .addLoreLine("")
                    .addLoreLine("&7Click &eizquierdo &7para entrar al mundo.")
                    .addLoreLine("")
                    .addLoreLine(world.getMembers().contains(ownerUUID) ? "&7Click &ederecho &7para ver la lista de miembros online" : null)
                    .addTag("world-uuid", world.getUUID().toString())
                    .addTag("world-server", world.getServer())
                    .build()
            );
        }
        
        
        for ( int i = 0; i < finalWorlds.size(); i++ ){
            try {
                inventory.setItem(11 + i, finalWorlds.get(i));
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        
        inventory.setItem(18, super.CLOSE_ITEM);
        inventory.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem())
                .setDisplayName("&bWarps")
                .addTag("type", "warps")
                .build());
    }
    
    
    @Override
    public void checkSomething(Player p, int slot, ItemStack item, String name, String lore, UUID menu_uuid){
        
        if (isOnSchedule) return;
        
        isOnSchedule = true;
        
        p.getOpenInventory().getTopInventory().setItem(slot, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=")
                .addLoreLine(lore)
                .setDisplayName(name)
                .build());
        setOnSchedule(p, slot, item, menu_uuid);
        inventory.setItem(18, super.CLOSE_ITEM.clone());
        
        inventory.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem())
                .setDisplayName("&bWarps")
                .addTag("type", "warps")
                .build());
        
    }
    
}
