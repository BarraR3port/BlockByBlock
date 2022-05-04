package net.lymarket.comissionss.youmind.bbb.world;

import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import lombok.Data;
import org.bukkit.Difficulty;
import org.bukkit.World;

import static com.grinderwolf.swm.api.world.properties.SlimeProperties.*;

@Data
public class WorldData {
    
    
    private String dataSource = "file";
    
    
    private String spawn = "0.5, 255, 0.5";
    
    
    private String difficulty = "peaceful";
    
    
    private boolean allowMonsters = true;
    
    private boolean allowAnimals = true;
    
    
    private boolean dragonBattle = false;
    
    
    private boolean pvp = true;
    
    
    private String environment = "NORMAL";
    
    private String worldType = "DEFAULT";
    
    private String defaultBiome = "minecraft:plains";
    
    
    private boolean loadOnStartup = true;
    
    
    private boolean readOnly = false;
    
    public String getDataSource( ){
        return dataSource;
    }
    
    public void setDataSource( String dataSource ){
        this.dataSource = dataSource;
    }
    
    public String getSpawn( ){
        return spawn;
    }
    
    public void setSpawn( String spawn ){
        this.spawn = spawn;
    }
    
    public String getDifficulty( ){
        return difficulty;
    }
    
    public void setDifficulty( String difficulty ){
        this.difficulty = difficulty;
    }
    
    public boolean isAllowMonsters( ){
        return allowMonsters;
    }
    
    public void setAllowMonsters( boolean allowMonsters ){
        this.allowMonsters = allowMonsters;
    }
    
    public boolean isAllowAnimals( ){
        return allowAnimals;
    }
    
    public void setAllowAnimals( boolean allowAnimals ){
        this.allowAnimals = allowAnimals;
    }
    
    public boolean isDragonBattle( ){
        return dragonBattle;
    }
    
    public void setDragonBattle( boolean dragonBattle ){
        this.dragonBattle = dragonBattle;
    }
    
    public boolean isPvp( ){
        return pvp;
    }
    
    public void setPvp( boolean pvp ){
        this.pvp = pvp;
    }
    
    public String getEnvironment( ){
        return environment;
    }
    
    public void setEnvironment( String environment ){
        this.environment = environment;
    }
    
    public String getWorldType( ){
        return worldType;
    }
    
    public void setWorldType( String worldType ){
        this.worldType = worldType;
    }
    
    public String getDefaultBiome( ){
        return defaultBiome;
    }
    
    public void setDefaultBiome( String defaultBiome ){
        this.defaultBiome = defaultBiome;
    }
    
    public boolean isLoadOnStartup( ){
        return loadOnStartup;
    }
    
    public void setLoadOnStartup( boolean loadOnStartup ){
        this.loadOnStartup = loadOnStartup;
    }
    
    public boolean isReadOnly( ){
        return readOnly;
    }
    
    public void setReadOnly( boolean readOnly ){
        this.readOnly = readOnly;
    }
    
    public SlimePropertyMap toPropertyMap( ){
        try {
            Enum.valueOf( Difficulty.class , this.difficulty.toUpperCase( ) );
        } catch ( IllegalArgumentException ex ) {
            throw new IllegalArgumentException( "unknown difficulty '" + this.difficulty + "'" );
        }
        
        String[] spawnLocationSplit = spawn.split( ", " );
        
        double spawnX, spawnY, spawnZ;
        
        try {
            spawnX = Double.parseDouble( spawnLocationSplit[0] );
            spawnY = Double.parseDouble( spawnLocationSplit[1] );
            spawnZ = Double.parseDouble( spawnLocationSplit[2] );
        } catch ( NumberFormatException | ArrayIndexOutOfBoundsException ex ) {
            throw new IllegalArgumentException( "invalid spawn location '" + this.spawn + "'" );
        }
        
        String environment = this.environment;
        
        try {
            Enum.valueOf( World.Environment.class , environment.toUpperCase( ) );
        } catch ( IllegalArgumentException ex ) {
            try {
                int envId = Integer.parseInt( environment );
                
                if ( envId < -1 || envId > 1 ) {
                    throw new NumberFormatException( environment );
                }
                
                environment = World.Environment.getEnvironment( envId ).name( );
            } catch ( NumberFormatException ex2 ) {
                throw new IllegalArgumentException( "unknown environment '" + this.environment + "'" );
            }
        }
        
        SlimePropertyMap propertyMap = new SlimePropertyMap( );
        
        propertyMap.setValue( SPAWN_X , ( int ) spawnX );
        propertyMap.setValue( SPAWN_Y , ( int ) spawnY );
        propertyMap.setValue( SPAWN_Z , ( int ) spawnZ );
        
        propertyMap.setValue( DIFFICULTY , difficulty );
        propertyMap.setValue( ALLOW_MONSTERS , allowMonsters );
        propertyMap.setValue( ALLOW_ANIMALS , allowAnimals );
        propertyMap.setValue( DRAGON_BATTLE , dragonBattle );
        propertyMap.setValue( PVP , pvp );
        propertyMap.setValue( ENVIRONMENT , environment );
        propertyMap.setValue( WORLD_TYPE , worldType );
        propertyMap.setValue( DEFAULT_BIOME , defaultBiome );
        
        return propertyMap;
    }
}