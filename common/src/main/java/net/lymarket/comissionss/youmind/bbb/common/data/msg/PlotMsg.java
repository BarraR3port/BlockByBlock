package net.lymarket.comissionss.youmind.bbb.common.data.msg;

import net.lymarket.comissionss.youmind.bbb.common.data.plot.Plot;

import java.util.UUID;

public class PlotMsg extends Msg {
    
    private final Plot plot;
    
    public PlotMsg( UUID owner , String msg , String version , Plot plot ){
        super( owner , msg , version );
        this.plot = plot;
    }
    
    public Plot getPlot( ){
        return plot;
    }
    
}
