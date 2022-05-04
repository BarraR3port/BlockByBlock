package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class MainCommand implements ILyCommand {
    
    
    /**
     * @param context
     *
     * @return
     */
    @Command(name = "ymh", permission = "blockbyblock.admin", aliases = "ymhub", description = "YouMindHub Command")
    public boolean command( SCommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            Player player = ( Player ) context.getSender( );
            if ( !player.hasPermission( "blockbyblock.admin" ) ) return false;
            if ( context.getArgs( ).length == 0 ) {
                Main.getApi( ).getUtils( ).sendMessage( player , "&c&lBlockByBlockHub &7- &a&lV" + Main.getInstance( ).getDescription( ).getVersion( ) );
                Main.getApi( ).getUtils( ).sendMessage( player , " " );
                Main.getApi( ).getUtils( ).sendMessage( player , "&aCommands: " );
                Main.getApi( ).getUtils( ).sendMessage( player , " " );
                Main.getApi( ).getUtils( ).sendMessage( player , " > ymh reload" );
                return true;
            } else if ( context.getArgs( ).length == 1 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "reload" ) ) {
                    Main.getApi( ).getUtils( ).sendMessage( player , "&c&lBlockByBlockHub &7- Reloading" );
                    
                    Main.getInstance( ).getConfig( ).reloadConfig( );
                    Main.getInstance( ).getItems( ).reloadConfig( );
                    Main.getLang( ).reloadLang( );
                    
                    Main.getApi( ).getUtils( ).sendMessage( player , "&c&lBlockByBlockHub reloaded Successfully!" );
                    Settings.init( Main.getInstance( ).getConfig( ) );
                    Items.init( Main.getInstance( ).getItems( ) );
                    return true;
                }
            }
        }
        
        
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        if ( context.getSender( ) instanceof Player ) {
            Player player = ( Player ) context.getSender( );
            if ( !player.hasPermission( "blockbyblock.admin" ) ) return new ArrayList <>( );
        }
        return new ArrayList <>( );
    }
}
