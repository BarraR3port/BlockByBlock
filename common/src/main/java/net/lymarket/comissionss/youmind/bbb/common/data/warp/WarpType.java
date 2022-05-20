package net.lymarket.comissionss.youmind.bbb.common.data.warp;

public enum WarpType {
    CASAS( "CASAS" ),
    ARBOLES( "ARBOLES" ),
    VARIOS( "VARIOS" );
    
    private final String name;
    
    WarpType( String name ){
        this.name = name;
    }
    
    public String getName( ){
        return name;
    }
}
