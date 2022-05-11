package net.lymarket.comissionss.youmind.bbb.commands.home;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.error.HomeNotFoundError;
import net.lymarket.comissionss.youmind.bbb.settings.ServerType;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class HomeCommand implements ILyCommand {
    
    
    @Command(name = "home", permission = "bbb.home.goto")
    public boolean command( SCommandContext context ){
        
        if ( !(context.getSender( ) instanceof Player) ) {
            Main.getLang( ).sendErrorMsg( context.getSender( ) , "cant-execute-commands-from-console" );
            return true;
        }
        Player p = ( Player ) context.getSender( );
        if ( Settings.SERVER_TYPE != ServerType.WORLDS ) {
            Main.getLang( ).sendErrorMsg( p , "world.not-in-server" );
            return true;
        }
        if ( context.getArgs( ).length == 1 ) {
            final String homeName = context.getArg( 0 );
            try {
                final Home home = Main.getInstance( ).getHomes( ).getHome( UUID.fromString( homeName ) );
                if ( home != null ) {
                    Main.getInstance( ).getSocket( ).sendJoinHome( p.getUniqueId( ) , home );
                    return true;
                } else {
                    throw new HomeNotFoundError( homeName );
                }
            } catch ( IllegalArgumentException e ) {
                final Home home = Main.getInstance( ).getHomes( ).getUserHomeByName( p.getUniqueId( ) , homeName );
                if ( home != null ) {
                    Main.getInstance( ).getSocket( ).sendJoinHome( p.getUniqueId( ) , home );
                    return true;
                } else {
                    throw new HomeNotFoundError( homeName );
                }
            } catch ( HomeNotFoundError e ) {
                Main.getLang( ).sendErrorMsg( context.getSender( ) , "home.not-found" , "home" , homeName );
            }
            return true;
        }
        Main.getLang( ).sendErrorMsg( context.getSender( ) , "wrong-command" , "command" , "/home <nombre_del_home>&c." );
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
