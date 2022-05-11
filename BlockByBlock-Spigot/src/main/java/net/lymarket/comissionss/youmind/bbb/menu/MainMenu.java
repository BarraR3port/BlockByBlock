package net.lymarket.comissionss.youmind.bbb.menu;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.Stats;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.plot.PlotMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MainMenu extends Menu {
    
    private final UUID targetUserUUID;
    
    public MainMenu( IPlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
        this.targetUserUUID = getOwner( ).getUniqueId( );
    }
    
    public MainMenu( IPlayerMenuUtility playerMenuUtility , UUID targetUserUUID ){
        super( playerMenuUtility );
        this.targetUserUUID = targetUserUUID;
    }
    
    @Override
    public String getMenuName( ){
        return "Block By Block Menu";
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        if ( NBTItem.hasTag( item , "server-version" ) ) {
            final String version = NBTItem.getTag( item , "server-version" );
            new PlotMenu( playerMenuUtility , version , targetUserUUID ).open( );
        } else if ( NBTItem.hasTag( item , "world" ) ) {
            new WorldManagerMenu( playerMenuUtility , targetUserUUID ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            getOwner( ).closeInventory( );
        }
    }
    
    
    @Override
    public void setMenuItems( ){
    
        User user = Main.getInstance( ).getPlayers( ).getUpdatedPlayer( targetUserUUID );
        if ( user == null ) {
            user = Main.getInstance( ).getPlayers( ).getPlayer( getOwner( ).getName( ) );
            if ( user == null ) {
                getOwner( ).closeInventory( );
                return;
            }
            user.setUUID( getOwner( ).getUniqueId( ) );
            Main.getInstance( ).getPlayers( ).savePlayer( user );
        }
        final Stats stats = user.getStats( );
        inventory.setItem( 20 , Items.BUILDER_1_12 );
    
        inventory.setItem( 22 , Items.BUILDER_1_16 );
    
        inventory.setItem( 24 , Items.BUILDER_1_18 );
    
        inventory.setItem( 40 , new ItemBuilder( Items.WORLDS.clone( ) )
                .addLoreLine( "&7Mundos: &a" + Main.getInstance( ).getWorlds( ).getWorldsByUser( targetUserUUID ).size( ) )
                .build( ) );
        
        inventory.setItem( 53 , new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( user.getSkin( ) )
                .setDisplayName( "&b&lStats" )
                .addLoreLine( "" )
                .addLoreLine( "&aTiempo Jugado: " + stats.getFormattedTimePlayed( ) )
                .addLoreLine( "&aBloques destruidos: " + stats.getBLOCKS_BROKEN( ) )
                .addLoreLine( "&aBloques Colocados: " + stats.getBLOCKS_PLACED( ) )
                .addTag( "stats" , "stats" )
                .build( ) );
        
        
        inventory.setItem( 45 , super.CLOSE_ITEM );
        
    }
}
