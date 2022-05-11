package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class MainCommand implements ILyCommand {
    
    
    /**
     * @param context
     *
     * @return
     */
    @Command(name = "ym", permission = "blockbyblock.visitor", description = "Admin Command")
    public boolean command( SCommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            final Player player = ( Player ) context.getSender( );
            if ( context.getArgs( ).length == 0 ) {
                new MainMenu( LyApi.getPlayerMenuUtility( player ) ).open( );
                return true;
            }
        }
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        if ( context.getSender( ).hasPermission( "blockbyblock.admin" ) ) {
            if ( context.getArgs( ).length == 0 ) {
                list.add( "reload" );
                list.add( "worlds" );
            }
            if ( context.getArgs( ).length == 1 ) {
                list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( ) );
            }
            
        }
        
        return list;
    }
}
