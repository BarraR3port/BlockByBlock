package net.lymarket.comissionss.youmind.bbb.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Collectors;

public class Placeholders extends PlaceholderExpansion {
    
    // We get an instance of the plugin later.
    private final Main plugin;
    
    public Placeholders( Main plugin ){
        this.plugin = plugin;
    }
    
    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist( ){
        return true;
    }
    
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister( ){
        return true;
    }
    
    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor( ){
        return "BarraR3port";
    }
    
    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier( ){
        return "bbb";
    }
    
    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion( ){
        return plugin.getDescription( ).getVersion( );
    }
    
    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link Player}.
     * @param identifier A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    
    // PLACEHOLDERS
    @Override
    public String onPlaceholderRequest( Player player , @NotNull String identifier ){
        
        if ( player == null ) return "";
        
        switch ( identifier ) {
            case "server": {
                return Settings.PROXY_SERVER_NAME;
            }
            case "health": {
                return String.valueOf( player.getHealth( ) );
            }
            case "gamemode": {
                return String.valueOf( player.getGameMode( ) );
            }
            
        }
        
        final User p = Main.getInstance( ).getPlayers( ).getPlayer( player.getName( ) );
        
        if ( p == null ) return "Jugador no encontrado";
        //%lydark_lycoins%
        
        switch ( identifier ) {
            case "stats_blocks_placed": {
                return String.valueOf( p.getStats( ).getBLOCKS_PLACED( ) );
            }
            case "stats_blocks_broken": {
                return String.valueOf( p.getStats( ).getBLOCKS_BROKEN( ) );
            }
            
            case "stats_time_played": {
                return String.valueOf( p.getStats( ).getTIME_PLAYED( ) );
            }
            case "address": {
                return p.getAddress( );
            }
            /*case "lycoins_formatted": {
                DecimalFormat df = new DecimalFormat( "#.##" );
                if ( p.getLyCoins( ).getCoins( ) > 1000000 ) {
                    return df.format( p.getLyCoins( ).getCoins( ) / 1000000 ) + "&eM ⛃";
                }
                if ( p.getLyCoins( ).getCoins( ) > 10000 ) {
                    return df.format( p.getLyCoins( ).getCoins( ) / 1000 ) + "&eK ⛃";
                }
                return p.getLyCoins( ).getCoins( ) + "&eK ⛃";
            }*/
        }
        try {
            final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( UUID.fromString( player.getWorld( ).getName( ) ) );
            if ( world == null ) return "Invalid World";
            switch ( identifier ) {
                case "world_name": {
                    return world.getName( );
                }
                case "world_members": {
                    return String.valueOf( world.getOnlineMembers( ).size( ) );
                }
                case "world_uuid": {
                    return world.getUUID( ).toString( );
                }
                case "world_members_list": {
                    return world.getOnlineMembers( ).stream( ).map( ( uuid ) -> Main.getInstance( ).getPlayers( ).getPlayer( uuid ).getName( ) ).collect( Collectors.joining( ", " ) );
                }
                case "world_version": {
                    return world.getVersion( );
                }
                case "world_server": {
                    return world.getServer( );
                }
                case "world_owner": {
                    return Main.getInstance( ).getPlayers( ).getPlayer( world.getOwner( ) ).getName( );
                }
                
            }
            
            
        } catch ( Exception e ) {
            return "Invalid World";
        }
        
        
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }
}
