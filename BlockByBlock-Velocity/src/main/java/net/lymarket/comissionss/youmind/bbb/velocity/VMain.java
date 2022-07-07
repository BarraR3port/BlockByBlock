package net.lymarket.comissionss.youmind.bbb.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ProxyStats;
import net.lymarket.comissionss.youmind.bbb.common.log.Slf4jPluginLogger;
import net.lymarket.comissionss.youmind.bbb.velocity.commands.Lobby;
import net.lymarket.comissionss.youmind.bbb.velocity.commands.VAdmin;
import net.lymarket.comissionss.youmind.bbb.velocity.config.Config;
import net.lymarket.comissionss.youmind.bbb.velocity.listener.PlayerEvents;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.PlayersRepository;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerManager;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.WorldManager;
import net.lymarket.comissionss.youmind.bbb.velocity.socketmanager.ProxySocketServer;
import net.lymarket.comissionss.youmind.bbb.velocity.socketmanager.ServerSocketTask;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.ChatColor;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.lyapi.velocity.LyApiVelocity;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "blockbyblock",
        name = "BlockByBlock",
        version = "1.2.1",
        authors = {"BarraR3port"},
        url = "https://lymarket.net/",
        dependencies = {@Dependency(id = "luckperms")})
public final class VMain extends LyApiVelocity {
    
    private static VMain instance;
    private static Config config;
    private final ServerSocketManager serverSocketManager = new ServerSocketManager();
    private final ServerManager serverManager = new ServerManager();
    private final ProxyServer proxy;
    private final Slf4jPluginLogger logger;
    private final Path path;
    private PlayersRepository playersRepository;
    private WorldManager worldManager;
    public final ProxyStats proxyServersStats;
    
    
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
        this.proxyServersStats = new ProxyStats();
    }
    
    @Internal
    public static VMain getInstance( ){
        return instance;
    }
    
    @Internal
    public static void debug(String msg){
        if (config.getConfig().isDebug()){
            instance.getLogger().info(ChatColor.RED + "[DEBUG] " + ChatColor.LIGHT_PURPLE + msg);
        }
    }
    
    @Internal
    public static Config getConfig( ){
        return config;
    }
    
    @Subscribe
    @Internal
    public void onProxyInitialize(ProxyInitializeEvent event){
        // Plugin startup logic
        instance = this;
        config = new Config(path);
        proxy.getChannelRegistrar().register(new LegacyChannelIdentifier("lymarket:bbb"));
        proxy.getEventManager().register(this, new PlayerEvents());
        //serverManager.init( );
        if (ServerSocketTask.init()){
            debug("ServerSocketTask started");
        }
        String url = /*config.getDb_urli( ).equals( "" ) ? "mongodb://" + config.getDb_username( ) + ":" + config.getDb_password( ) + "@" + config.getDb_host( ) + ":" + config.getDb_port( ) :*/ config.getConfig().getDb_urli();
        final MongoDBClient mongo = new MongoDBClient(url, config.getConfig().getDb_database());
        playersRepository = new PlayersRepository(mongo, "players");
        worldManager = new WorldManager(mongo, "worlds");
    
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), this::sendInfo).repeat(5, TimeUnit.SECONDS).schedule();
    
        new Lobby(proxy.getCommandManager());
        new VAdmin(proxy.getCommandManager());
    
    }
    
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event){
        // Plugin shutdown logic
        ServerSocketTask.stopTasks();
    }
    
    public void sendInfo( ){
        for ( ProxySocketServer socket : VMain.getInstance().getServerSocketManager().getSocketByServer().values() ){
            try {
                socket.sendProxyServerStats(proxyServersStats);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
