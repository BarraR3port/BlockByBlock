package net.lymarket.comissionss.youmind.bbb.common.data.world;

import java.util.ArrayList;
import java.util.UUID;

public class BWorld {
    
    private final UUID uuid;
    private final ArrayList < UUID > members = new ArrayList <>( );
    private ArrayList < UUID > online_members = new ArrayList <>( );
    private final ArrayList < String > description = new ArrayList <>( );
    private final ArrayList < WorldVisitRequest > visitors = new ArrayList <>( );
    private String name;
    private UUID owner;
    private String server;
    private String version;
    
    public BWorld( UUID owner , String name , String server , String version ){
        this.owner = owner;
        this.name = name;
        this.server = server;
        this.version = version;
        this.uuid = UUID.randomUUID( );
        this.members.add( owner );
    }
    
    public BWorld( UUID owner , String server , String version ){
        this.owner = owner;
        this.uuid = UUID.randomUUID( );
        this.name = uuid.toString( );
        this.server = server;
        this.version = version;
        this.members.add( owner );
    }
    
    public BWorld( UUID owner , String name , String server , String version , UUID uuid ){
        this.owner = owner;
        this.name = name;
        this.server = server;
        this.version = version;
        this.uuid = uuid;
        this.members.add( owner );
    }
    
    public UUID getOwner( ){
        return owner;
    }
    
    public void setOwner( UUID owner ){
        this.owner = owner;
    }
    
    public String getName( ){
        return name;
    }
    
    public void setName( String name ){
        this.name = name;
    }
    
    public UUID getUUID( ){
        return uuid;
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
    
    public ArrayList < UUID > getMembers( ){
        return members;
    }
    
    
    public void addMember( UUID member ){
        if ( !members.contains( member ) ) {
            members.add( member );
        }
    }
    
    public void removeMember( UUID member ){
        members.remove( member );
    }
    
    public boolean isMember( UUID member ){
        return members.contains( member );
    }
    
    public ArrayList < String > getDescription( ){
        return description;
    }
    
    public void addDescription( String description ){
        this.description.add( description );
    }
    
    public void removeDescription( String description ){
        this.description.remove( description );
    }
    
    public ArrayList < UUID > getOnlineMembers( ){
        return online_members;
    }
    
    public void setOnlineMembers( ArrayList < UUID > online_members ){
        this.online_members.clear( );
        this.online_members = online_members;
    }
    
    public ArrayList < WorldVisitRequest > getVisitors( ){
        return visitors;
    }
    
    public WorldVisitRequest getVisitor( UUID uuid ){
        return visitors.stream( ).filter( visitor -> visitor.getGuest( ).equals( uuid ) ).findFirst( ).orElse( null );
        
    }
    
    public void addVisitor( WorldVisitRequest visitor ){
        if ( !visitors.contains( visitor ) ) {
            visitors.add( visitor );
        }
    }
    
    public void removeVisitor( UUID visitor ){
        visitors.removeIf( v -> v.getGuest( ).equals( visitor ) );
    }
    
    public boolean isVisitor( UUID visitor ){
        return visitors.stream( ).map( WorldVisitRequest::getGuest ).anyMatch( visitor::equals );
    }
    
    
    public void addOnlineMember( UUID member ){
        if ( !online_members.contains( member ) ) {
            online_members.add( member );
        }
    }
    
    public void removeOnlineMember( UUID member ){
        online_members.remove( member );
    }
    
    public boolean hasOnlineMember( UUID member ){
        return online_members.contains( member );
    }
    
    
}
