package net.lymarket.comissionss.youmind.bbb.common.error;

import java.util.UUID;

public class HomeNotFoundError extends RuntimeException {
    
    public HomeNotFoundError(UUID homeUUID){
        super("Home " + homeUUID.toString() + " not found");
    }
    
    public HomeNotFoundError(String home){
        super("Home " + home + " not found");
    }
}
