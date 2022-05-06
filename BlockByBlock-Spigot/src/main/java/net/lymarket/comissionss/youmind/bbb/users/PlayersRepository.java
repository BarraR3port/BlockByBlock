package net.lymarket.comissionss.youmind.bbb.users;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.comissionss.youmind.bbb.common.error.UserNotFoundException;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PlayersRepository extends IPlayerRepository {
    
    
    public PlayersRepository( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    @Override
    public void trashFinder( ){
        
    }
    
    @Override
    public User getPlayer( String name ){
        
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "name" , name ) );
        if ( doc == null ) return null;
        User player = Api.getGson( ).fromJson( doc.toJson( ) , User.class );
        if ( player == null ) {
            throw new UserNotFoundException( name );
        }
        
        return player;
    }
    
    @Override
    public User getPlayer( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        if ( doc == null ) return null;
        return Api.getGson( ).fromJson( doc.toJson( ) , User.class );
    }
    
    @Override
    public User getPlayer( UUID uuid , String name ){
        Document docUUID = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        if ( docUUID == null ) {
            Document docName = database.findOneFast( TABLE_NAME , Filters.eq( "name" , name ) );
            if ( docName == null ) {
                return null;
            } else {
                User player = Api.getGson( ).fromJson( docName.toJson( ) , User.class );
                player.setUUID( uuid );
                return savePlayer( player );
            }
            
        }
        
        return Api.getGson( ).fromJson( docUUID.toJson( ) , User.class );
    }
    
    @Override
    public User createPlayer( String name , UUID uuid , String address ){
        User player = new User( name , uuid );
        player.setAddress( address );
        
        database.insertOne( TABLE_NAME , player );
        return player;
    }
    
    @Override
    public User getOrCreatePlayer( String name , UUID uuid , String address ){
        User player = getPlayer( uuid , name );
        if ( player == null ) {
            return createPlayer( name , uuid , address );
        }
        
        return player;
    }
    
    @Override
    public void unloadPlayer( UUID uuid ){
        list.remove( uuid );
    }
    
    @Override
    public void unloadPlayer( User user ){
        savePlayer( user );
    }
    
    @Override
    public void deletePlayer( UUID uuid ){
        database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
    }
    
    @Override
    public HashMap < UUID, User > getPlayers( ){
        return list;
    }
    
    @Override
    public User savePlayer( UUID uuid ){
        User user = getPlayer( uuid );
        database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) , user );
        return user;
    }
    
    @Override
    public User savePlayer( User player ){
        database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , player.getUUID( ).toString( ) ) , player );
        return player;
    }
    
    @Override
    public void updatePlayer( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        if ( doc == null ) return;
        Api.getGson( ).fromJson( doc.toJson( ) , User.class );
    }
    
    @Override
    public User getUpdatedPlayer( UUID uuid ){
        Document doc = database.findOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
        return Api.getGson( ).fromJson( doc.toJson( ) , User.class );
    }
    
    @Override
    public void addProperty( User player , String key , String value ){
        player.addProperty( key , value );
    }
    
    @Override
    public HashMap < String, String > getProperties( User player ){
        return player.getProperties( );
    }
    
    
    @Override
    public void addPlot( User player , Plot plot ){
        player.addPlot( plot );
        savePlayer( player );
    }
    
    @Override
    public void removePlot( User player , String plotID ){
        player.removePlot( plotID );
        savePlayer( player );
    }
    
    
    @Override
    public void addHome( User player , Home home ){
        player.addHome( home );
        savePlayer( player );
    }
    
    @Override
    public void removeHome( User player , UUID uuid ){
        player.removeHome( uuid );
        savePlayer( player );
    }
    
    @Override
    public void addWarp( User player , Warp warp ){
        player.addWarp( warp );
        savePlayer( player );
    }
    
    @Override
    public void removeWarp( User player , UUID uuid ){
        player.removeWarp( uuid );
        savePlayer( player );
    }
    
    @Override
    public ArrayList < String > getPlayersName( String playerName ){
        return database.findMany( TABLE_NAME , User.class ).stream( ).map( User::getName ).filter( name -> !name.equalsIgnoreCase( playerName ) ).collect( Collectors.toCollection( ArrayList::new ) );
    }
    
    @Override
    public ArrayList < String > getPlayersUUID( ArrayList<UUID> playersUUID ){
        final ArrayList < String > players = new ArrayList < > ( );
        for ( UUID uuid : playersUUID ){
            players.add( getPlayer( uuid ).getName() );
        }
        return players;
    }
    
    @Override
    public ArrayList < String > getPlayersName( ){
        return database.findMany( TABLE_NAME , User.class ).stream( ).map( User::getName ).collect( Collectors.toCollection( ArrayList::new ) );
    }
    
    
}
