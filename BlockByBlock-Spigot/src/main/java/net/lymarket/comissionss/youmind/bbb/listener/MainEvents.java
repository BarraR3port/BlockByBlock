package net.lymarket.comissionss.youmind.bbb.listener;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public abstract class MainEvents implements Listener {
    
    public MainEvents( ){
    }
    
    public abstract void subPlayerQuitEvent( PlayerQuitEvent e );
    
    
    public abstract void subPlayerJoinEvent( PlayerJoinEvent e );
    
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeft( PlayerQuitEvent e ){
        subPlayerQuitEvent( e );
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent( AsyncPlayerPreLoginEvent e ){
        Main.getInstance( ).getPlayers( ).getOrCreatePlayer( e.getName( ) , e.getUniqueId( ) , String.valueOf( e.getAddress( ) ).replace( "/" , "" ) );
    }
    
    @EventHandler
    public abstract void onPlayerTeleport( PlayerTeleportEvent e );
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin( PlayerJoinEvent e ){
        final Player p = e.getPlayer( );
        p.setHealth( 20 );
        p.setFoodLevel( 20 );
        p.setFireTicks( 0 );
        p.setSaturation( 20 );
        p.setExp( 0 );
        p.setLevel( 0 );
        p.setAllowFlight( true );
        p.setFlying( true );
        p.setGameMode( GameMode.CREATIVE );
        p.setInvulnerable( true );
        subPlayerJoinEvent( e );
    }
    
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPlaceBlock( BlockPlaceEvent e ){
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        final User user = Main.getInstance( ).getPlayers( ).getPlayer( uuid );
        user.getStats( ).addBLOCKS_PLACED( 1 );
        Main.getInstance( ).getPlayers( ).savePlayer( user );
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerBreakBlock( BlockBreakEvent e ){
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        final User user = Main.getInstance( ).getPlayers( ).getPlayer( uuid );
        user.getStats( ).addBLOCKS_BROKEN( 1 );
        Main.getInstance( ).getPlayers( ).savePlayer( user );
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage( EntityDamageEvent e ){
        if ( e.getEntityType( ) == EntityType.PLAYER ) {
            e.setCancelled( true );
        }
    }
    
    @EventHandler
    public void onPlayerFoodLevelChange( FoodLevelChangeEvent e ){
        e.setCancelled( true );
    }
    
}
