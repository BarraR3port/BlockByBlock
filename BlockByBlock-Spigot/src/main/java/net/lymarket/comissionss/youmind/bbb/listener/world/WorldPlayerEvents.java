package net.lymarket.comissionss.youmind.bbb.listener.world;


import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.transformers.Transformer;
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
        final boolean tpToWorld = Main.getInstance( ).getWorlds( ).isPlayerToTP( uuid );
        final boolean tpToHome = Main.getInstance( ).getHomes( ).isPlayerToTP( uuid );
        final boolean isWarpWorld = e.getPlayer( ).getLocation( ).getWorld( ).getName( ).equalsIgnoreCase( "warp" );
        final BWorld visitWorld = Main.getInstance( ).getWorlds( ).getWorldByVisitor( uuid );
    
        if ( visitWorld != null ) {
            Main.getInstance( ).debug( "Player " + e.getPlayer( ).getName( ) + " is in visit world " + visitWorld.getName( ) );
            final Location loc = Bukkit.getWorld( visitWorld.getUUID( ).toString( ) ).getSpawnLocation( );
            visitWorld.addOnlineMember( uuid );
            final Player p = Bukkit.getPlayer( visitWorld.getVisitor( uuid ).getTarget_uuid( ) );
            visitWorld.removeVisitor( uuid );
            Main.getInstance( ).getWorlds( ).saveWorld( visitWorld );
            if ( p != null ) {
                final HashMap < String, String > replace = new HashMap <>( );
                replace.put( "player" , e.getPlayer( ).getName( ) );
                replace.put( "world" , visitWorld.getName( ).split( "-" )[0] );
                Main.getLang( ).sendMsg( p , "world.visit-join-to-owner" , replace );
            }
            if ( e.getPlayer( ).teleport( loc , PlayerTeleportEvent.TeleportCause.PLUGIN ) ) {
                Main.getLang( ).sendMsg( e.getPlayer( ) , "world.visit-join-to-visitor" , "player" , p != null ? p.getName( ) : "&atu amigo" );
                Main.getInstance( ).manageVisitorPermissions( uuid , visitWorld.getUUID( ) , false );
                Main.getInstance( ).getWorlds( ).removeGuestFromVisitWorldList( uuid );
            }
        
        } else if ( tpToWorld ) {
            final BWorld world = Main.getInstance( ).getWorlds( ).getPlayerToTP( uuid );
            final Location loc = Bukkit.getWorld( world.getUUID( ).toString( ) ).getSpawnLocation( );
            world.addOnlineMember( uuid );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            if ( e.getPlayer( ).teleport( loc , PlayerTeleportEvent.TeleportCause.PLUGIN ) ) {
                Main.getInstance( ).getWorlds( ).removePlayerToTP( uuid );
                Main.getInstance( ).managePermissions( uuid , world.getUUID( ) , false );
            }
        } else if ( tpToHome ) {
            final Home home = Main.getInstance( ).getHomes( ).getPlayerToTP( uuid );
            final Loc homeLoc = home.getLocation( );
            final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( homeLoc.getBWorld( ) );
            world.addOnlineMember( uuid );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            if ( e.getPlayer( ).teleport( Transformer.toLocation( homeLoc ) , PlayerTeleportEvent.TeleportCause.PLUGIN ) ) {
                Main.getInstance( ).getHomes( ).removePlayerToTP( uuid );
                Main.getInstance( ).managePermissions( uuid , world.getUUID( ) , false );
            }
        } else if ( isWarpWorld ) {
        
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBreaksBlock( BlockBreakEvent e ){
        final boolean isWarpWorld = e.getBlock( ).getLocation( ).getWorld( ).getName( ).equalsIgnoreCase( "warp" );
        if ( isWarpWorld ) {
            return;
        }
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        if ( world == null ) {
            Main.getInstance( ).getSocket( ).sendKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        
        if ( !world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) && !player.hasPermission( "blockbyblock.admin.block.place.other" ) ) {
            e.setCancelled( true );
            if ( e.getPlayer( ).hasPermission( "blockbyblock.visit" ) ) return;
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).debug( "Player " + player.getName( ) + " is in world " + world.getName( ) + " and is not owner and not allowed to place blocks" );
            Main.getInstance( ).getSocket( ).sendKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
        
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPlaceBlocks( BlockPlaceEvent e ){
        final boolean isWarpWorld = e.getBlock( ).getLocation( ).getWorld( ).getName( ).equalsIgnoreCase( "warp" );
        if ( isWarpWorld ) {
            return;
        }
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        final Player player = e.getPlayer( );
        if ( world == null ) {
            Main.getInstance( ).getSocket( ).sendKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        
        if ( !world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) && !player.hasPermission( "blockbyblock.admin.block.place.other" ) ) {
            e.setCancelled( true );
            if ( e.getPlayer( ).hasPermission( "blockbyblock.visit" ) ) return;
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).debug( "Player " + player.getName( ) + " is in world " + world.getName( ) + " and is not owner and not allowed to place blocks" );
            Main.getInstance( ).getSocket( ).sendKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport( PlayerTeleportEvent e ){
        final boolean isWarpWorld = e.getTo( ).getWorld( ).getName( ).equalsIgnoreCase( "warp" );
        if ( isWarpWorld ) {
            return;
        }
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
    
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            if ( e.getFrom( ).getWorld( ).equals( e.getTo( ).getWorld( ) ) ) {
                Main.getInstance( ).getSocket( ).sendJoinServer( playerUUID , "lobby" );
                Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
                e.setCancelled( true );
            }
            return;
        }
        final User user = Main.getInstance( ).getPlayers( ).getPlayer( playerUUID );
        if ( !e.getFrom( ).getWorld( ).equals( e.getTo( ).getWorld( ) ) ) {
    
            final BWorld visitWorld = Main.getInstance( ).getWorlds( ).getWorldByVisitor( playerUUID );
    
            if ( visitWorld != null ) {
                visitWorld.removeVisitor( playerUUID );
                Main.getInstance( ).getWorlds( ).saveWorld( visitWorld );
                Main.getInstance( ).getWorlds( ).removeGuestFromVisitWorldList( playerUUID );
                /*if ( p != null ) {
                    final HashMap < String, String > replace = new HashMap <>( );
                    replace.put( "player" , e.getPlayer( ).getName( ) );
                    replace.put( "world" , visitWorld.getName( ).split( "-" )[0] );
                    Main.getLang( ).sendMsg( p , "world.visit-join-to-owner" , replace );
                }*/
                Main.getInstance( ).manageVisitorPermissions( playerUUID , visitWorld.getUUID( ) , false ).thenAccept( a -> Main.getLang( ).sendMsg( e.getPlayer( ) , "world.visit-visit-join-to-visitor" , "player" , /*p != null ? p.getName( ) :*/ "&atu amigo" ) );
        
            }
    
            final BWorld prevWorld = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getFrom( ).getWorld( ).getName( ) ) );
            final BWorld postWorld = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getTo( ).getWorld( ).getName( ) ) );
    
            if ( prevWorld == null || postWorld == null ) {
                e.setCancelled( true );
                Main.getInstance( ).getSocket( ).sendKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
                Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
                return;
            }
            if ( postWorld.getMembers( ).contains( playerUUID ) || postWorld.getOwner( ).equals( playerUUID ) || user.getRank( ).isAdmin( ) ) {
                Main.getInstance( ).managePermissions( playerUUID , worldUUID , false );
            } else {
                if ( e.getPlayer( ).hasPermission( "blockbyblock.visit" ) ) return;
                e.setCancelled( true );
                postWorld.removeOnlineMember( playerUUID );
                Main.getInstance( ).getWorlds( ).saveWorld( postWorld );
                Main.getInstance( ).debug( "[PlayerTeleportEvent] Player " + playerUUID + " is in world " + postWorld.getName( ) + " and is not owner and not allowed to be in this world" );
                Main.getInstance( ).getSocket( ).sendKickFromWorld( playerUUID , postWorld , postWorld.getOwner( ) );
                Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            }
        }
    
        final Location loc = e.getTo( );
        user.setLastLocation( new Loc( Settings.PROXY_SERVER_NAME , worldUUID.toString( ) , loc.getX( ) , loc.getY( ) , loc.getZ( ) , worldUUID ) );
        Main.getInstance( ).getPlayers( ).savePlayer( user );
        
    }
    
    @EventHandler
    public void onPlayerChangeWorld( PlayerChangedWorldEvent e ){
        final boolean isWarpWorld = e.getPlayer( ).getLocation( ).getWorld( ).getName( ).equalsIgnoreCase( "warp" );
        if ( isWarpWorld ) {
            return;
        }
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            return;
        }
        
        final BWorld postWorld = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) ) );
        
        if ( postWorld == null ) {
            Main.getInstance( ).getSocket( ).sendKickFromWorld( playerUUID , e.getPlayer( ).getWorld( ).getName( ) , Settings.PROXY_SERVER_NAME , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            return;
        }
        if ( postWorld.getMembers( ).contains( playerUUID ) || postWorld.getOwner( ).equals( playerUUID ) ) {
            Main.getInstance( ).managePermissions( playerUUID , worldUUID , false );
            Main.getInstance( ).getWorlds( ).addPlayerToWorldOnlineMembers( playerUUID , postWorld );
        } else {
            if ( e.getPlayer( ).hasPermission( "blockbyblock.visit" ) ) return;
            postWorld.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( postWorld );
            Main.getInstance( ).debug( "[PlayerChangedWorldEvent] Player " + playerUUID + " is in world " + postWorld.getName( ) + " and is not owner and not allowed to be in this world" );
            Main.getInstance( ).getSocket( ).sendKickFromWorld( playerUUID , postWorld , postWorld.getOwner( ) );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
        }
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocessEvent( PlayerCommandPreprocessEvent e ){
        final boolean isWarpWorld = e.getPlayer( ).getLocation( ).getWorld( ).getName( ).equalsIgnoreCase( "warp" );
        if ( isWarpWorld ) {
            return;
        }
        final UUID playerUUID = e.getPlayer( ).getUniqueId( );
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString( e.getPlayer( ).getWorld( ).getName( ) );
        } catch ( IllegalArgumentException ex ) {
            Main.getInstance( ).getSocket( ).sendJoinServer( playerUUID , "lobby" );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
            e.setCancelled( true );
            return;
        }
        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( worldUUID );
        
        if ( world == null ) {
            // kick the player with the bukkit api
            Main.getInstance( ).getSocket( ).sendJoinServer( playerUUID , "lobby" , "&cMundo no encontrado" );
            e.setCancelled( true );
            return;
        }
        String command = e.getMessage( );
        if ( (command.startsWith( "//calc" ) || command.startsWith( "//eval" ) || command.startsWith( "//solve" ) || command.startsWith( "//evaluate " ) && !(Main.getInstance( ).getPlayers( ).getPlayer( playerUUID ).getRank( ).isAdmin( ))) ) {
            e.setCancelled( true );
            Main.getInstance( ).getSocket( ).sendKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-use-commands-in-this-world" );
        }
        if ( !world.getMembers( ).contains( playerUUID ) && !world.getOwner( ).equals( playerUUID ) ) {
            if ( e.getPlayer( ).hasPermission( "blockbyblock.visit" ) ) return;
            e.setCancelled( true );
            world.removeOnlineMember( playerUUID );
            Main.getInstance( ).getWorlds( ).saveWorld( world );
            Main.getInstance( ).debug( "[PlayerCommandPreprocessEvent] Player " + playerUUID + " is in world " + world.getName( ) + " and is not owner and not allowed to be in this world" );
            Main.getInstance( ).getSocket( ).sendKickFromWorld( world.getOwner( ) , world , playerUUID );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( playerUUID , "error.world.not-allowed-to-join-world" );
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
