package net.lymarket.comissionss.youmind.bbb.commands.home;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.server.ServerType;
import net.lymarket.comissionss.youmind.bbb.home.SpigotHome;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SetHome implements ILyCommand {
    
    
    @Command(name = "sethome", permission = "bbb.home.set")
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
        if (p.getWorld().getName().equals("warp")){
            Main.getLang().sendErrorMsg(p, "world.not-in-world");
            return new CommandResponse();
        }
        if (context.getArgs().length == 1){
            final Loc location = new Loc(Settings.SERVER_NAME.getName(), p.getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), UUID.fromString(p.getWorld().getName()));
            final SpigotHome home = new SpigotHome(p.getUniqueId(), context.getArg(0), location, Settings.VERSION);
            Main.getInstance().getHomes().createHome(home);
            Main.getLang().sendMsg(context.getSender(), "home.created-successfully", "home", home.getName());
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "wrong-command", "command", "/sethome <nombre>");
            return new CommandResponse();
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList < String > tabComplete(STabContext context){
        return new ArrayList <>();
    }
}
