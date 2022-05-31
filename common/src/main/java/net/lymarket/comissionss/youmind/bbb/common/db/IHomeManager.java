package net.lymarket.comissionss.youmind.bbb.common.db;

import net.lymarket.common.db.MongoDB;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IHomeManager< H > extends MongoDB < UUID, H > {
    private final HashMap < UUID, H > playersToTP = new HashMap <>( );
    
    public IHomeManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    public abstract ArrayList < H > getHomesByUser( UUID uuid );
    
    public abstract ArrayList < H > getHomesByUserAndVersion( UUID uuid , String version );
    
    public abstract ArrayList < H > getHomesByServer( String server );
    
    public abstract ArrayList < H > getHomesByServer( );
    
    public abstract ArrayList < H > getHomesByWorld( UUID uuid );
    
    public abstract void createHome( H world );
    
    public abstract ArrayList < H > getHomes( );
    
    public abstract boolean saveHome( H world );
    
    public abstract H getHome( UUID uuid );
    
    public abstract H getUserHomeByName( UUID uuid , String homeName );
    
    public void addPlayerToTP( UUID uuid , H home ){
        playersToTP.put( uuid , home );
    }
    
    public void removePlayerToTP( UUID uuid ){
        playersToTP.remove( uuid );
    }
    
    public boolean isPlayerToTP( UUID uuid ){
        return playersToTP.containsKey( uuid );
    }
    
    public H getPlayerToTP( UUID uuid ){
        return playersToTP.get( uuid );
    }
    
    public HashMap < UUID, H > getPlayersToTP( ){
        return playersToTP;
    }
    
    public abstract boolean deleteHome( H home );
}
