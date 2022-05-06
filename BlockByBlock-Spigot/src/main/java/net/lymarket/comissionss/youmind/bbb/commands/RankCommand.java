package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.common.commands.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class RankCommand implements ILyCommand {
    
    @Command(name = "rank", permission = "blockbyblock.rank")
    public boolean command( SCommandContext context ){
        
        if ( context.getArgs( ).length == 0 ) {
            context.getSender( ).sendMessage( "§b§m----------------------------------------------------" );
            context.getSender( ).sendMessage( "                                §a§lRank System" );
            context.getSender( ).sendMessage( "" );
            if ( context.getSender( ) instanceof Player ) {
                Player p = ( Player ) context.getSender( );
                p.spigot( ).sendMessage( Main.getApi( ).getUtils( ).formatTC( "&7Establece el rango a un jugador con este " ) , Main.getApi( ).getUtils( ).hoverOverMessageSuggestCommand( "&dcomando" , Collections.singletonList( "&e/rank set <jugador> <rango>" ) , "/rank set " ) );
            } else {
                context.getSender( ).sendMessage( "§e- §dEstablece el rango a un jugador §e- §d/rank set (jugador) (rango)" );
            }
            context.getSender( ).sendMessage( "§b§m----------------------------------------------------" );
            return true;
        }
        
        if ( context.getArg( 0 ).equalsIgnoreCase( "set" ) ) {
            if ( context.getArgs( ).length == 3 ) {
                final String target = context.getArg( 1 );
                try {
                    Rank rank = Rank.valueOf( context.getArg( 2 ).toUpperCase( Locale.ROOT ) );
                    User user = Main.getInstance( ).getPlayers( ).getPlayer( target );
                    Bukkit.dispatchCommand( Bukkit.getConsoleSender( ) , "lp user " + target + " group set " + rank.getLpName( ) );
                    user.setRank( rank );
                    Main.getInstance( ).getPlayers( ).savePlayer( user );
                } catch ( NullPointerException e ) {
                    Main.getLang( ).sendErrorMsg( context.getSender( ) , "player.not-fund" , "player" , target );
                }
            } else {
                Main.getLang( ).sendErrorMsg( context.getSender( ) , "wrong-command" , "command" , "/rank set (jugador) (rango)" );
            }
            return true;
        }
        return false;
    }
    
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        ArrayList < String > list = new ArrayList <>( );
        if ( context.getSender().hasPermission( "blockbyblock.rank" )){
            if ( context.getArgs( ).length == 1 ) {
                list.add( "set" );
            }
            if ( context.getArgs( ).length == 2 ) {
                list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( context.getSender( ).getName( ) ) );
            }
            if ( context.getArgs( ).length == 3 ) {
                for ( Rank rank : Rank.values( ) ) {
                    list.add( StringUtils.capitalize( rank.name( ).toLowerCase( Locale.ROOT ) ) );
                }
            }
        }
        return list;
    }
}
