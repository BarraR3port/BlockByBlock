package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.common.commands.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Visit implements ILyCommand {
    
    @Command(name = "visit", permission = "bbb.visit")
    public boolean command( SCommandContext context ){
        switch ( context.getArgs( ).length ) {
            case 1: {
                final User userTarget = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 0 ) );
                if ( userTarget != null ) {
                    Main.getInstance( ).getSocket( ).sendVisitRequest( (( Player ) context.getSender( )).getUniqueId( ) , userTarget.getUUID( ) );
                } else {
                    Main.getLang( ).sendErrorMsg( context.getSender( ) , "player.not-fund" , "player" , context.getArg( 0 ) );
                }
                return true;
            }
            default:
                return false;
        }
    }
    
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        if ( context.getSender( ).hasPermission( "bbb.visit" ) ) {
            if ( context.getArgs( ).length == 1 ) {
                list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( context.getSender( ).getName( ) ) );
                
            }
        }
        return list;
    }
}
