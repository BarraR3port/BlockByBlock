package net.lymarket.comissionss.youmind.bbb.common.data.rank;

import java.util.Arrays;

public enum Rank {
    ADMIN( "admin" , "&d「Admin⏌" , "&d「Admin⏌" ),
    DEV( "dev" , " &9「Dev⏌" , " &9「Dev⏌" ),
    BUILDER( "builder" , " &b「B⏌" , "&b「Builder⏌" ),
    VISITOR( "default" , "&7「Visitor⏌" , "&7「Visitor⏌" );
    
    private final String lpName;
    private final String prefix;
    private final String tabPrefix;
    
    Rank( String lpName , String prefix , String tabPrefix ){
        this.lpName = lpName;
        this.prefix = prefix;
        this.tabPrefix = tabPrefix;
    }
    
    
    public static Rank fromString( String rank ){
        return Arrays.stream( Rank.values( ) ).filter( r -> r.getLpName( ).equalsIgnoreCase( rank ) ).findFirst( ).orElse( null );
    }
    
    public String getLpName( ){
        return this.lpName;
    }
    
    public String getPrefix( ){
        return this.prefix;
    }
    
    public String getTabPrefix( ){
        return this.tabPrefix;
    }
    
    public boolean isBuilder( ){
        return this == BUILDER || this == DEV || this == ADMIN;
    }
    
    public boolean isDev( ){
        return this == DEV;
    }
    
    public boolean isAdmin( ){
        return this == ADMIN;
    }
    
}
