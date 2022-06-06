package net.lymarket.comissionss.youmind.bbb.listener.world;


import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.query.QueryOptions;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.WorldMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.comissionss.youmind.bbb.warp.SpigotWarp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class WorldPlayerEvents extends MainEvents {
    
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        try {
            final UUID uuid = e.getPlayer().getUniqueId();
            final BWorld world = Main.getInstance().getWorlds().getWorld(UUID.fromString(e.getPlayer().getWorld().getName()));
            Main.getInstance().getWorlds().getWorlds().forEach(worlds -> {
                worlds.removeOnlineMember(uuid);
                Main.getInstance().getWorlds().saveWorld(worlds);
            });
            Main.getInstance().managePermissions(uuid, world.getUUID(), true).thenAccept(a -> Main.getInstance().getPlayers().unloadPlayer(uuid));
        } catch (IllegalArgumentException ignored) {
        }
    }
    
    @Override
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        final boolean tpToWorld = Main.getInstance().getWorlds().isPlayerToTP(uuid);
        final boolean tpToHome = Main.getInstance().getHomes().isPlayerToTP(uuid);
        final boolean isWarpWorld = p.getLocation().getWorld().getName().equalsIgnoreCase("warp");
        final BWorld visitWorld = Main.getInstance().getWorlds().getWorldByVisitor(uuid);
        Main.getInstance().debug("Join World Type: ");
        if (visitWorld != null){
            Main.getInstance().debug("Visit World\n");
            Main.getInstance().debug("Player " + p.getName() + " is in visit world " + visitWorld.getName());
            Location loc = Bukkit.getWorld(visitWorld.getUUID().toString()).getSpawnLocation();
            final Player worldOwner = Bukkit.getPlayer(visitWorld.getOwner());
            if (worldOwner != null){
                loc = worldOwner.getLocation();
            }
            visitWorld.addOnlineMember(uuid);
            final Player target = Bukkit.getPlayer(visitWorld.getVisitor(uuid).getTarget_uuid());
            visitWorld.removeVisitor(uuid);
            Main.getInstance().getWorlds().saveWorld(visitWorld);
            if (target != null){
                final HashMap < String, String > replace = new HashMap <>();
                replace.put("player", target.getName());
                replace.put("world", visitWorld.getName().split("-")[0]);
                Main.getLang().sendMsg(target, "world.visit-join-to-owner", replace);
            }
            Location finalLoc = loc;
            Main.getInstance().manageVisitorPermissions(uuid, visitWorld.getUUID(), false).thenAccept(a -> {
                Main.getInstance().getWorlds().removeGuestFromVisitWorldList(uuid);
                Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                    if (target.teleport(finalLoc, PlayerTeleportEvent.TeleportCause.PLUGIN)){
                        Main.getLang().sendMsg(target, "world.visit-join-to-visitor", "player", target != null ? target.getName().split("-")[0] : "&atu amigo");
                    }
                });
            });
            
            
        } else if (tpToWorld){
            Main.getInstance().debug("Tp To World\n");
            final BWorld world = Main.getInstance().getWorlds().getPlayerToTP(uuid);
            Location loc = Bukkit.getWorld(world.getUUID().toString()).getSpawnLocation();
            world.addOnlineMember(uuid);
            Main.getInstance().getWorlds().saveWorld(world);
            Main.getInstance().managePermissions(uuid, world.getUUID(), false).thenAccept(a ->
                    Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                        if (p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN)){
                            System.out.println("TELEPORTINGGGG to " + loc.getWorld().getName());
                            Main.getInstance().getWorlds().removePlayerToTP(uuid);
                        } else {
                            System.out.println("Not Teleporting");
                        }
                    }));
            
        } else if (tpToHome){
            Main.getInstance().debug("Tp To Home\n");
            final SpigotHome home = Main.getInstance().getHomes().getPlayerToTP(uuid);
            final Loc homeLoc = home.getLocation();
            final BWorld world = Main.getInstance().getWorlds().getWorld(homeLoc.getBWorld());
            world.addOnlineMember(uuid);
            Main.getInstance().getWorlds().saveWorld(world);
            if (p.teleport(home.getBukkitLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)){
                Main.getInstance().getHomes().removePlayerToTP(uuid);
                Main.getInstance().managePermissions(uuid, world.getUUID(), false);
            }
        } else if (isWarpWorld){
            Main.getInstance().debug("Tp To Warps\n");
            final SpigotWarp warp = Main.getInstance().getWarps().getPlayerToTP(uuid);
            if (p.teleport(warp.getBukkitLocation())){
                Main.getLang().sendMsg(p, "warps.tp-to-warp", "warp", warp.getType().getName());
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBreaksBlock(BlockBreakEvent e){
        final boolean isWarpWorld = e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("warp");
        if (isWarpWorld){
            return;
        }
        final UUID playerUUID = e.getPlayer().getUniqueId();
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(e.getPlayer().getWorld().getName());
        } catch (IllegalArgumentException ex) {
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[BlockBreakEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby");
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            e.setCancelled(true);
            return;
        }
        final BWorld world = Main.getInstance().getWorlds().getWorld(worldUUID);
        final Player player = e.getPlayer();
        if (world == null){
            Main.getInstance().getSocket().sendKickFromWorld(playerUUID, e.getPlayer().getWorld().getName(), Settings.SERVER_NAME, playerUUID);
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            e.setCancelled(true);
            return;
        }
        
        if (!world.getMembers().contains(playerUUID) && !world.getOwner().equals(playerUUID) && !player.hasPermission("blockbyblock.admin.block.place.other")){
            
            if (Settings.DEBUG) return;
            e.setCancelled(true);
            CachedPermissionData cachedPermissionData = Objects.requireNonNull(Main.getLuckPerms().getUserManager().getUser(playerUUID)).getCachedData().getPermissionData(QueryOptions.contextual(MutableContextSet.of("world", world.getUUID().toString())));
            if (!cachedPermissionData.checkPermission("blockbyblock.visit").asBoolean()){
                world.removeOnlineMember(playerUUID);
                Main.getInstance().getWorlds().saveWorld(world);
                Main.getInstance().debug("Player " + player.getName() + " is in world " + world.getName().split("-")[0] + " and is not owner and not allowed to place blocks");
                Main.getInstance().getSocket().sendKickFromWorld(world.getOwner(), world, playerUUID);
                Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            }
        }
        
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPlaceBlocks(BlockPlaceEvent e){
        final boolean isWarpWorld = e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("warp");
        if (isWarpWorld){
            return;
        }
        final UUID playerUUID = e.getPlayer().getUniqueId();
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(e.getPlayer().getWorld().getName());
        } catch (IllegalArgumentException ex) {
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[BlockPlaceEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby");
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            e.setCancelled(true);
            return;
        }
        final BWorld world = Main.getInstance().getWorlds().getWorld(worldUUID);
        final Player player = e.getPlayer();
        if (world == null){
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[BlockPlaceEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendKickFromWorld(playerUUID, e.getPlayer().getWorld().getName(), Settings.SERVER_NAME, playerUUID);
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            e.setCancelled(true);
            return;
        }
        
        if (!world.getMembers().contains(playerUUID) && !world.getOwner().equals(playerUUID) && !player.hasPermission("blockbyblock.admin.block.place.other")){
            if (Settings.DEBUG) return;
            e.setCancelled(true);
            CachedPermissionData cachedPermissionData = Objects.requireNonNull(Main.getLuckPerms().getUserManager().getUser(playerUUID)).getCachedData().getPermissionData(QueryOptions.contextual(MutableContextSet.of("world", world.getUUID().toString())));
            if (!cachedPermissionData.checkPermission("blockbyblock.visit").asBoolean()){
                world.removeOnlineMember(playerUUID);
                Main.getInstance().getWorlds().saveWorld(world);
                Main.getInstance().debug("Player " + player.getName() + " is in world " + world.getName().split("-")[0] + " and is not owner and not allowed to place blocks");
                Main.getInstance().getSocket().sendKickFromWorld(world.getOwner(), world, playerUUID);
                Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        for ( Player others : Bukkit.getOnlinePlayers() ){
            others.showPlayer(Main.getInstance(), p);
            p.showPlayer(Main.getInstance(), others);
        }
        final boolean isWarpWorld = e.getTo().getWorld().getName().equalsIgnoreCase("warp");
        if (isWarpWorld){
            return;
        }
        final UUID playerUUID = e.getPlayer().getUniqueId();
        UUID worldUUID;
    
        try {
            worldUUID = UUID.fromString(e.getPlayer().getWorld().getName());
        } catch (IllegalArgumentException ex) {
            if (e.getFrom().getWorld().equals(e.getTo().getWorld())){
                Main.getInstance().debug("[PlayerTeleportEvent] Player " + playerUUID + " is in the Default World.");
                if (Settings.DEBUG) return;
                Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby");
                Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
                e.setCancelled(true);
            }
            return;
        }
        final SpigotUser user = Main.getInstance().getPlayers().getPlayer(playerUUID);
        if (!e.getFrom().getWorld().equals(e.getTo().getWorld())){
            
            
            final BWorld prevWorld = Main.getInstance().getWorlds().getWorld(UUID.fromString(e.getFrom().getWorld().getName()));
            final BWorld postWorld = Main.getInstance().getWorlds().getWorld(UUID.fromString(e.getTo().getWorld().getName()));
            
            if (prevWorld == null || postWorld == null){
                e.setCancelled(true);
                Main.getInstance().debug("[PlayerTeleportEvent] Player " + playerUUID + " is in the Default World.");
                if (Settings.DEBUG) return;
                Main.getInstance().getSocket().sendKickFromWorld(playerUUID, e.getPlayer().getWorld().getName(), Settings.SERVER_NAME, playerUUID);
                Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
                return;
            }
            
            final BWorld visitWorld = Main.getInstance().getWorlds().getWorldByVisitor(playerUUID);
            
            if (visitWorld != null){
                visitWorld.removeVisitor(playerUUID);
                Main.getInstance().getWorlds().saveWorld(visitWorld);
                Main.getInstance().getWorlds().removeGuestFromVisitWorldList(playerUUID);
                /*if ( p != null ) {
                    final HashMap < String, String > replace = new HashMap <>( );
                    replace.put( "player" , e.getPlayer( ).getName( ) );
                    replace.put( "world" , visitWorld.getName( ).split( "-" )[0] );
                    Main.getLang( ).sendMsg( p , "world.visit-join-to-owner" , replace );
                }*/
                Main.getInstance().debug("visitWorld != null");
                Main.getInstance().debug("removeGuestFromVisitWorldList");
                Main.getInstance().managePermissions(playerUUID, prevWorld.getUUID(), true).thenAccept((success) -> Main.getInstance().manageVisitorPermissions(playerUUID, visitWorld.getUUID(), false).thenAccept(a -> Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> Main.getLang().sendMsg(e.getPlayer(), "world.visit-join-to-visitor", "player", /*p != null ? p.getName( ) :*/ "&atu amigo"))));
            }
            
            Main.getInstance().debug("Online Members: " + postWorld.getOnlineMembers());
            CachedPermissionData cachedPermissionData = Objects.requireNonNull(Main.getLuckPerms().getUserManager().getUser(playerUUID)).getCachedData().getPermissionData(QueryOptions.contextual(MutableContextSet.of("world", e.getTo().getWorld().getName())));
            Main.getInstance().debug("Has blockbyblock.visit permission: " + cachedPermissionData.checkPermission("blockbyblock.visit").asBoolean());
            if (postWorld.getMembers().contains(playerUUID) || postWorld.getOwner().equals(playerUUID) /*|| postWorld.getOnlineMembers( ).contains( playerUUID )*/ || user.getRank().isAdmin()){
                Main.getInstance().debug("Is allowedd");
                Main.getInstance().managePermissions(playerUUID, worldUUID, false);
            } else {
                if (!cachedPermissionData.checkPermission("blockbyblock.visit").asBoolean()){
                    if (Settings.DEBUG) return;
                    e.setCancelled(true);
                    postWorld.removeOnlineMember(playerUUID);
                    Main.getInstance().getWorlds().saveWorld(postWorld);
                    Main.getInstance().debug("[PlayerTeleportEvent] Player " + playerUUID + " is in world " + postWorld.getName() + " and is not owner and not allowed to be in this world");
                    Main.getInstance().getSocket().sendKickFromWorld(postWorld.getOwner(), postWorld, playerUUID);
                    Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
                }
            }
        }
        
        final Location loc = e.getTo();
        user.setLastLocation(new Loc(Settings.SERVER_NAME, worldUUID.toString(), loc.getX(), loc.getY(), loc.getZ(), worldUUID));
        Main.getInstance().getPlayers().savePlayer(user);
        
    }
    
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e){
        final boolean isWarpWorld = e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("warp");
        if (isWarpWorld){
            return;
        }
        final UUID playerUUID = e.getPlayer().getUniqueId();
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(e.getPlayer().getWorld().getName());
        } catch (IllegalArgumentException ex) {
            
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[PlayerChangedWorldEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby");
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            return;
        }
        
        final BWorld postWorld = Main.getInstance().getWorlds().getWorld(UUID.fromString(e.getPlayer().getWorld().getName()));
        
        if (postWorld == null){
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[PlayerChangedWorldEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendKickFromWorld(playerUUID, e.getPlayer().getWorld().getName(), Settings.SERVER_NAME, playerUUID);
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            return;
        }
        if (postWorld.getMembers().contains(playerUUID) /*|| postWorld.getOnlineMembers( ).contains( playerUUID )*/ || postWorld.getOwner().equals(playerUUID)){
            Main.getInstance().managePermissions(playerUUID, worldUUID, false);
            Main.getInstance().getWorlds().addPlayerToWorldOnlineMembers(playerUUID, postWorld);
        } else {
            CachedPermissionData cachedPermissionData = Objects.requireNonNull(Main.getLuckPerms().getUserManager().getUser(playerUUID)).getCachedData().getPermissionData(QueryOptions.contextual(MutableContextSet.of("world", postWorld.getUUID().toString())));
            if (!cachedPermissionData.checkPermission("blockbyblock.visit").asBoolean()){
                if (Settings.DEBUG) return;
                postWorld.removeOnlineMember(playerUUID);
                Main.getInstance().getWorlds().saveWorld(postWorld);
                Main.getInstance().debug("[PlayerChangedWorldEvent] Player " + playerUUID + " is in world " + postWorld.getName() + " and is not owner and not allowed to be in this world");
                Main.getInstance().getSocket().sendKickFromWorld(postWorld.getOwner(), postWorld, playerUUID);
                Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            }
        }
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
        final boolean isWarpWorld = e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("warp");
        if (isWarpWorld){
            return;
        }
        final UUID playerUUID = e.getPlayer().getUniqueId();
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(e.getPlayer().getWorld().getName());
        } catch (IllegalArgumentException ex) {
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[PlayerCommandPreprocessEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby");
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            e.setCancelled(true);
            return;
        }
        final BWorld world = Main.getInstance().getWorlds().getWorld(worldUUID);
        
        if (world == null){
            // kick the player with the bukkit api
            if (Settings.DEBUG) return;
            Main.getInstance().debug("[PlayerCommandPreprocessEvent] Player " + playerUUID + " is in the Default World.");
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby", "&cMundo no encontrado");
            e.setCancelled(true);
            return;
        }
        String command = e.getMessage();
        if ((command.startsWith("//calc") || command.startsWith("//eval") || command.startsWith("//solve") || command.startsWith("//evaluate ") && !(Main.getInstance().getPlayers().getPlayer(playerUUID).getRank().isAdmin()))){
            e.setCancelled(true);
            Main.getInstance().getSocket().sendKickFromWorld(world.getOwner(), world, playerUUID);
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-use-commands-in-this-world");
        }
        if (!world.getMembers().contains(playerUUID) && !world.getOwner().equals(playerUUID)){
            CachedPermissionData cachedPermissionData = Objects.requireNonNull(Main.getLuckPerms().getUserManager().getUser(playerUUID)).getCachedData().getPermissionData(QueryOptions.contextual(MutableContextSet.of("world", world.getUUID().toString())));
            if (!cachedPermissionData.checkPermission("blockbyblock.visit").asBoolean()){
                if (Settings.DEBUG) return;
                e.setCancelled(true);
                world.removeOnlineMember(playerUUID);
                Main.getInstance().getWorlds().saveWorld(world);
                Main.getInstance().debug("[PlayerCommandPreprocessEvent] Player " + playerUUID + " is in world " + world.getName().split("-")[0] + " and is not owner and not allowed to be in this world");
                Main.getInstance().getSocket().sendKickFromWorld(world.getOwner(), world, playerUUID);
                Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            }
        }
    }
    
    @Override
    public void subPlayerChatEvent(AsyncPlayerChatEvent e){
        final boolean isWarpWorld = e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("warp");
        if (isWarpWorld){
            final BWorld warpWorld = new BWorld(UUID.randomUUID(), "Warps", Settings.SERVER_NAME, Settings.VERSION, "GRASS_BLOCK");
            final WorldMsg msg = new WorldMsg(e.getPlayer().getUniqueId(), e.getMessage(), Settings.VERSION, warpWorld);
            Main.getInstance().getSocket().sendMsgFromPlayer(msg);
            return;
        }
        final UUID playerUUID = e.getPlayer().getUniqueId();
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(e.getPlayer().getWorld().getName());
        } catch (IllegalArgumentException ex) {
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby");
            Main.getInstance().getSocket().sendMSGToPlayer(playerUUID, "error.world.not-allowed-to-join-world");
            e.setCancelled(true);
            return;
        }
        final BWorld world = Main.getInstance().getWorlds().getWorld(worldUUID);
        
        if (world == null){
            if (Settings.DEBUG) return;
            // kick the player with the bukkit api
            Main.getInstance().getSocket().sendJoinServer(playerUUID, "lobby", "&cMundo no encontrado");
            e.setCancelled(true);
            return;
        }
        final WorldMsg msg = new WorldMsg(e.getPlayer().getUniqueId(), e.getMessage(), Settings.VERSION, world);
        Main.getInstance().getSocket().sendMsgFromPlayer(msg);
        
        
    }
    
    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent e){
        switch(e.getEntityType()){
            case PLAYER:
            case FISHING_HOOK:
            case DROPPED_ITEM:
            case MINECART_MOB_SPAWNER:
            case MINECART_FURNACE:
            case MINECART_HOPPER:
            case MINECART_CHEST:
            case MINECART:
            case BOAT:
            case ARMOR_STAND:
            case WITHER_SKULL:
            case ITEM_FRAME:
            case THROWN_EXP_BOTTLE:
            case ARROW:
            case AREA_EFFECT_CLOUD:
            case EXPERIENCE_ORB:
                return;
            default:
                e.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent e){
        if (!(e.getTarget() instanceof Player)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityInteractEvent(EntityInteractEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent e){
        e.setCancelled(true);
    }
    
    
    @EventHandler
    public void onEntityPortalExitEvent(EntityPortalExitEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityTameEvent(EntityDamageEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityTargetEvent(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    
}
