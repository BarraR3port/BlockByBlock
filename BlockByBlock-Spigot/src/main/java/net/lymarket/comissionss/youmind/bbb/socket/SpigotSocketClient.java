package net.lymarket.comissionss.youmind.bbb.socket;

import com.google.gson.*;
import com.grinderwolf.swm.api.world.SlimeWorld;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.LobbyMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.Msg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.PlotMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.WorldMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ProxyStats;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerName;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.data.world.WorldVisitRequest;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocket;
import net.lymarket.comissionss.youmind.bbb.common.socket.ISocketClient;
import net.lymarket.comissionss.youmind.bbb.event.PrevCreateWorld;
import net.lymarket.comissionss.youmind.bbb.home.HomeManager;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.add.AddPlayersToWorldMenuSelector;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.comissionss.youmind.bbb.users.PlayersRepository;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.comissionss.youmind.bbb.warp.SpigotWarp;
import net.lymarket.comissionss.youmind.bbb.warp.WarpManager;
import net.lymarket.comissionss.youmind.bbb.world.WorldManager;
import net.lymarket.lyapi.spigot.menu.IUpdatableMenu;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SpigotSocketClient extends ISocket<SlimeWorld, SpigotUser, SpigotHome, SpigotWarp> {
    
    private final VersionSupport<SlimeWorld, SpigotUser, SpigotHome, SpigotWarp> vs;
    private boolean reconnecting = false;
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private ProxySocket mainSocket;
    
    public SpigotSocketClient(PlayersRepository players, WorldManager worlds, HomeManager homes, WarpManager warps, VersionSupport<SlimeWorld, SpigotUser, SpigotHome, SpigotWarp> versionSupport){
        super(players, worlds, homes, warps);
        this.vs = versionSupport;
        
    }
    
    public ISocketClient getSocket( ){
        return mainSocket;
    }
    
    @Override
    public void sendCreateWorldMSG(Object event){
        final PrevCreateWorld e = (PrevCreateWorld) event;
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CREATE_WORLD");
        js.addProperty("server_name", Settings.SERVER_NAME.getName());
        js.addProperty("world_name", e.getWorld().getName());
        js.addProperty("owner_uuid", e.getUser().toString());
        js.addProperty("world_uuid", e.getWorld().getUUID().toString());
        js.addProperty("world_version", e.getWorld().getVersion());
        js.addProperty("world_server", e.getWorld().getServer());
        js.addProperty("world_layer_material", e.getMaterial().toString());
        sendMessage(js);
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget){
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CONNECT_TO_SERVER");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("msg", "EMPTY");
        sendMessage(js);
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget, String msg){
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CONNECT_TO_SERVER");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("msg", msg);
        sendMessage(js);
    }
    
    @Override
    public void sendKickFromWorld(UUID owner, BWorld world, UUID target){
        final JsonObject js = new JsonObject();
        js.addProperty("type", "KICK_FROM_WORLD");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", world.getServer());
        js.addProperty("world_uuid", world.getUUID().toString());
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("target_uuid", target.toString());
        sendMessage(js);
    }
    
    @Override
    public void sendKickFromWorld(UUID owner, String world_uuid, String server, UUID target){
        final JsonObject js = new JsonObject();
        js.addProperty("type", "KICK_FROM_WORLD");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", server);
        js.addProperty("world_uuid", world_uuid);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("target_uuid", target.toString());
        sendMessage(js);
    }
    
    @Override
    public void sendWorldDeleteRequest(Object player, BWorld world){
        final Player owner = (Player) player;
        JsonObject js = new JsonObject();
        js.addProperty("type", "WORLD_DELETE_PREV");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", world.getServer());
        js.addProperty("owner_uuid", owner.getUniqueId().toString());
        js.addProperty("world_uuid", world.getUUID().toString());
        js.addProperty("has_permission", owner.hasPermission("blockbyblock.admin.world.delete.other"));
        sendMessage(js);
    }
    
    @Override
    public void sendJoinWorldRequest(UUID owner, String serverTarget, UUID worldUUID, int item_slot){
        JsonObject js = new JsonObject();
        js.addProperty("type", "JOIN_WORLD_REQUEST");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("world_uuid", worldUUID.toString());
        js.addProperty("item_slot", item_slot);
        sendMessage(js);
    }
    
    
    @Override
    public void sendJoinPlotRequest(UUID owner, String server_version, String plotID, PlotType plotType, int item_slot){
        JsonObject js = new JsonObject();
        js.addProperty("type", "JOIN_PLOT_REQUEST");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_version", server_version);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("plot_id", plotID == null ? "NONE" : plotID);
        js.addProperty("item_slot", item_slot);
        js.addProperty("plot_type", plotType.toString());
        sendMessage(js);
    }
    
    @Override
    public void sendVisitRequest(UUID owner_uuid, UUID target_uuid, String target_name){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_VISIT_REQUEST");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("owner_uuid", owner_uuid.toString());
        js.addProperty("target_uuid", target_uuid.toString());
        js.addProperty("target_name", target_name);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", false);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, String word, String replacement){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", true);
        JsonObject replacements = new JsonObject();
        replacements.addProperty(word, replacement);
        js.add("replacements", replacements);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, HashMap<String, String> replacementsMap){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", true);
        JsonObject replacements = new JsonObject();
        for ( String word : replacementsMap.keySet() ){
            replacements.addProperty(word, replacementsMap.get(word));
        }
        js.add("replacements", replacements);
        sendMessage(js);
    }
    
    @Override
    public void sendJoinHome(UUID owner, Home home){
        JsonObject js = new JsonObject();
        js.addProperty("type", "JOIN_HOME");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", home.getLocation().getServer());
        js.addProperty("home_uuid", home.getUUID().toString());
        js.addProperty("owner_uuid", owner.toString());
        sendMessage(js);
    }
    
    @Override
    public void sendJoinWarp(UUID owner, Warp warp){
        JsonObject js = new JsonObject();
        js.addProperty("type", "JOIN_WARP");
        js.addProperty("current_server", Settings.SERVER_NAME.getName());
        js.addProperty("server_target", warp.getLocation().getServer());
        js.addProperty("warp_uuid", warp.getUUID().toString());
        js.addProperty("owner_uuid", owner.toString());
        sendMessage(js);
    }
    
    @Override
    public void sendWorldVisitResponse(WorldVisitRequest request){
        JsonObject js = new JsonObject();
        js.addProperty("type", "VISIT_WORLD_REQUEST_POST");
        js.addProperty("guest_server", request.getGuest_server());
        js.addProperty("response", request.isAccepted());
        if (request.isAccepted()){
            js.addProperty("target_uuid", request.getTarget_uuid().toString());
            js.addProperty("target_server", request.getTarget_server());
            js.addProperty("guest", request.getGuest().toString());
            
        }
        sendMessage(js);
    }
    
    @Override
    public void sendMsgFromPlayer(Msg msg){
        JsonObject js = new JsonObject();
        js.addProperty("type", "PLAYER_SENT_MSG");
        if (msg instanceof PlotMsg){
            js.addProperty("msg_type", "PlotMsg");
            final PlotMsg plotMsg = (PlotMsg) msg;
            js.addProperty("msg", gson.toJson(plotMsg));
        } else if (msg instanceof WorldMsg){
            js.addProperty("msg_type", "WorldMsg");
            final WorldMsg worldMsg = (WorldMsg) msg;
            js.addProperty("msg", gson.toJson(worldMsg));
        } else {
            js.addProperty("msg_type", "OTHER");
            final LobbyMsg lobbyMsg = (LobbyMsg) msg;
            js.addProperty("msg", gson.toJson(lobbyMsg));
        }
        sendMessage(js);
    }
    
    
    @Override
    public void sendUpdate( ){
        JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE");
        js.addProperty("server_name", Settings.SERVER_NAME.getName());
        js.addProperty("online_players", Bukkit.getOnlinePlayers().size());
        sendMessage(js);
    }
    
    @Override
    public void sendDisconnectInfoToProxy( ){
        JsonObject js = new JsonObject();
        js.addProperty("type", "DISCONNECT");
        js.addProperty("server_name", Settings.SERVER_NAME.getName());
        sendMessage(js);
    }
    
    @Override
    public String encrypt(String msg){
        msg = msg.replace("\\n", "");
        msg = msg.replace("\n", "");
        msg = msg.replace("\\\"", "\"");
        msg = msg.replace("\"{", "{");
        msg = msg.replace("}\"", "}");
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }
    
    @Override
    public String decrypt(String data){
        return new String(Base64.getDecoder().decode(data.getBytes()));
    }
    
    public SpigotSocketClient init( ) throws IOException{
        if (mainSocket != null){
            mainSocket.socket.close();
        }
        Socket socket = new Socket(Settings.SOCKET_IP, Settings.SOCKET_PORT);
        mainSocket = new ProxySocket(socket, Settings.SERVER_NAME);
        return this;
    }
    
    /**
     * Send arena data to the lobbies.
     */
    public void sendMessage(JsonObject message){
        if (message == null) return;
        
        if (mainSocket == null){
            try {
                Socket socket = new Socket(Settings.SOCKET_IP, Settings.SOCKET_PORT);
                mainSocket = new ProxySocket(socket, Settings.SERVER_NAME);
                mainSocket.sendMessage(message);
                reconnecting = false;
            } catch (IOException err) {
                err.printStackTrace();
            }
        } else {
            mainSocket.sendMessage(message);
        }
    }
    
    /**
     * Close active sockets.
     */
    public void disable( ){
        mainSocket.disable("Close active sockets");
    }
    
    private class ProxySocket implements ISocketClient {
        private final Socket socket;
        private final ServerName name;
        private PrintWriter out;
        private Scanner in;
        private boolean compute = true;
        
        private ProxySocket(Socket socket, ServerName name){
            this.name = name;
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ignored) {
                out = null;
                return;
            }
            
            try {
                in = new Scanner(socket.getInputStream());
            } catch (IOException ignored) {
                return;
            }
            
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ( ) -> {
                while (compute) {
                    try {
                        if (in.hasNext()){
                            final String encrypted = in.nextLine();
                            String decryptedMessage = decrypt(encrypted);
                            if (decryptedMessage == null || decryptedMessage.isEmpty()){
                                Main.getInstance().debug("Received bad data from: " + socket.getInetAddress().toString() + "\nMsg: " + encrypted);
                                continue;
                            }
                            final JsonObject json;
                            try {
                                json = new JsonParser().parse(decryptedMessage).getAsJsonObject();
                            } catch (JsonSyntaxException e) {
                                Main.getInstance().debug("Received bad data from: " + socket.getInetAddress().toString());
                                continue;
                            }
                            if (json == null) continue;
                            if (!json.has("type")) continue;
                            final String type = json.get("type").getAsString().toUpperCase();
                            if (!type.equals("UPDATE_ONLINE_PLAYERS_IN_WORLDS") && !type.equals("MSG_RECEIVED")){
                                if (Settings.DEVELOPMENT_MODE){
                                    Main.getInstance().debug("Received Encrypted from " + name.getName() + ": \n" + encrypted);
                                    Main.getInstance().debug("Received message from " + name.getName() + ": \n" + gson.toJson(json));
                                } else {
                                    Main.getInstance().debug("Received message from " + name.getName() + ": \n" + gson.toJson(json));
                                }
                            }
                            switch(type){
                                case "MSG_RECEIVED":{
                                    Main.getInstance().debug("Received message from " + name.getName() + ": \n" + gson.toJson(json));
                                    continue;
                                }
                                case "INIT_CREATE_WORLD":{
                                    if (!json.has("server_name")) continue;
                                    if (!json.has("world_name")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_version")) continue;
                                    if (!json.has("world_server")) continue;
                                    if (!json.has("world_layer_material")) continue;
    
                                    String world_name = json.get("world_name").getAsString();
                                    UUID owner = UUID.fromString(json.get("owner_uuid").getAsString());
                                    UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    String world_version = json.get("world_version").getAsString();
                                    String world_server = json.get("world_server").getAsString();
                                    String world_layer_material = json.get("world_layer_material").getAsString();
                                    Main.getInstance().debug("Received INIT_CREATE_WORLD request: world_name:" + world_name + " world_uuid:" + world_uuid + " world_version:" + world_version + " world_server:" + world_server);
                                    if (Settings.SERVER_NAME.getName().equals(world_server)){
                                        Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                                            Main.getInstance().debug("[World Creation] [Phase 1/2] Creating World: " + world_name);
                                            BWorld world = new BWorld(owner, world_name, world_server, world_version, world_uuid, world_layer_material);
                                            getWorlds().createCustomLayerWorld(world, world_layer_material);
                                            Main.getInstance().debug("[World Creation] [Phase 2/2] Creating World: " + world_uuid);
                                            getWorlds().createWorld(world);
                                            final Player p = Bukkit.getPlayer(owner);
                                            final boolean teleport = p == null;
                                            getWorlds().addPlayerToTP(owner, world);
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> Main.getInstance().managePermissions(owner, world_uuid, false));
                                            
                                            json.remove("type");
                                            json.addProperty("type", "INIT_CREATE_WORLD_SUCCESS");
                                            json.addProperty("teleport", teleport);
                                            sendMessage(json);
                                            Main.getInstance().debug("[World Creation] Sending message to Proxy: " + json);
                                        });
                                    } else {
                                        Main.getInstance().debug("Received INIT_CREATE_WORLD but its meant for another server.\n " + json);
                                    }
                                    continue;
                                }
                                case "REMOVE_PLAYER_TO_TP_TO_WORLD":{
                                    if (!json.has("uuid")) continue;
                                    UUID owner = UUID.fromString(json.get("uuid").getAsString());
                                    try {
                                        getWorlds().removePlayerToTP(owner);
                                    } catch (Exception ignored) {
                                    }
                                    continue;
                                }
                                case "UPDATE_INV_POST_INIT_CREATE_WORLD_SUCCESS":{
                                    if (!json.has("server_name")) continue;
                                    if (!json.has("world_name")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_version")) continue;
                                    if (!json.has("world_server")) continue;
                                    if (!json.has("world_layer_material")) continue;
                                    if (!json.has("teleport")) continue;
                                    final UUID owner = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final Player p = Bukkit.getPlayer(owner);
                                    if (p.getInventory().getHolder() instanceof IUpdatableMenu){
                                        ((IUpdatableMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                                    }
                                    continue;
                                }
                                case "UPDATE_INV_WORLD_DELETE_SUCCESS":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("same_server")) continue;
                                    if (!json.has("has_permission")) continue;
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    try {
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        
                                        if (p.getInventory().getHolder() instanceof AddPlayersToWorldMenuSelector){
                                            AddPlayersToWorldMenuSelector plotMenu = (AddPlayersToWorldMenuSelector) p.getInventory().getHolder();
                                            plotMenu.handleAccept();
                                        }
                                        
                                    } catch (NullPointerException ignored) {
                                    }
                                    continue;
                                }
                                case "JOIN_WORLD_REQUEST_PREV":{
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("item_slot")) continue;
                                    final String server_target = json.get("server_target").getAsString();
                                    final String current_server = json.get("current_server").getAsString();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    final BWorld world = getWorlds().getWorld(world_uuid);
                                    if (server_target.equals(current_server)){
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        final World localWorld = Bukkit.getWorld(world.getUUID().toString());
                                        if (p != null && localWorld != null && (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid))){
                                            Main.getInstance().debug("Teleporting " + p.getName() + " to " + localWorld.getName());
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> p.teleport(localWorld.getSpawnLocation()));
                                            getWorlds().addPlayerToWorldOnlineMembers(owner_uuid, world_uuid);
                                        } else {
                                            Main.getInstance().debug("Teleporting " + owner_uuid + " to " + world.getUUID().toString());
                                        }
                                        sendMSGToPlayer(owner_uuid, "world.join", "world", world.getName().split("-")[0]);
                                        continue;
                                    }
                                    
                                    json.remove("type");
                                    json.addProperty("type", "JOIN_WORLD_REQUEST_POST");
    
                                    if (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid) || getPlayers().getPlayer(owner_uuid).getRank().isAdmin()){
                                        getWorlds().addPlayerToTP(owner_uuid, world);
                                        json.addProperty("response", true);
                                    } else {
                                        json.addProperty("response", false);
                                    }
                                    sendMessage(json);
                                    continue;
                                }
                                case "JOIN_WORLD_REQUEST_POST_DENY":{
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("item_slot")) continue;
                                    final int item_slot = json.get("item_slot").getAsInt();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    try {
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        if (p.getOpenInventory().getTopInventory().getHolder() instanceof WorldManagerMenu){
                                            WorldManagerMenu menu = (WorldManagerMenu) p.getOpenInventory().getTopInventory().getHolder();
                                            menu.checkSomething(p, item_slot, menu.getInventory().getItem(item_slot), "&cSin permisos", "&cNo tienes permisos", menu.getMenuUUID());
                                            sendMSGToPlayer(owner_uuid, "error.world.not-allowed-to-join-world");
                                        }
                                    } catch (Exception ignored) {
                                    }
                                    continue;
                                }
                                case "JOIN_PLOT_REQUEST_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("server_version")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("plot_id")) continue;
                                    if (!json.has("item_slot")) continue;
                                    if (!json.has("plot_type")) continue;
                                    
                                    vs.getPlotManager().manageJoinPlot(json);
                                    continue;
                                }
                                case "JOIN_PLOT_REQUEST_POST_DENY":{
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("item_slot")) continue;
                                    final int item_slot = json.get("item_slot").getAsInt();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    try {
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        if (p.getOpenInventory().getTopInventory().getHolder() instanceof WorldManagerMenu){
                                            WorldManagerMenu menu = (WorldManagerMenu) p.getOpenInventory().getTopInventory().getHolder();
                                            menu.checkSomething(p, item_slot, menu.getInventory().getItem(item_slot), "&cSin permisos", "&cNo tienes permisos", menu.getMenuUUID());
                                            sendMSGToPlayer(owner_uuid, "error.world.not-allowed-to-join-world");
                                        }
                                    } catch (Exception ignored) {
                                    }
                                    continue;
                                }
                                case "WORLD_DELETE_INIT":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("same_server")) continue;
                                    if (!json.has("has_permission")) continue;
                                    final boolean has_permission = json.get("has_permission").getAsBoolean();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    final BWorld world = getWorlds().getWorld(world_uuid);
                                    final String server_target = json.get("server_target").getAsString();
                                    json.remove("type");
                                    
                                    if (world.getOwner().equals(owner_uuid) || has_permission){
                                        Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> getWorlds().deleteWorldFromOutside(owner_uuid, world, server_target, json));
                                    } else {
                                        json.addProperty("type", "ERROR");
                                        json.addProperty("error", "WORLD_DELETE_FAILED");
                                        sendMessage(json);
                                        continue;
                                    }
                                    continue;
                                }
                                case "KICK_FROM_WORLD_PREV":{
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("target_uuid")) continue;
                                    final String server_target = json.get("server_target").getAsString();
                                    final String currentServer = json.get("current_server").getAsString();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    if (server_target.equalsIgnoreCase(currentServer)){
                                        if (owner_uuid.equals(target_uuid)){
                                            sendMSGToPlayer(owner_uuid, "world.cant-kick-own");
                                            continue;
                                        }
                                    }
                                    
                                    json.remove("type");
                                    json.addProperty("type", "WORLD_KICK_SUCCESS");
                                    sendMessage(json);
                                    continue;
                                }
                                case "WORLD_KICK_SUCCESS":{
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("target_uuid")) continue;
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    final UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    
                                    try {
                                        final HashMap<String, String> replace = new HashMap<>();
                                        replace.put("world", world_uuid.toString());
                                        replace.put("player", getPlayers().getPlayer(target_uuid).getName());
                                        sendMSGToPlayer(owner_uuid, "world.kick-success", replace);
                                    } catch (NullPointerException ignored) {
                                    }
                                    continue;
                                }
                                case "SEND_MSG_TO_PLAYER_POST":{
                                    if (!json.has("target_uuid")) continue;
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("key")) continue;
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    final String key = json.get("key").getAsString();
                                    final boolean hasReplacements = json.get("has-replacements").getAsBoolean();
                                    
                                    try {
                                        final Player player = Bukkit.getPlayer(target_uuid);
                                        if (player == null) continue;
                                        if (hasReplacements){
                                            final JsonObject replacements = json.get("replacements").getAsJsonObject();
                                            final HashMap<String, String> replace = new HashMap<>();
    
                                            for ( Map.Entry<String, JsonElement> entry : replacements.entrySet() ){
                                                replace.put(entry.getKey(), entry.getValue().getAsString());
                                            }
                                            player.sendMessage(Main.getLang().getMSG(key, replace));
                                        } else {
                                            player.sendMessage(Main.getLang().getMSG(key));
                                        }
                                        
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                    continue;
                                }
                                case "UPDATE_ONLINE_PLAYERS_IN_WORLDS":{
                                    if (!json.has("stats")) continue;
                                    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ( ) -> {
                                                ProxyStats proxyStats = gson.fromJson(json.getAsJsonObject("stats"), ProxyStats.class);
                                                Main.getInstance().setProxyStats(proxyStats);
                                                if (Settings.SERVER_TYPE.equals(ServerType.WORLDS)){
                                                    getWorlds().getWorldsByServer().forEach(world -> {
                                                        boolean save = false;
                                                        final ArrayList<UUID> playersToAdd = new ArrayList<>();
                                                        final World bukkitWorld = Bukkit.getWorld(world.getUUID().toString());
                                                        if (bukkitWorld == null){
                                                            if (world.getOnlineMembers().size() > 0){
                                                                world.setOnlineMembers(new ArrayList<>());
                                                                save = true;
                                                            }
                                                        } else {
                                                            if (bukkitWorld.getPlayers().size() != world.getOnlineMembers().size()){
                                                                playersToAdd.addAll(bukkitWorld.getPlayers().stream().map(Player::getUniqueId).collect(Collectors.toList()));
                                                            }
                                                            if (playersToAdd.size() > 0){
                                                                world.setOnlineMembers(playersToAdd);
                                                                save = true;
                                                            }
    
                                                        }
                                                        if (save){
                                                            getWorlds().saveWorld(world);
                                                        }
    
                                                    });
                                                }
                                            }
                                    );
                                    continue;
                                }
                                case "VISIT_REQUEST_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("target_uuid")) continue;
                                    final String currentServer = json.get("current_server").getAsString();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    
                                    final User targetUser = getPlayers().getPlayer(target_uuid);
                                    if (targetUser == null){
                                        sendMSGToPlayer(owner_uuid, "error.player-not-found");
                                        continue;
                                    }
                                    if (!targetUser.getOption("allow-visit-world-requests") && !targetUser.getOption("allow-visit-plot-requests")){
                                        sendMSGToPlayer(owner_uuid, "error.player.not-available-to-be-visited", "player", targetUser.getName());
                                        continue;
                                    }
                                    switch(Settings.SERVER_TYPE){
                                        case LOBBY:{
                                            sendMSGToPlayer(owner_uuid, "error.player.in-lobby", "player", targetUser.getName());
                                            continue;
                                        }
                                        case PLOT:{
                                            if (!targetUser.getOption("allow-visit-plot-requests")){
                                                sendMSGToPlayer(owner_uuid, "error.player.not-available-to-be-visited", "player", targetUser.getName());
                                                continue;
                                            } else {
                                                sendMSGToPlayer(owner_uuid, "visit.sent", "player", targetUser.getName());
                                                vs.getPlotManager().manageVisitJoinPlot(owner_uuid, targetUser, currentServer, Settings.SERVER_NAME.getName());
                                            }
                                            continue;
                                        }
                                        case WORLDS:{
                                            if (!targetUser.getOption("allow-visit-world-requests")){
                                                sendMSGToPlayer(owner_uuid, "error.player.not-available-to-be-visited", "player", targetUser.getName());
                                                continue;
                                            } else {
                                                final Player targetPlayer = Bukkit.getPlayer(target_uuid);
                                                if (targetPlayer != null){
                                                    try {
                                                        final BWorld world = getWorlds().getWorld(UUID.fromString(targetPlayer.getWorld().getName()));
                                                        final String current_server = json.get("current_server").getAsString();
                                                        final UUID world_uuid = world.getUUID();
                                                        if (current_server.equals(Settings.SERVER_NAME.getName())){
                                                            final Player p = Bukkit.getPlayer(owner_uuid);
                                                            final World localWorld = Bukkit.getWorld(world.getUUID().toString());
                                                            if (p != null && localWorld != null && (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid))){
                                                                Main.getInstance().debug("Teleporting " + p.getName() + " to " + localWorld.getName());
                                                                Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> p.teleport(localWorld.getSpawnLocation()));
                                                                getWorlds().addPlayerToWorldOnlineMembers(owner_uuid, world_uuid);
                                                            } else {
                                                                Main.getInstance().debug("Teleporting " + owner_uuid + " to " + world.getUUID().toString());
                                                            }
                                                            sendMSGToPlayer(owner_uuid, "world.join", "world", world.getName().split("-")[0]);
                                                            continue;
                                                        }
    
                                                        json.remove("type");
                                                        json.addProperty("type", "JOIN_WORLD_REQUEST_POST");
                                                        json.addProperty("world_uuid", world_uuid.toString());
                                                        json.addProperty("server_target", Settings.SERVER_NAME.getName());
                                                        json.addProperty("item_slot", 0);
    
                                                        if (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid) || getPlayers().getPlayer(owner_uuid).getRank().isAdmin()){
                                                            getWorlds().addPlayerToTP(owner_uuid, world);
                                                            json.addProperty("response", true);
                                                        } else {
                                                            sendMSGToPlayer(owner_uuid, "visit.sent", "player", targetUser.getName());
                                                            final WorldVisitRequest request = new WorldVisitRequest(owner_uuid, target_uuid, null, currentServer, Settings.SERVER_NAME.getName());
                                                            getWorlds().manageVisitJoinWorld(request);
                                                            continue;
                                                        }
                                                        sendMessage(json);
                                                        continue;
    
                                                    } catch (IllegalArgumentException ignored) {
                                                    }
                                                }
    
                                                sendMSGToPlayer(owner_uuid, "visit.sent", "player", targetUser.getName());
                                                final WorldVisitRequest request = new WorldVisitRequest(owner_uuid, target_uuid, null, currentServer, Settings.SERVER_NAME.getName());
                                                getWorlds().manageVisitJoinWorld(request);
                                            }
                                            continue;
                                        }
                                    }
                                    continue;
                                }
                                case "VISIT_REQUEST_DENY":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("reason")) continue;
                                    final Player p = Bukkit.getPlayer(UUID.fromString(json.get("owner_uuid").getAsString()));
                                    if (p != null){
                                        final String reason = json.get("reason").getAsString();
                                        Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> Main.getLang().sendErrorMsg(p, reason));
                                    }
                                    continue;
                                }
    
                                case "JOIN_HOME_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("home_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    try {
                                        final String currentServer = json.get("current_server").getAsString();
                                        final String targetServer = json.get("server_target").getAsString();
                                        final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                        final SpigotHome home = getHomes().getHome(UUID.fromString(json.get("home_uuid").getAsString()));
                                        if (currentServer.equalsIgnoreCase(targetServer)){
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                                                final Player player = Bukkit.getPlayer(owner_uuid);
                                                player.teleport(home.getBukkitLocation());
                                                Main.getLang().sendMsg(player, "home.tp-to-home", "home", home.getName());
                                            });
                                        } else {
                                            getHomes().addPlayerToTP(owner_uuid, home);
                                            json.remove("type");
                                            json.addProperty("type", "JOIN_HOME_POST");
                                            json.addProperty("tp", true);
                                            sendMessage(json);
                                        }
                                        continue;
                                    } catch (Exception e) {
                                        json.remove("type");
                                        json.addProperty("type", "JOIN_HOME_POST");
                                        json.addProperty("tp", false);
                                        sendMessage(json);
                                        continue;
                                    }
                                }
                                case "JOIN_WARP_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("warp_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    try {
                                        final String currentServer = json.get("current_server").getAsString();
                                        final String targetServer = json.get("server_target").getAsString();
                                        final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                        final SpigotWarp warp = getWarps().getWarp(UUID.fromString(json.get("warp_uuid").getAsString()));
                                        if (currentServer.equalsIgnoreCase(targetServer)){
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                                                final Player player = Bukkit.getPlayer(owner_uuid);
                                                player.teleport(warp.getBukkitLocation());
                                                Main.getLang().sendMsg(player, "warps.tp-to-warp", "warp", warp.getType().getName());
                                            });
                                        } else {
                                            getWarps().addPlayerToTP(owner_uuid, warp);
                                            json.remove("type");
                                            json.addProperty("type", "JOIN_WARP_POST");
                                            json.addProperty("tp", true);
                                            sendMessage(json);
                                        }
                                        continue;
                                    } catch (Exception e) {
                                        json.remove("type");
                                        json.addProperty("type", "JOIN_WARP_POST");
                                        json.addProperty("tp", false);
                                        sendMessage(json);
                                        continue;
                                    }
                                }
                                case "ERROR":{
                                    if (!json.has("error")) continue;
                                    final String error = json.get("error").getAsString();
                                    switch(error){
                                        case "SERVER_NOT_ONLINE":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("server_target")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            sendMSGToPlayer(owner_uuid, "error.server.not-online", "server", server_target);
                                        }
                                        case "WORLD_DELETE_FAILED":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("current_server")) continue;
                                            if (!json.has("server_target")) continue;
                                            if (!json.has("world_uuid")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            final String world_uuid = json.get("world_uuid").getAsString();
                                            try {
                                                final HashMap<String, String> replace = new HashMap<>();
                                                replace.put("server", server_target);
                                                replace.put("world", world_uuid);
                                                sendMSGToPlayer(owner_uuid, "error.world.delete-failed", replace);
    
                                            } catch (NullPointerException ignored) {
                                            }
                                        }
                                    }
                                    continue;
                                }
                                case "SUCCESS_MSG":{
                                    if (!json.has("success_type")) continue;
                                    final String success_type = json.get("success_type").getAsString();
                                    if (success_type.equals("VISIT_REQUEST")){
                                        final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                        final String target_name = json.get("target_name").getAsString();
                                        Main.getLang().sendMsg(Bukkit.getPlayer(owner_uuid), "visit.sent", "player", target_name);
                                    }
                                    switch(success_type){
                                        case "VISIT_REQUEST":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("server_target")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            sendMSGToPlayer(owner_uuid, "error.server.not-online", "server", server_target);
                                        }
                                        case "WORLD_DELETE_FAILED":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("current_server")) continue;
                                            if (!json.has("server_target")) continue;
                                            if (!json.has("world_uuid")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            final String world_uuid = json.get("world_uuid").getAsString();
                                            try {
                                                final HashMap<String, String> replace = new HashMap<>();
                                                replace.put("server", server_target);
                                                replace.put("world", world_uuid);
                                                sendMSGToPlayer(owner_uuid, "error.world.delete-failed", replace);
    
                                            } catch (NullPointerException ignored) {
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            reconnect("Server Closed Connection");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        public boolean sendMessage(JsonObject message){
            if (socket == null){
                reconnect("Socket is null");
                return false;
            }
            if (!socket.isConnected()){
                reconnect("Socket is not connected");
                return false;
            }
            if (out == null){
                reconnect("Output stream is null");
                return false;
            }
            if (in == null){
                reconnect("Input stream is null");
                return false;
            }
            if (out.checkError()){
                reconnect("Output stream has error");
                return false;
            }
            if (Settings.DEVELOPMENT_MODE){
                Main.getInstance().debug("Sending message to " + name.getName() + ": \n" + gson.toJson(message));
                Main.getInstance().debug("Sending Encrypted Message to " + name.getName() + ": \n" + encrypt(gson.toJson(message)));
            } else {
                Main.getInstance().debug("Sending message to " + name.getName() + ": \n" + gson.toJson(message));
            }
            final UUID msgUUID = UUID.randomUUID();
            message.addProperty("socket-msg-uuid", msgUUID.toString());
            out.println(encrypt(gson.toJson(message)));
    
            return true;
        }
        
        public void reconnect(String msg){
            if (reconnecting) return;
            AtomicInteger reconnect_attempts = new AtomicInteger(1);
            if (!Main.getInstance().isEnabled()) return;
            disable("Reconnecting");
            reconnecting = true;
            Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), ( ) -> {
                if (!reconnecting) return;
                int currentAttempt = reconnect_attempts.getAndIncrement();
                Main.getInstance().debug("Reconnecting to Proxy Socket, attempt: " + currentAttempt + " of 10");
                if (currentAttempt > 10){
                    reconnecting = false;
                    Bukkit.shutdown();
                    return;
                }
                try {
                    init();
                    Main.getInstance().debug("Reconnected to Proxy Socket");
                    sendUpdate();
                    reconnecting = false;
                } catch (IOException e) {
                    Main.getInstance().debug("Failed to reconnect to " + name.getName() + " attempt " + currentAttempt);
                }
            }, 20, 150);
    
        }
        
        public void disable(String reason){
            compute = false;
            Main.getInstance().debug("Disabling socket: " + socket.toString());
            Main.getInstance().debug("Disabling socket Reason: " + reason);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = null;
            out = null;
        }
    }
}
