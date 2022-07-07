package net.lymarket.comissionss.youmind.bbb.velocity.manager;

import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerName;
import net.lymarket.comissionss.youmind.bbb.velocity.socketmanager.ProxySocketServer;

import java.util.HashMap;
import java.util.Optional;

public class ServerSocketManager {
    
    private static ServerSocketManager instance;
    
    private final HashMap<ServerName, ProxySocketServer> socketByServer = new HashMap<>();
    
    
    public ServerSocketManager( ){
        instance = this;
        
    }
    
    public static ServerSocketManager getInstance( ){
        return instance;
    }
    
    public static Optional<ProxySocketServer> getSocketByServer(ServerName server){
        return Optional.ofNullable(getInstance().socketByServer.get(server));
    }
    
    public void registerServerSocket(ServerName server, ProxySocketServer task){
        if (socketByServer.containsKey(server)){
            socketByServer.replace(server, task);
            return;
        }
        socketByServer.put(server, task);
    }
    
    public HashMap<ServerName, ProxySocketServer> getSocketByServer( ){
        return socketByServer;
    }
    
}
