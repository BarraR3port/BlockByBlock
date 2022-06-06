package net.lymarket.comissionss.youmind.bbb.socket;

import com.google.gson.*;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.LobbyMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.Msg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.PlotMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.WorldMsg;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.UUID;


public class SpigotSocketClientTest {
    
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    
    @Test
    public void sendWorldVisitResponse( ){
        LobbyMsg lobbyMsg = new LobbyMsg(UUID.fromString("bc7d7eb8-cb64-4002-8ee7-6bd68e04d789"), "dddddddddddddddddddd", "1.12");
        
        /*String msg = "{\n" +
                "  \"type\": \"PLAYER_SENT_MSG\",\n" +
                "  \"msg_type\": \"WorldMsg\",\n" +
                "  \"msg\": \"{\\n  \\\"world\\\": {\\n    \\\"uuid\\\": \\\"69f3696e-4b0e-40a4-8d77-5409cf30f83e\\\",\\n    \\\"members\\\": [\\n      \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\"\\n    ],\\n    \\\"description\\\": [],\\n    \\\"visitors\\\": [],\\n    \\\"block_base\\\": \\\"GRASS\\\",\\n    \\\"online_members\\\": [\\n      \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\"\\n    ],\\n    \\\"name\\\": \\\"69f3696e-4b0e-40a4-8d77-5409cf30f83e\\\",\\n    \\\"owner\\\": \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\",\\n    \\\"server\\\": \\\"PW-112-1\\\",\\n    \\\"version\\\": \\\"1.12\\\"\\n  },\\n  \\\"owner\\\": \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\",\\n  \\\"version\\\": \\\"1.12\\\",\\n  \\\"msg\\\": \\\"" + encrypt("holaaa\\\"\\a \\\"\\") + "\\\"\\n}\"\n" +
                "}";*/
        System.out.println("Original MSG Class:\n" + lobbyMsg.getMsg());
        String msg = toJson(lobbyMsg).toString();
        msg = msg.replace("\\n", "");
        msg = msg.replace("\n", "");
        msg = msg.replace("\\\"", "\"");
        msg = msg.replace("\"{", "{");
        msg = msg.replace("}\"", "}");
        System.out.println("Original MSG:\n" + msg);
        String encrypted = encrypt(msg);
        System.out.println(encrypted);
        String decryptedMessage = decrypt(encrypted);
        System.out.println(decryptedMessage);
        
        JsonElement jse = new JsonParser().parse(decryptedMessage);
        
        final JsonObject json = jse.getAsJsonObject();
        //final LobbyMsg < SpigotUser > lobbyMsg = gson.fromJson( json.getAsJsonObject( "msg" ).getAsString( ) , LobbyMsg.class );
        
        //System.out.println( json.getAsJsonObject( "msg" ) );
        LobbyMsg finalLobbyMsg = gson.fromJson(json.getAsJsonObject("msg"), LobbyMsg.class);
        System.out.println(finalLobbyMsg.getMsg());
        
    }
    
    //@Test
    
    public JsonObject toJson(Msg msg){
        JsonObject js = new JsonObject();
        js.addProperty("type", "PLAYER_SENT_MSG");
        if (msg instanceof PlotMsg){
            js.addProperty("msg_type", "PlotMsg");
            final PlotMsg plotMsg = (PlotMsg) msg;
            js.addProperty("msg", gson.toJson(plotMsg));
        } else if (msg instanceof WorldMsg){
            js.addProperty("msg_type", "WorldMsg");
            final WorldMsg worldMsg = (WorldMsg) msg;
            js.addProperty("msg", gson.toJson(worldMsg));
        } else {
            js.addProperty("msg_type", "OTHER");
            final LobbyMsg lobbyMsg = (LobbyMsg) msg;
            js.addProperty("msg", gson.toJson(lobbyMsg));
        }
        return js;
    }
    
    public void sendMessageFromPlayer( ) throws IOException{
        /*String msg = "{\n" +
                "  \"type\": \"PLAYER_SENT_MSG\",\n" +
                "  \"msg_type\": \"OTHER\",\n" +
                "  \"msg\": {\n" +
                "    \"owner\": \"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\",\n" +
                "    \"version\": \"1.12\",\n" +
                "    \"msg\": \"Test de compilación coddddmpletado, pueden seguir en lo que estaban\"\n" +
                "  }\n" +
                "}";*/
        
        String msg = "{\n" +
                "  \"type\": \"PLAYER_SENT_MSG\",\n" +
                "  \"msg_type\": \"WorldMsg\",\n" +
                "  \"msg\": \"{\\n  \\\"world\\\": {\\n    \\\"uuid\\\": \\\"69f3696e-4b0e-40a4-8d77-5409cf30f83e\\\",\\n    \\\"members\\\": [\\n      \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\"\\n    ],\\n    \\\"description\\\": [],\\n    \\\"visitors\\\": [],\\n    \\\"block_base\\\": \\\"GRASS\\\",\\n    \\\"online_members\\\": [\\n      \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\"\\n    ],\\n    \\\"name\\\": \\\"69f3696e-4b0e-40a4-8d77-5409cf30f83e\\\",\\n    \\\"owner\\\": \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\",\\n    \\\"server\\\": \\\"PW-112-1\\\",\\n    \\\"version\\\": \\\"1.12\\\"\\n  },\\n  \\\"owner\\\": \\\"bc7d7eb8-cb64-4002-8ee7-6bd68e04d789\\\",\\n  \\\"version\\\": \\\"1.12\\\",\\n  \\\"msg\\\": \\\"" + encrypt("holaaa\\\"\\a \\\"\\") + "\\\"\\n}\"\n" +
                "}";
        System.out.println("Original MSG:\n" + msg);
        
        msg = msg.replace("\\n", "");
        msg = msg.replace("\n", "");
        msg = msg.replace("\\\"", "\"");
        msg = msg.replace("\"{", "{");
        msg = msg.replace("}\"", "}");
        String encrypted = encrypt(msg);
        System.out.println(encrypted);
        String decryptedMessage = decrypt(encrypted);
        System.out.println(decryptedMessage);
        
        JsonElement jse = new JsonParser().parse(decryptedMessage);
        
        final JsonObject json = jse.getAsJsonObject();
        //final LobbyMsg < SpigotUser > lobbyMsg = gson.fromJson( json.getAsJsonObject( "msg" ).getAsString( ) , LobbyMsg.class );
        
        //System.out.println( json.getAsJsonObject( "msg" ) );
        System.out.println(gson.fromJson(json.getAsJsonObject("msg"), WorldMsg.class).getMsg());
        
        
        try {
            Socket socket = new Socket("207.244.238.225", 5555);
            socket.setSoTimeout(2000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(encrypted);
            //BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
            //System.out.println( in.readLine( ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String encrypt(String json){
        return Base64.getEncoder().encodeToString(json.getBytes());
    }
    
    public String decrypt(String data){
        return new String(Base64.getDecoder().decode(data.getBytes()));
    }
}