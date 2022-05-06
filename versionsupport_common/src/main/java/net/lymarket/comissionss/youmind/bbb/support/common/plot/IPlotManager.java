package net.lymarket.comissionss.youmind.bbb.support.common.plot;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.UUID;

public abstract class IPlotManager< V > {
    
    private final HashMap < UUID, V > playersToTP = new HashMap <>( );
    
    public abstract void manageJoinPlot( JsonObject json );
    
    public void addPlot( UUID key , V value ){
        playersToTP.put( key , value );
    }
    
    public void removePlot( UUID key ){
        playersToTP.remove( key );
    }
    
    public V getPlot( UUID key ){
        return playersToTP.get( key );
    }
    
    public boolean hasPlot( UUID key ){
        return playersToTP.containsKey( key );
    }
    
}
