package net.lymarket.comissionss.youmind.bbb.common.error;

import java.util.UUID;

public class WarpNotFoundError extends RuntimeException {
    
    public WarpNotFoundError( UUID warpUUID ){
        super( "Warp " + warpUUID.toString( ) + " not found" );
    }
    
    public WarpNotFoundError( String warp ){
        super( "Warp " + warp + " not found" );
    }
}