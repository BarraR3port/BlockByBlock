package net.lymarket.comissionss.youmind.bbb.common.data.user;

import net.lymarket.comissionss.youmind.bbb.common.data.home.Home;
import net.lymarket.comissionss.youmind.bbb.common.data.loc.Loc;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot;
import net.lymarket.comissionss.youmind.bbb.common.data.plot.PlotType;
import net.lymarket.comissionss.youmind.bbb.common.data.rank.Rank;
import net.lymarket.comissionss.youmind.bbb.common.data.warp.Warp;
import net.lymarket.comissionss.youmind.bbb.common.skin.SkinManager;
import net.lymarket.common.Api;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.*;
import java.util.stream.Collectors;

public abstract class User {
    private final Date createDate = new Date();
    private final HashMap < String, Boolean > options = new HashMap <>();
    private final HashMap < String, String > properties = new HashMap <>();
    private final ArrayList < Plot > plots = new ArrayList <>();
    private final ArrayList < Warp > warps = new ArrayList <>();
    private final ArrayList < Home > homes = new ArrayList <>();
    
    private Rank rank;
    private UUID uuid;
    private Stats stats;
    private String name;
    private String address;
    private String skin;
    private Loc lastLocation;
    
    @TestOnly
    public User( ){
    }
    
    public User(String name, UUID uuid){
        this.name = name;
        this.uuid = uuid;
        stats = new Stats();
        skin = SkinManager.getSkin(name);
        rank = Rank.VISITOR;
    }
    
    
    public String getName( ){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public Date getCreateDate( ){
        return createDate;
    }
    
    public @NotNull UUID getUUID( ){
        return uuid;
    }
    
    public void setUUID(UUID uuid){
        this.uuid = uuid;
    }
    
    @Override
    public String toString( ){
        return Api.getGson().toJson(this);
    }
    
    public boolean getOption(String key){
        return options.getOrDefault(key, false);
    }
    
    public void removeOption(String key){
        options.remove(key);
    }
    
    public void setOption(String key, boolean value){
        options.put(key, value);
    }
    
    public String getProperty(String key){
        return properties.get(key);
    }
    
    public void removeProperty(String key){
        properties.remove(key);
    }
    
    public void addProperty(String key, String value){
        properties.put(key, value);
    }
    
    public String serialize( ){
        return Api.getGson().toJson(this);
    }
    
    public User deserialize(String s){
        return Api.getGson().fromJson(s, User.class);
    }
    
    public HashMap < String, String > getProperties( ){
        return properties;
    }
    
    public Date getDateCreate( ){
        return this.createDate;
    }
    
    public ArrayList < Plot > getPlots( ){
        return plots;
    }
    
    public ArrayList < Plot > getPlots31(String version){
        return this.plots.stream().filter(plot -> plot.getType().equals(PlotType.P31) && plot.getVersion().equals(version)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList < Plot > getPlots101(String version){
        return this.plots.stream().filter(plot -> plot.getType().equals(PlotType.P101) && plot.getVersion().equals(version)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList < Plot > getPlots501(String version){
        return this.plots.stream().filter(plot -> plot.getType().equals(PlotType.P501) && plot.getVersion().equals(version)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList < Plot > getPlots1001(String version){
        return this.plots.stream().filter(plot -> plot.getType().equals(PlotType.P1001) && plot.getVersion().equals(version)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public void addPlot(Plot plot){
        plots.add(plot);
    }
    
    public void removePlot(String plotId, PlotType plotType){
        plots.removeIf(plot -> Objects.equals(plot.getPlotID(), plotId) && Objects.equals(plot.getType(), plotType));
    }
    
    public Plot getPlot(String uuid){
        for ( Plot plot : plots ){
            if (Objects.equals(plot.getPlotID(), uuid)){
                return plot;
            }
        }
        return null;
    }
    
    public String getAddress( ){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public ArrayList < Warp > getWarps( ){
        return warps;
    }
    
    public void addWarp(Warp warp){
        warps.add(warp);
    }
    
    public void removeWarp(Warp warp){
        warps.remove(warp);
    }
    
    public void removeWarp(UUID warp){
        warps.removeIf(warp1 -> warp1.getUUID().equals(warp));
    }
    
    public ArrayList < Home > getHomes( ){
        return homes;
    }
    
    public void addHome(Home home){
        homes.add(home);
    }
    
    public void removeHome(Home home){
        homes.remove(home);
    }
    
    public void removeHome(UUID home){
        homes.removeIf(home1 -> home1.getUUID().equals(home));
    }
    
    public Stats getStats( ){
        return stats;
    }
    
    public void setStats(Stats stats){
        this.stats = stats;
    }
    
    public String getSkin( ){
        return skin;
    }
    
    public void updateSkin( ){
        this.skin = SkinManager.getSkin(this.name);
    }
    
    public Rank getRank( ){
        return rank;
    }
    
    public void setRank(Rank rank){
        this.rank = rank;
    }
    
    public Loc getLastLocation( ){
        return this.lastLocation;
    }
    
    public void setLastLocation(Loc loc){
        this.lastLocation = loc;
    }
    
}
