package net.lymarket.comissionss.youmind.bbb.common.db;

import com.google.gson.JsonObject;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.data.world.WorldVisitRequest;
import net.lymarket.common.db.MongoDB;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IBWorldManager< V > extends MongoDB < UUID, BWorld > {
    
    private final HashMap < UUID, BWorld > playersToTP = new HashMap <>( );
    
    public IBWorldManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    public static BWorld getWorldFormatted( UUID ownerUUID , String versionTarget , String material ){
        /*ArrayList < String > list = new ArrayList <>( );
        String versionFormatted = versionTarget.replace( "." , "" ).replace( "_" , "" );
        list.add( "PW-" + versionFormatted + "-1" );
        list.add( "PW-" + versionFormatted + "-2" );
        
        String server = list.get( ( int ) (Math.random( ) * list.size( )) );
        
        return new BWorld( ownerUUID , server , versionTarget );*/
        final String versionFormatted = versionTarget.replace( "." , "" ).replace( "_" , "" );
        return new BWorld( ownerUUID , "PW-" + versionFormatted + "-1" , versionTarget , material );
    }
    
    public void addPlayerToTP( UUID uuid , BWorld world ){
        playersToTP.put( uuid , world );
    }
    
    public void removePlayerToTP( UUID uuid ){
        playersToTP.remove( uuid );
    }
    
    public boolean isPlayerToTP( UUID uuid ){
        return playersToTP.containsKey( uuid );
    }
    
    public BWorld getPlayerToTP( UUID uuid ){
        return playersToTP.get( uuid );
    }
    
    public HashMap < UUID, BWorld > getPlayersToTP( ){
        return playersToTP;
    }
    
    
    public abstract ArrayList < BWorld > getWorldsByUser( UUID uuid );
    
    public abstract ArrayList < BWorld > getWorldsByServer( String serverName );
    
    public abstract ArrayList < BWorld > getWorldsByServer( );
    
    public abstract V createWorldSlimeWorld( BWorld world );
    
    public abstract V createCustomLayerWorld( BWorld world , String material );
    
    
    public abstract void createWorld( BWorld world );
    
    public abstract void deleteWorldFromOutside( UUID senderUUID , BWorld bworld , String serverTarget , JsonObject json );
    
    public abstract void saveWorld( Object world );
    
    
    public boolean addPlayerToWorldOnlineMembers( UUID uuid , BWorld world ){
        if ( removePlayerFromWorldOnlineMembers( uuid ) ) {
            world.addOnlineMember( uuid );
            saveWorld( world );
        }
        return true;
    }
    
    public boolean addPlayerToWorldOnlineMembers( UUID uuid , UUID world_uuid ){
        return addPlayerToWorldOnlineMembers( uuid , getWorld( world_uuid ) );
    }
    
    public boolean removePlayerFromWorldOnlineMembers( UUID uuid ){
        
        for ( BWorld world : getWorlds( ) ) {
            if ( world.hasOnlineMember( uuid ) ) {
                world.removeOnlineMember( uuid );
                saveWorld( world );
            }
        }
        return true;
    
    }
    
    public ArrayList < BWorld > getWorlds( ){
        return database.findMany( TABLE_NAME , BWorld.class );
    }
    
    public abstract boolean saveWorld( BWorld world );
    
    public abstract BWorld getWorld( UUID uuid );
    
    public abstract boolean manageVisitJoinWorld( WorldVisitRequest request );
}
