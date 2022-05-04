package net.lymarket.comissionss.youmind.bbb.listener.lobby;

import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class LobbyPlayerEvents extends MainEvents {
    
    public void subPlayerQuitEvent( PlayerQuitEvent e ){
    
    }
    
    public void subPlayerJoinEvent( PlayerJoinEvent e ){
        Player p = e.getPlayer( );
        try {
            p.teleport( Settings.SPAWN_LOCATION );
        } catch ( NullPointerException | IllegalArgumentException ex ) {
            p.teleport( p.getWorld( ).getSpawnLocation( ) );
        }
        Items.setItems( p );
        p.setHealth( 20 );
        p.setSaturation( 20F );
        p.setGameMode( GameMode.ADVENTURE );
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClicks( PlayerInteractEvent e ){
        ItemStack item = e.getItem( );
        
        if ( NBTItem.hasTag( item , "lobby-item" ) ) {
            new MainMenu( LyApi.getPlayerMenuUtility( e.getPlayer( ) ) ).open( );
        }
        if ( NBTItem.hasTag( item , "lobby-book" ) ) {
            return;
        }
        e.setCancelled( true );
        
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakBlocks( BlockBreakEvent e ){
        if ( !Settings.BREAK_BLOCKS ) e.setCancelled( true );
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlaceBlocks( BlockPlaceEvent e ){
        if ( !Settings.PLACE_BLOCKS ) e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerInteractAtEntityEvent( PlayerInteractAtEntityEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.PLAYER_INTERACT_AT_ENTITY ) e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerInteractEntityEvent( PlayerInteractEntityEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.PLAYER_INTERACT_ENTITY ) e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerBedEnterEvent( PlayerBedEnterEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.PLAYER_BED_EVENTS ) e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerBucketFillEvent( PlayerBucketFillEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerBucketEmptyEvent( PlayerBucketEmptyEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerDropItemEvent( PlayerDropItemEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.PLAYER_DROP_ITEMS ) e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerPickupItemEvent( EntityPickupItemEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.PLAYER_PICKUP_ITEMS && e.getEntity( ) instanceof Player ) e.setCancelled( true );
        
    }
    
    @EventHandler
    public void onPlayerPickupArrowEvent( PlayerPickupArrowEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.PLAYER_PICKUP_ITEMS ) e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerItemConsumeEvent( PlayerItemConsumeEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.CONSUME_ITEMS ) e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerPortalEvent( PlayerPortalEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerItemDamageEvent( PlayerItemDamageEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !Settings.CONSUME_ITEMS ) e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerShearEntityEvent( PlayerShearEntityEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerFishEvent( PlayerFishEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onExplosionPrimeEvent( ExplosionPrimeEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityExplodeEvent( EntityExplodeEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityTargetLivingEntityEvent( EntityTargetLivingEntityEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityDamageByEntityEvent( EntityDamageByEntityEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityDamageByBlockEvent( EntityDamageByBlockEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityBreakDoorEvent( EntityBreakDoorEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityCreatePortalEvent( EntityCreatePortalEvent e ){
        if ( e.isCancelled( ) ) return;
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntitySpawnEvent( EntitySpawnEvent e ){
        if ( e.isCancelled( ) ) return;
        if ( !(e.getEntity( ) instanceof Player) )
            e.setCancelled( true );
    }
    
    @EventHandler
    public void onPlayerRespawnEvent( PlayerRespawnEvent e ){
        Items.setItems( e.getPlayer( ) );
    }
    
    @EventHandler
    public void onPlayerRespawnEvent( PlayerMoveEvent e ){
        if ( e.isCancelled( ) ) return;
        
        if ( e.getPlayer( ).getLocation( ).getY( ) < 10 ) {
            e.getPlayer( ).teleport( e.getPlayer( ).getWorld( ).getSpawnLocation( ) );
        }
    }
    
    
}
