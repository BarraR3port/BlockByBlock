package net.lymarket.comissionss.youmind.bbb.commands.home;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.users.SpigotUser;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Homes implements ILyCommand {
    
    
    @Command(name = "homes", permission = "bbb.home.list")
    public CommandResponse command(SCommandContext context){
    
        if (!(context.getSender() instanceof Player)){
            Main.getLang().sendErrorMsg(context.getSender(), "cant-execute-commands-from-console");
            return new CommandResponse();
        }
        Player p = (Player) context.getSender();
        if (Settings.SERVER_TYPE != ServerType.WORLDS){
            Main.getLang().sendErrorMsg(p, "world.not-in-server");
            return new CommandResponse();
        }
        if (context.getArgs().length == 0){
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ( ) -> {
                final List < SpigotHome > homes = Main.getInstance().getHomes().getHomesByUserAndVersion(p.getUniqueId(), Settings.VERSION);
            
                if (homes == null || homes.isEmpty()){
                    Main.getLang().sendErrorMsg(context.getSender(), "home.no-homes");
                    return;
                }
                //todo improve this system and maybe make an GUI of it
                p.spigot().sendMessage(Utils.formatTC(Main.getLang().getMSG("home.homes-of", "player", p.getName())));
                int friendsPerPage = 7;
                int friendsIHave = homes.size();
            
                int pages = (friendsIHave / friendsPerPage) + (friendsIHave % friendsPerPage == 0 ? 0 : 1);
            
            
                for ( int i = 1; i <= pages; i++ ){
                    int max = Math.min(i * friendsPerPage, friendsIHave);
                    int min = Math.min((i - 1) * friendsPerPage, friendsIHave);
                    TextComponent text = Utils.formatTC("");
                    p.spigot().sendMessage(Utils.formatTC(Main.getLang().getMSG("home.homes-of-page").replace("%currentPage%", String.valueOf(i)).replace("%pages%", String.valueOf(pages))));
                    for ( int a = min; a < max; a++ ){
                        List < String > args = Main.getLang().getConfig().getStringList("home.homes-of-description");
                        SpigotHome h = homes.get(a);
                        List < String > replaced = new ArrayList <>();
                        for ( String arg : args ){
                            replaced.add(arg.replace("%home%", h.getName())
                                    .replace("%world_name%", (h.getLocation().getWorld().contains("-") ? h.getLocation().getWorld().split("-")[0] : h.getLocation().getWorld()))
                                    .replace("%x_location%", String.valueOf(h.getLocation().getX()))
                                    .replace("%y_location%", String.valueOf(h.getLocation().getY()))
                                    .replace("%z_location%", String.valueOf(h.getLocation().getZ()))
                                    .replace("%version%", String.valueOf(h.getVersion()))
                                    .replace("%server%", String.valueOf(h.getLocation().getServer()))
                            );
                        }
                        text.addExtra(Utils.hoverOverMessageRunCommand("&a" + h.getName(), replaced, "/home " + h.getUUID()));
                        text.addExtra(Utils.formatTC((a < max - 1) ? "&7, " : ""));
                    }
                    p.spigot().sendMessage(text);
                }
            });
            return new CommandResponse();
        } else if (context.getArgs().length == 1){
            final SpigotUser targetUser = Main.getInstance().getPlayers().getPlayer(context.getArg(0));
            if (targetUser != null){
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ( ) -> {
                    ArrayList < SpigotHome > homes = Main.getInstance().getHomes().getHomesByUser(targetUser.getUUID());
                
                    if (homes == null){
                        Main.getLang().sendErrorMsg(context.getSender(), "home.no-homes-other");
                        return;
                    }
                    //todo improve this system and maybe make an GUI of it
                    int homesPerPage = 7;
                    int homesIHave = homes.size();
                
                    int pages = (homesIHave / homesPerPage) + (homesIHave % homesPerPage == 0 ? 0 : 1);
                
                    p.spigot().sendMessage(Utils.formatTC(Main.getLang().getMSG("home.homes-of", "player", targetUser.getName())));
                
                    for ( int i = 1; i <= pages; i++ ){
                        int max = Math.min(i * homesPerPage, homesIHave);
                        int min = Math.min((i - 1) * homesPerPage, homesIHave);
                        TextComponent text = Utils.formatTC("");
                        p.spigot().sendMessage(Utils.formatTC(Main.getLang().getMSG("home.homes-of-page").replace("%currentPage%", String.valueOf(i)).replace("%pages%", String.valueOf(pages))));
                        for ( int a = min; a < max; a++ ){
                            List < String > args = Main.getLang().getConfig().getStringList("home.homes-of-description");
                            SpigotHome h = homes.get(a);
                            List < String > replaced = new ArrayList <>();
                            for ( String arg : args ){
                                replaced.add(arg.replace("%home%", h.getName())
                                        .replace("%world_name%", (h.getLocation().getWorld().contains("-") ? h.getLocation().getWorld().split("-")[0] : h.getLocation().getWorld()))
                                        .replace("%x_location%", String.valueOf(h.getLocation().getX()))
                                        .replace("%y_location%", String.valueOf(h.getLocation().getY()))
                                        .replace("%z_location%", String.valueOf(h.getLocation().getZ()))
                                        .replace("%version%", String.valueOf(h.getVersion())));
                            }
                            text.addExtra(Utils.hoverOverMessageRunCommand("&a" + h.getName(), replaced, "/home " + h.getUUID()));
                            text.addExtra(Utils.formatTC((a < max - 1) ? "&7, " : ""));
                        }
                        p.spigot().sendMessage(text);
                    }
                });
            } else {
                Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
                return new CommandResponse();
            }
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList < String > tabComplete(STabContext context){
        ArrayList < String > list = new ArrayList <>();
        if (context.getArgs().length == 1){
            return Main.getInstance().getPlayers().getPlayersName();
        }
        return list;
    }
}
