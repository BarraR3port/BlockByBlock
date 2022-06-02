package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Undo implements ILyCommand {
    
    
    @Command(name = "undo", permission = "blockbyblock.undo", aliases = "u")
    public CommandResponse command(SCommandContext context){
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            p.performCommand("//undo");
        }
        
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList < String > tabComplete(STabContext context){
        return new ArrayList <>();
    }
}
