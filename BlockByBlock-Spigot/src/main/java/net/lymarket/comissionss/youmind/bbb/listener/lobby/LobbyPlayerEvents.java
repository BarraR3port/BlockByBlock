package net.lymarket.comissionss.youmind.bbb.listener.lobby;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.LobbyMsg;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class LobbyPlayerEvents extends MainEvents {
    
    public LobbyPlayerEvents( ){
    }
    
    public void subPlayerQuitEvent( PlayerQuitEvent e ){
    
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport( PlayerTeleportEvent e ){
        try {
            
            final World world = e.getTo( ).getWorld( );
            final UUID playerUUID = e.getPlayer( ).getUniqueId( );
    
            final SpigotUser user = Main.getInstance( ).getPlayers( ).getPlayer( playerUUID );
            final Location loc = e.getTo( );
            user.setLastLocation( new Loc( Settings.SERVER_NAME , world.getName( ) , loc.getX( ) , loc.getY( ) , loc.getZ( ) ) );
            Main.getInstance( ).getPlayers( ).savePlayer( user );
        } catch ( NullPointerException ignored ) {
        }
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
    
    @Override
    public void subPlayerChatEvent( AsyncPlayerChatEvent e ){
        final LobbyMsg msg = new LobbyMsg( e.getPlayer( ).getUniqueId( ) , e.getMessage( ) , Settings.VERSION );
        Main.getInstance( ).getSocket( ).sendMsgFromPlayer( msg );
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakBlocks( BlockBreakEvent e ){
        if ( !Settings.BREAK_BLOCKS ) e.setCancelled( true );
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlaceBlocks( BlockPlaceEvent e ){
        if ( !Settings.PLACE_BLOCKS ) e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntityEvent( PlayerInteractAtEntityEvent e ){
        if ( !Settings.PLAYER_INTERACT_AT_ENTITY ) e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent( PlayerInteractEntityEvent e ){
        if ( !Settings.PLAYER_INTERACT_ENTITY ) e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnterEvent( PlayerBedEnterEvent e ){
        if ( !Settings.PLAYER_BED_EVENTS ) e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFillEvent( PlayerBucketFillEvent e ){
        e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmptyEvent( PlayerBucketEmptyEvent e ){
        e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItemEvent( PlayerDropItemEvent e ){
        if ( !Settings.PLAYER_DROP_ITEMS ) e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItemEvent( EntityPickupItemEvent e ){
        if ( !Settings.PLAYER_PICKUP_ITEMS && e.getEntity( ) instanceof Player ) e.setCancelled( true );
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupArrowEvent( PlayerPickupArrowEvent e ){
        if ( !Settings.PLAYER_PICKUP_ITEMS ) e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsumeEvent( PlayerItemConsumeEvent e ){
        if ( !Settings.CONSUME_ITEMS ) e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortalEvent( PlayerPortalEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamageEvent( PlayerItemDamageEvent e ){
        if ( !Settings.CONSUME_ITEMS ) e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerShearEntityEvent( PlayerShearEntityEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerFishEvent( PlayerFishEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onExplosionPrimeEvent( ExplosionPrimeEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplodeEvent( EntityExplodeEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntityEvent( EntityTargetLivingEntityEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntityEvent( EntityDamageByEntityEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByBlockEvent( EntityDamageByBlockEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityBreakDoorEvent( EntityBreakDoorEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityCreatePortalEvent( EntityCreatePortalEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawnEvent( EntitySpawnEvent e ){
        if ( !(e.getEntity( ) instanceof Player) )
            e.setCancelled( true );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent( PlayerRespawnEvent e ){
        Items.setItems( e.getPlayer( ) );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent( PlayerMoveEvent e ){
        if ( e.getPlayer( ).getLocation( ).getY( ) < 10 ) {
            e.getPlayer( ).teleport( e.getPlayer( ).getWorld( ).getSpawnLocation( ) );
        }
    }
    
    
}
