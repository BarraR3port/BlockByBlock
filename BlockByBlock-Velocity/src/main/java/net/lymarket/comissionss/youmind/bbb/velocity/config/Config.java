package net.lymarket.comissionss.youmind.bbb.velocity.config;

import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class Config {
    private MainConfig config;
    
    public Config( Path path ){
        Path configPath = path.resolve( "config.yml" );
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder( )
                .defaultOptions( opts -> opts
                        .shouldCopyDefaults( true )
                        .header( "BlockByBlock | BarraR3port\n------------- | -------------" )
                )
                .path( configPath )
                .build( );
        
        try {
            final CommentedConfigurationNode node = loader.load( );
            config = node.get( MainConfig.class );
            node.set( MainConfig.class , config );
            loader.save( node );
        } catch ( ConfigurateException exception ) {
            VMain.debug( "Could not load config.yml file, error: " + exception.getMessage( ) );
        }
    }
    
    public MainConfig getConfig( ){
        return config;
    }
    
    @ConfigSerializable
    public static class MainConfig {
        
        @Comment("When true, many logs will be showed up in the console.")
        @Setting(value = "debug")
        private boolean debug = true;
        
        @Comment("Mongodb host")
        @Setting(value = "host")
        private String db_host = "207.244.238.225";
        
        @Comment("Mongodb port")
        @Setting(value = "port")
        private int db_port = 27017;
    
        @Comment("Mongodb database")
        @Setting(value = "database")
        private String db_database = "bbb";
    
        @Comment("Mongodb urli")
        @Setting(value = "urli")
        private String db_urli = "mongodb://bbb:djj200akdlll2kKASDJJ2KA@207.244.238.225:27017/?directConnection=true";
    
    
        @Comment("Mongodb username")
        @Setting(value = "username")
        private String db_username = "bbb";
    
        @Comment("Mongodb password")
        @Setting(value = "password")
        private String db_password = "djj200akdlll2kKASDJJ2KA";
        
        
        public boolean isDebug( ){
            return debug;
        }
        
        public void setDebug( boolean debug ){
            this.debug = debug;
        }
        
        public String getDb_host( ){
            return db_host;
        }
        
        public void setDb_host( String db_host ){
            this.db_host = db_host;
        }
        
        public int getDb_port( ){
            return db_port;
        }
        
        public void setDb_port( int db_port ){
            this.db_port = db_port;
        }
        
        public String getDb_database( ){
            return db_database;
        }
        
        public void setDb_database( String db_database ){
            this.db_database = db_database;
        }
        
        public String getDb_urli( ){
            return db_urli;
        }
        
        public void setDb_urli( String db_urli ){
            this.db_urli = db_urli;
        }
        
        public String getDb_username( ){
            return db_username;
        }
        
        public void setDb_username( String db_username ){
            this.db_username = db_username;
        }
        
        public String getDb_password( ){
            return db_password;
        }
        
        public void setDb_password( String db_password ){
            this.db_password = db_password;
        }
    }
    
}
