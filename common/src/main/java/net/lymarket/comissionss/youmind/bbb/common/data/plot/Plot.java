package net.lymarket.comissionss.youmind.bbb.common.data.plot;

public class Plot {
    
    private final PlotType type;
    
    private String PlotID;
    
    
    public Plot( PlotType type , String plotID ){
        this.type = type;
        this.PlotID = plotID;
        
    }
    
    public String getPlotID( ){
        return PlotID;
    }
    
    public void setPlotID( String plotID ){
        PlotID = plotID;
    }
    
    public PlotType getType( ){
        return type;
    }
}


