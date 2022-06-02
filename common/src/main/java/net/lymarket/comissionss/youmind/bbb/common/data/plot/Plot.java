package net.lymarket.comissionss.youmind.bbb.common.data.plot;

public class Plot {
    
    private final PlotType type;
    private final String version;
    private String PlotID;
    
    public Plot(PlotType type, String plotID, String version){
        this.type = type;
        this.PlotID = plotID;
        this.version = version;
    
    }
    
    public String getPlotID( ){
        return PlotID;
    }
    
    public void setPlotID(String plotID){
        PlotID = plotID;
    }
    
    public PlotType getType( ){
        return type;
    }
    
    public String getVersion( ){
        return version;
    }
}


