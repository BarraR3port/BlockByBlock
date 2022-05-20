package net.lymarket.comissionss.youmind.bbb.warp;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.WarpType;
import net.lymarket.comissionss.youmind.bbb.common.db.IWarpManager;
import net.lymarket.comissionss.youmind.bbb.common.error.WarpNotFoundError;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class WarpManager extends IWarpManager {
    
    
    public WarpManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    @Override
    public ArrayList < Warp > getWarpsByUser( UUID uuid ){
        return database.findMany( TABLE_NAME , warp -> warp.getMembers( ).contains( uuid ) , Warp.class );
    }
    
    @Override
    public ArrayList < Warp > getWarpsByUserAndVersion( UUID uuid , String version ){
        return null;
    }
    
    @Override
    public ArrayList < Warp > getWarpsByServer( String server ){
        return database.findMany( TABLE_NAME , warp -> warp.getLocation( ).getServer( ).equalsIgnoreCase( server ) , Warp.class );
    }
    
    @Override
    public ArrayList < Warp > getWarpsByVersion( String version ){
        return database.findMany( TABLE_NAME , warp -> warp.getVersion( ).equalsIgnoreCase( version ) , Warp.class );
    }
    
    @Override
    public ArrayList < Warp > getWarpsByServer( ){
        return null;
    }
    
    @Override
    public ArrayList < Warp > getWarpsByWorld( UUID uuid ){
        return null;
    }
    
    @Override
    public void createWarp( Warp warp ){
        try {
            getUserWarpByName( warp.getType( ) , warp.getLocation( ).getServer( ) );
        } catch ( WarpNotFoundError e ) {
            database.insertOne( TABLE_NAME , warp );
        }
    
    }
    
    @Override
    public boolean saveWarp( Warp warp ){
        return database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , warp.getUUID( ).toString( ) ) , warp );
    }
    
    @Override
    public Warp getWarp( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        Warp warp = Api.getGson( ).fromJson( doc.toJson( ) , Warp.class );
        if ( warp == null ) {
            throw new WarpNotFoundError( uuid );
        }
        return warp;
    }
    
    @Override
    public Warp getUserWarpByName( WarpType warpType , String serverName ){
    
        ArrayList < Document > documents = database.findManyFast( TABLE_NAME , Filters.eq( "type" , warpType.toString( ) ) );
        if ( documents == null || documents.isEmpty( ) ) {
            throw new WarpNotFoundError( warpType.toString( ) , serverName );
        }
        Warp foundWarp = null;
        for ( Document doc : documents ) {
            final Warp warp = Api.getGson( ).fromJson( doc.toJson( ) , Warp.class );
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
    public boolean deleteWarp( Warp warp ){
        return database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , warp.getUUID( ).toString( ) ) );
    }
    
    @Override
    public void trashFinder( ){
    
    }
}
