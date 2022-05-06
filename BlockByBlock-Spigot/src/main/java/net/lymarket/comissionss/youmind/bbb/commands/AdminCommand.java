package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.admin.AdminMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AdminCommand implements ILyCommand {
    
    @Command(name = "admin", permission = "blockbyblock.admin", usage = "admin", description = "Admin Command")
    public boolean command( SCommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            if ( context.getArgs( ).length == 0 ) {
                Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , "&c&lBlockByBlockHub &7- &a&lV" + Main.getInstance( ).getDescription( ).getVersion( ) );
                Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , " " );
                Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , "&aCommands: " );
                Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , " " );
                Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , " > ym reload" );
                return true;
            } else if ( context.getArgs( ).length == 1 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "reload" ) ) {
                    Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , "&c&lBlockByBlockHub &7- Reloading" );
            
                    Main.getInstance( ).getConfig( ).reloadConfig( );
                    Main.getInstance( ).getItems( ).reloadConfig( );
                    Main.getLang( ).reloadLang( );
            
                    Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , "&c&lBlockByBlockHub reloaded Successfully!" );
                    Settings.init( Main.getInstance( ).getConfig( ) );
                    Items.init( Main.getInstance( ).getItems( ) );
                } else {
                    Player p = ( Player ) context.getSender( );
                    final User target = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 0 ) );
                    if ( target == null || p == null) {
                        Main.getLang( ).sendErrorMsg( p , "player.not-found" );
                        return true;
                    }
                    new AdminMenu( LyApi.getPlayerMenuUtility( p ) , target ).open( );
                }
                return true;
            } else if ( context.getArgs( ).length == 2 && context.getSender( ) instanceof Player ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "worlds" ) ) {
                    final User user = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 1 ) );
                    if ( !(user == null) ) {
                        final Player p = ( Player ) context.getSender( );
                        new WorldManagerMenu( LyApi.getPlayerMenuUtility( p ) , user.getUUID( ) ).open( );
                
                    } else {
                        Main.getApi( ).getUtils( ).sendMessage( context.getSender( ) , "&cPlayer not found!" );
                    }
                    return true;
                }
            }
        }
        
        
        return false;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        if ( context.getSender( ).hasPermission( "blockbyblock.admin" ) ) {
            if ( context.getArgs( ).length == 0 ) {
                list.add( "reload" );
                list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( context.getSender( ).getName( ) ) );
            }
        }
        
        return list;
    }
}
