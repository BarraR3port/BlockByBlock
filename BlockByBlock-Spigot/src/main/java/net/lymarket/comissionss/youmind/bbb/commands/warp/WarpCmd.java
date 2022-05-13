package net.lymarket.comissionss.youmind.bbb.commands.warp;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.settings.ServerType;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.transformers.Transformer;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class WarpCmd implements ILyCommand {
    
    @Command(name = "warp", permission = "blockbyblock.warp", aliases = {"w"})
    public boolean command( SCommandContext context ){
        if ( !(context.getSender( ) instanceof Player) ) {
            Main.getLang( ).sendErrorMsg( context.getSender( ) , "cant-execute-commands-from-console" );
            return true;
        }
        Player p = ( Player ) context.getSender( );
        final User user = Main.getInstance( ).getPlayers( ).getPlayer( p.getUniqueId( ) );
        if ( Settings.SERVER_TYPE != ServerType.WORLDS ) {
            Main.getLang( ).sendErrorMsg( p , "world.not-in-server" );
            return true;
        }
        switch ( context.getArgs( ).length ) {
            case 1: {
                switch ( context.getArg( 0 ) ) {
                    case "list": {
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
                        return true;
                    }
                    case "gotoworld": {
                        if ( p.hasPermission( "blockbyblock.warp.gotoworld" ) || user.getRank( ).isBuilder( ) ) {
                            p.teleport( Bukkit.getWorld( "warp" ).getSpawnLocation( ) );
                            Main.getLang( ).sendMsg( p , "warp.teleported-to-world" );
                            return true;
                        }
                        Main.getLang( ).sendErrorMsg( context.getSender( ) , "no-permission" );
                        return true;
                    }
                }
            }
            case 2: {
                switch ( context.getArg( 0 ) ) {
                    case "delete": {
                        try {
                            final UUID uuid = UUID.fromString( context.getArg( 1 ) );
                            Warp warp = Main.getInstance( ).getWarps( ).getWarp( uuid );
                            if ( warp.getOwner( ).equals( p.getUniqueId( ) ) || p.hasPermission( "blockbyblock.warp.delete.other" ) || user.getRank( ).isBuilder( ) ) {
                                Main.getInstance( ).getWarps( ).deleteWarp( warp );
                                Main.getLang( ).sendMsg( p , "warp.deleted" , "warp" , warp.getName( ) );
                            } else {
                                Main.getLang( ).sendErrorMsg( p , "warp.no-permission-to-delete" );
                            }
                            return true;
                        } catch ( IllegalArgumentException e ) {
                            Main.getLang( ).sendErrorMsg( p , "warp.invalid-name" );
                        }
                        return true;
                    }
                    case "goto": {
                        try {
                            final UUID uuid = UUID.fromString( context.getArg( 1 ) );
                            Warp warp = Main.getInstance( ).getWarps( ).getWarp( uuid );
                            if ( warp.isPublic( ) || warp.isMember( p.getUniqueId( ) ) || warp.getOwner( ).equals( p.getUniqueId( ) ) || p.hasPermission( "blockbyblock.warp.goto" ) || user.getRank( ).isBuilder( ) ) {
                                p.teleport( Transformer.toLocation( warp.getLocation( ) ) );
                            } else {
                                Main.getLang( ).sendErrorMsg( p , "warp.no-permission-to-go" );
                            }
                        } catch ( IllegalArgumentException e ) {
                            Main.getLang( ).sendErrorMsg( p , "warp.invalid-name" );
                            
                        }
                        return true;
                    }
                    case "create": {
                        if ( !p.getWorld( ).getName( ).equals( "warp" ) ) {
                            Main.getLang( ).sendErrorMsg( p , "warp.not-in-world-of-warps" );
                            return true;
                        }
                        final String warpName = context.getArg( 1 );
                        final Warp warp = new Warp( warpName , Transformer.toLoc( p.getLocation( ) , null , null ) , p.getUniqueId( ) , Settings.VERSION );
                        Main.getInstance( ).getWarps( ).createWarp( warp );
                        TextComponent text = new TextComponent( "" );
                        List < String > args = Main.getLang( ).getConfig( ).getStringList( "warp.warps-of-description" );
                        List < String > replaced = new ArrayList <>( );
                        for ( String arg : args ) {
                            replaced.add( arg.replace( "%warp%" , warp.getName( ) )
                                    .replace( "%world_name%" , (warp.getLocation( ).getWorld( ).contains( "-" ) ? warp.getLocation( ).getWorld( ).split( "-" )[0] : warp.getLocation( ).getWorld( )) )
                                    .replace( "%x_location%" , String.valueOf( warp.getLocation( ).getX( ) ) )
                                    .replace( "%y_location%" , String.valueOf( warp.getLocation( ).getY( ) ) )
                                    .replace( "%z_location%" , String.valueOf( warp.getLocation( ).getZ( ) ) )
                                    .replace( "%server%" , String.valueOf( warp.getLocation( ).getServer( ) ) )
                            );
                        }
                        text.addExtra( Utils.hoverOverMessageRunCommand( "&a" + warp.getName( ) , replaced , "/warp goto" + warp.getUUID( ) ) );
    
                        Main.getLang( ).sendMsg( p , "warp.created" , "warp" , text );
                        return true;
                    }
                }
    
            }
            default: {
                Utils.sendMessage( p , "&e&lWarps" );
                Utils.sendMessage( p , " " );
                Utils.sendMessage( p , "&aCommands: " );
                Utils.sendMessage( p , " " );
                Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/warp list" , Arrays.asList( "&7Con este comando se muestran todas" , "&7las warps disponibles." ) , "/warp list" ) );
                Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/warp create <nombre>" , Collections.singletonList( "&7Con este comando puedes crear una warp" ) , "/warp create " ) );
                Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/warp delete <nombre>" , Collections.singletonList( "&7Con este comando puedes borrar una warp" ) , "/warp delete " ) );
                Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/warp goto <nombre>" , Collections.singletonList( "&7Con este comando puedes ir a una warp" ) , "/warp goto " ) );
                if ( p.hasPermission( "blockbyblock.warp.gotoworld" ) || user.getRank( ).isBuilder( ) ) {
                    Utils.sendMessage( p , Utils.formatTC( "&e> &b" ) , Utils.hoverOverMessageRunCommand( "&b/warp gotoworld" , Collections.singletonList( "&7Con este comando puedes ir al mundo de warps" ) , "/warp gotoworld" ) );
                }
                return true;
            }
        }
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        return list;
    }
}
