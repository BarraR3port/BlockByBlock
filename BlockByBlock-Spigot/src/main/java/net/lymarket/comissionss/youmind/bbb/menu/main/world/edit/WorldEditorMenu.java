package net.lymarket.comissionss.youmind.bbb.menu.main.world.edit;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.items.Items;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.WorldManagerMenu;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.edit.delete.DeleteWorldSelector;
import net.lymarket.comissionss.youmind.bbb.menu.main.world.playersInWorld.PlayersInWorldMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

public class WorldEditorMenu extends UpdatableMenu {
    
    private final UUID targetUserUUID;
    
    private final UUID ownerUUID;
    
    private final UUID world_uuid;
    
    private BWorld world;
    
    
    public WorldEditorMenu( IPlayerMenuUtility playerMenuUtility , UUID targetUserUUID , UUID world_uuid ){
        super( playerMenuUtility );
        this.targetUserUUID = targetUserUUID;
        this.world_uuid = world_uuid;
        this.world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
        this.ownerUUID = getOwner( ).getUniqueId( );
    }
    
    @Override
    public void onReOpen( ){
        this.world = Main.getInstance( ).getWorlds( ).getWorld( world_uuid );
    }
    
    public String getMenuName( ){
        return "Editar el mundo: " + (world.getName( ).contains( "-" ) ? world.getName( ).split( "-" )[0] : world.getName( ));
    }
    
    public int getSlots( ){
        return 27;
    }
    
    public void setMenuItems( ){
        
        inventory.setItem( 12 , new ItemBuilder( XMaterial.PAPER.parseItem( ) )
                .setDisplayName( "&6&lNombre del mundo" )
                .addLoreLine( "&7Click para cambiar el nombre del mundo." )
                .addTag( "edit-name" , "edit-name" )
                .build( ) );
        
        inventory.setItem( 10 , new ItemBuilder( Items.PLAYERS_IN_WORLD_BASE.clone( ) )
                .setDisplayName( "&bMundo: " + (world.getName( ).contains( "-" ) ? world.getName( ).split( "-" )[0] : world.getName( )) )
                .addLoreLine( "&7Click para ver la lista" )
                .addLoreLine( "&7de jugadores dentro del mundo." )
                .addLoreLine( "" )
                .addLoreLine( "&bInfo:" )
                .addLoreLine( " &b> &7Versión: &a" + world.getVersion( ) )
                .addLoreLine( " &b> &7Server: &a" + world.getServer( ) )
                .addLoreLine( " &b> &7Usuarios dentro: &a" + world.getOnlineMembers( ).size( ) )
                .addLoreLine( " &b> &7ID: &a" + world.getUUID( ).toString( ).split( "-" )[0] )
                .addTag( "world-uuid" , world.getUUID( ).toString( ) )
                .addTag( "world-server" , world.getServer( ) )
                .addTag( "players-in-world" , "players-in-world" )
                .build( ) );
        
        ItemStack members = new ItemBuilder( Items.PLAYERS_IN_WORLD_BASE.clone( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNkNTkxNTUzNzhiNGFjNGQyYjE0MmUyZjIzNWQwMzdmNjhhOWI4ZTI0YWU5ZWQ0ODU3MzE2YjI4ZGNlMDU2ZiJ9fX0=" )
                .setDisplayName( "&bMiembros: " + world.getMembers( ).size( ) )
                .addLoreLine( "&7Click para ver la lista" )
                .addLoreLine( "&7de miembros dentro del mundo." )
                .addLoreLine( "" )
                .addLoreLine( "&bMiembros:" )
                .addTag( "world-uuid" , world.getUUID( ).toString( ) )
                .addTag( "world-server" , world.getServer( ) )
                .addTag( "members-in-world" , "members-in-world" )
                .build( );
        
        for ( int i = 0; i < world.getMembers( ).size( ); i++ ) {
            final UUID member = world.getMembers( ).get( i );
            if ( i > 6 ) {
                members = new ItemBuilder( members )
                        .addLoreLine( " &b> &e" + Main.getInstance( ).getPlayers( ).getPlayer( member ).getName( ) + "&c..." )
                        .build( );
                break;
            } else {
                members = new ItemBuilder( members )
                        .addLoreLine( " &b> " + (!world.getOwner( ).equals( member ) ? "&e" : "&c") + Main.getInstance( ).getPlayers( ).getPlayer( member ).getName( ) )
                        .build( );
            }
        }
        
        inventory.setItem( 16 , members );
        
        inventory.setItem( 14 , new ItemBuilder( XMaterial.BARRIER.parseMaterial( ) )
                .setDisplayName( "&cBorrar Mundo" )
                .addLoreLine( "&7Click para borrar mundo" )
                .addTag( "delete-world" , "delete-world" )
                .build( ) );
        
        inventory.setItem( 26 , new ItemBuilder( Items.PLAYERS_IN_WORLD_BASE.clone( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNkNTkxNTUzNzhiNGFjNGQyYjE0MmUyZjIzNWQwMzdmNjhhOWI4ZTI0YWU5ZWQ0ODU3MzE2YjI4ZGNlMDU2ZiJ9fX0=" )
                .setDisplayName( "&aAgregar miembros: " )
                .addLoreLine( "&7Click para agregar miembros." )
                .addTag( "world-uuid" , world.getUUID( ).toString( ) )
                .addTag( "add-members-to-world" , "add-members-to-world" )
                .build( ) );
        
        inventory.setItem( 18 , super.CLOSE_ITEM );
        
    }
    
    public void handleMenu( InventoryClickEvent e ){
        final ItemStack item = e.getCurrentItem( );
        final Player p = ( Player ) e.getWhoClicked( );
        
        if ( NBTItem.hasTag( item , "edit-name" ) ) {
            p.closeInventory( );
            p.sendMessage( Utils.format( "&8&m----------------------------------------------&e" ) );
            p.spigot( ).sendMessage( Utils.formatTC( "&7Edita el nombre de tu mundo dándole " ) , Utils.hoverOverMessageSuggestCommand( "&aCLICK AQUÍ." , Collections.singletonList( "&e/worlds set name " + world.getUUID( ) + " < El nombre >" ) , "/worlds set name " + world.getUUID( ) + " " ) );
            p.sendMessage( Utils.format( "&8&m----------------------------------------------&e" ) );
        } else if ( NBTItem.hasTag( item , "delete-world" ) ) {
            if ( world.getOwner( ).equals( ownerUUID ) || Main.getInstance( ).getPlayers( ).getPlayer( ownerUUID ).getRank( ) == Rank.ADMIN ) {
                new DeleteWorldSelector( playerMenuUtility , world.getUUID( ) , this , p.getUniqueId( ) ).open( );
            } else {
                checkSomething( getOwner( ) , e.getSlot( ) , item , "&cNo puedes borrar este mundo" , "" , getMenuUUID( ) );
            }
            
        } else if ( NBTItem.hasTag( item , "add-members-to-world" ) ) {
            //TODO CREATE THE MENU
            if ( world.getOwner( ).equals( ownerUUID ) || Main.getInstance( ).getPlayers( ).getPlayer( ownerUUID ).getRank( ) == Rank.ADMIN ) {
                p.spigot( ).sendMessage( Utils.formatTC( "&7Agrega miembros al mundo dándole " ) , Utils.hoverOverMessageSuggestCommand( "&aCLICK AQUÍ." , Collections.singletonList( "&e/worlds trust " + world.getUUID( ) + " < El nombre >" ) , "/worlds trust " + world.getUUID( ) + " " ) );
                p.closeInventory( );
            } else {
                checkSomething( getOwner( ) , e.getSlot( ) , item , "&cNo tienes permisos o no eres el Dueño del mundo" , "" , getMenuUUID( ) );
            }
            
        } else if ( NBTItem.hasTag( item , "players-in-world" ) ) {
            new PlayersInWorldMenu( playerMenuUtility , world.getUUID( ) , false , this ).open( );
        } else if ( NBTItem.hasTag( item , "members-in-world" ) ) {
            new PlayersInWorldMenu( playerMenuUtility , world.getUUID( ) , true , this ).open( );
        } else if ( NBTItem.hasTag( item , "ly-menu-close" ) ) {
            new WorldManagerMenu( playerMenuUtility , world.getVersion( ) , targetUserUUID , 10L ).open( );
        }
    }
}
