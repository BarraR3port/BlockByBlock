package net.lymarket.comissionss.youmind.bbb.support.version.v1_12_R1.world;

import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import org.bukkit.event.Listener;


public class WorldListener implements Listener {
    
    
    private final VersionSupport vs;
    
    
    public WorldListener( VersionSupport vs ){
        this.vs = vs;
    }
    
    /*@EventHandler
    public void onSlimeWorldPreGenerate( PreGenerateWorldEvent event ){
        try {
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            System.out.println( "WOrld Name: " + event.getSlimeWorld( ).getName( ) );
            final BWorld world = vs.getBbbApi( ).getWorlds( ).getWorld( UUID.fromString( event.getSlimeWorld( ).getName( ) ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            System.out.println( "World Block Base: " + world.getBlock_base( ) );
            event.setMaterial_base( Material.valueOf( world.getBlock_base( ) ) );
        } catch ( Exception ignored ) {
        
        }
    }*/
    
    
}
