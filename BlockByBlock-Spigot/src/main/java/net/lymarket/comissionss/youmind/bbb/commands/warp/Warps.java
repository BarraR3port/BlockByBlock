package net.lymarket.comissionss.youmind.bbb.commands.warp;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.settings.ServerType;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Warps implements ILyCommand {
    
    @Command(name = "warps", permission = "blockbyblock.warp.list")
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
        if ( context.getArgs( ).length == 0 ) {
            ArrayList < Warp > warps = Main.getInstance( ).getWarps( ).getWarpsByVersion( Settings.VERSION );
            if ( warps == null || warps.isEmpty( ) ) {
                Main.getLang( ).sendErrorMsg( context.getSender( ) , "warp.no-warps" );
                return true;
            }
            int warpsPerPage = 7;
            int warpsIHave = warps.size( );
            
            int pages = (warpsIHave / warpsPerPage) + (warpsIHave % warpsPerPage == 0 ? 0 : 1);
            
            Utils.sendMessage( context.getSender( ) , Main.getLang( ).getMSG( "warp.warp-list" ) );
            
            for ( int i = 1; i <= pages; i++ ) {
                int max = Math.min( i * warpsPerPage , warpsIHave );
                int min = Math.min( (i - 1) * warpsPerPage , warpsIHave );
                TextComponent text = new TextComponent( "" );
                
                Utils.sendMessage( p , Utils.formatTC( Main.getLang( ).getMSG( "warp.warps-of-page" ).replace( "%currentPage%" , String.valueOf( i ) ).replace( "%pages%" , String.valueOf( pages ) ) ) );
                
                for ( int a = min; a < max; a++ ) {
                    List < String > args = Main.getLang( ).getConfig( ).getStringList( "warp.warps-of-description" );
                    Warp h = warps.get( a );
                    List < String > replaced = new ArrayList <>( );
                    for ( String arg : args ) {
                        replaced.add( arg.replace( "%warp%" , h.getName( ) )
                                .replace( "%world_name%" , (h.getLocation( ).getWorld( ).contains( "-" ) ? h.getLocation( ).getWorld( ).split( "-" )[0] : h.getLocation( ).getWorld( )) )
                                .replace( "%x_location%" , String.valueOf( h.getLocation( ).getX( ) ) )
                                .replace( "%y_location%" , String.valueOf( h.getLocation( ).getY( ) ) )
                                .replace( "%z_location%" , String.valueOf( h.getLocation( ).getZ( ) ) )
                                .replace( "%server%" , String.valueOf( h.getLocation( ).getServer( ) ) )
                        );
                    }
                    text.addExtra( Utils.hoverOverMessageRunCommand( "&a" + h.getName( ) , replaced , "/warp goto " + h.getUUID( ) ) );
                    text.addExtra( Utils.formatTC( (a < max - 1) ? "&7, " : "" ) );
                }
                Utils.sendMessage( p , text );
            }
        } else {
            Main.getLang( ).sendErrorMsg( context.getSender( ) , "wrong-command" , "command" , "/warps" );
        }
        
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        return new ArrayList <>( );
    }
}
