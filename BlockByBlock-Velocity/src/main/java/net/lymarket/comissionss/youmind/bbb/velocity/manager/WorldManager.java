package net.lymarket.comissionss.youmind.bbb.velocity.manager;

import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.data.world.WorldVisitRequest;
import net.lymarket.comissionss.youmind.bbb.common.db.IBWorldManager;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class WorldManager extends IBWorldManager < Void > {
    
    
    public WorldManager(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    public ArrayList < BWorld > getWorldsByUser(UUID uuid){
        return null;
    }
    
    @Override
    public ArrayList < BWorld > getWorldsByServer(String serverName){
        return null;
    }
    
    @Override
    public ArrayList < BWorld > getWorldsByServer( ){
        return null;
    }
    
    public Void createWorldSlimeWorld(BWorld world){
        return null;
    }
    
    public Void createCustomLayerWorld(BWorld world, String material){
        return null;
    }
    
    public void createWorld(BWorld world){
        database.insertOne(TABLE_NAME, world);
    }
    
    @Override
    public void deleteWorldFromOutside(UUID senderUUID, BWorld bworld, String serverTarget, JsonObject json){
    
    }
    
    public boolean saveWorld(BWorld world){
        return database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", world.getUUID().toString()), world);
    }
    
    public BWorld getWorld(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        return Api.getGson().fromJson(doc.toJson(), BWorld.class);
    }
    
    @Override
    public boolean manageVisitJoinWorld(WorldVisitRequest request){
        return false;
    }
    
    public void saveWorld(Object world){
    
    }
    
    public ArrayList < BWorld > getAllWorlds( ){
        return database.findMany(TABLE_NAME, BWorld.class);
    }
    
    public void trashFinder( ){
        
        /*VMain.getInstance( ).getProxy( ).getScheduler( ).buildTask( VMain.getInstance( ) , ( ) -> {
            for ( BWorld world : getAllWorlds( ) ) {
                final HashMap<UUID, BWorld> playersToRemove = new HashMap<>();
                for ( UUID uuid : world.getOnlineMembers( ) ) {
                    VMain.getInstance( ).getProxy( ).getPlayer( uuid ).flatMap( Player::getCurrentServer ).ifPresent( server -> {
                        if ( !world.getServer( ).equalsIgnoreCase( server.getServerInfo( ).getName( ) ) ) {
                            playersToRemove.put( uuid , world );
                        }
    
                    } );
                }
                ---
                VMain.getInstance( ).getProxy( ).getAllPlayers( ).forEach( player -> {
                    if ( world.getOnlineMembers( ).contains( player.getUniqueId( ) ) ) {
                        player.getCurrentServer( ).ifPresent( server -> {
                            if ( !world.getServer( ).equalsIgnoreCase( server.getServerInfo( ).getName( ) ) ) {
                            
                            }
                            
                        } );
                    }
                } );---
            }
            
            
        } ).delay( 1 , TimeUnit.MINUTES ).schedule( );
        */
    }
}
