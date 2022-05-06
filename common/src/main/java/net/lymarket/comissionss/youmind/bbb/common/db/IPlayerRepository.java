package net.lymarket.comissionss.youmind.bbb.common.db;

import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IPlayerRepository extends MongoDB < UUID, User > {
    
    public IPlayerRepository( MongoDBClient database , String TABLE_NAME ){
        super( database , TABLE_NAME );
    }
    
    public abstract User getPlayer( String name );
    
    public abstract User getPlayer( UUID uuid );
    
    public abstract User getPlayer( UUID uuid , String name );
    
    public abstract User createPlayer( String name , UUID uuid , String address );
    
    public abstract User getOrCreatePlayer( String name , UUID uuid , String address );
    
    public abstract void unloadPlayer( UUID uuid );
    
    public abstract void unloadPlayer( User user );
    
    public abstract void deletePlayer( UUID uuid );
    
    public abstract HashMap < UUID, User > getPlayers( );
    
    public abstract User savePlayer( UUID uuid );
    
    public abstract User savePlayer( User player );
    
    public abstract void updatePlayer( UUID uuid );
    
    public abstract User getUpdatedPlayer( UUID uuid );
    
    public abstract void addProperty( User player , String key , String value );
    
    public abstract HashMap < String, String > getProperties( User player );
    
    public abstract void addPlot( User player , Plot plot );
    
    public abstract void removePlot( User player , String plotID );
    
    public abstract void addHome( User player , Home home );
    
    public abstract void removeHome( User player , UUID uuid );
    
    public abstract void addWarp( User player , Warp warp );
    
    public abstract void removeWarp( User player , UUID uuid );
    
    public abstract ArrayList < String > getPlayersUUID( ArrayList < UUID > playersUUID );
    
    public abstract ArrayList < String > getPlayersName( );
    
    public abstract ArrayList < String > getPlayersName( String playerName );
}
