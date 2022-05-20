package net.lymarket.comissionss.youmind.bbb.menu.main.world.create;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.event.PrevCreateWorld;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.world.WorldManager;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class WorldCreatorMenu extends UpdatableMenu {
    
    
    private final String serverVersion;
    
    private final UUID targetUserUUID;
    
    private final ArrayList < ItemStack > items = new ArrayList <>( );
    
    private int currentIndex = 0;
    
    
    public WorldCreatorMenu( IPlayerMenuUtility playerMenuUtility , String serverVersion , UUID targetUserUUID ){
        super( playerMenuUtility );
        this.serverVersion = serverVersion;
        this.targetUserUUID = targetUserUUID;
        
        items.add( new ItemBuilder( XMaterial.GRASS_BLOCK.parseMaterial( ) )
                .setDisplayName( "&7Selecciona el material base de tu mundo" )
                .addLoreLine( "" )
                .addLoreLine( "&7Material seleccionado: &a" )
                .addLoreLine( " &7> &a" + XMaterial.GRASS_BLOCK )
                .addLoreLine( "" )
                .build( ) );
    
        items.add( new ItemBuilder( XMaterial.BEDROCK.parseMaterial( ) )
                .setDisplayName( "&7Selecciona el material base de tu mundo" )
                .addLoreLine( "" )
                .addLoreLine( "&7Material seleccionado: &a" )
                .addLoreLine( " &7> &a" + XMaterial.BEDROCK )
                .addLoreLine( "" )
                .build( ) );
        
        items.add( new ItemBuilder( XMaterial.GLASS_PANE.parseMaterial( ) )
                .setDisplayName( "&7Selecciona el material base de tu mundo" )
                .addLoreLine( "" )
                .addLoreLine( "&7Material seleccionado: &a" )
                .addLoreLine( " &7> &aVacío" )
                .addLoreLine( "" )
                .build( ) );
    }
    
    public String getMenuName( ){
        return "Crear un mundo &bV" + serverVersion;
    }
    
    public int getSlots( ){
        return 27;
    }
    
    public void setMenuItems( ){
    
        inventory.setItem( 7 , new ItemBuilder( XMaterial.GLASS_PANE.parseMaterial( ) , 5 ).setDisplayName( "&7Click para cambiar de material" ).build( ) );
        
        inventory.setItem( 13 , new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgwZDMyOTVkM2Q5YWJkNjI3NzZhYmNiOGRhNzU2ZjI5OGE1NDVmZWU5NDk4YzRmNjlhMWMyYzc4NTI0YzgyNCJ9fX0=" )
                .setDisplayName( "&7Click para crear el mundo" )
                .addTag( "world" , "world" )
                .build( ) );
        
        inventory.setItem( 16 , items.get( currentIndex ) );
        
        inventory.setItem( 18 , super.CLOSE_ITEM );
    
        inventory.setItem( 25 , new ItemBuilder( XMaterial.GLASS_PANE.parseMaterial( ) , 14 ).setDisplayName( "&7Click para cambiar de material" ).build( ) );
        
        
    }
    
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        final Player p = ( Player ) e.getWhoClicked( );
        
        if ( e.getSlot( ) == 7 ) {
            if ( currentIndex + 1 >= items.size( ) ) {
                currentIndex = 0;
            } else {
                currentIndex = currentIndex + 1;
            }
            reOpen( );
            return;
        } else if ( e.getSlot( ) == 25 ) {
            if ( currentIndex - 1 <= 0 ) {
                currentIndex = items.size( ) - 1;
            } else {
                currentIndex = currentIndex - 1;
            }
            reOpen( );
            return;
        }
        if ( NBTItem.hasTag( item , "world" ) ) {
            new WorldCreatorMenu( this.playerMenuUtility , serverVersion , targetUserUUID ).open( );
            final Material material = items.get( currentIndex ).getType( ) != XMaterial.GLASS_PANE.parseMaterial( ) ? items.get( currentIndex ).getType( ) : XMaterial.AIR.parseMaterial( );
            final BWorld world = WorldManager.getWorldFormatted( p.getUniqueId( ) , serverVersion , material.toString( ) );
            Bukkit.getPluginManager( ).callEvent( new PrevCreateWorld( p.getUniqueId( ) , world , material ) );
            new WorldManagerMenu( this.playerMenuUtility , serverVersion , targetUserUUID , 10L ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            new WorldManagerMenu( playerMenuUtility , serverVersion , targetUserUUID , 10L ).open( );
        }
    }
    
}
