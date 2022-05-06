package net.lymarket.comissionss.youmind.bbb;

import com.grinderwolf.swm.api.SlimePlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.node.Node;
import net.lymarket.comissionss.youmind.bbb.commands.*;
import net.lymarket.comissionss.youmind.bbb.common.BBBApi;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.config.ConfigManager;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.lang.ESLang;
import net.lymarket.comissionss.youmind.bbb.listener.lobby.LobbyPlayerEvents;
import net.lymarket.comissionss.youmind.bbb.listener.plot.PlotsPlayerEvent;
import net.lymarket.comissionss.youmind.bbb.listener.plugin.PluginMessage;
import net.lymarket.comissionss.youmind.bbb.listener.world.WorldPlayerEvents;
import net.lymarket.comissionss.youmind.bbb.papi.Placeholders;
import net.lymarket.comissionss.youmind.bbb.settings.ServerType;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.socket.ProxyMSGManager;
import net.lymarket.comissionss.youmind.bbb.socket.SpigotSocketClient;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.comissionss.youmind.bbb.users.PlayersRepository;
import net.lymarket.comissionss.youmind.bbb.world.WorldManager;
import net.lymarket.common.config.ConfigGenerator;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.common.error.LyApiInitializationError;
import net.lymarket.common.lang.ILang;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.menu.IUpdatableMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class Main extends JavaPlugin implements BBBApi {
    
    private static LyApi api;
    private static Main instance;
    private static SlimePlugin slimePlugin;
    private static LuckPerms lpApi;
    public WorldManager worldManager;
    private Config config;
    private Config items;
    private String version;
    private PlayersRepository players;
    private VersionSupport nms;
    private SpigotSocketClient socket;
    
    public static LyApi getApi( ){
        return api;
    }
    
    public static ILang getLang( ){
        return LyApi.getLanguage( );
    }
    
    public static Main getInstance( ){
        return instance;
    }
    
    public static LuckPerms getLuckPerms( ){
        return lpApi;
    }
    
    public static SlimePlugin getSlimePlugin( ){
        return slimePlugin;
    }
    
    @Override
    public void debug( String message ){
        if ( Settings.DEBUG ) {
            instance.getLogger( ).info( "DEBUG: " + message );
        }
    }
    
    @Override
    public void onEnable( ){
        instance = this;
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( this , "BungeeCord" );
        getServer( ).getMessenger( ).registerIncomingPluginChannel( this , "lymarket:bbb" , new PluginMessage( ) );
        getServer( ).getMessenger( ).registerOutgoingPluginChannel( this , "lymarket:bbb" );
        final ConfigManager configManager = new ConfigManager( this );
        config = configManager.getConfig( );
        items = configManager.getItems( );
        try {
            api = new LyApi( this , "BlockByBlock" , "&cSin permisos" , new ESLang( new ConfigGenerator( this , "es.yml" ) , config.getString( "global.prefix" ) , "&c[&4ERROR&c]" ) );
        } catch ( LyApiInitializationError e ) {
            e.printStackTrace( );
            getServer( ).shutdown( );
        }
        Settings.init( config );
        Items.init( items );
        version = Bukkit.getServer( ).getClass( ).getName( ).split( "\\." )[3];
        
        try {
            Class < ? > supp = Class.forName( "net.lymarket.comissionss.youmind.bbb.support.version." + version + "." + version );
            this.nms = ( VersionSupport ) supp.getConstructor( Class.forName( "org.bukkit.plugin.java.JavaPlugin" ) ).newInstance( this );
        } catch ( InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                  ClassNotFoundException e ) {
            e.printStackTrace( );
            getServer( ).shutdown( );
        }
        if ( Bukkit.getPluginManager( ).getPlugin( "LuckPerms" ) != null ) {
            RegisteredServiceProvider < LuckPerms > provider = Bukkit.getServicesManager( ).getRegistration( LuckPerms.class );
            if ( provider != null ) {
                lpApi = provider.getProvider( );
            } else {
                getLogger( ).log( Level.SEVERE , String.format( "[%s] - Disabled due to no LuckPerms dependency found!" , getDescription( ).getName( ) ) );
                getServer( ).getPluginManager( ).disablePlugin( this );
                getServer( ).shutdown( );
            }
        }
        
        try {
            slimePlugin = ( SlimePlugin ) Bukkit.getPluginManager( ).getPlugin( "SlimeWorldManager" );
        } catch ( Exception e ) {
            e.printStackTrace( );
            getServer( ).shutdown( );
        }
        
        if ( Bukkit.getPluginManager( ).getPlugin( "PlaceholderAPI" ) != null ) {
            new Placeholders( this ).register( );
        }
        switch ( Settings.SERVER_TYPE ) {
            case LOBBY: {
                getServer( ).getPluginManager( ).registerEvents( new LobbyPlayerEvents( ) , this );
                break;
            }
            case WORLDS: {
                getServer( ).getPluginManager( ).registerEvents( new WorldPlayerEvents( ) , this );
                break;
            }
            case PLOT: {
                getServer( ).getPluginManager( ).registerEvents( new PlotsPlayerEvent( ) , this );
                break;
            }
        }
        
        getServer( ).getPluginManager( ).registerEvents( new ProxyMSGManager( ) , this );
        api.getCommandService( ).registerCommands( new MainCommand( ) );
        api.getCommandService( ).registerCommands( new WorldManagementCommand( ) );
        api.getCommandService( ).registerCommands( new SetSpawnCommand( ) );
        api.getCommandService( ).registerCommands( new DelSpawnCommand( ) );
        api.getCommandService( ).registerCommands( new SpawnCommand( ) );
        api.getCommandService( ).registerCommands( new MenuCommand( ) );
        api.getCommandService( ).registerCommands( new AdminCommand( ) );
        api.getCommandService( ).registerCommands( new RankCommand( ) );
        
        final MongoDBClient mongo = new MongoDBClient( "mongodb://" + config.getString( "db.host" ) + ":" + config.getString( "db.port" ) , config.getString( "db.database" ) );
        players = new PlayersRepository( mongo , "players" );
        worldManager = new WorldManager( mongo , "worlds" );
        try {
            socket = new SpigotSocketClient( players , worldManager ).init( );
        } catch ( IOException | IllegalArgumentException e ) {
            e.printStackTrace( );
            getServer( ).shutdown( );
        }
        socket.sendFormattedUpdate( );
        
        getServer( ).getScheduler( ).runTaskTimer( this , ( ) -> {
            
            for ( Player p : Bukkit.getOnlinePlayers( ) ) {
                if ( p.getOpenInventory( ).getTopInventory( ).getHolder( ) instanceof IUpdatableMenu ) {
                    (( IUpdatableMenu ) p.getOpenInventory( ).getTopInventory( ).getHolder( )).reOpen( );
                }
            }
            
        } , 10 , 20L );
        //new PacketManager( this );
        
    }
    
    @Override
    public Config getConfig( ){
        return config;
    }
    
    public Config getItems( ){
        return items;
    }
    
    
    public String getVersion( ){
        return version;
    }
    
    public PlayersRepository getPlayers( ){
        return players;
    }
    
    public VersionSupport getNMS( ){
        return nms;
    }
    
    public WorldManager getWorlds( ){
        return worldManager;
    }
    
    public SpigotSocketClient getSocket( ){
        return socket;
    }
    
    @Override
    public String getProxyServerName( ){
        return Settings.PROXY_SERVER_NAME;
    }
    
    
    public CompletableFuture < Void > managePermissions( UUID player_uuid , UUID world_uuid , boolean delete ){
        if ( Settings.SERVER_TYPE != ServerType.WORLDS ) return CompletableFuture.completedFuture( null );
        Main.getInstance( ).debug( "[Permission Manager] Updating permissions to " + player_uuid + " in world " + world_uuid );
        Main.getInstance( ).debug( "[Permission Manager] Removing all permissions from worlds." );
        return lpApi.getUserManager( ).modifyUser( player_uuid , user -> {
            for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ) {
                for ( final BWorld world : worldManager.getWorlds( ) ) {
                    user.data( ).remove( Node.builder( perm ).context( MutableContextSet.of( "world" , world.getUUID( ).toString( ) ).mutableCopy( ) ).build( ) );
                }
            }
            if ( delete ) return;
            Main.getInstance( ).debug( "[Permission Manager] Removed all permissions from worlds." );
            Main.getInstance( ).debug( "[Permission Manager] Adding all permissions to world=" + world_uuid );
            for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ) {
                user.data( ).add( Node.builder( perm ).context( MutableContextSet.of( "world" , world_uuid.toString( ) ).mutableCopy( ) ).build( ) );
            }
        } );
    }
    
    public CompletableFuture < Void > removePermissionsInOneWorld( UUID player_uuid , UUID world_uuid ){
        if ( Settings.SERVER_TYPE != ServerType.WORLDS ) return CompletableFuture.completedFuture( null );
        Main.getInstance( ).debug( "[Permission Manager] Updating permissions to " + player_uuid + " in world " + world_uuid );
        Main.getInstance( ).debug( "[Permission Manager] Removing all permissions from worlds." );
        return lpApi.getUserManager( ).modifyUser( player_uuid , user -> {
            for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ) {
                user.data( ).remove( Node.builder( perm ).context( MutableContextSet.of( "world" , world_uuid.toString( ) ).mutableCopy( ) ).build( ) );
            }
        } );
    }
    
}
