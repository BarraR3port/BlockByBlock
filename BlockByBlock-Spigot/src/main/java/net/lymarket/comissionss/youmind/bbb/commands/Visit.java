package net.lymarket.comissionss.youmind.bbb.commands;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.common.data.world.BWorld;
import net.lymarket.comissionss.youmind.bbb.common.data.world.WorldVisitRequest;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Visit implements ILyCommand {
    
    @Command(name = "visit", permission = "bbb.visit")
    public CommandResponse command(SCommandContext context){
        switch(context.getArgs().length){
            case 1:{
                final User userTarget = Main.getInstance().getPlayers().getPlayer(context.getArg(0));
                if (userTarget != null){
                    final UUID p = ((Player) context.getSender()).getUniqueId();
                    if (!p.equals(userTarget.getUUID())){
                        Main.getInstance().getSocket().sendVisitRequest(p, userTarget.getUUID(), context.getArg(0));
                    } else {
                        Main.getLang().sendErrorMsg(context.getSender(), "visit.self");
                    }
                } else {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.not-fund", "player", context.getArg(0));
                }
                return new CommandResponse();
            }
            case 4:{
                if (context.getArg(0).equalsIgnoreCase("accept") && context.getArg(1).equalsIgnoreCase("world")){
                    if (!(context.getSender() instanceof Player)){
                        return new CommandResponse();
                    }
                    final Player p = (Player) context.getSender();
                    try {
                        UUID owner_uuid = UUID.fromString(context.getArg(2));
                        UUID world_uuid = UUID.fromString(context.getArg(3));
    
                        final BWorld world = Main.getInstance().getWorlds().getWorld(world_uuid);
    
                        if (world.isVisitor(owner_uuid)){
                            final WorldVisitRequest rq = world.getVisitor(owner_uuid);
                            Main.getInstance().debug("Visit Accept");
                            Main.getInstance().getWorlds().manageVisitJoinWorld(rq.accept());
                        } else {
                            Main.getLang().sendErrorMsg(p, "visit.expired");
                        }
                        return new CommandResponse();
    
                    } catch (IllegalArgumentException e) {
                        Main.getLang().sendErrorMsg(p, "player.not-fund", "player", context.getArg(2));
                        return new CommandResponse();
                    }
                }
            }
            default:
                return new CommandResponse("bbb.visit");
        }
    }
    
    
    @Tab
    public ArrayList < String > tabComplete(STabContext context){
        final ArrayList < String > list = new ArrayList <>();
        if (context.getSender().hasPermission("bbb.visit")){
            if (context.getArgs().length == 1){
                list.addAll(Main.getInstance().getPlayers().getPlayersName(context.getSender().getName()));
                
            }
        }
        return list;
    }
}
