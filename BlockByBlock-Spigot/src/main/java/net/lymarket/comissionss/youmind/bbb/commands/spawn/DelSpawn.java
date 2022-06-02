package net.lymarket.comissionss.youmind.bbb.commands.spawn;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class DelSpawn implements ILyCommand {
    
    /**
     * @param context
     *
     * @return
     */
    @Command(name = "delspawn", usage = "delspawn", description = "Set the spawn point of the server")
    public CommandResponse command(SCommandContext context){
        
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            
            if (!p.hasPermission("blockbyblock.admin")){
                if (!p.hasPermission("blockbyblock.admin.delspawn"))
                    return new CommandResponse("blockbyblock.admin.delspawn");
                return new CommandResponse("blockbyblock.admin");
            }
            
            Main.getInstance().getConfig().set("spawn.location", null);
            Main.getInstance().getConfig().saveData();
            
        }
        
        
        return new CommandResponse();
    }
    
    /**
     * @param context
     *
     * @return
     */
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        return new ArrayList <>( );
    }
}
