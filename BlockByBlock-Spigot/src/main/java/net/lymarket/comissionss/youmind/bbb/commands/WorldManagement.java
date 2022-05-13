package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.error.UserNotFoundException;
import net.lymarket.comissionss.youmind.bbb.common.error.WorldNotFoundError;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public final class WorldManagement implements ILyCommand {
    
    @Command(name = "world", permission = "blockbyblock.world", usage = "world", description = "World Management", aliases = {"w" , "worlds" , "mundo" , "mundos"})
    public boolean command( SCommandContext context ){
        
        if ( context.getSender( ) instanceof Player ) {
            final Player player = ( Player ) context.getSender( );
            if ( context.getArgs( ).length == 0 ) {
                new WorldManagerMenu( LyApi.getPlayerMenuUtility( player ) ).open( );
                return true;
            }
            if ( context.getArgs( ).length == 1 && player.hasPermission( "blockbyblock.world.other" ) ) {
                final String userName = context.getArg( 0 );
                final User user = Main.getInstance( ).getPlayers( ).getPlayer( userName );
                if ( user != null ) {
                    new WorldManagerMenu( LyApi.getPlayerMenuUtility( player ) , user.getUUID( ) ).open( );
                    return true;
                } else {
                    Main.getLang( ).sendErrorMsg( player , "player.not-found" , "player" , userName );
                    return false;
                }
            } else if ( context.getArgs( ).length == 2 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "delete" ) ) {
                    try {
                        final UUID uuid = UUID.fromString( context.getArg( 1 ) );
                        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( uuid );
                        if ( world.getOwner( ) == player.getUniqueId( ) || player.hasPermission( "blockbyblock.world.edit.other" ) ) {
                            Main.getInstance( ).getSocket( ).sendWorldDeleteRequest( player , world );
                        }
                    } catch ( IllegalArgumentException | WorldNotFoundError e ) {
                        Main.getLang( ).sendErrorMsg( player , "world.not-found" , "world" , context.getArg( 2 ) );
                    }
                }
            } else if ( context.getArgs( ).length == 3 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "trust" ) ) {
                    try {
                        final UUID uuid = UUID.fromString( context.getArg( 1 ) );
                        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( uuid );
                        final User userTarget = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 2 ) );
                        if ( world.getOwner( ) == player.getUniqueId( ) || player.hasPermission( "blockbyblock.world.edit.other" ) ) {
                            world.addMember( userTarget.getUUID( ) );
                            Main.getInstance( ).getWorlds( ).saveWorld( world );
                            final HashMap < String, String > replace = new HashMap <>( );
                            replace.put( "world" , world.getName( ) );
                            replace.put( "player" , userTarget.getName( ) );
                            Main.getLang( ).sendMsg( player , "world.trust.add" , replace );
                        } else {
                            Main.getLang( ).sendErrorMsg( player , "world.trust-no-permission" , "world" , world.getName( ) );
                        }
                        return true;
                    } catch ( IllegalArgumentException | WorldNotFoundError e ) {
                        Main.getLang( ).sendErrorMsg( player , "world.not-found" , "world" , context.getArg( 1 ) );
                    } catch ( NullPointerException e ) {
                        Main.getLang( ).sendErrorMsg( player , "player.not-found" , "player" , context.getArg( 2 ) );
                        
                    }
                    return true;
                } else if ( context.getArg( 0 ).equalsIgnoreCase( "untrust" ) ) {
                    try {
                        final UUID uuid = UUID.fromString( context.getArg( 1 ) );
                        final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( uuid );
                        final User userTarget = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 2 ) );
                        if ( world.getOwner( ) == player.getUniqueId( ) || player.hasPermission( "blockbyblock.world.edit.other" ) ) {
                            world.removeMember( userTarget.getUUID( ) );
                            Main.getInstance( ).getWorlds( ).saveWorld( world );
                            final HashMap < String, String > replace = new HashMap <>( );
                            replace.put( "world" , world.getName( ) );
                            replace.put( "player" , userTarget.getName( ) );
                            Main.getLang( ).sendMsg( player , "world.un-trust" , replace );
                        } else {
                            Main.getLang( ).sendErrorMsg( player , "world.un-trust-no-permission" , "world" , world.getName( ) );
                        }
                        
                    } catch ( IllegalArgumentException | WorldNotFoundError e ) {
                        Main.getLang( ).sendErrorMsg( player , "world.not-found" , "world" , context.getArg( 1 ) );
                    } catch ( NullPointerException e ) {
                        Main.getLang( ).sendErrorMsg( player , "player.not-found" , "player" , context.getArg( 2 ) );
                        
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
                        
                    } else if ( context.getArg( 1 ).equalsIgnoreCase( "owner" ) ) {
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
                    } else {
                        return false;
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
            list.add( "set" );
            list.add( "delete" );
            list.add( "visit" );
            list.add( "trust" );
            list.add( "untrust" );
        } else if ( context.getArgs( ).length == 2 ) {
            switch ( context.getArg( 0 ) ) {
                case "delete":
                case "visit":
                case "trust":
                case "untrust":
                case "set": {
                    if ( context instanceof Player ) {
                        list.addAll( Main.getInstance( ).getWorlds( ).getWorldsByUser( (( Player ) context.getSender( )).getUniqueId( ) ).stream( ).map( BWorld::getUUID ).map( UUID::toString ).collect( Collectors.toList( ) ) );
                    } else {
                        list.addAll( Main.getInstance( ).getWorlds( ).getWorlds( ).stream( ).map( BWorld::getUUID ).map( UUID::toString ).collect( Collectors.toList( ) ) );
                        
                    }
                    break;
                }
                default: {
                    list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( ) );
                }
            }
        } else if ( context.getArgs( ).length == 3 ) {
            if ( context.getArg( 0 ).equals( "trust" ) ) {
                try {
                    final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( context.getArg( 1 ) ) );
                    list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( context.getSender( ).getName( ) ) );
                    list.removeIf( p -> world.isMember( Main.getInstance( ).getPlayers( ).getPlayer( p ).getUUID( ) ) );
                } catch ( WorldNotFoundError | IllegalArgumentException | NullPointerException ignored ) {
                }
            } else if ( context.getArg( 0 ).equals( "untrust" ) ) {
                try {
                    final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( context.getArg( 1 ) ) );
                    list.addAll( Main.getInstance( ).getPlayers( ).getPlayersUUID( world.getMembers( ) ) );
                } catch ( WorldNotFoundError | IllegalArgumentException | NullPointerException ignored ) {
                }
            }
            
        } else if ( context.getArgs( ).length == 4 ) {
            if ( "set".equals( context.getArg( 0 ) ) ) {
                list.add( "owner" );
                list.add( "name" );
            }
        }
        
        
        return list;
    }
}
