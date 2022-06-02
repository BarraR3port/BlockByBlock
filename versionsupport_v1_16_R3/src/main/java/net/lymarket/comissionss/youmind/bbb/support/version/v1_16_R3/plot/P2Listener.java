package net.lymarket.comissionss.youmind.bbb.support.version.v1_16_R3.plot;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.*;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.user.User;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.UUID;

public class P2Listener implements Listener {
    
    private final VersionSupport vs;
    
    private final String version;
    
    public P2Listener(PlotAPI api, VersionSupport vs){
        this.vs = vs;
        api.registerListener(this);
        this.version = vs.getBbbApi().getVersion();
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        final UUID uuid = e.getPlayer().getUniqueId();
        final boolean tpToPlot = vs.getPlotManager().hasPlot(uuid);
        final boolean tpToWorld = vs.getPlotManager().hasWorldToTp(uuid);
        if (tpToPlot){
            final Plot plot = (Plot) vs.getPlotManager().getPlot(uuid);
            plot.teleportPlayer(new PlotAPI().wrapPlayer(uuid), (result) -> {
                if (result){
                    vs.getPlotManager().removePlot(uuid);
                }
            });
        } else if (tpToWorld){
            final World world = vs.getPlotManager().getWorldToTp(uuid);
            vs.getBbbApi().getSocket().sendMSGToPlayer(uuid, "plot.join", "plot", world.getName().split("-")[0]);
            e.getPlayer().teleport(world.getSpawnLocation());
            
        }
    }
    
    @Subscribe
    public void onPlayerEnterPlot(PlayerClaimPlotEvent e){
        final Player p = Bukkit.getPlayer(e.getPlotPlayer().getUUID());
        final Plot plot = e.getPlot();
        final User user = (User) vs.getBbbApi().getPlayers().getPlayer(p.getUniqueId());
        if (user == null){
            e.setEventResult(Result.DENY);
            return;
        }
        boolean canCreate = true;
        boolean changed = user.getOption("changed-plots");
        switch(user.getRank()){
            case ADMIN:
            case DEV:
                break;
            case BUILDER:{
                if (plot.getWorldName().equals(PlotType.P31.getWorldName())){
                    if (user.getPlots31(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_31() + user.getStats().getMAX_PLOTS_31() : Rank.BUILDER.getMAX_PLOTS_31())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (plot.getWorldName().equals(PlotType.P101.getWorldName())){
                    if (user.getPlots101(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_101() + user.getStats().getMAX_PLOTS_101() : Rank.BUILDER.getMAX_PLOTS_101())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P101.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (plot.getWorldName().equals(PlotType.P501.getWorldName())){
                    if (user.getPlots501(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_501() + user.getStats().getMAX_PLOTS_501() : Rank.BUILDER.getMAX_PLOTS_501())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P501.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (plot.getWorldName().equals(PlotType.P1001.getWorldName())){
                    if (user.getPlots1001(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_1001() + user.getStats().getMAX_PLOTS_1001() : Rank.BUILDER.getMAX_PLOTS_1001())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P1001.getFormattedName());
                        canCreate = false;
                        break;
                    }
                }
                break;
            }
            case VISITOR:{
                if (plot.getWorldName().equals(PlotType.P31.getWorldName())){
                    if (user.getPlots31(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_31() + user.getStats().getMAX_PLOTS_31() : Rank.VISITOR.getMAX_PLOTS_31())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (plot.getWorldName().equals(PlotType.P101.getWorldName())){
                    if (user.getPlots101(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_101() + user.getStats().getMAX_PLOTS_101() : Rank.VISITOR.getMAX_PLOTS_101())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P101.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (plot.getWorldName().equals(PlotType.P501.getWorldName())){
                    if (user.getPlots501(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_501() + user.getStats().getMAX_PLOTS_501() : Rank.VISITOR.getMAX_PLOTS_501())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P501.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (plot.getWorldName().equals(PlotType.P1001.getWorldName())){
                    if (user.getPlots1001(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_1001() + user.getStats().getMAX_PLOTS_1001() : Rank.VISITOR.getMAX_PLOTS_1001())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
                        canCreate = false;
                        break;
                    }
                }
            }
        }
        
        if (canCreate){
            if (plot.getWorldName().equals(PlotType.P31.getWorldName())){
                user.addPlot((new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P31, plot.getId().toString(), version)));
            } else if (plot.getWorldName().equals(PlotType.P101.getWorldName())){
                user.addPlot((new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P101, plot.getId().toString(), version)));
            } else if (plot.getWorldName().equals(PlotType.P501.getWorldName())){
                user.addPlot(new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P501, plot.getId().toString(), version));
            } else if (plot.getWorldName().equals(PlotType.P1001.getWorldName())){
                user.addPlot(new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P1001, plot.getId().toString(), version));
            } else {
                e.setEventResult(Result.DENY);
                return;
            }
            vs.getBbbApi().getPlayers().savePlayer(user);
        } else {
            e.setEventResult(Result.DENY);
        }
        
        
    }
    
    @Subscribe
    public void onPlayerEnterPlot(PlotDeleteEvent e){
        final Plot plot = e.getPlot();
        final @NonNull HashSet < UUID > members = plot.getMembers();
        members.add(e.getPlot().getOwner());
        for ( UUID p : members ){
            final User user = (User) vs.getBbbApi().getPlayers().getPlayer(p);
            if (user == null){
                return;
            }
            if (plot.getWorldName().equals(PlotType.P31.getWorldName())){
                user.removePlot(plot.getId().toString(), PlotType.P31);
            } else if (plot.getWorldName().equals(PlotType.P101.getWorldName())){
                user.removePlot(plot.getId().toString(), PlotType.P101);
            } else if (plot.getWorldName().equals(PlotType.P501.getWorldName())){
                user.removePlot(plot.getId().toString(), PlotType.P501);
            } else if (plot.getWorldName().equals(PlotType.P1001.getWorldName())){
                user.removePlot(plot.getId().toString(), PlotType.P1001);
            }
            
            vs.getBbbApi().getPlayers().savePlayer(user);
        }
    }
    
    @Subscribe
    public void onPlayerAutoPlotEvent(PlayerAutoPlotEvent e){
        
        final Player p = Bukkit.getPlayer(e.getPlayer().getUUID());
        final String worldName = e.getPlayer().getLocation().getWorldName();
        final User user = (User) vs.getBbbApi().getPlayers().getPlayer(p.getUniqueId());
        if (user == null){
            e.setEventResult(Result.DENY);
            LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
            return;
        }
        boolean canCreate = true;
        boolean changed = user.getOption("changed-plots");
        switch(user.getRank()){
            case ADMIN:
            case DEV:
                break;
            case BUILDER:{
                if (worldName.equals(PlotType.P31.getWorldName())){
                    if (user.getPlots31(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_31() + user.getStats().getMAX_PLOTS_31() : Rank.BUILDER.getMAX_PLOTS_31())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (worldName.equals(PlotType.P101.getWorldName())){
                    if (user.getPlots101(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_101() + user.getStats().getMAX_PLOTS_101() : Rank.BUILDER.getMAX_PLOTS_101())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P101.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (worldName.equals(PlotType.P501.getWorldName())){
                    if (user.getPlots501(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_501() + user.getStats().getMAX_PLOTS_501() : Rank.BUILDER.getMAX_PLOTS_501())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P501.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (worldName.equals(PlotType.P1001.getWorldName())){
                    if (user.getPlots1001(version).size() >= (changed ? Rank.BUILDER.getMAX_PLOTS_1001() + user.getStats().getMAX_PLOTS_1001() : Rank.BUILDER.getMAX_PLOTS_1001())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P1001.getFormattedName());
                        canCreate = false;
                        break;
                    }
                }
                break;
            }
            case VISITOR:{
                if (worldName.equals(PlotType.P31.getWorldName())){
                    if (user.getPlots31(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_31() + user.getStats().getMAX_PLOTS_31() : Rank.VISITOR.getMAX_PLOTS_31())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (worldName.equals(PlotType.P101.getWorldName())){
                    if (user.getPlots101(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_101() + user.getStats().getMAX_PLOTS_101() : Rank.VISITOR.getMAX_PLOTS_101())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P101.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (worldName.equals(PlotType.P501.getWorldName())){
                    if (user.getPlots501(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_501() + user.getStats().getMAX_PLOTS_501() : Rank.VISITOR.getMAX_PLOTS_501())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P501.getFormattedName());
                        canCreate = false;
                        break;
                    }
                } else if (worldName.equals(PlotType.P1001.getWorldName())){
                    if (user.getPlots1001(version).size() >= (changed ? Rank.VISITOR.getMAX_PLOTS_1001() + user.getStats().getMAX_PLOTS_1001() : Rank.VISITOR.getMAX_PLOTS_1001())){
                        e.setEventResult(Result.DENY);
                        LyApi.getLanguage().sendErrorMsg(p, "plot.create.failed", "plot-type", PlotType.P31.getFormattedName());
                        canCreate = false;
                        break;
                    }
                }
            }
        }
        
        if (!canCreate){
            e.setEventResult(Result.DENY);
        }
        
        
    }
    
    @Subscribe
    public void onPlayerAutoPlotsChosenEvent(PlayerAutoPlotsChosenEvent e){
        
        final Player p = Bukkit.getPlayer(e.getPlotPlayer().getUUID());
        final Plot plot = e.getPlot();
        final String worldName = e.getPlotPlayer().getLocation().getWorldName();
        final User user = (User) vs.getBbbApi().getPlayers().getPlayer(p.getUniqueId());
        if (user == null || plot == null){
            return;
        }
        
        if (plot.getWorldName().equals(PlotType.P31.getWorldName())){
            user.addPlot((new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P31, plot.getId().toString(), version)));
        } else if (plot.getWorldName().equals(PlotType.P101.getWorldName())){
            user.addPlot((new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P101, plot.getId().toString(), version)));
        } else if (plot.getWorldName().equals(PlotType.P501.getWorldName())){
            user.addPlot(new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P501, plot.getId().toString(), version));
        } else if (plot.getWorldName().equals(PlotType.P1001.getWorldName())){
            user.addPlot(new net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot(PlotType.P1001, plot.getId().toString(), version));
        } else {
            return;
        }
        vs.getBbbApi().getPlayers().savePlayer(user);
        
        
    }
    
    @Subscribe
    public void onPlayerTeleport(PlayerTeleportToPlotEvent e){
        final UUID playerUUID = e.getPlotPlayer().getUUID();
        final Plot plot = e.getPlot();
        final User user = (User) vs.getBbbApi().getPlayers().getPlayer(playerUUID);
        final Location loc = plot.getCenterSynchronous();
        final Loc location = new Loc(vs.getBbbApi().getProxyServerName(), plot.getWorldName(), loc.getX(), loc.getY(), loc.getZ(), plot.getId().toString());
        user.setLastLocation(location);
        vs.getBbbApi().getPlayers().savePlayer(user);
    }
    
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e){
        final Player player = e.getPlayer();
        final World world = player.getWorld();
        final UUID playerUUID = player.getUniqueId();
        
        final User user = (User) vs.getBbbApi().getPlayers().getPlayer(playerUUID);
        if (world.getName().equals(PlotType.P1001.getWorldName())){
            if (user.getRank().equals(Rank.VISITOR)){
                vs.getBbbApi().getSocket().sendJoinServer(playerUUID, "lobby");
                vs.getBbbApi().getSocket().sendMSGToPlayer(playerUUID, "error.plot.not-allowed-to-join-1001");
            }
        }
        
    }
    
}