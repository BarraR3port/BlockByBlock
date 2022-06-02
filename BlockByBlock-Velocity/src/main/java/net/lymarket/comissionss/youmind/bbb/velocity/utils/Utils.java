package net.lymarket.comissionss.youmind.bbb.velocity.utils;

import net.kyori.adventure.text.Component;

public class Utils {
    
    public static Component format(String s){
        return Component.text(ChatColor.translateAlternateColorCodes('&', s));
    }
    
}
