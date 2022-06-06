package net.lymarket.comissionss.youmind.bbb.common.data.server;

public enum ServerType {
    LOBBY("Lobby"),
    WORLDS("World"),
    PLOT("Plot");
    
    private final String name;
    
    ServerType(String name){
        this.name = name;
    }
    
    public String getName( ){
        return name;
    }
}
