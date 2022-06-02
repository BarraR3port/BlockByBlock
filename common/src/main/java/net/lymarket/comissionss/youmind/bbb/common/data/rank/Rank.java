package net.lymarket.comissionss.youmind.bbb.common.data.rank;

import java.util.Arrays;

public enum Rank {
    ADMIN("admin", "&c&l「Admin⏌", "&c&l「Admin⏌", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 5),
    DEV("dev", "&d&l「Dev⏌", " &d&l「Dev⏌", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 5),
    BUILDER("builder", "&9「B⏌", "&9&l「Builder⏌", 15, 10, 3, 1, 3),
    VISITOR("default", "&7「Visitor⏌", "&7「Visitor⏌", 15, 5, 2, 0, 0);
    
    private final String lpName;
    private final String prefix;
    private final String tabPrefix;
    
    private final int max31Plots;
    
    private final int max101Plots;
    
    private final int max501Plots;
    
    private final int max1001Plots;
    
    private final int maxWorlds;
    
    Rank(String lpName, String prefix, String tabPrefix, int max31Plots, int max101Plots, int max501Plots, int max1001Plots, int maxWorlds){
        this.lpName = lpName;
        this.prefix = prefix;
        this.tabPrefix = tabPrefix;
        this.max31Plots = max31Plots;
        this.max101Plots = max101Plots;
        this.max501Plots = max501Plots;
        this.max1001Plots = max1001Plots;
        this.maxWorlds = maxWorlds;
    }
    
    
    public static Rank fromString(String rank){
        return Arrays.stream(Rank.values()).filter(r -> r.getLpName().equalsIgnoreCase(rank)).findFirst().orElse(null);
    }
    
    public String getLpName( ){
        return this.lpName;
    }
    
    public String getPrefix( ){
        return this.prefix;
    }
    
    public String getTabPrefix( ){
        return this.tabPrefix;
    }
    
    public boolean isBuilder( ){
        return this == BUILDER || this == DEV || this == ADMIN;
    }
    
    public boolean isDev( ){
        return this == DEV || this == ADMIN;
    }
    
    public boolean isAdmin( ){
        return this == ADMIN;
    }
    
    public int getMAX_PLOTS_31( ){
        return max31Plots;
    }
    
    public int getMAX_PLOTS_101( ){
        return max101Plots;
    }
    
    public int getMAX_PLOTS_501( ){
        return max501Plots;
    }
    
    public int getMAX_PLOTS_1001( ){
        return max1001Plots;
    }
    
    public int getMAX_WORLDS( ){
        return maxWorlds;
    }
}
