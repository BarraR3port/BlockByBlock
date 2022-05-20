package net.lymarket.comissionss.youmind.bbb.home;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.db.IHomeManager;
import net.lymarket.comissionss.youmind.bbb.common.error.HomeNotFoundError;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class HomeManager extends IHomeManager {
    
    public HomeManager( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    @Override
    public ArrayList < Home > getHomesByUser( UUID uuid ){
        return database.findMany( TABLE_NAME , home -> home.getOwner( ).equals( uuid ) , Home.class );
    }
    
    @Override
    public ArrayList < Home > getHomesByUserAndVersion( UUID uuid , String version ){
        return database.findMany( TABLE_NAME , home -> home.getOwner( ).equals( uuid ) && home.getVersion( ).equals( version ) , Home.class );
    }
    
    @Override
    public ArrayList < Home > getHomesByServer( String server ){
        return database.findMany( TABLE_NAME , home -> home.getLocation( ).getServer( ).equalsIgnoreCase( server ) , Home.class );
    }
    
    @Override
    public ArrayList < Home > getHomesByServer( ){
        return database.findMany( TABLE_NAME , home -> home.getLocation( ).getServer( ).equalsIgnoreCase( Settings.SERVER_NAME ) , Home.class );
    }
    
    @Override
    public ArrayList < Home > getHomesByWorld( UUID uuid ){
        return database.findMany( TABLE_NAME , home -> home.getLocation( ).getBWorld( ).equals( uuid ) , Home.class );
    }
    
    @Override
    public void createHome( Home home ){
        database.insertOne( TABLE_NAME , home );
    }
    
    @Override
    public boolean saveHome( Home home ){
        return database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , home.getUUID( ).toString( ) ) , home );
    }
    
    @Override
    public Home getHome( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        Home home = Api.getGson( ).fromJson( doc.toJson( ) , Home.class );
        if ( home == null ) {
            throw new HomeNotFoundError( uuid );
        }
        return home;
    }
    
    @Override
    public Home getUserHomeByName( UUID uuid , String homeName ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.and( Filters.eq( "owner" , uuid.toString( ) ) , Filters.eq( "name" , homeName ) ) );
        Home home = Api.getGson( ).fromJson( doc.toJson( ) , Home.class );
        if ( home == null ) {
            throw new HomeNotFoundError( uuid );
        }
        return home;
    }
    
    @Override
    public boolean deleteHome( Home home ){
        return database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , home.getUUID( ).toString( ) ) );
    }
    
    @Override
    public void trashFinder( ){
    
    }
}
