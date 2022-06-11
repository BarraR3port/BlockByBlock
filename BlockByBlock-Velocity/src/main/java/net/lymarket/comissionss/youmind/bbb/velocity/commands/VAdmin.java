package net.lymarket.comissionss.youmind.bbb.velocity.commands;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import net.lymarket.comissionss.youmind.bbb.velocity.VMain;
import net.lymarket.comissionss.youmind.bbb.velocity.utils.Utils;

public class VAdmin implements SimpleCommand {
    public VAdmin(CommandManager commandManager){
        final CommandMeta meta = commandManager.metaBuilder("vadmin").build();
        commandManager.register(meta, this);
    }
    
    @Override
    public void execute(Invocation invocation){
        if (invocation.arguments().length == 1){
            if (invocation.source().hasPermission("blockbyblock.admin")){
                if (invocation.arguments()[0].equalsIgnoreCase("reload")){
                    invocation.source().sendMessage(Utils.format("&aReloading config..."));
                    VMain.getConfig().reloadConfig();
                } else if (invocation.arguments()[0].equalsIgnoreCase("debug")){
                    boolean result = !VMain.getConfig().getConfig().isDebug();
                    invocation.source().sendMessage(Utils.format("&aDebug mode is now " + (result ? "&aenabled" : "&cdisabled")));
                    VMain.getConfig().getConfig().setDebug(result);
                }
            }
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation){
        return invocation.source().hasPermission("blockbyblock.admin");
    }
}
