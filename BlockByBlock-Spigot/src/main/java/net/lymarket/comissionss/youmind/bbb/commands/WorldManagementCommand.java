package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.error.UserNotFoundException;
import net.lymarket.comissionss.youmind.bbb.common.error.WorldNotFoundError;
import net.lymarket.comissionss.youmind.bbb.menu.MainMenu;
import net.lymarket.comissionss.youmind.bbb.socket.SpigotSocketClient;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class WorldManagementCommand implements ILyCommand {
    
    @Command(name = "world", permission = "blockbyblock.world", usage = "world", description = "World Management", aliases = {"w" , "worlds" , "mundo" , "mundos"})
    public boolean command( SCommandContext context ){
        
        if ( context.getSender( ) instanceof Player ) {
            final Player player = ( Player ) context.getSender( );
            if ( context.getArgs( ).length == 0 && player.hasPermission( "blockbyblock.world" ) ) {
                new MainMenu( LyApi.getPlayerMenuUtility( player ) ).open( );
                return true;
            }
            if ( context.getArgs( ).length == 1 && player.hasPermission( "blockbyblock.world.other" ) ) {
                final String userName = context.getArg( 0 );
                final User user = Main.getInstance( ).getPlayers( ).getPlayer( userName );
                if ( user != null ) {
                    new MainMenu( LyApi.getPlayerMenuUtility( player ) , user.getUUID( ) ).open( );
                } else {
                    Main.getLang( ).sendErrorMsg( player , "player.not-found" , "player" , userName );
                }
            }
            if ( context.getArgs( ).length == 3 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "delete" ) ) {
                    try {
                        final UUID uuid = UUID.fromString( context.getArg( 2 ) );
                        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( uuid );
                        if ( world.getOwner( ) == player.getUniqueId( ) || player.hasPermission( "blockbyblock.world.edit.other" ) ) {
                            Main.getInstance( ).getSocket( ).sendMessage( SpigotSocketClient.formatWorldDeleteRequest( player , world ) );
                        }
                    } catch ( IllegalArgumentException | WorldNotFoundError e ) {
                        Main.getLang( ).sendErrorMsg( player , "world.not-found" , "world" , context.getArg( 2 ) );
                    }
                }
            } else if ( context.getArgs( ).length == 4 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "set" ) ) {
                    if ( context.getArg( 1 ).equalsIgnoreCase( "name" ) ) {
                        try {
                            final UUID uuid = UUID.fromString( context.getArg( 2 ) );
                            final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( uuid );
                            final String name = context.getArg( 3 );
                            if ( world.getOwner( ) == player.getUniqueId( ) || player.hasPermission( "blockbyblock.world.edit.other" ) ) {
                                world.setName( name );
                                Main.getInstance( ).getWorlds( ).saveWorld( world );
                                Main.getLang( ).sendMsg( player , "world.set-name" , "name" , name );
                            }
                        } catch ( IllegalArgumentException | WorldNotFoundError e ) {
                            Main.getLang( ).sendErrorMsg( player , "world.not-found" , "world" , context.getArg( 2 ) );
                        }
                        
                    }
                    if ( context.getArg( 1 ).equalsIgnoreCase( "owner" ) ) {
                        try {
                            final UUID uuid = UUID.fromString( context.getArg( 2 ) );
                            final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( uuid );
                            final String name = context.getArg( 3 );
                            final User user = Main.getInstance( ).getPlayers( ).getPlayer( name );
                            if ( world.getOwner( ) == player.getUniqueId( ) || player.hasPermission( "blockbyblock.world.edit.other" ) ) {
                                world.setOwner( user.getUUID( ) );
                                Main.getInstance( ).getWorlds( ).saveWorld( world );
                                final HashMap < String, String > replace = new HashMap <>( );
                                replace.put( "world" , world.getUUID( ).toString( ) );
                                replace.put( "player" , user.getName( ) );
                                Main.getLang( ).sendMsg( player , "world.set-owner" , replace );
                            }
                        } catch ( IllegalArgumentException | WorldNotFoundError e ) {
                            Main.getLang( ).sendErrorMsg( player , "world.not-found" , "world" , context.getArg( 2 ) );
                        } catch ( UserNotFoundException e ) {
                            Main.getLang( ).sendErrorMsg( player , "player.not-found" , "player" , context.getArg( 3 ) );
                        }
                        
                    }
                }
            }
        }
        
        
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        
        if ( context.getArgs( ).length == 1 ) {
            return Main.getInstance( ).getPlayers( ).getPlayersName( context.getSender( ).getName( ) );
        }
        
        return list;
    }
}
