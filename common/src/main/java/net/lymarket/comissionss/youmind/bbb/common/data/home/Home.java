package net.lymarket.comissionss.youmind.bbb.common.data.home;


import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;

import java.util.Date;
import java.util.UUID;

public class Home {
    
    private final String owner;
    private final UUID uuid;
    private final Date date;
    private String name;
    private Loc location;
    
    public Home( String owner , String name , Loc location ){
        this.owner = owner;
        this.uuid = UUID.randomUUID( );
        this.date = new Date( );
        this.name = name;
        this.location = location;
    }
    
    public Home( String uuid , String owner , String name , Loc location , Date date ){
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.uuid = UUID.fromString( uuid );
        this.date = date;
    }
    
    public String getOwner( ){
        return owner;
    }
    
    public String getName( ){
        return name;
    }
    
    public void setName( String homeName ){
        this.name = homeName;
    }
    
    public Loc getLocation( ){
        return location;
    }
    
    public void setLocation( Loc homeLoc ){
        this.location = homeLoc;
    }
    
    public UUID getUUID( ){
        return uuid;
    }
    
    public Date getDate( ){
        return date;
    }
}
