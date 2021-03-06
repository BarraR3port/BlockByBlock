package net.lymarket.comissionss.youmind.bbb;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.node.Node;
import net.lymarket.comissionss.youmind.bbb.commands.*;
import net.lymarket.comissionss.youmind.bbb.commands.home.HomeCommand;
import net.lymarket.comissionss.youmind.bbb.commands.home.Homes;
import net.lymarket.comissionss.youmind.bbb.commands.home.RemoveHome;
import net.lymarket.comissionss.youmind.bbb.commands.home.SetHome;
import net.lymarket.comissionss.youmind.bbb.commands.spawn.DelSpawn;
import net.lymarket.comissionss.youmind.bbb.commands.spawn.SetSpawn;
import net.lymarket.comissionss.youmind.bbb.commands.spawn.Spawn;
import net.lymarket.comissionss.youmind.bbb.commands.warp.WarpCmd;
import net.lymarket.comissionss.youmind.bbb.commands.warp.Warps;
import net.lymarket.comissionss.youmind.bbb.common.BBBApi;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ProxyStats;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerName;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.config.ConfigManager;
import net.lymarket.comissionss.youmind.bbb.home.HomeManager;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.lang.ESLang;
import net.lymarket.comissionss.youmind.bbb.listener.ProxyMSGManager;
import net.lymarket.comissionss.youmind.bbb.listener.lobby.LobbyPlayerEvents;
import net.lymarket.comissionss.youmind.bbb.listener.plot.PlotsPlayerEvent;
import net.lymarket.comissionss.youmind.bbb.listener.plugin.PluginMessage;
import net.lymarket.comissionss.youmind.bbb.listener.world.WorldPlayerEvents;
import net.lymarket.comissionss.youmind.bbb.papi.Placeholders;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.socket.SpigotSocketClient;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.comissionss.youmind.bbb.users.PlayersRepository;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.comissionss.youmind.bbb.warp.SpigotWarp;
import net.lymarket.comissionss.youmind.bbb.warp.WarpManager;
import net.lymarket.comissionss.youmind.bbb.world.WorldManager;
import net.lymarket.common.config.ConfigGenerator;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.common.error.LyApiInitializationError;
import net.lymarket.common.lang.ILang;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.menu.IUpdatableMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class Main extends JavaPlugin implements BBBApi < SlimeWorld, SpigotUser, SpigotHome, SpigotWarp > {
    
    private static LyApi api;
    private static Main instance;
    private static SlimePlugin slimePlugin;
    private static LuckPerms lpApi;
    public WorldManager worlds;
    public HomeManager homes;
    private Config config;
    private Config items;
    public ProxyStats proxyStats = new ProxyStats();
    private String nms_version;
    private PlayersRepository players;
    private VersionSupport nms;
    private SpigotSocketClient socket;
    private WarpManager warps;
    private ViaAPI < Player > viaVersionApi;
    
    public static LyApi getApi( ){
        return api;
    }
    
    public static ILang getLang( ){
        return LyApi.getLanguage();
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
    public void debug(String message){
        if (Settings.DEBUG){
            instance.getLogger().info(ChatColor.RED + "[DEBUG] " + ChatColor.LIGHT_PURPLE + message);
        }
    }
    
    @Override
    public void error(String message){
        if (Settings.DEBUG){
            instance.getLogger().severe(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + message);
        }
    }
    
    @Override
    public void onEnable( ){
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "lymarket:bbb", new PluginMessage());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "lymarket:bbb");
        final ConfigManager configManager = new ConfigManager(this);
        config = configManager.getConfig();
        items = configManager.getItems();
        try {
            api = new LyApi(this, "BlockByBlock", "&c[&4ERROR&c] &cNo tienes el siguiente permiso:&e permission", new ESLang(new ConfigGenerator(this, "es.yml"), config.getString("global.prefix"), "&c[&4ERROR&c]"));
        } catch (LyApiInitializationError e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        this.nms_version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        Settings.init(config);
        Items.init(items);
        
        try {
            slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        } catch (Exception e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        
        try {
            Class < ? > supp = Class.forName("net.lymarket.comissionss.youmind.bbb.support.version." + nms_version + "." + nms_version);
            this.nms = (VersionSupport) supp.getConstructor(Class.forName("org.bukkit.plugin.java.JavaPlugin")).newInstance(this);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 ClassNotFoundException e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null){
            RegisteredServiceProvider < LuckPerms > provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null){
                lpApi = provider.getProvider();
            } else {
                getLogger().log(Level.SEVERE, String.format("[%s] - Disabled due to no LuckPerms dependency found!", getDescription().getName()));
                getServer().getPluginManager().disablePlugin(this);
                getServer().shutdown();
            }
        }
        
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Placeholders(this).register();
        }
        
        if (Bukkit.getPluginManager().getPlugin("ViaVersion") != null){
            viaVersionApi = Via.getAPI();
        }
        
        
        getServer().getPluginManager().registerEvents(new ProxyMSGManager(), this);
        api.getCommandService().registerCommands(new WorldManagement());
        api.getCommandService().registerCommands(new SetSpawn());
        api.getCommandService().registerCommands(new DelSpawn());
        api.getCommandService().registerCommands(new Spawn());
        api.getCommandService().registerCommands(new Menu());
        api.getCommandService().registerCommands(new Admin());
        api.getCommandService().registerCommands(new RankMenu());
        api.getCommandService().registerCommands(new Visit());
        api.getCommandService().registerCommands(new HomeCommand());
        api.getCommandService().registerCommands(new Homes());
        api.getCommandService().registerCommands(new RemoveHome());
        api.getCommandService().registerCommands(new SetHome());
        api.getCommandService().registerCommands(new WarpCmd());
        api.getCommandService().registerCommands(new Warps());
        api.getCommandService().registerCommands(new Undo());
        
        //final MongoDBClient mongo = new MongoDBClient( "mongodb://" + config.getString( "db.host" ) + ":" + config.getString( "db.port" ) , config.getString( "db.database" ) );
        final MongoDBClient mongo = new MongoDBClient(config.getString("db.urli"), "bbb");
        players = new PlayersRepository(mongo, "players");
        worlds = new WorldManager(mongo, "worlds");
        homes = new HomeManager(mongo, "homes");
        warps = new WarpManager(mongo, "warps");
        try {
            socket = new SpigotSocketClient(players, worlds, homes, warps, nms).init();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        socket.sendUpdate();
        
        switch(Settings.SERVER_TYPE){
            case LOBBY:{
                getServer().getPluginManager().registerEvents(new LobbyPlayerEvents(), this);
                break;
            }
            case WORLDS:{
                getServer().getPluginManager().registerEvents(new WorldPlayerEvents(), this);
                break;
            }
            case PLOT:{
                getServer().getPluginManager().registerEvents(new PlotsPlayerEvent(), this);
                break;
            }
        }
    
        getServer().getScheduler().runTaskTimerAsynchronously(this, ( ) -> {
            for ( Player p : Bukkit.getOnlinePlayers() ){
                if (p.getOpenInventory().getTopInventory().getHolder() instanceof IUpdatableMenu){
                    ((IUpdatableMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                }
            }
        }, 0L, 20L);
        //new PacketManager( this );
        
    }
    
    @Override
    public void onDisable( ){
        nms.saveWorlds();
        socket.sendDisconnectInfoToProxy();
        socket.disable();
        getServer().getScheduler().cancelTasks(this);
    }
    
    @Override
    public Config getConfig( ){
        return config;
    }
    
    public Config getItems( ){
        return items;
    }
    
    public PlayersRepository getPlayers( ){
        return players;
    }
    
    public WorldManager getWorlds( ){
        return worlds;
    }
    
    public HomeManager getHomes( ){
        return homes;
    }
    
    public WarpManager getWarps( ){
        return warps;
    }
    
    public SpigotSocketClient getSocket( ){
        return socket;
    }
    
    public String getVersion( ){
        return Settings.VERSION;
    }
    
    @Override
    public String getNMSVersion( ){
        return nms_version;
    }
    
    public ViaAPI < Player > getViaVersion( ){
        return viaVersionApi;
    }
    
    @Override
    public ServerName getProxyServerName( ){
        return Settings.SERVER_NAME;
    }
    
    public CompletableFuture < Void > managePermissions(UUID player_uuid, UUID world_uuid, boolean delete){
        if (Settings.SERVER_TYPE != ServerType.WORLDS) return CompletableFuture.completedFuture(null);
        Main.getInstance().debug("[Permission Manager] Updating permissions to " + player_uuid + " in world " + world_uuid);
        Main.getInstance().debug("[Permission Manager] Removing all permissions from worlds.");
        return lpApi.getUserManager().modifyUser(player_uuid, user -> {
            for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ){
                for ( final BWorld world : worlds.getWorlds() ){
                    user.data().remove(Node.builder(perm).context(MutableContextSet.of("world", world.getUUID().toString()).mutableCopy()).build());
                }
            }
            for ( final BWorld world : worlds.getWorlds() ){
                user.data().remove(Node.builder("blockbyblock.visit").context(MutableContextSet.of("world", world.getUUID().toString()).mutableCopy()).build());
            }
            Main.getInstance().debug("[Permission Manager] Removed all permissions from worlds.");
            if (delete) return;
            Main.getInstance().debug("[Permission Manager] Adding all permissions to world=" + world_uuid);
            for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ){
                user.data().add(Node.builder(perm).context(MutableContextSet.of("world", world_uuid.toString()).mutableCopy()).build());
            }
        });
    }
    
    public CompletableFuture < Void > manageVisitorPermissions(UUID player_uuid, UUID world_uuid, boolean delete){
        if (Settings.SERVER_TYPE != ServerType.WORLDS) return CompletableFuture.completedFuture(null);
        return lpApi.getUserManager().modifyUser(player_uuid, user -> {
            for ( final BWorld world : worlds.getWorlds() ){
                user.data().remove(Node.builder("blockbyblock.visit").context(MutableContextSet.of("world", world.getUUID().toString()).mutableCopy()).build());
            }
            Main.getInstance().debug("[Permission Manager] Removed blockbyblock.visit to world=" + world_uuid);
            if (delete) return;
            Main.getInstance().debug("[Permission Manager] Adding blockbyblock.visit to world=" + world_uuid);
            user.data().add(Node.builder("blockbyblock.visit").context(MutableContextSet.of("world", world_uuid.toString()).mutableCopy()).build());
            
        });
    }
    
    public void removePermissionsInOneWorld(UUID player_uuid, UUID world_uuid){
        if (Settings.SERVER_TYPE != ServerType.WORLDS) return;
        Main.getInstance().debug("[Permission Manager] Updating permissions to " + player_uuid + " in world " + world_uuid);
        Main.getInstance().debug("[Permission Manager] Removing all permissions from worlds.");
        lpApi.getUserManager().modifyUser(player_uuid, user -> {
            for ( String perm : Settings.PERMS_WHEN_CREATING_WORLD ){
                user.data().remove(Node.builder(perm).context(MutableContextSet.of("world", world_uuid.toString()).mutableCopy()).build());
            }
        });
    }
    
    public void reconnectToProxy( ){
        socket.disable();
        try {
            socket.init();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        socket.sendUpdate();
        getServer().getScheduler().runTaskTimer(this, ( ) -> {
            
            for ( Player p : Bukkit.getOnlinePlayers() ){
                if (p.getOpenInventory().getTopInventory().getHolder() instanceof IUpdatableMenu){
                    ((IUpdatableMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                }
            }
            
        }, 20L, 20L);
    }
    
    public ProxyStats getProxyStats( ){
        return proxyStats;
    }
    
    public void setProxyStats(ProxyStats proxyStats){
        this.proxyStats = proxyStats;
    }
    
    @Override
    public ServerType getServerType( ){
        return Settings.SERVER_TYPE;
    }
}
