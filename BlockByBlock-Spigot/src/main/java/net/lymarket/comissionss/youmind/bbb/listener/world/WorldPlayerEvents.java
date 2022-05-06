package net.lymarket.comissionss.youmind.bbb.listener.world;


import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.UUID;

public final class WorldPlayerEvents extends MainEvents {
    
    
    public void subPlayerQuitEvent( PlayerQuitEvent e ){
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) ) );
        Main.getInstance( ).getWorlds( ).getWorlds( ).forEach( worlds -> {
            worlds.removeOnlineMember( uuid );
            Main.getInstance( ).getWorlds( ).saveWorld( worlds );
        } );
        Main.getInstance( ).managePermissions( uuid , world.getUUID( ) , true ).thenAccept( a -> Main.getInstance( ).getPlayers( ).unloadPlayer( uuid ) );
        
        
    }
    
    @Override
    public void subPlayerJoinEvent( PlayerJoinEvent e ){
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        final boolean tp = Main.getInstance( ).getWorlds( ).isPlayerToTP( uuid );
        if ( tp ) {
            final BWorld world = Main.getInstance( ).getWorlds( ).getPlayerToTP( uuid );
            final Location loc = Bukkit.getWorld( world.getUUID( ).toString( ) ).getSpawnLocation( );
            world.addOnlineMember( uuid );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            if ( e.getPlayer( ).teleport( loc , PlayerTeleportEvent.TeleportCause.PLUGIN ) ) {
                Main.getInstance( ).getWorlds( ).removePlayerToTP( uuid );
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBreaksBlock( BlockBreakEvent e ){
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        if ( world == null ) {
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        
        if ( !world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) && !player.hasPermission( "blockbyblock.admin.block.place.other" ) ) {
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
        
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPlaceBlocks( BlockPlaceEvent e ){
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        if ( world == null ) {
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        
        if ( !world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) && !player.hasPermission( "blockbyblock.admin.block.place.other" ) ) {
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport( PlayerTeleportEvent e ){
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            if ( e.getFrom( ).getWorld( ).equals( e.getTo( ).getWorld( ) ) ) {
                Main.getInstance( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" );
                Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
                e.setCancelled( true );
            }
            return;
        }
        if ( !e.getFrom( ).getWorld( ).equals( e.getTo( ).getWorld( ) ) ) {
            final BWorld prevWorld = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getFrom( ).getWorld( ).getName( ) ) );
            final BWorld postWorld = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getTo( ).getWorld( ).getName( ) ) );
            
            if ( prevWorld == null || postWorld == null ) {
                e.setCancelled( true );
                Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
                Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
                return;
            }
            if ( postWorld.getMembers( ).contains( playerUUID ) || postWorld.getOwner( ).equals( playerUUID ) ) {
                Main.getInstance( ).managePermissions( playerUUID , worldUUID , false );
            } else {
                e.setCancelled( true );
                postWorld.removeOnlineMember( playerUUID );
                Main.getInstance( ).getWorlds( ).saveWorld( postWorld );
                Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( playerUUID , postWorld , postWorld.getOwner( ) );
                Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            }
        }
        final User user = Main.getInstance( ).getPlayers( ).getPlayer( playerUUID );
        final Location loc = e.getTo( );
        user.setLastLocation( new Loc( Settings.PROXY_SERVER_NAME , worldUUID.toString( ) , loc.getX( ) , loc.getY( ) , loc.getZ( ) , worldUUID ) );
        Main.getInstance( ).getPlayers( ).savePlayer( user );
        
    }
    
    @EventHandler
    public void onPlayerChangeWorld( PlayerChangedWorldEvent e ){
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            return;
        }
        
        final BWorld postWorld = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) ) );
        
        if ( postWorld == null ) {
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            return;
        }
        if ( postWorld.getMembers( ).contains( playerUUID ) || postWorld.getOwner( ).equals( playerUUID ) ) {
            Main.getInstance( ).managePermissions( playerUUID , worldUUID , false );
            Main.getInstance( ).getWorlds( ).addPlayerToWorldOnlineMembers( playerUUID , postWorld );
        } else {
            postWorld.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( postWorld );
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( playerUUID , postWorld , postWorld.getOwner( ) );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocessEvent( PlayerCommandPreprocessEvent e ){
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        
        if ( world == null ) {
            // kick the player with the bukkit api
            Main.getInstance( ).getSocket( ).sendFormattedJoinServer( playerUUID , "lobby" , "&cMundo no encontrado" );
            e.setCancelled( true );
            return;
        }
        
        Main.getInstance( ).debug( "Player " + player.getName( ) + " is in world " + world.getName( ) );
        Main.getInstance( ).debug( "Player " + player.getName( ) + " is member: " + world.getMembers( ).contains( playerUUID ) );
        Main.getInstance( ).debug( "Player " + player.getName( ) + " is owner: " + world.getOwner( ).equals( playerUUID ) );
        Main.getInstance( ).debug( "Player " + player.getName( ) + " can Join: " + (!world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID )) );
        
        String command = e.getMessage( );
        if ( (command.startsWith( "//calc" ) || command.startsWith( "//eval" ) || command.startsWith( "//solve" ) || command.startsWith( "//evaluate " ) && !(Main.getInstance( ).getPlayers( ).getPlayer( playerUUID ).getRank( ).equals( Rank.ADMIN ))) ) {
            e.setCancelled( true );
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-use-commands-in-this-world" );
        }
        
        if ( !world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) ) {
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).getSocket( ).sendFormattedKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendFormattedSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
        
        
    }
    
    @EventHandler
    public void onEntitySpawnEvent( EntitySpawnEvent e ){
        switch ( e.getEntityType( ) ) {
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
                e.setCancelled( true );
        }
        
    }
    
    @EventHandler
    public void onEntityExplodeEvent( EntityExplodeEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityDamageEvent( EntityDamageEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityDamageByEntityEvent( EntityDamageByEntityEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityTargetEvent( EntityTargetEvent e ){
        if ( !(e.getTarget( ) instanceof Player) ) {
            e.setCancelled( true );
        }
    }
    
    @EventHandler
    public void onEntityTargetLivingEntityEvent( EntityTargetLivingEntityEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityInteractEvent( EntityInteractEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityChangeBlockEvent( EntityChangeBlockEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityPortalEvent( EntityPortalEvent e ){
        e.setCancelled( true );
    }
    
    
    @EventHandler
    public void onEntityPortalExitEvent( EntityPortalExitEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityRegainHealthEvent( EntityRegainHealthEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityShootBowEvent( EntityShootBowEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityTameEvent( EntityDamageEvent e ){
        e.setCancelled( true );
    }
    
    @EventHandler
    public void onEntityTargetEvent( FoodLevelChangeEvent e ){
        e.setCancelled( true );
    }
    
}
