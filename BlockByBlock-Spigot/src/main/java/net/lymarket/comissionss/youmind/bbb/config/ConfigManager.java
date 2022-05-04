package net.lymarket.comissionss.youmind.bbb.config;


import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.lyapi.spigot.config.Config;

public final class ConfigManager {
    
    private final Config config;
    private final Config items;
    
    public ConfigManager( Main plugin ){
        config = new Config( plugin , "config.yml" );
        items = new Config( plugin , "items.yml" );
    }
    
    public Config getConfig( ){
        return config;
    }
    
    public Config getItems( ){
        return items;
    }
}