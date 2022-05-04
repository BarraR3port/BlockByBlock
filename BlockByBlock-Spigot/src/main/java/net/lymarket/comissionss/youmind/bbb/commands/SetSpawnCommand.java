package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.common.commands.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class SetSpawnCommand implements ILyCommand {
    
    /**
     * @param context
     *
     * @return
     */
    @Command(name = "setspawn", usage = "setspawn", description = "Set the spawn point of the server")
    public boolean command( SCommandContext context ){
        
        if ( context.getSender( ) instanceof Player ) {
            Player p = ( Player ) context.getSender( );
            
            if ( !p.hasPermission( "blockbyblock.admin" ) )
                if ( !p.hasPermission( "blockbyblock.admin.setspawn" ) ) return false;
            
            final Location loc = p.getLocation( );
            p.getWorld( ).setSpawnLocation( loc );
            Main.getInstance( ).getConfig( ).set( "spawn.location" , loc );
            Main.getInstance( ).getConfig( ).saveData( );
            
        }
        
        
        return true;
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
