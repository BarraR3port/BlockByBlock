package net.lymarket.comissionss.youmind.bbb.items;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public final class Items {
    
    public static ItemStack LOBBY_MENU;
    
    public static ItemStack LOBBY_BOOK;
    public static ItemStack BUILDER_1_12;
    public static ItemStack BUILDER_1_16;
    public static ItemStack BUILDER_1_18;
    
    public static ItemStack WORLDS;
    public static ItemStack PLOTS;
    public static ItemStack AVAILABLE_WORLD;
    public static ItemStack UNAVAILABLE_WORLD;
    public static ItemStack CREATED_WORLD_BASE;
    public static ItemStack PLAYERS_IN_WORLD_BASE;
    public static ItemStack PLOT_31_BASE;
    public static ItemStack PLOT_101_BASE;
    public static ItemStack PLOT_501_BASE;
    public static ItemStack PLOT_1001_BASE;
    public static ItemStack RANK_VISITOR_BASE;
    public static ItemStack RANK_BUILDER_BASE;
    public static ItemStack RANK_DEV_BASE;
    public static ItemStack RANK_ADMIN_BASE;
    public static ItemStack WARP_CASAS_BASE;
    public static ItemStack WARP_ARBOLES_BASE;
    public static ItemStack WARP_VARIOS_BASE;
    
    public Items( ){
    
    }
    
    public static void init( Config config ){
        LOBBY_MENU = new ItemBuilder( config.getItem( "lobby-item" ) )
                .addTag( "lobby-item" , "lobby-item" )
                .build( );
        LOBBY_BOOK = new ItemBuilder( config.getItem( "lobby-book" ) )
                .addTag( "lobby-book" , "lobby-book" )
                .setBookTitle( Utils.format( Main.getLang( ).getConfig( ).getString( "lobby-book-details.title" ) ) )
                .setBookAuthor( Utils.format( Main.getLang( ).getConfig( ).getString( "lobby-book-details.author" ) ) )
                .build( );
        
        BookMeta bookMeta = ( BookMeta ) LOBBY_BOOK.getItemMeta( );
        
        final ArrayList < String > pages = new ArrayList <>( Main.getLang( ).getConfig( ).getConfigurationSection( "lobby-book-details.pages" ).getKeys( false ) );
        
        for ( String page : pages ) {
            final StringBuilder lines = new StringBuilder( );
            for ( String paragraph : Main.getLang( ).getConfig( ).getStringList( "lobby-book-details.pages." + page + ".paragraphs" ) ) {
                lines.append( ChatColor.translateAlternateColorCodes( '&' , paragraph ) ).append( "\n\n" );
            }
            bookMeta.addPage( lines.toString( ) );
            
        }
        LOBBY_BOOK.setItemMeta( bookMeta );
        for ( Player p : Bukkit.getOnlinePlayers( ) ) {
            setItems( p );
        }
        
        BUILDER_1_12 = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMxOTA5NjFhODI2MDhjNDY4ZGU0Y2Q4NDJkNjBlNzUzNjRiYjRhMmUyNDdjMzUxOGYwMDU1YzdiMmMyOTBkYSJ9fX0=" )
                .setDisplayName( "&b&lBuilding &61.12" )
                .addLoreLine( "&7Entra a construir con bloques" )
                .addLoreLine( "&7de la 1.12" )
                .addLoreLine( "" )
                .addLoreLine( "&7Estado: &aACTIVO" )
                .addLoreLine( "" )
                .addTag( "server-version" , "1.12" )
                .build( );
        
        BUILDER_1_16 = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMxOTA5NjFhODI2MDhjNDY4ZGU0Y2Q4NDJkNjBlNzUzNjRiYjRhMmUyNDdjMzUxOGYwMDU1YzdiMmMyOTBkYSJ9fX0=" )
                .setDisplayName( "&b&lBuilding &e1.16" )
                .addLoreLine( "&7Entra a construir con bloques" )
                .addLoreLine( "&7más nuevos." )
                .addLoreLine( "" )
                .addLoreLine( "&7Estado: &aACTIVO" )
                .addLoreLine( "" )
                .addTag( "server-version" , "1.16" )
                .build( );
        
        BUILDER_1_18 = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMxOTA5NjFhODI2MDhjNDY4ZGU0Y2Q4NDJkNjBlNzUzNjRiYjRhMmUyNDdjMzUxOGYwMDU1YzdiMmMyOTBkYSJ9fX0=" )
                .setDisplayName( "&b&lBuilding &a1.18" )
                .addLoreLine( "&7Click para construir con" )
                .addLoreLine( "&7los nuevos bloques!" )
                .addLoreLine( "" )
                .addLoreLine( "&7Estado: &aACTIVO" )
                .addLoreLine( "" )
                .addTag( "server-version" , "1.18" )
                .build( );
        
        WORLDS = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgwZDMyOTVkM2Q5YWJkNjI3NzZhYmNiOGRhNzU2ZjI5OGE1NDVmZWU5NDk4YzRmNjlhMWMyYzc4NTI0YzgyNCJ9fX0=" )
                .setDisplayName( "&b&lWorlds Menu" )
                .addLoreLine( "&7Click para ver tus lista" )
                .addLoreLine( "&7de mundos o para crear mundos." )
                .addLoreLine( "" )
                .addTag( "world" , "world" )
                .build( );
        
        PLOTS = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNkOGViYjExZjVhMDVjN2FmMjJiMTcyOTc0ZDZiMWUzYzI3OGY4OGJiOTAxOTc3NzY2MTU0M2FkNjJiZDBhYSJ9fX0=" )
                .setDisplayName( "&7Plots Menu" )
                .addLoreLine( "&7Click para ver y entrar a" )
                .addLoreLine( "&7una de tus plots" )
                .addLoreLine( "" )
                .addTag( "plot" , "plot" )
                .build( );
        
        AVAILABLE_WORLD = new ItemBuilder( XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial( ) , 5 )
                .setDisplayName( "&7Mundo &aDisponible" )
                .addLoreLine( "&7Click para configurar un mundo." )
                .addTag( "world-available" , "world-available" )
                .build( );
        
        UNAVAILABLE_WORLD = new ItemBuilder( XMaterial.RED_STAINED_GLASS_PANE.parseMaterial( ) , 14 )
                .setDisplayName( "&7Mundo &cno disponible" )
                .addLoreLine( "&7Necesitas ser un rango más alto" )
                .addLoreLine( "&7para acceder a este slot." )
                .addTag( "world-unavailable" , "world-unavailable" )
                .build( );
        
        CREATED_WORLD_BASE = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgwZDMyOTVkM2Q5YWJkNjI3NzZhYmNiOGRhNzU2ZjI5OGE1NDVmZWU5NDk4YzRmNjlhMWMyYzc4NTI0YzgyNCJ9fX0=" ).build( );
        
        PLAYERS_IN_WORLD_BASE = new ItemBuilder( XMaterial.PLAYER_HEAD.parseMaterial( ) )
                .setHeadSkin( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBjZjZjZGMxMWRiNzRjMGQ3N2JhMzc1NmM2ZmRlMzQ1ZmU1NDQzZWNmN2VhNGE0MWQxNjI1NGU2NTk1ODRjZiJ9fX0" ).build( );
        
        PLOT_31_BASE = new ItemBuilder( XMaterial.WHITE_WOOL.parseMaterial( ) , 0 )
                .setDisplayName( "&7Plots &a31" )
                .addLoreLine( "&7Click para entrar a este plot." )
                .addLoreLine( "&7Tamaño: &a31x31" )
                .addTag( "plot-type" , "P31" )
                .build( );
        
        PLOT_101_BASE = new ItemBuilder( XMaterial.LIGHT_GRAY_WOOL.parseMaterial( ) , 8 )
                .setDisplayName( "&7Plots &a101" )
                .addLoreLine( "&7Click para entrar a este plot." )
                .addLoreLine( "&7Tamaño: &a101x101" )
                .addTag( "plot-type" , "P101" )
                .build( );
        
        PLOT_501_BASE = new ItemBuilder( XMaterial.GRAY_WOOL.parseMaterial( ) , 7 )
                .setDisplayName( "&7Plots &a501x501" )
                .addLoreLine( "&7Click para entrar a este plot." )
                .addLoreLine( "&7Tamaño: &a501x501" )
                .addTag( "plot-type" , "P501" )
                .build( );
        
        PLOT_1001_BASE = new ItemBuilder( XMaterial.BLACK_WOOL.parseMaterial( ) , 15 )
                .setDisplayName( "&7Plots &a1001x1001" )
                .addLoreLine( "&7Click para entrar a este plot." )
                .addLoreLine( "&7Tamaño: &a1001x1001" )
                .addTag( "plot-type" , "P1001" )
                .build( );
        
        RANK_VISITOR_BASE = new ItemBuilder( XMaterial.LIGHT_BLUE_WOOL.parseMaterial( ) , 3 )
                .setDisplayName( "&bVisitor" )
                .addLoreLine( "&7Click poner este rango al jugador." )
                .addTag( "rank" , "VISITOR" )
                .build( );
        
        RANK_BUILDER_BASE = new ItemBuilder( XMaterial.YELLOW_WOOL.parseMaterial( ) , 4 )
                .setDisplayName( "&eBuilder" )
                .addLoreLine( "&7Click poner este rango al jugador." )
                .addTag( "rank" , "BUILDER" )
                .build( );
        
        RANK_DEV_BASE = new ItemBuilder( XMaterial.PURPLE_WOOL.parseMaterial( ) , 10 )
                .setDisplayName( "&dBuilder" )
                .addLoreLine( "&7Click poner este rango al jugador." )
                .addTag( "rank" , "DEV" )
                .build( );
        
        RANK_ADMIN_BASE = new ItemBuilder( XMaterial.RED_WOOL.parseMaterial( ) , 14 )
                .setDisplayName( "&cADMIN" )
                .addLoreLine( "&7Click poner este rango al jugador." )
                .addTag( "rank" , "ADMIN" )
                .build( );
        
        WARP_CASAS_BASE = new ItemBuilder( XMaterial.BRICKS.parseItem( ) )
                .setDisplayName( "&aCasas" )
                .addLoreLine( "&7Click para ir a la warp de Casas." )
                .addTag( "warp" , "CASAS" )
                .build( );
        
        WARP_ARBOLES_BASE = new ItemBuilder( XMaterial.JUNGLE_SAPLING.parseItem( ) )
                .setDisplayName( "&aArboles" )
                .addLoreLine( "&7Click para ir a la warp de Arboles" )
                .addTag( "warp" , "ARBOLES" )
                .build( );
        
        WARP_VARIOS_BASE = new ItemBuilder( XMaterial.CHEST.parseItem( ) )
                .setDisplayName( "&aVarios" )
                .addLoreLine( "&7Click para ir a las otras warps" )
                .addTag( "warp" , "VARIOS" )
                .build( );
        
    }
    
    public static void setItems( Player p ){
        p.getInventory( ).clear( );
        p.getInventory( ).setItem( Main.getInstance( ).getItems( ).getInt( "items.lobby-item.slot" ) , LOBBY_MENU );
        p.getInventory( ).setItem( Main.getInstance( ).getItems( ).getInt( "items.lobby-book.slot" ) , LOBBY_BOOK );
        p.updateInventory( );
    }
    
}
