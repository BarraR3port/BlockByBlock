package net.lymarket.comissionss.youmind.bbb.common.data.plot;

public enum PlotType {
    P101( "101x101" ),
    P501( "501x501" ),
    P1001( "1001x1001" );
    
    private final String formattedName;
    
    PlotType( String formattedName ){
        this.formattedName = formattedName;
    }
    
    public String getFormattedName( ){
        return formattedName;
    }
    
}
