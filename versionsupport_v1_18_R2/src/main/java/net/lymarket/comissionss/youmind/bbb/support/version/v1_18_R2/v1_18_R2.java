package net.lymarket.comissionss.youmind.bbb.support.version.v1_18_R2;

import com.mojang.datafixers.util.Pair;
import net.lymarket.common.version.VersionSupport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class v1_18_R2 extends VersionSupport {
    
    private static final UUID chatUUID = new UUID( 0L , 0L );
    
    public v1_18_R2( Plugin plugin , String name ){
        super( plugin , name );
    }
    
    @Override
    public String getTag( org.bukkit.inventory.ItemStack itemStack , String key ){
        ItemStack i = CraftItemStack.asNMSCopy( itemStack );
        NBTTagCompound tag = i.t( );
        return tag == null ? null : tag.e( key ) ? tag.l( key ) : null;
    }
    
    @Override
    public boolean hasTag( org.bukkit.inventory.ItemStack itemStack , String key ){
        ItemStack i = CraftItemStack.asNMSCopy( itemStack );
        NBTTagCompound tag = i.t( );
        return tag != null && tag.e( key );
    }
    
    @Override
    public void sendTitle( Player p , String title , String subtitle , int fadeIn , int stay , int fadeOut ){
        p.sendTitle( title == null ? " " : title , subtitle == null ? " " : subtitle , fadeIn , stay , fadeOut );
    }
    
    @Override
    public void playAction( Player p , String text ){
        CraftPlayer cPlayer = ( CraftPlayer ) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + text + "\"}" );
        PacketPlayOutChat ppoc = new PacketPlayOutChat( cbc , ChatMessageType.c , chatUUID );
        cPlayer.getHandle( ).b.a( ppoc );
    }
    
    @Override
    public void hideEntity( Entity e , Player p ){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy( e.getEntityId( ) );
        (( CraftPlayer ) p).getHandle( ).b.a( packet );
        
    }
    
    @Override
    public void hideArmor( Player victim , Player receiver ){
        List < Pair < EnumItemSlot, ItemStack > > items = new ArrayList <>( );
        List < Pair < EnumItemSlot, ItemStack > > hands = new ArrayList <>( );
        hands.add( new Pair <>( EnumItemSlot.a , new ItemStack( Item.b( 0 ) ) ) );
        hands.add( new Pair <>( EnumItemSlot.b , new ItemStack( Item.b( 0 ) ) ) );
        
        items.add( new Pair <>( EnumItemSlot.f , new ItemStack( Item.b( 0 ) ) ) );
        items.add( new Pair <>( EnumItemSlot.e , new ItemStack( Item.b( 0 ) ) ) );
        items.add( new Pair <>( EnumItemSlot.d , new ItemStack( Item.b( 0 ) ) ) );
        items.add( new Pair <>( EnumItemSlot.c , new ItemStack( Item.b( 0 ) ) ) );
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , items );
        PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , hands );
        EntityPlayer pc = (( CraftPlayer ) receiver).getHandle( );
        if ( victim != receiver ) {
            pc.b.a( packet2 );
        }
        pc.b.a( packet1 );
    }
    
    @Override
    public void showArmor( Player victim , Player receiver ){
        List < Pair < EnumItemSlot, ItemStack > > items = new ArrayList <>( );
        List < Pair < EnumItemSlot, ItemStack > > hands = new ArrayList <>( );
        
        hands.add( new Pair <>( EnumItemSlot.a , CraftItemStack.asNMSCopy( victim.getInventory( ).getItemInMainHand( ) ) ) );
        hands.add( new Pair <>( EnumItemSlot.b , CraftItemStack.asNMSCopy( victim.getInventory( ).getItemInOffHand( ) ) ) );
        
        items.add( new Pair <>( EnumItemSlot.f , CraftItemStack.asNMSCopy( victim.getInventory( ).getHelmet( ) ) ) );
        items.add( new Pair <>( EnumItemSlot.e , CraftItemStack.asNMSCopy( victim.getInventory( ).getChestplate( ) ) ) );
        items.add( new Pair <>( EnumItemSlot.d , CraftItemStack.asNMSCopy( victim.getInventory( ).getLeggings( ) ) ) );
        items.add( new Pair <>( EnumItemSlot.c , CraftItemStack.asNMSCopy( victim.getInventory( ).getBoots( ) ) ) );
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , items );
        PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment( victim.getEntityId( ) , hands );
        EntityPlayer pc = (( CraftPlayer ) receiver).getHandle( );
        if ( victim != receiver ) {
            pc.b.a( packet2 );
        }
        pc.b.a( packet1 );
    }
    
    @Override
    public org.bukkit.inventory.ItemStack addCustomData( org.bukkit.inventory.ItemStack i , String data ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.t( );
        if ( tag == null ) {
            tag = new NBTTagCompound( );
            itemStack.c( tag );
        }
        
        tag.a( "LyApi" , data );
        return CraftItemStack.asBukkitCopy( itemStack );
    }
    
    @Override
    public org.bukkit.inventory.ItemStack setTag( org.bukkit.inventory.ItemStack itemStack , String key , String value ){
        ItemStack is = CraftItemStack.asNMSCopy( itemStack );
        NBTTagCompound tag = is.t( );
        if ( tag == null ) {
            tag = new NBTTagCompound( );
            is.c( tag );
        }
        
        tag.a( key , value );
        return CraftItemStack.asBukkitCopy( is );
    }
    
    @Override
    public boolean isCustomBedWarsItem( org.bukkit.inventory.ItemStack i ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.t( );
        if ( tag == null ) return false;
        return tag.e( "LyApi" );
    }
    
    @Override
    public String getCustomData( org.bukkit.inventory.ItemStack i ){
        ItemStack itemStack = CraftItemStack.asNMSCopy( i );
        NBTTagCompound tag = itemStack.t( );
        if ( tag == null ) return "";
        return tag.l( "LyApi" );
    }
    
    @Override
    public org.bukkit.Material materialPlayerHead( ){
        return org.bukkit.Material.PLAYER_HEAD;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getPlayerHead( Player player , org.bukkit.inventory.ItemStack copyTagFrom ){
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack( materialPlayerHead( ) );
        
        if ( copyTagFrom != null ) {
            ItemStack i = CraftItemStack.asNMSCopy( head );
            i.c( CraftItemStack.asNMSCopy( copyTagFrom ).t( ) );
            head = CraftItemStack.asBukkitCopy( i );
        }

//        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
//        FIXME: current hotfix will get rate limited! how the hell do we set head texture now?
//        wtf is this: SkullOwner:{Id:[I;-1344581477,-1919271229,-1306015584,-647763423],Name:"andrei1058"}
//        Field profileField;
//        try {
//            //noinspection ConstantConditions
//            profileField = headMeta.getMenuUUID().getDeclaredField("profile");
//            profileField.setAccessible(true);
//            profileField.set(headMeta, ((CraftPlayer) player).getProfile());
//        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
//            e1.printStackTrace();
//        }
//        assert headMeta != null;
//        headMeta.setOwningPlayer(player);
//        head.setItemMeta(headMeta);
        
        return head;
    }
    
    @Override
    public void spigotShowPlayer( Player victim , Player receiver ){
        receiver.showPlayer( getPlugin( ) , victim );
    }
    
    @Override
    public void spigotHidePlayer( Player victim , Player receiver ){
        receiver.hidePlayer( getPlugin( ) , victim );
    }
    
    
}
