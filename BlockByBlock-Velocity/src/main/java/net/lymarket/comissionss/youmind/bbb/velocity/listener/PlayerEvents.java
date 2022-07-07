package net.lymarket.comissionss.youmind.bbb.velocity.listener;


import com.google.gson.JsonObject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerName;
import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import net.lymarket.comissionss.youmind.bbb.velocity.manager.ServerSocketManager;
import net.lymarket.comissionss.youmind.bbb.velocity.user.VelocityUser;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.Utils;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PlayerEvents {
    
    private final HashMap < UUID, Long > timeOnline = new HashMap <>();
    
    public PlayerEvents( ){
    
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), ( ) -> {
            final long currentTime = System.currentTimeMillis();
            VMain.getInstance().getProxy().getAllPlayers().forEach(player -> {
                VelocityUser user = VMain.getInstance().getPlayers().getPlayer(player.getUniqueId());
                if (user == null){
                    user = VMain.getInstance().getPlayers().getPlayer(player.getUsername());
                    if (user == null){
                        return;
                    }
                    user.setUUID(player.getUniqueId());
                }
                user.getStats().addTIME_PLAYED(currentTime - timeOnline.getOrDefault(player.getUniqueId(), 0L));
                VMain.getInstance().getPlayers().savePlayer(user);
                timeOnline.remove(player.getUniqueId());
                timeOnline.put(player.getUniqueId(), currentTime);
            });
        
        }).repeat(5, TimeUnit.SECONDS).schedule();
        
    }
    
    @Subscribe
    public void onPostLoginEvent(PostLoginEvent e){
        VMain.getInstance().getPlayers().getOrCreatePlayer(e.getPlayer().getUsername(), e.getPlayer().getUniqueId(), String.valueOf(e.getPlayer().getRemoteAddress().getAddress()).replace("/", ""));
        
        timeOnline.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
    
    @Subscribe
    public void onServerPostConnectEvent(ServerPostConnectEvent e){
        Component firstJoinMsg = LegacyComponentSerializer.legacyAmpersand().deserialize("&aBienvenido a &9Block By Block.")
                //? Hover Message
                .hoverEvent(LegacyComponentSerializer.legacyAmpersand().deserialize("&7Click para abrir el Menu"))
                .clickEvent(ClickEvent.runCommand("/menu"));
        TextComponent secondJoinMsg = LegacyComponentSerializer.legacyAmpersand().deserialize("&aConstruye y disfruta!")
                .hoverEvent(null)
                .clickEvent(null);
        e.getPlayer().sendMessage(firstJoinMsg);
        e.getPlayer().sendMessage(secondJoinMsg);
        /*VMain.getInstance().getServerSocketManager().getSocketByServer().forEach( ((serverName, proxySocketServer) -> {
            proxySocketServer.sendMessage();
        }));*/
    }
    
    @Subscribe
    public void onPlayerPreLogin(PreLoginEvent e){
        if (e.getUsername().contains("McDown_pw_") || e.getUsername().contains("McDown")){
            e.setResult(PreLoginEvent.PreLoginComponentResult.denied(Utils.format("PENDEJO")));
        }
    }
    
    @Subscribe
    public void onDisconnectEvent(DisconnectEvent e){
        final long currentTime = System.currentTimeMillis();
        final UUID uuid = e.getPlayer().getUniqueId();
        final VelocityUser user = VMain.getInstance().getPlayers().getPlayer(uuid);
        user.getStats().addTIME_PLAYED(currentTime - timeOnline.get(uuid));
        VMain.getInstance().getPlayers().savePlayer(user);
        timeOnline.remove(uuid);
        VMain.getInstance().getWorldManager().getWorlds().forEach(world -> {
            world.removeOnlineMember(user.getUUID());
            VMain.getInstance().getWorldManager().saveWorld(world);
        });
    }
    
    @Subscribe(order = PostOrder.FIRST)
    public void onKickedFromServerEvent(KickedFromServerEvent e){
        if (e.getServer().ping().isDone()){
            VMain.getInstance().getWorldManager().getAllWorlds().forEach(world -> {
                if (world.getServer().equals(e.getServer().getServerInfo().getName())){
                    world.getOnlineMembers().remove(e.getPlayer().getUniqueId());
                    VMain.getInstance().getWorldManager().saveWorld(world);
                }
            });
        }
        
        VMain.debug("KickedFromServerEvent  " + e.getPlayer().getUsername() + "  " + e.getPlayer().getUniqueId());
        for ( RegisteredServer server : VMain.getInstance().getProxy().getAllServers() ){
            try {
                ServerSocketManager.getSocketByServer(ServerName.fromString(server.getServerInfo().getName())).ifPresent(socket -> {
                    final JsonObject json = new JsonObject();
                    json.addProperty("type", "REMOVE_PLAYER_TO_TP_TO_WORLD");
                    json.addProperty("uuid", e.getPlayer().getUniqueId().toString());
                    socket.sendMessage(json);
                });
            } catch (Exception ignored) {
            }
        }
        
        if (VMain.getInstance().getProxy().getServer("lobby").isPresent()){
            RegisteredServer lobby = VMain.getInstance().getProxy().getServer("lobby").get();
            try {
                if (lobby.ping().get() != null){
                    e.setResult(KickedFromServerEvent.RedirectPlayer.create(lobby));
                } else {
                    e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay lobbys disponibles!")));
                }
            } catch (InterruptedException | ExecutionException ex) {
                e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay lobbys disponibles!")));
            }
            return;
            
        }
        e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay lobbys disponibles!")));
        
    }
    
    @Subscribe(order = PostOrder.LAST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing prev = event.getPing();
        if (prev.getPlayers().isPresent() && prev.getFavicon().isPresent()){
            if (prev.getModinfo().isPresent()){
                
                if (prev.getVersion().getProtocol() > 757 || prev.getVersion().getProtocol() < 46){
                    prev.asBuilder().version(new ServerPing.Version(ProtocolVersion.MINECRAFT_1_18.getProtocol(), "BlockByBlock 1.12.2 - 1.19.x "));
                } else {
                    prev.asBuilder().version(new ServerPing.Version(prev.getVersion().getProtocol(), "BlockByBlock 1.12.2 - 1.19.x "));
                }
            }
            event.setPing(prev.asBuilder().build());
        }
        
    }
    
}
