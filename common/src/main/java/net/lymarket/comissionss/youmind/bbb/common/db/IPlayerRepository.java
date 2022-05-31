package net.lymarket.comissionss.youmind.bbb.common.db;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.common.db.MongoDB;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IPlayerRepository< U > extends MongoDB < UUID, U > {
    
    public IPlayerRepository( MongoDBClient database , String TABLE_NAME ){
        super( database , TABLE_NAME );
    }
    
    @Override
    public void trashFinder( ){
    }
    
    public U getLocalStoredPlayer( UUID uuid ){
        if ( list.containsKey( uuid ) ) {
            return list.get( uuid );
        } else {
            U user = getPlayer( uuid );
            list.put( uuid , user );
            return user;
        }
    }
    
    public abstract U getPlayer( String name );
    
    public abstract U getPlayer( UUID uuid );
    
    public abstract U getPlayer( UUID uuid , String name );
    
    public abstract void createPlayer( String name , UUID uuid , String address );
    
    public void getOrCreatePlayer( String name , UUID uuid , String address ){
        U user = getPlayer( uuid , name );
        if ( user == null ) {
            createPlayer( name , uuid , address );
        }
    }
    
    public void unloadPlayer( UUID uuid ){
        list.remove( uuid );
    }
    
    public void unloadPlayer( U user ){
        savePlayer( user );
    }
    
    public void deletePlayer( UUID uuid ){
        database.deleteOne( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) );
    }
    
    public HashMap < UUID, U > getPlayers( ){
        return list;
    }
    
    public U savePlayer( UUID uuid ){
        final U user = getPlayer( uuid );
        database.replaceOneFast( TABLE_NAME , Filters.eq( "uuid" , uuid.toString( ) ) , user );
        return user;
    }
    
    public abstract U savePlayer( U user );
    
    public abstract U savePlayer( U user , UUID prevUUID );
    
    public abstract void updatePlayer( UUID uuid );
    
    public abstract U getUpdatedPlayer( UUID uuid );
    
    public abstract void addProperty( U user , String key , String value );
    
    public abstract HashMap < String, String > getProperties( U user );
    
    public abstract void addPlot( U user , Plot plot );
    
    public abstract void removePlot( U user , String plotID , PlotType type );
    
    public abstract void addHome( U user , Home home );
    
    public abstract void removeHome( U user , UUID uuid );
    
    public abstract void addWarp( U user , Warp warp );
    
    public abstract void removeWarp( U user , UUID uuid );
    
    public abstract ArrayList < String > getPlayersName( String playerName );
    
    public abstract ArrayList < String > getPlayersUUID( ArrayList < UUID > playersUUID );
    
    public abstract ArrayList < String > getPlayersName( );
}
