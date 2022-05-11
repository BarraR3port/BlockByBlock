package net.lymarket.comissionss.youmind.bbb.common.db;

import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IHomeManager extends MongoDB < UUID, Home > {
    private final HashMap < UUID, Home > playersToTP = new HashMap <>( );
    
    public IHomeManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    public abstract ArrayList < Home > getHomesByUser( UUID uuid );
    
    public abstract ArrayList < Home > getHomesByUserAndVersion( UUID uuid , String version );
    
    public abstract ArrayList < Home > getHomesByServer( String server );
    
    public abstract ArrayList < Home > getHomesByServer( );
    
    public abstract ArrayList < Home > getHomesByWorld( UUID uuid );
    
    public abstract void createHome( Home world );
    
    public ArrayList < Home > getHomes( ){
        return database.findMany( TABLE_NAME , Home.class );
    }
    
    public abstract boolean saveHome( Home world );
    
    public abstract Home getHome( UUID uuid );
    
    public abstract Home getUserHomeByName( UUID uuid , String homeName );
    
    public void addPlayerToTP( UUID uuid , Home home ){
        playersToTP.put( uuid , home );
    }
    
    public void removePlayerToTP( UUID uuid ){
        playersToTP.remove( uuid );
    }
    
    public boolean isPlayerToTP( UUID uuid ){
        return playersToTP.containsKey( uuid );
    }
    
    public Home getPlayerToTP( UUID uuid ){
        return playersToTP.get( uuid );
    }
    
    public HashMap < UUID, Home > getPlayersToTP( ){
        return playersToTP;
    }
    
    public abstract boolean deleteHome( Home home );
}
