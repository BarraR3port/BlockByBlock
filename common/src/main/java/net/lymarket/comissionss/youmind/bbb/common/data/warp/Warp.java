package net.lymarket.comissionss.youmind.bbb.common.data.warp;

import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warp {
    private final UUID uuid;
    private final String version;
    private final WarpType type;
    private Loc location;
    private boolean isPublic = false;
    private ArrayList < UUID > members = new ArrayList <>( );
    
    public Warp( Loc location , String version , WarpType type ){
        this.uuid = UUID.randomUUID( );
        this.location = location;
        this.version = version;
        this.type = type;
    }
    
    public Warp( Loc location , String version , WarpType type , boolean isPublic ){
        this.uuid = UUID.randomUUID( );
        this.location = location;
        this.version = version;
        this.type = type;
        this.isPublic = isPublic;
    }
    
    public Warp( String uuid , Loc location , String version , WarpType type , boolean isPublic , List < UUID > members ){
        this.uuid = UUID.fromString( uuid );
        this.location = location;
        this.members.addAll( members );
        this.version = version;
        this.isPublic = isPublic;
        this.type = type;
    }
    
    public UUID getUUID( ){
        return uuid;
    }
    
    public Loc getLocation( ){
        return location;
    }
    
    public void setLocation( Loc location ){
        this.location = location;
    }
    
    public ArrayList < UUID > getMembers( ){
        return members;
    }
    
    public void setMembers( ArrayList < UUID > members ){
        this.members = members;
    }
    
    public void addMember( UUID member ){
        this.members.add( member );
    }
    
    public void removeMember( UUID member ){
        this.members.remove( member );
    }
    
    public boolean isMember( UUID member ){
        return this.members.contains( member );
    }
    
    public boolean isPublic( ){
        return isPublic;
    }
    
    public void setPublic( boolean aPublic ){
        isPublic = aPublic;
    }
    
    public String getVersion( ){
        return version;
    }
    
    public WarpType getType( ){
        return type;
    }
}
