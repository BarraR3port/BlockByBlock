/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, LyMarket
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * https://github.com/Lydark-Studio/LyApi/blob/master/LICENSE
 *
 * Contact: contact@lymarket.net
 */

package net.lymarket.comissionss.youmind.bbb.support.version.v1_18_R2;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.support.common.plot.IPlotManager;
import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import net.lymarket.comissionss.youmind.bbb.support.version.v1_18_R2.plot.P2Listener;
import net.lymarket.comissionss.youmind.bbb.support.version.v1_18_R2.plot.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class v1_18_R2 extends VersionSupport {
    
    private IPlotManager < Plot > plotManager;
    
    public v1_18_R2(JavaPlugin plugin){
        super(plugin);
    }
    
    @Override
    public void registerPlotEvents( ){
        PlotAPI api = new PlotAPI();
        plotManager = new PlotManager(plugin, this);
        Bukkit.getServer().getPluginManager().registerEvents(new P2Listener(api, this), plugin);
    }
    
    @Override
    public void registerWorldEvents( ){
    
    }
    
    @Override
    public IPlotManager < Plot > getPlotManager( ){
        return plotManager;
    }
    
    @Override
    public void saveWorlds( ){
    }
}
