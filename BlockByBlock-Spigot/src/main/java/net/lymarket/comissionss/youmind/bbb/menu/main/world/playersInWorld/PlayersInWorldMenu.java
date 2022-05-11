package net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.add.AddPlayersToWorldMenuSelector;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.kick.KickPlayerFromWorld;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.remove.RemoveMemberFromWorld;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.menu.UpPaginatedMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class PlayersInWorldMenu extends UpPaginatedMenu {
    
    private final UUID world_uuid;
    private final BukkitTask task;
    private final boolean members;
    private final Menu lastMenu;
    private BWorld world;
    private User user;
    private ArrayList < User > onlineMembers;
    
    public PlayersInWorldMenu( IPlayerMenuUtility playerMenuUtility , UUID world_uuid , boolean members , Menu lastMenu ){
        super( playerMenuUtility );
        this.world_uuid = world_uuid;
        this.world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
        onlineMembers = members ? world.getMembers( ).stream( ).map( uuid -> Main.getInstance( ).getPlayers( ).getPlayer( uuid ) ).collect( ArrayList::new , ArrayList::add , ArrayList::addAll ) : world.getOnlineMembers( ).stream( ).map( uuid -> Main.getInstance( ).getPlayers( ).getPlayer( uuid ) ).collect( ArrayList::new , ArrayList::add , ArrayList::addAll );
        user = Main.getInstance( ).getPlayers( ).getPlayer( getOwner( ).getUniqueId( ) );
        task = Bukkit.getServer( ).getScheduler( ).runTaskTimerAsynchronously( Main.getInstance( ) , this::reOpen , 40L , 20L );
        super.FILLER_GLASS = new ItemBuilder( super.FILLER_GLASS.clone( ) ).setDurability( ( short ) 15 ).build( );
        this.members = members;
        this.lastMenu = lastMenu;
    }
    
    public void setSize( ){
    
    }
    
    @Override
    public void handleClose( InventoryCloseEvent e ){
        task.cancel( );
    }
    
    public String getMenuName( ){
        return (members ? "Miembros de " : "Jugadores en: ") + (world.getName( ).contains( "-" ) ? world.getName( ).split( "-" )[0] : world.getName( ));
    }
    
    public void setMenuItems( ){
        addMenuBorder( );
        setHeadItems( );
    }
    
    private void setHeadItems( ){
        if ( !onlineMembers.isEmpty( ) )
            for ( int i = 0; i < maxItemsPerPage; i++ ) {
                this.index = maxItemsPerPage * this.page + i;
                if ( this.index >= onlineMembers.size( ) )
                    break;
                if ( onlineMembers.get( this.index ) != null ) {
                    final User user = onlineMembers.get( this.index );
                    if ( members ) {
                        this.inventory.addItem( new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                                .setHeadSkin( user.getSkin( ) )
                                .setDisplayName( "&6" + user.getName( ) )
                                .addLoreLine( "&7Es dueño: " + (world.getOwner( ).equals( user.getUUID( ) ) ? "&aSi" : "&cNo") )
                                .addLoreLine( "" )
                                .addLoreLine( world.getOwner( ).equals( user.getUUID( ) ) ? "&7Click &ederecho &7para eliminar de los miembros" : null )
                                .addTag( "user-name" , user.getName( ) )
                                .addTag( "user-uuid" , user.getUUID( ).toString( ) )
                                .build( ) );
                    } else {
                        this.inventory.addItem( new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                                .setHeadSkin( user.getSkin( ) )
                                .setDisplayName( "&6" + user.getName( ) )
                                .addLoreLine( "&7Es dueño: " + (world.getOwner( ).equals( user.getUUID( ) ) ? "&aSi" : "&cNo") )
                                .addLoreLine( "&7Es miembro: " + (world.getMembers( ).contains( user.getUUID( ) ) ? "&aSi" : "&cNo") )
                                .addLoreLine( "" )
                                .addLoreLine( world.getOwner( ).equals( user.getUUID( ) ) ? "&7Click &eizquierdo &7para agregar a la lista de miembros." : null )
                                .addLoreLine( "" )
                                .addLoreLine( world.getOwner( ).equals( user.getUUID( ) ) ? "&7Click &ederecho &7para sacar del mundo" : null )
                                .addTag( "user-name" , user.getName( ) )
                                .addTag( "user-uuid" , user.getUUID( ).toString( ) )
                                .build( ) );
                    }
                    
                }
            }
    }
    
    @Override
    public void addMenuBorder( ){
        inventory.setItem( 48 , page == 0 ? FILLER_GLASS : PREV_ITEM );
        
        inventory.setItem( 49 , CLOSE_ITEM );
        
        inventory.setItem( 50 , index + 1 >= size ? FILLER_GLASS : NEXT_ITEM );
        
        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , FILLER_GLASS );
            }
        }
        
        inventory.setItem( 17 , FILLER_GLASS );
        inventory.setItem( 18 , FILLER_GLASS );
        inventory.setItem( 26 , FILLER_GLASS );
        inventory.setItem( 27 , FILLER_GLASS );
        inventory.setItem( 35 , FILLER_GLASS );
        inventory.setItem( 36 , FILLER_GLASS );
        
        for ( int i = 44; i < 54; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , FILLER_GLASS );
            }
        }
        
    }
    
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        
        
        if ( NBTItem.hasTag( item , "user-name" ) ) {
            final UUID targetUUID = UUID.fromString( NBTItem.getTag( item , "user-uuid" ) );
            if ( members ) {
                if ( e.getClick( ).equals( ClickType.RIGHT ) ) {
                    if ( world.getOwner( ).equals( user.getUUID( ) ) || getOwner( ).hasPermission( "blockbyblock.admin.world.kick" ) || user.getRank( ) == Rank.ADMIN ) {
                        new KickPlayerFromWorld( playerMenuUtility , world_uuid , this , targetUUID ).open( );
                    }
                }
                if ( e.getClick( ).equals( ClickType.LEFT ) ) {
                    if ( world.getOwner( ).equals( user.getUUID( ) ) || getOwner( ).hasPermission( "blockbyblock.admin.world.members.add" ) || user.getRank( ) == Rank.ADMIN ) {
                        new AddPlayersToWorldMenuSelector( playerMenuUtility , world_uuid , this , targetUUID ).open( );
                    }
                    return;
                }
            } else {
                if ( e.getClick( ).equals( ClickType.RIGHT ) ) {
                    if ( world.getOwner( ).equals( user.getUUID( ) ) || getOwner( ).hasPermission( "blockbyblock.admin.world.kick" ) || user.getRank( ) == Rank.ADMIN ) {
                        new RemoveMemberFromWorld( playerMenuUtility , world_uuid , this , targetUUID ).open( );
                    }
                }
            }
            task.cancel( );
            e.getWhoClicked( ).closeInventory( );
            
            
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            task.cancel( );
            lastMenu.open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-next" ) ) {
            this.nextPage( );
        } else if ( NBTItem.hasTag( item , "ly-menu-previous" ) ) {
            this.prevPage( );
        }
    }
    
    @Override
    public void reOpen( ){
        try {
            for ( ItemStack item : inventory.getContents( ) ) {
                if ( NBTItem.hasTag( item , "user-name" ) ) {
                    inventory.remove( item );
                }
            }
            this.onReOpen( );
            setHeadItems( );
        } catch ( NullPointerException ignored ) {
            this.task.cancel( );
        }
    }
    
    @Override
    public void onReOpen( ){
        this.world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
        onlineMembers.clear( );
        onlineMembers = members ? world.getMembers( ).stream( ).map( uuid -> Main.getInstance( ).getPlayers( ).getPlayer( uuid ) ).collect( ArrayList::new , ArrayList::add , ArrayList::addAll ) : world.getOnlineMembers( ).stream( ).map( uuid -> Main.getInstance( ).getPlayers( ).getPlayer( uuid ) ).collect( ArrayList::new , ArrayList::add , ArrayList::addAll );
        user = Main.getInstance( ).getPlayers( ).getPlayer( getOwner( ).getUniqueId( ) );
    }
}
