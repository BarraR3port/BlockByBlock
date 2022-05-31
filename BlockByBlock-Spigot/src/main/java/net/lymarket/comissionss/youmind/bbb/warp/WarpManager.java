package net.lymarket.comissionss.youmind.bbb.warp;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.WarpType;
import net.lymarket.comissionss.youmind.bbb.common.db.IWarpManager;
import net.lymarket.comissionss.youmind.bbb.common.error.WarpNotFoundError;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class WarpManager extends IWarpManager < SpigotWarp > {
    
    
    public WarpManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarpsByUser( UUID uuid ){
        return database.findMany( TABLE_NAME , warp -> warp.getMembers( ).contains( uuid ) , SpigotWarp.class );
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarpsByUserAndVersion( UUID uuid , String version ){
        return null;
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarpsByServer( String server ){
        return database.findMany( TABLE_NAME , warp -> warp.getLocation( ).getServer( ).equalsIgnoreCase( server ) , SpigotWarp.class );
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarpsByVersion( String version ){
        return database.findMany( TABLE_NAME , warp -> warp.getVersion( ).equalsIgnoreCase( version ) , SpigotWarp.class );
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarpsByServer( ){
        return null;
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarpsByWorld( UUID uuid ){
        return null;
    }
    
    @Override
    public void createWarp( SpigotWarp warp ){
        try {
            getUserWarpByName( warp.getType( ) , warp.getLocation( ).getServer( ) );
        } catch ( WarpNotFoundError e ) {
            database.insertOne( TABLE_NAME , warp );
        }
        
    }
    
    @Override
    public boolean saveWarp( SpigotWarp warp ){
        return database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , warp.getUUID( ).toString( ) ) , warp );
    }
    
    @Override
    public SpigotWarp getWarp( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        SpigotWarp warp = Api.getGson( ).fromJson( doc.toJson( ) , SpigotWarp.class );
        if ( warp == null ) {
            throw new WarpNotFoundError( uuid );
        }
        return warp;
    }
    
    @Override
    public ArrayList < SpigotWarp > getWarps( ){
        return database.findMany( TABLE_NAME , SpigotWarp.class );
    }
    
    @Override
    public SpigotWarp getUserWarpByName( WarpType warpType , String serverName ){
        
        ArrayList < Document > documents = database.findManyFast( TABLE_NAME , Filters.eq( "type" , warpType.toString( ) ) );
        if ( documents == null || documents.isEmpty( ) ) {
            throw new WarpNotFoundError( warpType.toString( ) , serverName );
        }
        SpigotWarp foundWarp = null;
        for ( Document doc : documents ) {
            final SpigotWarp warp = Api.getGson( ).fromJson( doc.toJson( ) , SpigotWarp.class );
            if ( warp == null ) continue;
            if ( warp.getLocation( ).getServer( ).equals( serverName ) ) {
                foundWarp = warp;
            }
        }
        if ( foundWarp == null ) {
            throw new WarpNotFoundError( warpType.toString( ) , serverName );
        }
        
        return foundWarp;
    }
    
    @Override
    public boolean deleteWarp( SpigotWarp warp ){
        return database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , warp.getUUID( ).toString( ) ) );
    }
    
    @Override
    public void trashFinder( ){
    
    }
}
