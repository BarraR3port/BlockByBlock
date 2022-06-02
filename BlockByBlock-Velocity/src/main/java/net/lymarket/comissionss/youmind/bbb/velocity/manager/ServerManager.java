package net.lymarket.comissionss.youmind.bbb.velocity.manager;

import net.lymarket.comissionss.youmind.bbb.velocity.VMain;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerManager {
    
    
    private final ArrayList < String > servers;
    
    
    public ServerManager( ){
        servers = new ArrayList <>();
    }
    
    public void addServer(String server){
        servers.add(server);
    }
    
    public void removeServer(String server){
        servers.remove(server);
    }
    
    public void init( ){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), ( ) -> {
            final ArrayList < String > serversToRemove = new ArrayList <>();
            servers.forEach(
                    server ->
                            VMain.getInstance().getProxy().getServer(server).ifPresent(
                                    registeredServer -> serversToRemove.add(server)
                            ));
            
            serversToRemove.forEach(this::removeServer);
            
        }).repeat(5, TimeUnit.SECONDS).schedule();
        
    }
    
    
}
