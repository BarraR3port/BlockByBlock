package net.lymarket.comissionss.youmind.bbb.commands.home;


import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RemoveHome implements ILyCommand {
    
    @Command(name = "removehome", permission = "bbb.home.remove", aliases = {"rmhome" , "removeh" , "rmh"}, usage = "/removehome <home>")
    public boolean command( SCommandContext context ){
    
        if ( !(context.getSender( ) instanceof Player) ) {
            Main.getLang( ).sendErrorMsg( context.getSender( ) , "cant-execute-commands-from-console" );
            return true;
        }
    
        if ( context.getArgs( ).length == 1 ) {
            final Player p = ( Player ) context.getSender( );
            final String homeName = context.getArg( 0 );
            final Home home = Main.getInstance( ).getHomes( ).getUserHomeByName( p.getUniqueId( ) , homeName );
            if ( home.getOwner( ).equals( p.getUniqueId( ) ) || p.hasPermission( "bbb.admin.home.remove" ) ) {
                Main.getLang( ).sendMsg( p , (Main.getInstance( ).getHomes( ).deleteHome( home ) ? "home.deleted-successfully" : "error.home.error-deleting") , "home" , homeName );
                return true;
            } else {
                Main.getLang( ).sendMsg( p , "error.home.error-deleting" , "home" , homeName );
            }
        } else {
            Main.getLang( ).sendErrorMsg( context.getSender( ) , "wrong-command" , "command" , "/removehome <nombre>" );
        }
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        
        ArrayList < String > list = new ArrayList <>( );
        if ( context.getArgs( ).length == 1 ) {
            list = Main.getInstance( ).getHomes( ).getHomesByUserAndVersion( (( Player ) context.getSender( )).getUniqueId( ) , Settings.VERSION ).stream( ).map( Home::getName ).collect( Collectors.toCollection( ArrayList::new ) );
        }
        return list;
    }
}
