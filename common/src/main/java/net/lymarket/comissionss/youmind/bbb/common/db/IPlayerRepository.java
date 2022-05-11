package net.lymarket.comissionss.youmind.bbb.common.db;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.error.UserNotFoundException;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class IPlayerRepository extends MongoDB < UUID, User > {
    
    public IPlayerRepository( MongoDBClient database , String TABLE_NAME ){
        super( database , TABLE_NAME );
    }
    
    @Override
    public void trashFinder( ){
    
    }
    
    
    public User getLocalStoredPlayer( UUID uuid ){
        if ( list.containsKey( uuid ) ) {
            return list.get( uuid );
        } else {
            User user = getPlayer( uuid );
            list.put( uuid , user );
            return user;
        }
    }
    
    
    public User getPlayer( String name ){
        
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "name" , name ) );
        if ( doc == null ) return null;
        User user = Api.getGson( ).fromJson( doc.toJson( ) , User.class );
        if ( user == null ) {
            throw new UserNotFoundException( name );
        }
        list.put( user.getUUID( ) , user );
        return user;
    }
    
    
    public User getPlayer( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        if ( doc == null ) return null;
        final User user = Api.getGson( ).fromJson( doc.toJson( ) , User.class );
        list.put( uuid , user );
        return user;
    }
    
    
    public User getPlayer( UUID uuid , String name ){
        Document docUUID = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        if ( docUUID == null ) {
            Document docName = database.findOneFast( TABLE_NAME , Filters.eq( "name" , name ) );
            if ( docName == null ) {
                return null;
            } else {
                User user = Api.getGson( ).fromJson( docName.toJson( ) , User.class );
                user.setUUID( uuid );
                return savePlayer( user );
            }
            
        }
        User user = Api.getGson( ).fromJson( docUUID.toJson( ) , User.class );
        list.put( uuid , user );
        return user;
    }
    
    
    public User createPlayer( String name , UUID uuid , String address ){
        User user = new User( name , uuid );
        user.setAddress( address );
        user.setOption( "allow-visit-plot-requests" , true );
        user.setOption( "allow-visit-world-requests" , true );
        user.setOption( "allow-pm" , true );
        user.setOption( "allow-friend-requests" , true );
        
        database.insertOne( TABLE_NAME , user );
        list.put( uuid , user );
        return user;
    }
    
    
    public User getOrCreatePlayer( String name , UUID uuid , String address ){
        User user = getPlayer( uuid , name );
        if ( user == null ) {
            return createPlayer( name , uuid , address );
        }
        
        return user;
    }
    
    
    public void unloadPlayer( UUID uuid ){
        list.remove( uuid );
    }
    
    
    public void unloadPlayer( User user ){
        savePlayer( user );
    }
    
    
    public void deletePlayer( UUID uuid ){
        database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
    }
    
    
    public HashMap < UUID, User > getPlayers( ){
        return list;
    }
    
    
    public User savePlayer( UUID uuid ){
        final User user = getPlayer( uuid );
        database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) , user );
        return user;
    }
    
    
    public User savePlayer( User user ){
        database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , user.getUUID( ).toString( ) ) , user );
        list.put( user.getUUID( ) , user );
        return user;
    }
    
    
    public void updatePlayer( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        if ( doc == null ) return;
        Api.getGson( ).fromJson( doc.toJson( ) , User.class );
    }
    
    
    public User getUpdatedPlayer( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        final User user = Api.getGson( ).fromJson( doc.toJson( ) , User.class );
        list.put( uuid , user );
        return user;
    }
    
    
    public void addProperty( User user , String key , String value ){
        user.addProperty( key , value );
        savePlayer( user );
    }
    
    
    public HashMap < String, String > getProperties( User user ){
        return user.getProperties( );
    }
    
    
    public void addPlot( User user , Plot plot ){
        user.addPlot( plot );
        savePlayer( user );
    }
    
    
    public void removePlot( User user , String plotID ){
        user.removePlot( plotID );
        savePlayer( user );
    }
    
    
    public void addHome( User user , Home home ){
        user.addHome( home );
        savePlayer( user );
    }
    
    
    public void removeHome( User user , UUID uuid ){
        user.removeHome( uuid );
        savePlayer( user );
    }
    
    
    public void addWarp( User user , net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp warp ){
        user.addWarp( warp );
        savePlayer( user );
    }
    
    
    public void removeWarp( User user , UUID uuid ){
        user.removeWarp( uuid );
        savePlayer( user );
    }
    
    
    public ArrayList < String > getPlayersName( String playerName ){
        return database.findMany( TABLE_NAME , User.class ).stream( ).map( User::getName ).filter( name -> !name.equalsIgnoreCase( playerName ) ).collect( Collectors.toCollection( ArrayList::new ) );
    }
    
    
    public ArrayList < String > getPlayersUUID( ArrayList < UUID > playersUUID ){
        final ArrayList < String > players = new ArrayList <>( );
        for ( UUID uuid : playersUUID ) {
            players.add( getPlayer( uuid ).getName( ) );
        }
        return players;
    }
    
    
    public ArrayList < String > getPlayersName( ){
        return database.findMany( TABLE_NAME , User.class ).stream( ).map( User::getName ).collect( Collectors.toCollection( ArrayList::new ) );
    }
}
