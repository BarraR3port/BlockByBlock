package net.lymarket.comissionss.youmind.bbb.common.data.plot;

public enum PlotType {
    P31("31x31", "bbb-plot-31"),
    P101("101x101", "bbb-plot-101"),
    P501("501x501", "bbb-plot-501"),
    P1001("1001x1001", "bbb-plot-1001");
    
    private final String formattedName;
    
    private final String worldName;
    
    PlotType(String formattedName, String worldName){
        this.formattedName = formattedName;
        this.worldName = worldName;
    }
    
    public static PlotType getPlotTypeByWorld(String worldName){
        for ( PlotType plotType : values() ){
            if (plotType.getWorldName().equals(worldName)){
                return plotType;
            }
        }
        return null;
    }
    
    public String getFormattedName( ){
        return formattedName;
    }
    
    public String getWorldName( ){
        return worldName;
    }
}
