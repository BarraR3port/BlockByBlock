package net.lymarket.comissionss.youmind.bbb.common.data.server;

public final class ProxyStats {
    
    private final int lobby_player_size;
    private final int world_1_12_player_size;
    private final boolean world_1_12_online;
    private final int world_1_16_player_size;
    private final boolean world_1_16_online;
    private final int world_1_18_player_size;
    private final boolean world_1_18_online;
    private final int plot_1_12_player_size;
    private final boolean plot_1_12_online;
    private final int plot_1_16_player_size;
    private final boolean plot_1_16_online;
    private final int plot_1_18_player_size;
    private final boolean plot_1_18_online;
    
    
    public ProxyStats(int lobby_player_size,
                      int world_1_12_player_size,
                      boolean world_1_12_online,
                      int world_1_16_player_size,
                      boolean world_1_16_online,
                      int world_1_18_player_size,
                      boolean world_1_18_online,
                      int plot_1_12_player_size,
                      boolean plot_1_12_online,
                      int plot_1_16_player_size,
                      boolean plot_1_16_online,
                      int plot_1_18_player_size,
                      boolean plot_1_18_online){
        this.lobby_player_size = lobby_player_size;
        this.world_1_12_player_size = world_1_12_player_size;
        this.world_1_12_online = world_1_12_online;
        this.world_1_16_player_size = world_1_16_player_size;
        this.world_1_16_online = world_1_16_online;
        this.world_1_18_player_size = world_1_18_player_size;
        this.world_1_18_online = world_1_18_online;
        this.plot_1_12_player_size = plot_1_12_player_size;
        this.plot_1_12_online = plot_1_12_online;
        this.plot_1_16_player_size = plot_1_16_player_size;
        this.plot_1_16_online = plot_1_16_online;
        this.plot_1_18_player_size = plot_1_18_player_size;
        this.plot_1_18_online = plot_1_18_online;
    }
    
    public ProxyStats( ){
        this.lobby_player_size = 0;
        this.world_1_12_player_size = 0;
        this.world_1_12_online = false;
        this.world_1_16_player_size = 0;
        this.world_1_16_online = false;
        this.world_1_18_player_size = 0;
        this.world_1_18_online = false;
        this.plot_1_12_player_size = 0;
        this.plot_1_12_online = false;
        this.plot_1_16_player_size = 0;
        this.plot_1_16_online = false;
        this.plot_1_18_player_size = 0;
        this.plot_1_18_online = false;
    }
    
    public int getLobby_player_size( ){
        return lobby_player_size;
    }
    
    public int getWorld_1_12_player_size( ){
        return world_1_12_player_size;
    }
    
    public int getWorld_1_16_player_size( ){
        return world_1_16_player_size;
    }
    
    public int getWorld_1_18_player_size( ){
        return world_1_18_player_size;
    }
    
    public int getPlot_1_12_player_size( ){
        return plot_1_12_player_size;
    }
    
    public int getPlot_1_16_player_size( ){
        return plot_1_16_player_size;
    }
    
    public int getPlot_1_18_player_size( ){
        return plot_1_18_player_size;
    }
    
    public int getTotal_player_size( ){
        return lobby_player_size + world_1_12_player_size + world_1_16_player_size + world_1_18_player_size + plot_1_12_player_size + plot_1_16_player_size + plot_1_18_player_size;
    }
    
    public boolean isWorld_1_12_online( ){
        return world_1_12_online;
    }
    
    public boolean isWorld_1_16_online( ){
        return world_1_16_online;
    }
    
    public boolean isWorld_1_18_online( ){
        return world_1_18_online;
    }
    
    public boolean isPlot_1_12_online( ){
        return plot_1_12_online;
    }
    
    public boolean isPlot_1_16_online( ){
        return plot_1_16_online;
    }
    
    public boolean isPlot_1_18_online( ){
        return plot_1_18_online;
    }
}
