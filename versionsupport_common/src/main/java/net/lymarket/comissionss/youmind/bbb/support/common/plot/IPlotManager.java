package net.lymarket.comissionss.youmind.bbb.support.common.plot;

import com.google.gson.JsonObject;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import org.bukkit.World;

import java.util.HashMap;
import java.util.UUID;

public abstract class IPlotManager< V > {
    
    private final HashMap < UUID, V > playersToTP = new HashMap <>();
    private final HashMap < UUID, World > playersToTPToWorld = new HashMap <>();
    
    public abstract void manageJoinPlot(JsonObject json);
    
    public void addPlot(UUID key, V value){
        playersToTP.put(key, value);
    }
    
    public void addWorldToTp(UUID key, World value){
        playersToTPToWorld.put(key, value);
    }
    
    public void removePlot(UUID key){
        playersToTP.remove(key);
    }
    
    public void removeWorldToTp(UUID key){
        playersToTPToWorld.remove(key);
    }
    
    public V getPlot(UUID key){
        return playersToTP.get(key);
    }
    
    public World getWorldToTp(UUID key){
        return playersToTPToWorld.get(key);
    }
    
    public boolean hasPlot(UUID key){
        return playersToTP.containsKey(key);
    }
    
    public boolean hasWorldToTp(UUID key){
        return playersToTPToWorld.containsKey(key);
    }
    
    public abstract void manageVisitJoinPlot(UUID owner_uuid, User targetUser, String fromServer, String currentServer);
}
