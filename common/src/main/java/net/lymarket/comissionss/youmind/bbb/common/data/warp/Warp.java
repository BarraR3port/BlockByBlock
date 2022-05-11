package net.lymarket.comissionss.youmind.bbb.common.data.warp;

import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warp {
    private final UUID uuid;
    private String name;
    private Loc location;
    private final String version;
    private UUID owner;
    private boolean isPublic = false;
    private ArrayList < UUID > members = new ArrayList <>( );
    
    public Warp( String name , Loc location , UUID owner , String version ){
        this.uuid = UUID.randomUUID( );
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members.add( owner );
        this.version = version;
    }
    
    public Warp( String name , Loc location , UUID owner , String version , boolean isPublic ){
        this.uuid = UUID.randomUUID( );
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members.add( owner );
        this.isPublic = isPublic;
        this.version = version;
    }
    
    public Warp( String uuid , String name , Loc location , UUID owner , String version , boolean isPublic , List < UUID > members ){
        this.uuid = UUID.fromString( uuid );
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members.addAll( members );
        this.isPublic = isPublic;
        this.version = version;
    }
    
    public UUID getUUID( ){
        return uuid;
    }
    
    public String getName( ){
        return name;
    }
    
    public void setName( String name ){
        this.name = name;
    }
    
    public UUID getOwner( ){
        return owner;
    }
    
    public void setOwner( UUID owner ){
        this.owner = owner;
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
    
    
}
