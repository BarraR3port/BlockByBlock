package net.lymarket.comissionss.youmind.bbb.listener;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public abstract class MainEvents implements Listener {
    
    public MainEvents( ){
    }
    
    public abstract void subPlayerQuitEvent(PlayerQuitEvent e);
    
    public abstract void subPlayerJoinEvent(PlayerJoinEvent e);
    
    public abstract void subPlayerChatEvent(AsyncPlayerChatEvent e);
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeft(PlayerQuitEvent e){
        subPlayerQuitEvent(e);
    }
    
    @EventHandler
    public abstract void onPlayerTeleport(PlayerTeleportEvent e);
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e){
        Main.getInstance().getPlayers().getOrCreatePlayer(e.getName(), e.getUniqueId(), String.valueOf(e.getAddress()).replace("/", ""));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        e.setCancelled(true);
        subPlayerChatEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setSaturation(20);
        p.setExp(0);
        p.setLevel(0);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setGameMode(GameMode.CREATIVE);
        p.setInvulnerable(true);
        e.setJoinMessage("");
        subPlayerJoinEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerQuitEvent e){
        e.setQuitMessage("");
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPlaceBlock(BlockPlaceEvent e){
        final UUID uuid = e.getPlayer().getUniqueId();
        final SpigotUser user = Main.getInstance().getPlayers().getPlayer(uuid);
        user.getStats().addBLOCKS_PLACED(1);
        Main.getInstance().getPlayers().savePlayer(user);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerBreakBlock(BlockBreakEvent e){
        final UUID uuid = e.getPlayer().getUniqueId();
        final SpigotUser user = Main.getInstance().getPlayers().getPlayer(uuid);
        user.getStats().addBLOCKS_BROKEN(1);
        Main.getInstance().getPlayers().savePlayer(user);
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntityType() == EntityType.PLAYER){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    
    
    @EventHandler
    public void blockRedstoneEvent(BlockRedstoneEvent e){
        e.getBlock().setType(XMaterial.VOID_AIR.parseMaterial());
    }
    
    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        if (e.getTo().getBlockY() <= -40){
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        }
    }
}
