package net.lymarket.comissionss.youmind.bbb.commands.spawn;

import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class Spawn implements ILyCommand {
    
    
    /**
     * @param context
     *
     * @return
     */
    @Command(name = "spawn", description = "Teleports you to the spawn", usage = "spawn")
    public CommandResponse command(SCommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (Settings.SPAWN_LOCATION != null){
                p.teleport(Settings.SPAWN_LOCATION);
            }
            
        }
        
        return new CommandResponse();
    }
    
    /**
     * @param context
     *
     * @return
     */
    @Tab
    public ArrayList < String > tabComplete(STabContext context){
        return new ArrayList <>();
    }
}
