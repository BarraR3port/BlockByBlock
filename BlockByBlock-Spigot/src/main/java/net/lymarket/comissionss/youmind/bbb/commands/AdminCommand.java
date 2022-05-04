package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.menu.admin.AdminMenu;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AdminCommand implements ILyCommand {
    
    @Command(name = "admin", permission = "blockbyblock.admin", usage = "admin", description = "Admin Command")
    public boolean command( SCommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            final Player player = ( Player ) context.getSender( );
            if ( context.getArgs( ).length == 0 ) {
                Main.getLang( ).sendErrorMsg( player , "wrong-command" , "command" , "/admin <user>" );
            }
            if ( context.getArgs( ).length == 1 ) {
                final User target = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 0 ) );
                if ( target == null ) {
                    Main.getLang( ).sendErrorMsg( player , "player.not-found" );
                    return true;
                }
                new AdminMenu( LyApi.getPlayerMenuUtility( player ) , target ).open( );
            }
            
        }
        
        
        return false;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        return null;
    }
}
