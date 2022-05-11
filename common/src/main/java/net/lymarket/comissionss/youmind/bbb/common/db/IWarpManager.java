package net.lymarket.comissionss.youmind.bbb.common.db;

import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IWarpManager extends MongoDB < UUID, Warp > {
    private final HashMap < UUID, Warp > playersToTP = new HashMap <>( );
    
    public IWarpManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    public abstract ArrayList < Warp > getWarpsByUser( UUID uuid );
    
    public abstract ArrayList < Warp > getWarpsByUserAndVersion( UUID uuid , String version );
    
    public abstract ArrayList < Warp > getWarpsByServer( String server );
    
    public abstract ArrayList < Warp > getWarpsByVersion( String version );
    
    public abstract ArrayList < Warp > getWarpsByServer( );
    
    public abstract ArrayList < Warp > getWarpsByWorld( UUID uuid );
    
    public abstract void createWarp( Warp world );
    
    public ArrayList < Warp > getWarps( ){
        return database.findMany( TABLE_NAME , Warp.class );
    }
    
    public abstract boolean saveWarp( Warp world );
    
    public abstract Warp getWarp( UUID uuid );
    
    public abstract Warp getUserWarpByName( UUID uuid , String homeName );
    
    public void addPlayerToTP( UUID uuid , Warp home ){
        playersToTP.put( uuid , home );
    }
    
    public void removePlayerToTP( UUID uuid ){
        playersToTP.remove( uuid );
    }
    
    public boolean isPlayerToTP( UUID uuid ){
        return playersToTP.containsKey( uuid );
    }
    
    public Warp getPlayerToTP( UUID uuid ){
        return playersToTP.get( uuid );
    }
    
    public HashMap < UUID, Warp > getPlayersToTP( ){
        return playersToTP;
    }
    
    public abstract boolean deleteWarp( Warp home );
}
