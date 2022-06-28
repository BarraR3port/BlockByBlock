package net.lymarket.comissionss.youmind.bbb.listener.plot;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.common.data.msg.PlotMsg;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.listener.MainEvents;
import net.lymarket.comissionss.youmind.bbb.settings.Settings;
import net.lymarket.comissionss.youmind.bbb.support.common.events.PlotCreateFailed;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

public final class PlotsPlayerEvent extends MainEvents {
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
    
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        e.getPlayer().setGameMode(GameMode.CREATIVE);
        try {
            if (e.getPlayer().getWorld().getName().equals("world")){
                e.getPlayer().teleport(Bukkit.getWorld("bbb-plots-31").getSpawnLocation());
            }
        } catch (NullPointerException ignored) {
        
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        try {
            if (e.getPlayer().getWorld().getName().equals("world")){
                e.getPlayer().teleport(Bukkit.getWorld("bbb-plots-31").getSpawnLocation());
            }
        } catch (NullPointerException ignored) {
        
        }
    }
    
    @EventHandler
    public void onPlotCreateFailed(PlotCreateFailed e){
        Main.getLang().sendErrorMsg(e.getPlayer(), "plot.create.failed", "plot-type", e.getPlotType().getFormattedName());
    }
    
    @Override
    public void subPlayerChatEvent(AsyncPlayerChatEvent e){
        final String worldName = e.getPlayer().getWorld().getName();
        try {
            if (worldName.equals("world")){
                if (e.getPlayer().teleport(Bukkit.getWorld("ppp-bbb-31").getSpawnLocation())){
                    final net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot plot = new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.getPlotTypeByWorld("ppp-bbb-31"), "", Settings.VERSION);
                    final PlotMsg msg = new PlotMsg(e.getPlayer().getUniqueId(), e.getMessage(), Settings.VERSION, plot);
                    Main.getInstance().getSocket().sendMsgFromPlayer(msg);
                }
                return;
            }
        } catch (NullPointerException ignored) {
        
        }
        final net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot plot = new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.getPlotTypeByWorld(worldName), "", Settings.VERSION);
        final PlotMsg msg = new PlotMsg(e.getPlayer().getUniqueId(), e.getMessage(), Settings.VERSION, plot);
        Main.getInstance().getSocket().sendMsgFromPlayer(msg);
    }
    
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        try {
            if (e.getPlayer().getWorld().getName().equals("world")){
                e.getPlayer().teleport(Bukkit.getWorld("bbb-plots-31").getSpawnLocation());
            }
        } catch (NullPointerException ignored) {
        
        }
    }
    
}
