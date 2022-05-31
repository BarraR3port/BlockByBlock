package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.common.commands.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Undo implements ILyCommand {
    
    
    @Command(name = "u", permission = "blockbyblock.undo")
    public boolean command( SCommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            final Player p = ( Player ) context.getSender( );
            p.performCommand( "//undo" );
        }
        
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        return new ArrayList <>( );
    }
}
