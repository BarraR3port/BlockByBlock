package net.lymarket.comissionss.youmind.bbb.menu.admin;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.Stats;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.admin.rank.RankEditor;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class AdminMenu extends UpdatableMenu {
    
    private SpigotUser target;
    
    public AdminMenu( IPlayerMenuUtility playerMenuUtility , SpigotUser target ){
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
        final String version = Settings.VERSION;
        final boolean changed = target.getOption( "changed-plots" );
        final Rank rank = target.getRank( );
        final Stats stats = target.getStats( );
        final ItemStack plus = new ItemBuilder( XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial( ) , 5 )
                .setDisplayName( "&a+1" )
                .build( );
    
        final ItemStack minus = new ItemBuilder( XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial( ) , 14 )
                .setDisplayName( "&c-1" )
                .build( );
        final int currentWorlds = Main.getInstance( ).getWorlds( ).getWorldsByUser( target.getUUID( ) ).size( );
        //---------------------------------------------------------------------------------------------------------------------//
    
        inventory.setItem( 10 , plus );
        inventory.setItem( 19 ,
                new ItemBuilder( Items.PLOT_31_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 31x31:" )
                        .addLoreLine( " &b> &a" + target.getPlots31( version ).size( ) )
                        .addLoreLine( "&7Máximo de Plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_31( ) + stats.getMAX_PLOTS_31( ) : rank.getMAX_PLOTS_31( )) )
                        .build( ) );
        inventory.setItem( 28 , minus );
    
        //---------------------------------------------------------------------------------------------------------------------//
    
        inventory.setItem( 11 , plus );
        inventory.setItem( 20 ,
                new ItemBuilder( Items.PLOT_101_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 101x101:" )
                        .addLoreLine( " &b> &a" + target.getPlots101( version ).size( ) )
                        .addLoreLine( "&7Máximo de Plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_101( ) + stats.getMAX_PLOTS_101( ) : rank.getMAX_PLOTS_101( )) )
                        .build( ) );
        inventory.setItem( 29 , minus );
    
        //---------------------------------------------------------------------------------------------------------------------//
    
        inventory.setItem( 12 , plus );
        inventory.setItem( 21 ,
                new ItemBuilder( XMaterial.SUNFLOWER.parseItem( ) )
                        .setDisplayName( "&eElo: " + (stats.getELO( ) > 0 ? "&a" : "&c") + stats.getELO( ) )
                        .addTag( "elo" , "elo" )
                        .build( ) );
        inventory.setItem( 30 , minus );
    
        //---------------------------------------------------------------------------------------------------------------------//
    
        inventory.setItem( 14 , plus );
        inventory.setItem( 23 ,
                new ItemBuilder( Items.CREATED_WORLD_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .setDisplayName( "&eMundos actuales: " )
                        .addLoreLine( " &b> " + (currentWorlds > 0 ? "&a" : "&c") + currentWorlds )
                        .addLoreLine( "&7Máximo de Mundos: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_WORLDS( ) + stats.getADDITIONAL_WORLDS( ) : rank.getMAX_WORLDS( )) )
                        .build( ) );
        inventory.setItem( 32 , minus );
    
        //---------------------------------------------------------------------------------------------------------------------//
    
        inventory.setItem( 15 , plus );
        inventory.setItem( 24 ,
                new ItemBuilder( Items.PLOT_501_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 501x501:" )
                        .addLoreLine( " &b> &a" + target.getPlots501( version ).size( ) )
                        .addLoreLine( "&7Máximo de Plots: " )
                        .addLoreLine( " &b> &a" + (changed ? rank.getMAX_PLOTS_501( ) + stats.getMAX_PLOTS_501( ) : rank.getMAX_PLOTS_501( )) )
                        .build( ) );
        inventory.setItem( 33 , minus );
    
        //---------------------------------------------------------------------------------------------------------------------//
        
        inventory.setItem( 16 , plus );
        inventory.setItem( 25 ,
                new ItemBuilder( Items.PLOT_1001_BASE.clone( ) )
                        .setLore( Collections.singletonList( "" ) )
                        .addLoreLine( "&7Plots actuales de 1001x1001:" )
                        .addLoreLine( " &b> &a" + target.getPlots1001( version ).size( ) )
                        .addLoreLine( "&7Máximo de Plots: " )
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
            case 11: {
                target.getStats( ).addMAX_PLOTS_101( 1 );
                changed = true;
                break;
            }
            case 12: {
                target.getStats( ).addELO( 1 );
                changed = true;
                break;
            }
            case 14: {
                target.getStats( ).addADDITIONAL_WORLDS( 1 );
                changed = true;
                break;
            }
            case 15: {
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
            case 29: {
                target.getStats( ).removeMAX_PLOTS_101( 1 );
                changed = true;
                break;
            }
            case 30: {
                target.getStats( ).removeELO( 1 );
                changed = true;
                break;
            }
            case 32: {
                target.getStats( ).removeADDITIONAL_WORLDS( 1 );
                changed = true;
                break;
            }
            case 33: {
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
