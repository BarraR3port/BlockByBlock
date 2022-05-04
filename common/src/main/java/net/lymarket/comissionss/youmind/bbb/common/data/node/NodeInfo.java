package net.lymarket.comissionss.youmind.bbb.common.data.node;

public abstract class NodeInfo {
    
    private String server;
    
    private String version;
    
    public NodeInfo( String server , String version ){
        this.server = server;
        this.version = version;
    }
    
    public String getServer( ){
        return server;
    }
    
    public void setServer( String server ){
        this.server = server;
    }
    
    public String getVersion( ){
        return version;
    }
    
    public void setVersion( String version ){
        this.version = version;
    }
}
