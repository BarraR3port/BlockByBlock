package net.lymarket.comissionss.youmind.bbb.common.data.server;

import java.util.Arrays;

public enum ServerName {
    LOBBY("lobby"),
    REGISTRO("registro"),
    PUBLIC_WORLD_1_12_1("PW-112-1"),
    PUBLIC_WORLD_1_16_1("PW-116-1"),
    PUBLIC_WORLD_1_18_1("PW-118-1"),
    PUBLIC_PLOT_1_12_1("PP-112-1"),
    PUBLIC_PLOT_1_16_1("PP-116-1"),
    PUBLIC_PLOT_1_18_1("PP-118-1");
    
    private final String name;
    
    ServerName(String name){
        this.name = name;
    }
    
    public static ServerName fromString(String serverName){
        return Arrays.stream(ServerName.values()).filter(r -> r.getName().equalsIgnoreCase(serverName)).findFirst().orElse(LOBBY);
    }
    
    public String getName( ){
        return name;
    }
    
    public boolean isPlotServer( ){
        return this.equals(PUBLIC_PLOT_1_12_1) || this.equals(PUBLIC_PLOT_1_16_1) || this.equals(PUBLIC_PLOT_1_18_1);
    }
    
    public boolean isWorldServer( ){
        return this.equals(PUBLIC_WORLD_1_12_1) || this.equals(PUBLIC_WORLD_1_16_1) || this.equals(PUBLIC_WORLD_1_18_1);
    }
}
