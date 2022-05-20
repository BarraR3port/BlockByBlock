package net.lymarket.comissionss.youmind.bbb.listener.plugin;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {
    
    
    public PluginMessage( ){
    
    }
    
    @Override
    public void onPluginMessageReceived( String channel , Player p , byte[] msg ){
        if ( channel.equals( "lymarket:bbb" ) || channel.equals( "BungeeCord" ) ) {
            ByteArrayDataInput in = ByteStreams.newDataInput( msg );
            String subChannel = in.readUTF( );
            switch ( subChannel ) {
                case "OpenGui": {
                    String guiType = in.readUTF( );
                    String owner = in.readUTF( );
                    String target = in.readUTF( );
                    //Bukkit.getPluginManager( ).callEvent( new OpenGuiEvent( Enum.valueOf( GuiType.class , guiType ) , owner , target ) );
                    return;
                }
                case "GetServer": {
                    String servername = in.readUTF( );
                    if ( Settings.SERVER_NAME == null || Settings.SERVER_NAME.equals( "" ) ) {
                        Main.getInstance( ).getConfig( ).set( "global.proxy-server-name" , servername );
                        Main.getInstance( ).getConfig( ).saveData( );
                    }
                    Settings.SERVER_NAME = servername;
                    return;
                    
                }
                case "GetData": {
                    /*int tbPlayers = in.readInt( );
                    int bwPlayers = in.readInt( );
                    int svPlayers = in.readInt( );
                    int skPlayers = in.readInt( );
                    int lobby1Players = in.readInt( );
                    int pvpGamesTotalPlayers = in.readInt( );
                    int kitpvpPlayers = in.readInt( );
                    int arenapvpPlayers = in.readInt( );
                    int practicePlayers = in.readInt( );
                    int allPlayers = in.readInt( );*/
                    
                }
                
            }
        }
    }
    
    
}
