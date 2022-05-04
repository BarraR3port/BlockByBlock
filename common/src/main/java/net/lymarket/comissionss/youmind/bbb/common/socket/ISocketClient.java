package net.lymarket.comissionss.youmind.bbb.common.socket;

import com.google.gson.JsonObject;

public interface ISocketClient {
    
    boolean sendMessage( JsonObject message );
    
    boolean sendMessage( String message );
    
    void reconnect( String msg );
    
    void disable( String reason );
}
