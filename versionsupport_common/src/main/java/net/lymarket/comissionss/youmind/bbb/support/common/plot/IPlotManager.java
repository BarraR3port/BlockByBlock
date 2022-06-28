package net.lymarket.comissionss.youmind.bbb.support.common.plot;

import com.google.gson.JsonObject;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import org.bukkit.World;

import java.util.HashMap;
import java.util.UUID;

public abstract class IPlotManager< V > {
    
    private final HashMap < UUID, V > playersToTP = new HashMap <>();
    private final HashMap < UUID, UUID > playersToTPToPlayer = new HashMap <>();
    private final HashMap < UUID, World > playersToTPToWorldPlot = new HashMap <>();
    
    public abstract void manageJoinPlot(JsonObject json);
    
    public void addPlayerToTpToPlot(UUID key, V value){
        playersToTP.put(key, value);
    }
    
    public void addPlayerToTpToPlayer(UUID key, UUID value){
        playersToTPToPlayer.put(key, value);
    }
    
    public void addWorldToTp(UUID key, World value){
        playersToTPToWorldPlot.put(key, value);
    }
    
    public void removePlot(UUID key){
        playersToTP.remove(key);
    }
    
    public void removePlayerToTpToPlayer(UUID key){
        playersToTPToPlayer.remove(key);
    }
    
    public void removeWorldToTp(UUID key){
        playersToTPToWorldPlot.remove(key);
    }
    
    public V getPlot(UUID key){
        return playersToTP.getOrDefault(key, null);
    }
    
    public UUID getPlayerToTp(UUID key){
        return playersToTPToPlayer.get(key);
    }
    
    public World getWorldToTp(UUID key){
        return playersToTPToWorldPlot.get(key);
    }
    
    public boolean hasPlot(UUID key){
        return playersToTP.containsKey(key);
    }
    
    public boolean hasPlayerToTp(UUID key){
        return playersToTPToPlayer.containsKey(key);
    }
    
    public boolean hasWorldToTp(UUID key){
        return playersToTPToWorldPlot.containsKey(key);
    }
    
    public abstract void manageVisitJoinPlot(UUID owner_uuid, User targetUser, String fromServer, String currentServer);
}
