package net.lymarket.comissionss.youmind.bbb.commands.home;


import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RemoveHome implements ILyCommand {
    
    @Command(name = "removehome", permission = "bbb.home.remove", aliases = {"rmhome", "removeh", "rmh"}, usage = "/removehome <home>")
    public CommandResponse command(SCommandContext context){
        
        if (!(context.getSender() instanceof Player)){
            Main.getLang().sendErrorMsg(context.getSender(), "cant-execute-commands-from-console");
            return new CommandResponse();
        }
        
        if (context.getArgs().length == 1){
            final Player p = (Player) context.getSender();
            final String homeName = context.getArg(0);
            final SpigotHome home = Main.getInstance().getHomes().getUserHomeByName(p.getUniqueId(), homeName);
            if (home.getOwner().equals(p.getUniqueId()) || p.hasPermission("bbb.admin.home.remove")){
                Main.getLang().sendMsg(p, (Main.getInstance().getHomes().deleteHome(home) ? "home.deleted-successfully" : "error.home.error-deleting"), "home", homeName);
                return new CommandResponse("bbb.admin.home.remove");
            } else {
                Main.getLang().sendMsg(p, "error.home.error-deleting", "home", homeName);
            }
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "wrong-command", "command", "/removehome <nombre>");
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList < String > tabComplete(STabContext context){
        
        ArrayList < String > list = new ArrayList <>();
        if (context.getArgs().length == 1){
            list = Main.getInstance().getHomes().getHomesByUserAndVersion(((Player) context.getSender()).getUniqueId(), Settings.VERSION).stream().map(Home::getName).collect(Collectors.toCollection(ArrayList::new));
        }
        return list;
    }
}
