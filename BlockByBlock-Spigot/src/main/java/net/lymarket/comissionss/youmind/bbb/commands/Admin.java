package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.error.HomeNotFoundError;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.admin.AdminMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.common.commands.*;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Admin implements ILyCommand {
    
    @Command(name = "admin", permission = "blockbyblock.admin", usage = "admin", description = "Admin Command")
    public boolean command( SCommandContext context ){
        if ( context.getArgs( ).length == 1 ) {
            if ( context.getArg( 0 ).equalsIgnoreCase( "reload" ) ) {
                Utils.sendMessage( context.getSender( ) , "&c&lBlockByBlock &7- Reloading" );
                Main.getInstance( ).getConfig( ).reloadConfig( );
                Main.getInstance( ).getItems( ).reloadConfig( );
                Main.getLang( ).reloadLang( );
                Settings.init( Main.getInstance( ).getConfig( ) );
                Items.init( Main.getInstance( ).getItems( ) );
                Utils.sendMessage( context.getSender( ) , "&c&lBlockByBlock reloaded Successfully!" );
                return true;
            } else if ( context.getArg( 0 ).equalsIgnoreCase( "debug" ) ) {
                Settings.DEBUG = !Settings.DEBUG;
                Main.getInstance( ).getConfig( ).set( "global.debug" , Settings.DEBUG );
                Main.getInstance( ).getConfig( ).saveData( );
                Utils.sendMessage( context.getSender( ) , "&c&lBlockByBlock &dDEBUG " + (Settings.DEBUG ? "&aEnabled" : "&cDisabled") );
                return true;
            }
        }
        if ( context.getSender( ) instanceof Player ) {
            Player p = ( Player ) context.getSender( );
            if ( context.getArgs( ).length == 2 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "worlds" ) ) {
                    final User user = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 1 ) );
                    if ( !(user == null) ) {
                        new WorldManagerMenu( LyApi.getPlayerMenuUtility( p ) , user.getUUID( ) ).open( );
                    } else {
                        Utils.sendMessage( context.getSender( ) , "&cPlayer not found!" );
                    }
                    return true;
                } else if ( context.getArg( 0 ).equalsIgnoreCase( "homes" ) ) {
                    if ( Settings.SERVER_TYPE != ServerType.WORLDS ) {
                        Main.getLang( ).sendErrorMsg( context.getSender( ) , "world.not-in-server" );
                        return true;
                    }
                    final User user = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 1 ) );
                    final String homeName = context.getArg( 0 );
                    try {
                        final SpigotHome home = Main.getInstance( ).getHomes( ).getUserHomeByName( user.getUUID( ) , homeName );
                        if ( home != null ) {
                            Main.getLang( ).sendMsg( context.getSender( ) , "home.tp-to-home" , "home" , home.getName( ) );
                            final Loc homeLoc = home.getLocation( );
                            if ( Settings.SERVER_NAME.equals( homeLoc.getServer( ) ) ) {
                                (( Player ) context.getSender( )).teleport( home.getBukkitLocation( ) );
                            } else {
                                final HashMap < String, String > replace = new HashMap <>( );
                                replace.put( "home" , home.getName( ) );
                                replace.put( "world" , home.getLocation( ).getWorld( ) );
                                Main.getLang( ).sendErrorMsg( context.getSender( ) , "home.not-found-in-world" , replace );
                            }
                            return true;
                        } else {
                            throw new HomeNotFoundError( homeName );
                        }
                    } catch ( HomeNotFoundError e ) {
                        Main.getLang( ).sendErrorMsg( context.getSender( ) , "home.not-found" , "home" , homeName );
                    }
                    return true;
                } else if ( context.getArg( 0 ).equalsIgnoreCase( "menu" ) ) {
                    final SpigotUser target = Main.getInstance( ).getPlayers( ).getPlayer( context.getArg( 1 ) );
                    if ( target == null || p == null ) {
                        Main.getLang( ).sendErrorMsg( p , "player.not-found" , "player" , context.getArg( 1 ) );
                        return true;
                    }
                    new AdminMenu( LyApi.getPlayerMenuUtility( p ) , target ).open( );
                    return true;
                }
            }
            Utils.sendMessage( p , "&c&lBlockByBlock &7- &a&lV" + Main.getInstance( ).getDescription( ).getVersion( ) );
            Utils.sendMessage( p , " " );
            Utils.sendMessage( p , "&aCommands: " );
            Utils.sendMessage( p , " " );
            Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/admin menu <usuario>" , Collections.singletonList( "&7Con este comando adminstras al jugador de" ) , "/admin menu " ) );
            Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/admin worlds <usuario>" , Arrays.asList( "&7Con este comando ves los mundos de" , "&7un usuario específico." ) , "/admin worlds " ) );
            Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/admin homes <usuario>" , Arrays.asList( "&7Con este comando ves los homes de" , "&7un usuario específico." ) , "/admin homes " ) );
            Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageSuggestCommand( "&b/admin reload" , Arrays.asList( "&7Con este comando se recarga" , "&7la información de la config." ) , "/admin reload" ) );
            Utils.sendMessage( p , Utils.formatTC( "&e> " ) , Utils.hoverOverMessageRunCommand( "&b/admin debug" , Arrays.asList( "&7Con este comando activas o desactivas" , "&7el modo debug." ) , "/admin debug" ) );
            return true;
        }
        return false;
    }
    
    @Tab
    public ArrayList < String > tabComplete( STabContext context ){
        final ArrayList < String > list = new ArrayList <>( );
        if ( context.getSender( ).hasPermission( "blockbyblock.admin" ) ) {
            if ( context.getArgs( ).length == 1 ) {
                list.add( "reload" );
                list.add( "worlds" );
                list.add( "homes" );
                list.add( "worlds" );
                list.add( "debug" );
                list.add( "menu" );
                return list;
            }
            if ( context.getArgs( ).length == 2 ) {
                if ( context.getArg( 0 ).equalsIgnoreCase( "menu" ) || context.getArg( 0 ).equalsIgnoreCase( "worlds" ) || context.getArg( 0 ).equalsIgnoreCase( "homes" ) ) {
                    list.addAll( Main.getInstance( ).getPlayers( ).getPlayersName( context.getSender( ).getName( ) ) );
                }
            }
        }
        
        return list;
    }
}
