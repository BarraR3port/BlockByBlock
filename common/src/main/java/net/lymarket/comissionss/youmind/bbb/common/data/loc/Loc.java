package net.lymarket.comissionss.youmind.bbb.common.data.loc;

import net.lymarket.common.Api;

public class Loc {
    
    private final String Server;
    private final String World;
    
    private final double X;
    private final double Y;
    private final double Z;
    private final float Yaw;
    private final float Pitch;
    
    public Loc( String server , String world , double x , double y , double z , float yaw , float pitch ){
        Server = server;
        World = world;
        X = x;
        Y = y;
        Z = z;
        Yaw = yaw;
        Pitch = pitch;
    }
    
    public String serialize( ){
        return Api.getGson( ).toJson( this );
    }
    
    public double getX( ){
        return X;
    }
    
    public double getY( ){
        return Y;
    }
    
    public double getZ( ){
        return Z;
    }
    
    public float getYaw( ){
        return Yaw;
    }
    
    public float getPitch( ){
        return Pitch;
    }
    
    public String getServer( ){
        return Server;
    }
    
    public String getWorld( ){
        return World;
    }
    
}
