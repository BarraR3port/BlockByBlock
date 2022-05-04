package net.lymarket.comissionss.youmind.bbb.socket;

import net.lymarket.comissionss.youmind.bbb.Main;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LoadedUser {
    
    
    private static final ConcurrentHashMap < UUID, LoadedUser > loaded = new ConcurrentHashMap <>( );
    private UUID uuid;
    private String partyOwnerOrSpectateTarget = null;
    private long toleranceTime;
    private String arenaIdentifier;
    
    public LoadedUser( String uuid , String arenaIdentifier , String langIso , String partyOwnerOrSpectateTarget ){
        if ( Bukkit.getWorld( arenaIdentifier ) == null ) return;
        this.arenaIdentifier = arenaIdentifier;
        this.uuid = UUID.fromString( uuid );
        if ( partyOwnerOrSpectateTarget != null ) {
            if ( !partyOwnerOrSpectateTarget.isEmpty( ) ) {
                this.partyOwnerOrSpectateTarget = partyOwnerOrSpectateTarget;
            }
        }
        this.toleranceTime = System.currentTimeMillis( );
        
        loaded.put( this.uuid , this );
    }
    
    public static boolean isPreLoaded( UUID uuid ){
        return loaded.containsKey( uuid );
    }
    
    public static LoadedUser getPreLoaded( UUID uuid ){
        return loaded.get( uuid );
    }
    
    public static ConcurrentHashMap < UUID, LoadedUser > getLoaded( ){
        return loaded;
    }
    
    public boolean isTimedOut( ){
        return System.currentTimeMillis( ) > this.toleranceTime;
    }
    
    public UUID getUuid( ){
        return uuid;
    }
    
    public String getArenaIdentifier( ){
        return arenaIdentifier;
    }
    
    public void destroy( String reason ){
        Main.debug( "Destroyed PreLoaded User: " + uuid + " Reason: " + reason + ". Tolerance: " );
        loaded.remove( uuid );
    }
    
    // if arena is started is used as staff teleport target
    public String getPartyOwnerOrSpectateTarget( ){
        return partyOwnerOrSpectateTarget;
    }
}
