package net.lymarket.comissionss.youmind.bbb.listener.world;


import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.socket.SpigotSocketClient;
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

public class WorldPlayerEvents extends MainEvents {
    
    
    public void subPlayerQuitEvent( PlayerQuitEvent e ){
        final UUID uuid = e.getPlayer( ).getUniqueId( );
        User user = Main.getInstance( ).getPlayers( ).getPlayer( uuid );
        Main.getInstance( ).getWorlds( ).getWorlds( ).forEach( world -> {
            world.removeOnlineMember( user.getUUID( ) );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
        } );
        
        Main.getInstance( ).getPlayers( ).unloadPlayer( user );
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
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        final UUID playerUUID = player.getUniqueId( );
        if ( world == null ) {
            e.setCancelled( true );
            return;
        }
        
        if ( !world.getOnlineMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) && !player.hasPermission( "blockbyblock.admin.block.break.other" ) ) {
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatKickFromWorld( playerUUID , world , world.getOwner( ) ) );
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" ) );
        }
        
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPlaceBlocks( BlockPlaceEvent e ){
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        final UUID playerUUID = player.getUniqueId( );
        if ( world == null ) {
            e.setCancelled( true );
            return;
        }
        if ( !world.getOnlineMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) && !player.hasPermission( "blockbyblock.admin.block.place.other" ) ) {
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatKickFromWorld( playerUUID , world , world.getOwner( ) ) );
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" ) );
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoinWorld( PlayerTeleportEvent e ){
        Main.debug( "PlayerTeleportEvent " + e.getPlayer( ).getName( ) + " " + e.getPlayer( ).getWorld( ).getName( ) );
        
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        final UUID playerUUID = player.getUniqueId( );
        
        if ( world == null ) {
            e.setCancelled( true );
            return;
        }
        Main.debug( "Player is in world " + world.getName( ) );
        Main.debug( "Player is in world " + world.getName( ) + " and has permission " + player.hasPermission( "blockbyblock.admin.commands.other" ) );
        Main.debug( "World online members: " + world.getOnlineMembers( ) );
        Main.debug( "World members: " + world.getMembers( ) );
        
        
        if ( !world.getOnlineMembers( ).contains( playerUUID ) ) {
            if ( !world.getOwner( ).equals( playerUUID ) ) {
                if ( !player.hasPermission( "blockbyblock.admin.commands.other" ) ) {
                    world.removeOnlineMember( playerUUID );
                    Main.getInstance( ).getWorlds( ).saveWorld( world );
                    Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatKickFromWorld( playerUUID , world , world.getOwner( ) ) );
                    Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" ) );
                    
                }
            }
        }
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinWorld( PlayerChangedWorldEvent e ){
        Main.debug( "PlayerChangedWorldEvent " + e.getPlayer( ).getName( ) + " " + e.getPlayer( ).getWorld( ).getName( ) );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        final UUID playerUUID = player.getUniqueId( );
        
        if ( world == null ) {
            // kick the player with the bukkit api
            e.getPlayer( ).kickPlayer( Main.getLang( ).getMSG( "error.world.not-allowed-to-join-world" ) );
            return;
        }
        
        Main.debug( "Player is in world " + world.getName( ) );
        Main.debug( "Player is in world " + world.getName( ) + " and has permission " + player.hasPermission( "blockbyblock.admin.commands.other" ) );
        Main.debug( "World online members: " + world.getOnlineMembers( ) );
        Main.debug( "World members: " + world.getMembers( ) );
        
        
        if ( !world.getOnlineMembers( ).contains( playerUUID ) ) {
            if ( !world.getOwner( ).equals( playerUUID ) ) {
                if ( !player.hasPermission( "blockbyblock.admin.commands.other" ) ) {
                    world.removeOnlineMember( playerUUID );
                    Main.getInstance( ).getWorlds( ).saveWorld( world );
                    Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatKickFromWorld( playerUUID , world , world.getOwner( ) ) );
                    Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" ) );
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocessEvent( PlayerCommandPreprocessEvent e ){
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        final UUID playerUUID = player.getUniqueId( );
        
        if ( world == null ) {
            // kick the player with the bukkit api
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatJoinServer( playerUUID , "lobby" , "&cMundo no encontrado" ) );
            return;
        }
        
        if ( !world.getOnlineMembers( ).contains( playerUUID ) ) {
            if ( !world.getOwner( ).equals( playerUUID ) ) {
                if ( !player.hasPermission( "blockbyblock.admin.commands.other" ) ) {
                    e.setCancelled( true );
                    world.removeOnlineMember( playerUUID );
                    Main.getInstance( ).getWorlds( ).saveWorld( world );
                    Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatKickFromWorld( playerUUID , world , world.getOwner( ) ) );
                    Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-use-commands-in-this-world" ) );
                    return;
                }
            }
        }
        
        String command = e.getMessage( );
        if ( command.startsWith( "//calc" ) || command.startsWith( "//eval" ) || command.startsWith( "//solve" ) || command.startsWith( "//evaluate " ) ) {
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatKickFromWorld( playerUUID , world , world.getOwner( ) ) );
            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatSendMSGToPlayer( playerUUID , "error.world.not-allowed-to-use-commands-in-this-world" ) );
            return;
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
