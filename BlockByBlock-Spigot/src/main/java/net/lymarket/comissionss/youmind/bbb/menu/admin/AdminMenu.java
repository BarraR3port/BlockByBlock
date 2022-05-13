package net.lymarket.comissionss.youmind.bbb.menu.admin;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.Stats;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.admin.rank.RankEditor;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class AdminMenu extends UpdatableMenu {
    
    private User target;
    
    public AdminMenu( IPlayerMenuUtility playerMenuUtility , User target ){
        super( playerMenuUtility );
        this.target = target;
    }
    
    public String getMenuName( ){
        return "Administrar a: " + target.getName( );
    }
    
    public int getSlots( ){
        return 54;
    }
    
    public void setMenuItems( ){
        
        final boolean changed = target.getOption( "changed-plots" );
        final Rank rank = target.getRank( );
        final Stats stats = target.getStats( );
        final ItemStack plus = new ItemBuilder( Material.STAINED_GLASS_PANE , 5 )
                .setDisplayName( "&a+1" )
                .build( );
        
        final ItemStack minus = new ItemBuilder( Material.STAINED_GLASS_PANE , 14 )
                .setDisplayName( "&c-1" )
                .build( );
        
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 10 , plus );
        inventory.setItem( 19 ,
                new ItemBuilder( Items.PLOT_31_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 31x31:" )
                        .addLoreLine( " &b> &a" + target.getPlots31( ).size( ) )
                        .addLoreLine( "&7Máximo de plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_31( ) + stats.getMAX_PLOTS_31( ) : rank.getMAX_PLOTS_31( )) )
                        .build( ) );
        inventory.setItem( 28 , minus );
        
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 12 , plus );
        inventory.setItem( 21 ,
                new ItemBuilder( Items.PLOT_101_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 101x101:" )
                        .addLoreLine( " &b> &a" + target.getPlots101( ).size( ) )
                        .addLoreLine( "&7Máximo de plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_101( ) + stats.getMAX_PLOTS_101( ) : rank.getMAX_PLOTS_101( )) )
                        .build( ) );
        inventory.setItem( 30 , minus );
        
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 13 , plus );
        inventory.setItem( 22 ,
                new ItemBuilder( XMaterial.SUNFLOWER.parseItem( ) )
                        .setDisplayName( "&eElo: " + (stats.getELO( ) > 0 ? "&a" + stats.getELO( ) : "&c" + stats.getELO( )) )
                        .addTag( "elo" , "elo" )
                        .build( ) );
        inventory.setItem( 31 , minus );
        
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 14 , plus );
        inventory.setItem( 23 ,
                new ItemBuilder( Items.PLOT_501_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 501x501:" )
                        .addLoreLine( " &b> &a" + target.getPlots501( ).size( ) )
                        .addLoreLine( "&7Máximo de plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_501( ) + stats.getMAX_PLOTS_501( ) : rank.getMAX_PLOTS_501( )) )
                        .build( ) );
        inventory.setItem( 32 , minus );
        
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 16 , plus );
        inventory.setItem( 25 ,
                new ItemBuilder( Items.PLOT_1001_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 1001x1001:" )
                        .addLoreLine( " &b> &a" + target.getPlots1001( ).size( ) )
                        .addLoreLine( "&7Máximo de plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_1001( ) + stats.getMAX_PLOTS_1001( ) : rank.getMAX_PLOTS_1001( )) )
                        .build( ) );
        inventory.setItem( 34 , minus );
        
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 49 , new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( target.getSkin( ) )
                .setDisplayName( "&b&lStats" )
                .addLoreLine( "" )
                .addLoreLine( "&aTiempo Jugado: " + stats.getFormattedTimePlayed( ) )
                .addLoreLine( "&aBloques destruidos: " + stats.getBLOCKS_BROKEN( ) )
                .addLoreLine( "&aBloques Colocados: " + stats.getBLOCKS_PLACED( ) )
                .addLoreLine( "&aElo: " + (stats.getELO( ) > 0 ? "&a" + stats.getELO( ) : "&c" + stats.getELO( )) )
                .addTag( "stats" , "stats" )
                .build( ) );
        
        inventory.setItem( 45 , super.CLOSE_ITEM.clone( ) );
        
    }
    
    @Override
    public void onReOpen( ){
        target = Main.getInstance( ).getPlayers( ).getPlayer( target.getUUID( ) );
    }
    
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        final int slot = e.getRawSlot( );
        boolean changed = false;
        switch ( slot ) {
            case 10: {
                target.getStats( ).addMAX_PLOTS_31( 1 );
                changed = true;
                break;
            }
            case 12: {
                target.getStats( ).addMAX_PLOTS_101( 1 );
                changed = true;
                break;
            }
            case 13: {
                target.getStats( ).addELO( 1 );
                changed = true;
                break;
            }
            case 14: {
                target.getStats( ).addMAX_PLOTS_501( 1 );
                changed = true;
                break;
            }
            case 16: {
                target.getStats( ).addMAX_PLOTS_1001( 1 );
                changed = true;
                break;
            }
            case 28: {
                target.getStats( ).removeMAX_PLOTS_31( 1 );
                changed = true;
                break;
            }
            case 30: {
                target.getStats( ).removeMAX_PLOTS_101( 1 );
                changed = true;
                break;
            }
            case 31: {
                target.getStats( ).removeELO( 1 );
                changed = true;
                break;
            }
            case 32: {
                target.getStats( ).removeMAX_PLOTS_501( 1 );
                changed = true;
                break;
            }
            case 34: {
                target.getStats( ).removeMAX_PLOTS_1001( 1 );
                changed = true;
                break;
            }
        }
        if ( changed ) {
            target.setOption( "changed-plots" , true );
            Main.getInstance( ).getPlayers( ).savePlayer( target );
            reOpen( );
        }
        
        
        if ( NBTItem.hasTag( item , "stats" ) ) {
            new RankEditor( this.playerMenuUtility , target ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            getOwner( ).closeInventory( );
            //new WorldManagerMenu( playerMenuUtility , serverVersion , targetUserUUID , 10L ).open( );
        }
    }
    
}
