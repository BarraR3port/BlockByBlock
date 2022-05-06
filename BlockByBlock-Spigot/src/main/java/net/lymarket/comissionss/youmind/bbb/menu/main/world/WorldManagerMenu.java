package net.lymarket.comissionss.youmind.bbb.menu.main.world;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.plot.PlotMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.create.WorldCreatorMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.edit.WorldEditorMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.PlayersInWorldMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class WorldManagerMenu extends UpdatableMenu {
    
    private final String serverVersion;
    
    private final UUID targetUserUUID;
    
    private final UUID ownerUUID;
    
    public WorldManagerMenu( IPlayerMenuUtility playerMenuUtility , String serverVersion , UUID targetUserUUID ){
        super( playerMenuUtility );
        this.serverVersion = serverVersion;
        this.targetUserUUID = targetUserUUID;
        this.ownerUUID = getOwner( ).getUniqueId( );
    }
    
    public WorldManagerMenu( IPlayerMenuUtility playerMenuUtility , String serverVersion , UUID targetUserUUID , long reOpenDelay ){
        super( playerMenuUtility );
        this.serverVersion = serverVersion;
        this.targetUserUUID = targetUserUUID;
        Bukkit.getServer( ).getScheduler( ).runTaskLater( Main.getInstance( ) , this::reOpen , reOpenDelay );
        this.ownerUUID = getOwner( ).getUniqueId( );
        
    }
    
    /**
     * @param e
     */
    @Override
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        final Player p = ( Player ) e.getWhoClicked( );
        
        if ( NBTItem.hasTag( item , "world-uuid" ) ) {
            final String server_target = NBTItem.getTag( item , "world-server" );
            final UUID world_uuid = UUID.fromString( NBTItem.getTag( item , "world-uuid" ) );
            final BWorld world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
            if ( e.getClick( ).equals( ClickType.LEFT ) ) {
                Main.getInstance( ).getSocket( ).sendFormattedJoinWorldRequest( ownerUUID , server_target , world_uuid , e.getSlot( ) );
            } else if ( e.getClick( ).equals( ClickType.RIGHT ) ) {
                if ( world.getOwner( ).equals( ownerUUID ) || Main.getInstance( ).getPlayers( ).getPlayer( ownerUUID ).getRank( ) == Rank.ADMIN ) {
                    new WorldEditorMenu( playerMenuUtility , targetUserUUID , world_uuid ).open( );
                } else if ( world.isPublicWorld( ) || world.getMembers( ).contains( ownerUUID ) ) {
                    new PlayersInWorldMenu( playerMenuUtility , world_uuid , targetUserUUID , false , this ).open( );
                }
            }
        } else if ( NBTItem.hasTag( item , "world-available" ) && p.getUniqueId( ).equals( targetUserUUID ) ) {
            new WorldCreatorMenu( this.playerMenuUtility , serverVersion , targetUserUUID ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            getOwner( ).closeInventory( );
            new PlotMenu( playerMenuUtility , serverVersion , targetUserUUID ).open( );
        }
        
        
    }
    
    @Override
    public void setMenuItems( ){
        
        Rank rank = Main.getInstance( ).getPlayers( ).getPlayer( targetUserUUID ).getRank( );
        
        int available = 1;
        int start = 11;
        for ( int i = start; i < (start + 5); i++ ) {
            inventory.setItem( i , Items.UNAVAILABLE_WORLD );
        }
        
        if ( rank == Rank.ADMIN ) {
            available = 5;
        } else if ( rank == Rank.DEV ) {
            available = 4;
        } else if ( rank == Rank.BUILDER ) {
            available = 3;
        }
        
        for ( int i = start; i < (start + available); i++ ) {
            inventory.setItem( i , Items.AVAILABLE_WORLD );
        }
        
        final ArrayList < BWorld > worlds = Main.getInstance( ).getWorlds( ).getWorldsByUser( targetUserUUID );
        
        final ArrayList < ItemStack > finalWorlds = new ArrayList <>( );
        
        for ( BWorld world : worlds ) {
            if ( world.getVersion( ).equalsIgnoreCase( serverVersion ) ) {
                finalWorlds.add( new ItemBuilder( Items.CREATED_WORLD_BASE.clone( ) )
                        .setDisplayName( "&bMundo: " + (world.getName( ).contains( "-" ) ? world.getName( ).split( "-" )[0] : world.getName( )) )
                        .addLoreLine( "&7Click para ir al mundo." )
                        .addLoreLine( "&bInfo:" )
                        .addLoreLine( " &b> &7Versión: &a" + world.getVersion( ) )
                        .addLoreLine( " &b> &7Server: &a" + world.getServer( ) )
                        .addLoreLine( " &b> &7Usuarios dentro: &a" + world.getOnlineMembers( ).size( ) )
                        .addLoreLine( " &b> &7ID: &a" + world.getUUID( ).toString( ).split( "-" )[0] )
                        .addLoreLine( " &b> &7Publico: " + (world.isPublicWorld( ) ? "&aSi" : "&cNo") )
                        
                        .addLoreLine( "" )
                        .addLoreLine( world.getOwner( ).equals( ownerUUID ) || world.isPublicWorld( ) || world.getMembers( ).contains( ownerUUID ) ? "&7Click &eizquierdo &7para entrar al mundo." : null )
                        .addLoreLine( "" )
                        .addLoreLine( world.getOwner( ).equals( ownerUUID ) ? "&7Click &ederecho &7para editar el mundo" :
                                world.isPublicWorld( ) || world.getMembers( ).contains( ownerUUID ) ? "&7Click &ederecho &7para ver la lista de miembros online" : null )
                        .addTag( "world-uuid" , world.getUUID( ).toString( ) )
                        .addTag( "world-server" , world.getServer( ) )
                        .build( )
                );
            }
        }
        
        for ( int i = 0; i < finalWorlds.size( ); i++ ) {
            try {
                inventory.setItem( 11 + i , finalWorlds.get( i ) );
            } catch ( ArrayIndexOutOfBoundsException ignored ) {
            }
        }
        
        
        inventory.setItem( 18 , super.CLOSE_ITEM );
    }
    
    @Override
    public String getMenuName( ){
        return targetUserUUID.equals( ownerUUID ) ? "&bTus mundos" : "&bMundos de " + Main.getInstance( ).getPlayers( ).getPlayer( targetUserUUID ).getName( );
    }
    
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void checkSomething( Player p , int slot , ItemStack item , String name , String lore ){
        
        if ( isOnSchedule ) return;
        
        isOnSchedule = true;
        
        p.getOpenInventory( ).getTopInventory( ).setItem( slot , new ItemBuilder( XMaterial.PLAYER_HEAD.parseItem( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=" )
                .addLoreLine( lore )
                .setDisplayName( name )
                .build( ) );
        setOnSchedule( p , slot , item );
        
    }
    
}
