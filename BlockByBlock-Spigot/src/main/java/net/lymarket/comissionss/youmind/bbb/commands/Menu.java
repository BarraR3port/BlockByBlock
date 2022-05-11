package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class Menu implements ILyCommand {
    
    @Command(name = "menu", permission = "blockbyblock.menu", usage = "menu", description = "Menu", aliases = {"m"})
    public boolean command( SCommandContext context ){
        
        if ( context.getSender( ) instanceof Player ) {
            final Player player = ( Player ) context.getSender( );
            if ( context.getArgs( ).length == 0 ) {
                new MainMenu( LyApi.getPlayerMenuUtility( player ) ).open( );
                return true;
            }
            if ( context.getArgs( ).length == 1 && player.hasPermission( "blockbyblock.menu.other" ) ) {
                final String userName = context.getArg( 0 );
                final User user = Main.getInstance( ).getPlayers( ).getPlayer( userName );
                if ( user != null ) {
                    new MainMenu( LyApi.getPlayerMenuUtility( player ) , user.getUUID( ) ).open( );
                } else {
                    Main.getLang( ).sendErrorMsg( player , "player.not-found" , "player" , userName );
                }
            }
        }
        
        
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        
        if ( context.getArgs( ).length == 1 ) {
            return Main.getInstance( ).getPlayers( ).getPlayersName( );
        }
        
        return list;
    }
}