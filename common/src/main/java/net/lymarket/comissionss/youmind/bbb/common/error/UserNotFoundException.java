package net.lymarket.comissionss.youmind.bbb.common.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException( String msg ){
        super( "User not found: " + msg );
    }
}
