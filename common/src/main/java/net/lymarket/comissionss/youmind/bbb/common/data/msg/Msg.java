package net.lymarket.comissionss.youmind.bbb.common.data.msg;


import java.util.UUID;


public abstract class Msg {
    private final UUID owner;
    private final String version;
    private String msg;
    
    public Msg( UUID owner , String msg , String version ){
        this.msg = msg;
        this.owner = owner;
        this.version = version;
    }
    
    public String getMsg( ){
        return msg;
    }
    
    public void setMsg( String msg ){
        this.msg = msg;
    }
    
    public UUID getOwner( ){
        return owner;
    }
    
    
    public String getVersion( ){
        return version;
    }
}
