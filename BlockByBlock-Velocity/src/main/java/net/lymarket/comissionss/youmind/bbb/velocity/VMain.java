package net.lymarket.comissionss.youmind.bbb.velocity;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.lymarket.comissionss.youmind.bbb.common.log.Slf4jPluginLogger;
import net.lymarket.comissionss.youmind.bbb.velocity.commands.Lobby;
import net.lymarket.comissionss.youmind.bbb.velocity.config.Config;
import net.lymarket.comissionss.youmind.bbb.velocity.listener.PlayerEvents;
import net.lymarket.comissionss.youmind.bbb.velocity.listener.onPluginMessage;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.PlayersRepository;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerManager;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.WorldManager;
import net.lymarket.comissionss.youmind.bbb.velocity.socketmanager.ServerSocketTask;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.ChatColor;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.lyapi.velocity.LyApiVelocity;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "blockbyblock", name = "BlockByBlock", version = "1.0", authors = {"BarraR3port", "LyMarket Development Team"}, url = "https://lymarket.net/")
public final class VMain extends LyApiVelocity {
    
    private static VMain instance;
    private static Config.MainConfig config;
    private final ServerSocketManager serverSocketManager = new ServerSocketManager();
    private final ServerManager serverManager = new ServerManager();
    private final ProxyServer proxy;
    private final Slf4jPluginLogger logger;
    private final Path path;
    private PlayersRepository playersRepository;
    private WorldManager worldManager;
    
    
    /**
     * Constructor for ChatRegulator Plugin
     *
     * @param server the proxy server
     * @param logger logger
     * @param path   the plugin path
     */
    @Inject
    @Internal
    public VMain(final ProxyServer server, final Logger logger, final @DataDirectory Path path){
        super(server, "&cSin permisos");
        this.proxy = server;
        this.path = path;
        this.logger = new Slf4jPluginLogger(logger);
    }
    
    @Internal
    public static VMain getInstance( ){
        return instance;
    }
    
    @Internal
    public static void debug(String msg){
        if (config.isDebug()){
            instance.getLogger().info(ChatColor.RED + "[DEBUG] " + ChatColor.LIGHT_PURPLE + msg);
        }
    }
    
    @Internal
    public static Config.MainConfig getConfig( ){
        return config;
    }
    
    @Subscribe
    @Internal
    public void onProxyInitialize(ProxyInitializeEvent event){
        // Plugin startup logic
        instance = this;
        config = new Config(path).getConfig();
        proxy.getChannelRegistrar().register(new LegacyChannelIdentifier("lymarket:bbb"));
        proxy.getEventManager().register(this, new onPluginMessage());
        proxy.getEventManager().register(this, new PlayerEvents());
        //serverManager.init( );
        
        
        if (ServerSocketTask.init()){
            debug("ServerSocketTask started");
        }
        String url = /*config.getDb_urli( ).equals( "" ) ? "mongodb://" + config.getDb_username( ) + ":" + config.getDb_password( ) + "@" + config.getDb_host( ) + ":" + config.getDb_port( ) :*/ config.getDb_urli();
        final MongoDBClient mongo = new MongoDBClient(url, config.getDb_database());
        playersRepository = new PlayersRepository(mongo, "players");
        worldManager = new WorldManager(mongo, "worlds");
        proxy.getScheduler().buildTask(this, this::sendInfoToServers).repeat(10, TimeUnit.SECONDS).schedule();
        new Lobby(proxy.getCommandManager());
    }
    
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event){
        // Plugin shutdown logic
        ServerSocketTask.stopTasks();
    }
    
    private void sendInfoToServers( ){
        getServerSocketManager().getSocketByServer().forEach((server, socket) -> {
            if (server.startsWith("PW-")){
                final JsonObject js = new JsonObject();
                js.addProperty("type", "UPDATE_ONLINE_PLAYERS_IN_WORLDS");
                socket.sendMessage(js);
            }
        });
    
    }
    
    @Internal
    public ServerSocketManager getServerSocketManager( ){
        return serverSocketManager;
    }
    
    @Internal
    public ServerManager getServerManager( ){
        return serverManager;
    }
    
    @Internal
    public ProxyServer getProxy( ){
        return proxy;
    }
    
    @Internal
    public Slf4jPluginLogger getLogger( ){
        return logger;
    }
    
    public WorldManager getWorldManager( ){
        return worldManager;
    }
    
    public PlayersRepository getPlayers( ){
        return playersRepository;
    }
}
