package net.lymarket.comissionss.youmind.bbb.common.db;

import net.lymarket.common.db.MongoDBClient;

import java.util.HashMap;


public abstract class MongoDB< K, V > {
    
    protected final String TABLE_NAME;
    
    protected final HashMap < K, V > list = new HashMap <>( );
    
    protected final MongoDBClient database;
    
    public MongoDB( MongoDBClient database , String TABLE_NAME ){
        this.database = database;
        this.TABLE_NAME = TABLE_NAME;
        this.trashFinder( );
    }
    
    public abstract void trashFinder( );
    
}
