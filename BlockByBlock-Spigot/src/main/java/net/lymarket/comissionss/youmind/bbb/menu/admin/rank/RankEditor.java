package net.lymarket.comissionss.youmind.bbb.menu.admin.rank;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.admin.AdminMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RankEditor extends UpdatableMenu {
    
    private User user;
    
    public RankEditor( IPlayerMenuUtility playerMenuUtility , User user ){
        super( playerMenuUtility );
        this.user = user;
    }
    
    @Override
    public String getMenuName( ){
        return "Cambia el rango de: " + user.getName( );
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void setMenuItems( ){
        
        inventory.setItem( 10 , new ItemBuilder( Items.RANK_VISITOR_BASE.clone( ) ).setEnchanted( user.getRank( ).equals( Rank.VISITOR ) ).build( ) );
        inventory.setItem( 12 , new ItemBuilder( Items.RANK_BUILDER_BASE.clone( ) ).setEnchanted( user.getRank( ).equals( Rank.BUILDER ) ).build( ) );
        inventory.setItem( 14 , new ItemBuilder( Items.RANK_DEV_BASE.clone( ) ).setEnchanted( user.getRank( ).equals( Rank.DEV ) ).build( ) );
        inventory.setItem( 16 , new ItemBuilder( Items.RANK_ADMIN_BASE.clone( ) ).setEnchanted( user.getRank( ).equals( Rank.ADMIN ) ).build( ) );
        inventory.setItem( 18 , this.CLOSE_ITEM );
        
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        
        if ( NBTItem.hasTag( item , "rank" ) ) {
            final Rank rank = Rank.valueOf( NBTItem.getTag( item , "rank" ) );
            if ( user.getRank( ) != rank ) {
                Bukkit.dispatchCommand( Bukkit.getConsoleSender( ) , "lp user " + user.getName( ) + " group set " + rank.getLpName( ) );
                user.setRank( rank );
                Main.getInstance( ).getPlayers( ).savePlayer( user );
                reOpen( );
            } else {
                this.checkSomething( getOwner( ) , e.getSlot( ) , item , "&cEste usuario ya tiene este rango" , "" );
            }
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            new AdminMenu( this.playerMenuUtility , user ).open( );
        }
    }
    
    @Override
    public void onReOpen( ){
        this.user = Main.getInstance( ).getPlayers( ).getPlayer( user.getUUID( ) );
    }
}
