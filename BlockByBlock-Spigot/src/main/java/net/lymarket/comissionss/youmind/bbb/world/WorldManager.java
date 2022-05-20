package net.lymarket.comissionss.youmind.bbb.world;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.JsonObject;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.data.world.WorldVisitRequest;
import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.comissionss.youmind.bbb.common.error.WorldNotFoundError;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class WorldManager extends IBWorldManager < SlimeWorld > {
    
    private final SlimeLoader loader = Main.getSlimePlugin( ).getLoader( "file" );
    
    private final HashMap < UUID, BWorld > guestToVisitWorlds = new HashMap <>( );
    
    public WorldManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    
        try {
            WorldData worldData = new WorldData( );
            worldData.setSpawn( "0, 64, 0" );
            worldData.setWorldType( "flat" );
            worldData.setDragonBattle( false );
            worldData.setAllowMonsters( false );
            worldData.setAllowAnimals( false );
            worldData.setPvp( false );
            SlimePropertyMap propertyMap = worldData.toPropertyMap( );
            for ( String sw : loader.listWorlds( ) ) {
                final SlimeWorld world = Main.getSlimePlugin( ).loadWorld( loader , sw , true , propertyMap );
                try {
                    final BWorld bWorld = getWorld( UUID.fromString( world.getName( ) ) );
                    Main.getSlimePlugin( ).generateWorld( world , Material.valueOf( bWorld.getBlock_base( ) ) );
                } catch ( IllegalArgumentException | IndexOutOfBoundsException e ) {
                    Main.getSlimePlugin( ).generateWorld( world );
                }
            }
        } catch ( IOException | CorruptedWorldException | NewerFormatException | WorldInUseException |
                  UnknownWorldException | IndexOutOfBoundsException e ) {
            e.printStackTrace( );
        }
        
    }
    
    @Override
    public ArrayList < BWorld > getWorldsByUser( UUID uuid ){
        return database.findMany( TABLE_NAME , world -> world.getOwner( ).equals( uuid ) , BWorld.class );
    }
    
    @Override
    public ArrayList < BWorld > getWorldsByServer( String serverName ){
        return database.findMany( TABLE_NAME , world -> world.getServer( ).equalsIgnoreCase( serverName ) , BWorld.class );
    }
    
    @Override
    public ArrayList < BWorld > getWorldsByServer( ){
        return database.findMany( TABLE_NAME , world -> world.getServer( ).equalsIgnoreCase( Settings.SERVER_NAME ) , BWorld.class );
    }
    
    @Override
    public SlimeWorld createWorldSlimeWorld( BWorld world ){
        WorldData worldData = new WorldData( );
        worldData.setSpawn( "0, 64, 0" );
        worldData.setWorldType( "flat" );
        worldData.setDragonBattle( false );
        worldData.setAllowMonsters( false );
        worldData.setAllowAnimals( false );
        worldData.setPvp( false );
        SlimePropertyMap propertyMap = worldData.toPropertyMap( );
        SlimeWorld slimeWorld = null;
        try {
            slimeWorld = Main.getSlimePlugin( ).createEmptyWorld( loader , world.getUUID( ).toString( ) , false , propertyMap );
            
            Main.getSlimePlugin( ).generateWorld( slimeWorld );
            
            
        } catch ( IOException | WorldAlreadyExistsException | IllegalArgumentException ignored ) {
        
        }
        return slimeWorld;
    }
    
    @Override
    public SlimeWorld createCustomLayerWorld( BWorld world , String material ){
        WorldData worldData = new WorldData( );
        worldData.setSpawn( "0, 64, 0" );
        worldData.setWorldType( "flat" );
        worldData.setDragonBattle( false );
        worldData.setAllowMonsters( false );
        worldData.setAllowAnimals( false );
        worldData.setPvp( false );
        SlimePropertyMap propertyMap = worldData.toPropertyMap( );
        SlimeWorld slimeWorld = null;
        try {
            slimeWorld = Main.getSlimePlugin( ).createEmptyWorld( loader , world.getUUID( ).toString( ) , false , propertyMap );
            
            final Material mat = Material.valueOf( material );
    
            boolean empty = mat == XMaterial.AIR.parseMaterial( );
            if ( empty ) {
                Main.getSlimePlugin( ).generateEmptyWorld( slimeWorld , true );
            } else {
                Main.getSlimePlugin( ).generateWorld( slimeWorld , mat );
            }
            
            
        } catch ( IOException | WorldAlreadyExistsException | IllegalArgumentException ignored ) {
        
        }
        return slimeWorld;
    }
    
    /**
     * @param world
     */
    @Override
    public void createWorld( BWorld world ){
        database.insertOne( TABLE_NAME , world );
    }
    
    
    @Override
    public void deleteWorldFromOutside( UUID owner_uuid , BWorld bworld , String serverTarget , JsonObject json ){
        final String worldName = bworld.getUUID( ).toString( );
        final World world = Bukkit.getWorld( bworld.getUUID( ).toString( ) );
        final long time = System.currentTimeMillis( );
        boolean isDone = true;
        Main.getInstance( ).debug( "Initializing the WorldDestroyer:" );
        Main.getInstance( ).debug( "[WorldDestroyer] Deleting the world: " + worldName );
        if ( world == null ) {
            Main.getInstance( ).debug( "[WorldDestroyer] &4ERROR AT: world == null" );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( owner_uuid , Main.getLang( ).getMSG( "error.world.not-loaded" , "world" , worldName ) );
            
            isDone = false;
        }
        
        
        // Teleport all players outside the world before unloading it
        List < Player > players = world.getPlayers( );
        
        if ( !players.isEmpty( ) ) {
            players.forEach( player -> player.kickPlayer( "El mundo ha sido borrado" ) );
            Main.getInstance( ).getSocket( ).sendJoinServer( owner_uuid , "lobby" );
        }
        
        if ( !Bukkit.unloadWorld( world , true ) ) {
            Main.getInstance( ).debug( "[WorldDestroyer] &4ERROR AT: &e!Bukkit.unloadWorld( world , true )" );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( owner_uuid , Main.getLang( ).getMSG( "error.world.failed-to-unload" , "world" , worldName ) );
            isDone = false;
        }
        
        Main.getInstance( ).debug( "[WorldDestroyer] Attempting to unlock world.. " + worldName + "." );
        try {
            if ( loader != null && loader.isWorldLocked( worldName ) ) {
                Main.getInstance( ).debug( "[WorldDestroyer] World.. " + worldName + " is locked." );
                loader.unlockWorld( worldName );
                loader.deleteWorld( worldName );
                Main.getInstance( ).debug( "[WorldDestroyer] Attempted to unlock world.. " + worldName + "." );
            } else {
                Main.getInstance( ).debug( "[WorldDestroyer] " + worldName + " was not unlocked. This could be because the world is either unlocked or not in the config. This is not an error" );
            }
        } catch ( UnknownWorldException | IOException e ) {
            e.printStackTrace( );
        }
    
        final ArrayList < Home > homesByWorld = Main.getInstance( ).getHomes( ).getHomesByWorld( bworld.getUUID( ) );
        Main.getInstance( ).debug( "[WorldDestroyer] Deleting the " + homesByWorld.size( ) + " Homes from the world..." );
        for ( Home home : homesByWorld ) {
            Main.getInstance( ).getHomes( ).deleteHome( home );
            Main.getInstance( ).debug( "[WorldDestroyer] Deleted the home: " + home.getName( ) + " from the world" );
        }
        Main.getInstance( ).debug( "[WorldDestroyer] Deleted all the homes." );
    
        database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , bworld.getUUID( ).toString( ) ) );
    
        try {
            Main.getInstance( ).debug( "[WorldDestroyer] Deleting the World Folder of: " + worldName + "..." );
            FileUtils.deleteDirectory( new File( worldName ) );
            FileUtils.forceDelete( new File( Main.getInstance( ).getServer( ).getWorldContainer( ) , "/slime_worlds/" + worldName + ".slime" ) );
            Main.getInstance( ).debug( "[WorldDestroyer] Deleted the World Folder of: " + worldName + "." );
        } catch ( Exception e ) {
            e.printStackTrace( );
            Main.getInstance( ).debug( "[WorldDestroyer] &4ERROR AT: File file = new File( Main.getInstance( ).getDataFolder( ) , /" + worldName );
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( owner_uuid , Main.getLang( ).getMSG( "error.world.failed-to-unload" , "world" , worldName ) );
            isDone = false;
        }
        
        Main.getInstance( ).debug( "[WorldDestroyer] Removing all perms of the world members, owner and online members..." );
        
        final ArrayList < UUID > playersToRemovePerms = new ArrayList <>( bworld.getOnlineMembers( ) );
        playersToRemovePerms.add( bworld.getOwner( ) );
        playersToRemovePerms.addAll( bworld.getMembers( ) );
        
        
        for ( UUID uuid : playersToRemovePerms ) {
            try {
                Main.getInstance( ).removePermissionsInOneWorld( uuid , bworld.getUUID( ) );
            } catch ( NullPointerException ignored ) {
            }
        }
        
        Main.getInstance( ).debug( "[WorldDestroyer] World: " + worldName + " was unloaded and destroyed in " + (System.currentTimeMillis( ) - time) + "ms." );
        
        
        if ( isDone ) {
            json.addProperty( "type" , "WORLD_DELETE_SUCCESS" );
        } else {
            json.addProperty( "type" , "ERROR" );
            json.addProperty( "error" , "WORLD_DELETE_FAILED" );
        }
        Main.getInstance( ).getSocket( ).sendMessage( json );
        
    }
    
    /*private Location findValidDefaultSpawn( ){
        World defaultWorld = Bukkit.getWorlds( ).get( 0 );
        Location spawnLocation = defaultWorld.getSpawnLocation( );
        
        spawnLocation.setY( 64 );
        while (spawnLocation.getBlock( ).getType( ) != Material.AIR || spawnLocation.getBlock( ).getRelative( BlockFace.UP ).getType( ) != Material.AIR) {
            if ( spawnLocation.getY( ) >= 256 ) {
                spawnLocation.getWorld( ).getBlockAt( 0 , 64 , 0 ).setType( Material.BEDROCK );
            } else {
                spawnLocation.add( 0 , 1 , 0 );
            }
        }
        return spawnLocation;
    }*/
    
    @Override
    public void saveWorld( Object world ){
        /*SlimeWorld slimeWorld = ( SlimeWorld ) world;*/
    }
    
    @Override
    public boolean saveWorld( BWorld world ){
        return database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , world.getUUID( ).toString( ) ) , world );
    }
    
    
    @Override
    public BWorld getWorld( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        BWorld world = Api.getGson( ).fromJson( doc.toJson( ) , BWorld.class );
        if ( world == null ) {
            throw new WorldNotFoundError( uuid );
        }
        return world;
    }
    
    @Override
    public boolean manageVisitJoinWorld( WorldVisitRequest request ){
        final Player p = Bukkit.getPlayer( request.getTarget_uuid( ) );
        final boolean isInWorld = !p.getWorld( ).getName( ).equals( "warp" );
        if ( isInWorld ) {
            final BWorld world = getWorld( UUID.fromString( p.getWorld( ).getName( ) ) );
            final User guest = Main.getInstance( ).getPlayers( ).getPlayer( request.getGuest( ) );
            if ( world.getOwner( ).equals( request.getGuest( ) ) || world.isMember( request.getGuest( ) ) || guest.getRank( ).isAdmin( ) || request.isAccepted( ) ) {
                if ( request.getGuest_server( ).equals( request.getTarget_server( ) ) ) {
                    final Location loc = Bukkit.getWorld( world.getUUID( ).toString( ) ).getSpawnLocation( );
                    world.addOnlineMember( request.getGuest( ) );
                    world.removeVisitor( request.getGuest( ) );
                    Main.getInstance( ).getWorlds( ).addGuestToVisitWorldList( request.getGuest( ) , world );
                    Main.getInstance( ).manageVisitorPermissions( request.getGuest( ) , world.getUUID( ) , false );
                    final Player guestPlayer = Bukkit.getPlayer( request.getGuest( ) );
                    Bukkit.getScheduler( ).runTask( Main.getInstance( ) , ( ) -> guestPlayer.teleport( loc , PlayerTeleportEvent.TeleportCause.PLUGIN ) );
                } else {
                    request.accept( );
                    Main.getInstance( ).getSocket( ).sendWorldVisitResponse( request );
                }
                return true;
            } else {
                final HashMap < String, String > replace = new HashMap <>( );
                replace.put( "player" , guest.getName( ) );
                replace.put( "world" , world.getName( ).split( "-" )[0] );
                world.addVisitor( request );
                Main.getInstance( ).getWorlds( ).addGuestToVisitWorldList( request.getGuest( ) , world );
                
                Utils.sendMessage( p , Utils.hoverOverMessageRunCommand( Main.getLang( ).getMSG( "world.visit-request-to-owner" , replace ) ,
                        Collections.singletonList( "&7Click para &aACEPTAR" ) , "/visit accept world " + request.getGuest( ) + " " + world.getUUID( ) ) );
                return false;
            }
        } else {
            Main.getInstance( ).getSocket( ).sendMSGToPlayer( request.getGuest( ) , "error.visit.world.player-in-warp" , "player" , request.getGuest_name( ) );
            return false;
        }
        
    }
    
    public void addGuestToVisitWorldList( UUID guest , BWorld world ){
        guestToVisitWorlds.put( guest , world );
        saveWorld( world );
        
    }
    
    public BWorld getWorldByVisitor( UUID visitor ){
        return guestToVisitWorlds.getOrDefault( visitor , null );
    }
    
    public void removeGuestFromVisitWorldList( UUID guest ){
        guestToVisitWorlds.remove( guest );
    }
    
    @Override
    public void trashFinder( ){
    }
}
