package net.lymarket.comissionss.youmind.bbb.home;

import com.mongodb.client.model.Filters;
import net.lymarket.comissionss.youmind.bbb.common.db.IHomeManager;
import net.lymarket.comissionss.youmind.bbb.common.error.HomeNotFoundError;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public final class HomeManager extends IHomeManager < SpigotHome > {
    
    public HomeManager(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    @Override
    public ArrayList < SpigotHome > getHomesByUser(UUID uuid){
        return database.findMany(TABLE_NAME, home -> home.getOwner().equals(uuid), SpigotHome.class);
    }
    
    @Override
    public ArrayList < SpigotHome > getHomesByUserAndVersion(UUID uuid, String version){
        return database.findMany(TABLE_NAME, home -> home.getOwner().equals(uuid) && home.getVersion().equals(version), SpigotHome.class);
    }
    
    @Override
    public ArrayList < SpigotHome > getHomesByServer(String server){
        return database.findMany(TABLE_NAME, home -> home.getLocation().getServer().equalsIgnoreCase(server), SpigotHome.class);
    }
    
    @Override
    public ArrayList < SpigotHome > getHomesByServer( ){
        return database.findMany(TABLE_NAME, home -> home.getLocation().getServer().equalsIgnoreCase(Settings.SERVER_NAME.getName()), SpigotHome.class);
    }
    
    @Override
    public ArrayList < SpigotHome > getHomesByWorld(UUID uuid){
        return database.findMany(TABLE_NAME, home -> home.getLocation().getBWorld().equals(uuid), SpigotHome.class);
    }
    
    @Override
    public ArrayList < SpigotHome > getHomes( ){
        return database.findMany(TABLE_NAME, SpigotHome.class);
    }
    
    @Override
    public void createHome(SpigotHome home){
        database.insertOne(TABLE_NAME, home);
    }
    
    @Override
    public boolean saveHome(SpigotHome home){
        return database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", home.getUUID().toString()), home);
    }
    
    @Override
    public SpigotHome getHome(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        SpigotHome home = Api.getGson().fromJson(doc.toJson(), SpigotHome.class);
        if (home == null){
            throw new HomeNotFoundError(uuid);
        }
        return home;
    }
    
    @Override
    public SpigotHome getUserHomeByName(UUID uuid, String homeName){
        Document doc = database.findOneFast(TABLE_NAME, Filters.and(Filters.eq("owner", uuid.toString()), Filters.eq("name", homeName)));
        SpigotHome home = Api.getGson().fromJson(doc.toJson(), SpigotHome.class);
        if (home == null){
            throw new HomeNotFoundError(uuid);
        }
        return home;
    }
    
    @Override
    public boolean deleteHome(SpigotHome home){
        return database.deleteOne(TABLE_NAME, Filters.eq("uuid", home.getUUID().toString()));
    }
    
    @Override
    public void trashFinder( ){
    
    }
}
