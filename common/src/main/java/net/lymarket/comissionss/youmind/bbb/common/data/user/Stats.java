package net.lymarket.comissionss.youmind.bbb.common.data.user;

public class Stats {
    
    private long BLOCKS_PLACED;
    
    private long BLOCKS_BROKEN;
    
    private long TIME_PLAYED;
    
    private long ELO;
    
    public Stats( ){
        BLOCKS_PLACED = 0;
        BLOCKS_BROKEN = 0;
        TIME_PLAYED = 0;
        ELO = 0;
        
    }
    
    public long getBLOCKS_PLACED( ){
        return BLOCKS_PLACED;
    }
    
    public void addBLOCKS_PLACED( long BLOCKS_PLACED ){
        this.BLOCKS_PLACED = this.BLOCKS_PLACED + BLOCKS_PLACED;
    }
    
    public long getBLOCKS_BROKEN( ){
        return BLOCKS_BROKEN;
    }
    
    public void addBLOCKS_BROKEN( long BLOCKS_BROKEN ){
        this.BLOCKS_BROKEN = this.BLOCKS_BROKEN + BLOCKS_BROKEN;
    }
    
    public long getTIME_PLAYED( ){
        return TIME_PLAYED;
    }
    
    public void addTIME_PLAYED( long TIME_PLAYED ){
        this.TIME_PLAYED = this.TIME_PLAYED + TIME_PLAYED;
    }
    
    public long getELO( ){
        return ELO;
    }
    
    public void addELO( long ELO ){
        this.ELO = this.ELO + ELO;
    }
    
    public void removeELO( long ELO ){
        this.ELO = this.ELO - ELO;
    }
    
    public String getFormattedTimePlayed( ){
        long difference_In_Seconds = (TIME_PLAYED / 1000) % 60;
        long difference_In_Minutes = (TIME_PLAYED / (1000 * 60)) % 60;
        long difference_In_Hours = (TIME_PLAYED / (1000 * 60 * 60)) % 24;
        long difference_In_Years = (TIME_PLAYED / (1000L * 60 * 60 * 24 * 365));
        long difference_In_Days = (TIME_PLAYED / (1000 * 60 * 60 * 24)) % 365;
        
        return (difference_In_Years < 10 ? "0" : "") + difference_In_Years + ":" +
                (difference_In_Days < 10 ? "0" : "") + difference_In_Days + ":" +
                (difference_In_Hours < 10 ? "0" : "") + difference_In_Hours + ":" +
                (difference_In_Minutes < 10 ? "0" : "") + difference_In_Minutes + ":" +
                (difference_In_Seconds < 10 ? "0" : "") + difference_In_Seconds;
        
    }
}
