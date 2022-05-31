package net.lymarket.comissionss.youmind.bbb.common.db;

import net.lymarket.comissionss.youmind.bbb.common.data.warp.WarpType;
import net.lymarket.common.db.MongoDB;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IWarpManager< W > extends MongoDB < UUID, W > {
    private final HashMap < UUID, W > playersToTP = new HashMap <>( );
    
    public IWarpManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    public abstract ArrayList < W > getWarpsByUser( UUID uuid );
    
    public abstract ArrayList < W > getWarpsByUserAndVersion( UUID uuid , String version );
    
    public abstract ArrayList < W > getWarpsByServer( String server );
    
    public abstract ArrayList < W > getWarpsByVersion( String version );
    
    public abstract ArrayList < W > getWarpsByServer( );
    
    public abstract ArrayList < W > getWarpsByWorld( UUID uuid );
    
    public abstract void createWarp( W world );
    
    public abstract ArrayList < W > getWarps( );
    
    public abstract boolean saveWarp( W world );
    
    public abstract W getWarp( UUID uuid );
    
    public void addPlayerToTP( UUID uuid , W home ){
        playersToTP.put( uuid , home );
    }
    
    public void removePlayerToTP( UUID uuid ){
        playersToTP.remove( uuid );
    }
    
    public boolean isPlayerToTP( UUID uuid ){
        return playersToTP.containsKey( uuid );
    }
    
    public W getPlayerToTP( UUID uuid ){
        return playersToTP.get( uuid );
    }
    
    public HashMap < UUID, W > getPlayersToTP( ){
        return playersToTP;
    }
    
    
    public abstract W getUserWarpByName( WarpType warpType , String serverName );
    
    public abstract boolean deleteWarp( W home );
}
