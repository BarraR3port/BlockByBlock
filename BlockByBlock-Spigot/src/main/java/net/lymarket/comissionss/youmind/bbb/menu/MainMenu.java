package net.lymarket.comissionss.youmind.bbb.menu;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.Stats;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.plot.PlotMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.entity.Player;
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
            final Player p = ( Player ) e.getWhoClicked( );
            switch ( version ) {
                case "1.12": {
                    int playerVersion = Main.getInstance( ).getViaVersion( ).getPlayerVersion( p );
                    if ( playerVersion >= 340 ) {
                        new PlotMenu( playerMenuUtility , version , targetUserUUID ).open( );
                        return;
                    } else {
                        super.checkSomething( p , e.getSlot( ) , item , "&cVersión incompatible con tu cliente." , "" , this.getMenuUUID( ) );
                    }
                }
                case "1.16": {
                    int playerVersion = Main.getInstance( ).getViaVersion( ).getPlayerVersion( p );
                    if ( playerVersion >= 754 ) {
                        new PlotMenu( playerMenuUtility , version , targetUserUUID ).open( );
                        return;
                    } else {
                        super.checkSomething( p , e.getSlot( ) , item , "&cVersión incompatible con tu cliente." , "" , this.getMenuUUID( ) );
                    }
                }
                case "1.18": {
                    int playerVersion = Main.getInstance( ).getViaVersion( ).getPlayerVersion( p );
                    if ( playerVersion >= 758 ) {
                        new PlotMenu( playerMenuUtility , version , targetUserUUID ).open( );
                        return;
                    } else {
                        super.checkSomething( p , e.getSlot( ) , item , "&cVersión incompatible con tu cliente." , "" , this.getMenuUUID( ) );
                    }
                }
                default: {
                    super.checkSomething( p , e.getSlot( ) , item , "&cVersión incompatible con tu cliente." , "" , this.getMenuUUID( ) );
                }
            }
            super.checkSomething( p , e.getSlot( ) , item , "&cVersión incompatible con tu cliente." , "" , this.getMenuUUID( ) );
        } else if ( NBTItem.hasTag( item , "world" ) ) {
            new WorldManagerMenu( playerMenuUtility , targetUserUUID ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            getOwner( ).closeInventory( );
        }
    }
    
    
    @Override
    public void setMenuItems( ){
    
        SpigotUser user = Main.getInstance( ).getPlayers( ).getUpdatedPlayer( targetUserUUID );
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
                .addLoreLine( "&aElo: " + (stats.getELO( ) > 0 ? "&a" + stats.getELO( ) : "&c" + stats.getELO( )) )
                .addTag( "stats" , "stats" )
                .build( ) );
        
        
        inventory.setItem( 45 , super.CLOSE_ITEM );
        
    }
}
