package net.lymarket.comissionss.youmind.bbb.common.data.warp;

import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Warp {
    
    private final Date date;
    private final UUID uuid;
    private String name;
    private Loc location;
    private String owner;
    private ArrayList < String > members = new ArrayList <>( );
    private boolean isPublic = false;
    
    public Warp( String name , Loc location , String owner ){
        this.uuid = UUID.randomUUID( );
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members.add( owner );
        this.date = new Date( );
    }
    
    public Warp( String name , Loc location , String owner , boolean isPublic ){
        this.uuid = UUID.randomUUID( );
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members.add( owner );
        this.date = new Date( );
        this.isPublic = isPublic;
    }
    
    public Warp( String uuid , String name , Loc location , String owner , List < String > members , Date date , boolean isPublic ){
        this.uuid = UUID.fromString( uuid );
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members.addAll( members );
        this.date = date;
        this.isPublic = isPublic;
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
    
    public String getOwner( ){
        return owner;
    }
    
    public void setOwner( String owner ){
        this.owner = owner;
    }
    
    public Loc getLocation( ){
        return location;
    }
    
    public void setLocation( Loc location ){
        this.location = location;
    }
    
    public ArrayList < String > getMembers( ){
        return members;
    }
    
    public void setMembers( ArrayList < String > members ){
        this.members = members;
    }
    
    public void addMember( String member ){
        this.members.add( member );
    }
    
    public Date getDate( ){
        return date;
    }
    
    public boolean isPublic( ){
        return isPublic;
    }
    
    public void setPublic( boolean aPublic ){
        isPublic = aPublic;
    }
    
    
}
