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

package net.lymarket.comissionss.youmind.bbb.support.version.v1_16_R3;

import net.lymarket.comissionss.youmind.bbb.support.common.version.VersionSupport;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class v1_16_R3 extends VersionSupport {
    
    public v1_16_R3( JavaPlugin plugin ){
        super( plugin );
    }
    
    @Override
    public void registerPlotEvents( ){
        Bukkit.getServer( ).getPluginManager( ).registerEvents( new Events( ) , plugin );
    }
}
