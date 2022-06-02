package net.lymarket.comissionss.youmind.bbb.common.data.user;

public class Stats {
    
    private long BLOCKS_PLACED;
    
    private long BLOCKS_BROKEN;
    
    private long TIME_PLAYED;
    
    private long ELO;
    
    private int MAX_PLOTS_31;
    
    private int MAX_PLOTS_101;
    
    private int MAX_PLOTS_501;
    
    private int MAX_PLOTS_1001;
    
    private int ADDITIONAL_WORLDS;
    
    public Stats( ){
        BLOCKS_PLACED = 0;
        BLOCKS_BROKEN = 0;
        TIME_PLAYED = 0;
        ELO = 0;
        MAX_PLOTS_31 = 0;
        MAX_PLOTS_101 = 0;
        MAX_PLOTS_501 = 0;
        MAX_PLOTS_1001 = 0;
        ADDITIONAL_WORLDS = 0;
    }
    
    public long getBLOCKS_PLACED( ){
        return BLOCKS_PLACED;
    }
    
    public void addBLOCKS_PLACED(long BLOCKS_PLACED){
        this.BLOCKS_PLACED = this.BLOCKS_PLACED + BLOCKS_PLACED;
    }
    
    public long getBLOCKS_BROKEN( ){
        return BLOCKS_BROKEN;
    }
    
    public void addBLOCKS_BROKEN(long BLOCKS_BROKEN){
        this.BLOCKS_BROKEN = this.BLOCKS_BROKEN + BLOCKS_BROKEN;
    }
    
    public long getTIME_PLAYED( ){
        return TIME_PLAYED;
    }
    
    public void addTIME_PLAYED(long TIME_PLAYED){
        this.TIME_PLAYED = this.TIME_PLAYED + TIME_PLAYED;
    }
    
    public long getELO( ){
        return ELO;
    }
    
    public void addELO(long ELO){
        this.ELO = this.ELO + ELO;
    }
    
    public void removeELO(long ELO){
        final long nextValue = this.ELO - ELO;
        this.ELO = nextValue <= 0 ? 0 : nextValue;
    }
    
    public int getMAX_PLOTS_31( ){
        return MAX_PLOTS_31;
    }
    
    public void addMAX_PLOTS_31(int MAX_PLOTS_31){
        this.MAX_PLOTS_31 = this.MAX_PLOTS_31 + MAX_PLOTS_31;
    }
    
    public void removeMAX_PLOTS_31(int MAX_PLOTS_31){
        final int nextValue = this.MAX_PLOTS_31 - MAX_PLOTS_31;
        this.MAX_PLOTS_31 = nextValue <= 0 ? 0 : nextValue;
    }
    
    public int getMAX_PLOTS_101( ){
        return MAX_PLOTS_101;
    }
    
    public void addMAX_PLOTS_101(int MAX_PLOTS_101){
        this.MAX_PLOTS_101 = this.MAX_PLOTS_101 + MAX_PLOTS_101;
    }
    
    public void removeMAX_PLOTS_101(int MAX_PLOTS_101){
        final int nextValue = this.MAX_PLOTS_101 - MAX_PLOTS_101;
        this.MAX_PLOTS_101 = nextValue <= 0 ? 0 : nextValue;
    }
    
    public int getMAX_PLOTS_501( ){
        return MAX_PLOTS_501;
    }
    
    public void addMAX_PLOTS_501(int MAX_PLOTS_501){
        this.MAX_PLOTS_501 = this.MAX_PLOTS_501 + MAX_PLOTS_501;
    }
    
    public void removeMAX_PLOTS_501(int MAX_PLOTS_501){
        final int nextValue = this.MAX_PLOTS_501 - MAX_PLOTS_501;
        this.MAX_PLOTS_501 = nextValue <= 0 ? 0 : nextValue;
    }
    
    public int getMAX_PLOTS_1001( ){
        return MAX_PLOTS_1001;
    }
    
    public void addMAX_PLOTS_1001(int MAX_PLOTS_1001){
        this.MAX_PLOTS_1001 = this.MAX_PLOTS_1001 + MAX_PLOTS_1001;
    }
    
    public void removeMAX_PLOTS_1001(int MAX_PLOTS_1001){
        final int nextValue = this.MAX_PLOTS_1001 - MAX_PLOTS_1001;
        this.MAX_PLOTS_1001 = nextValue <= 0 ? 0 : nextValue;
    }
    
    public int getADDITIONAL_WORLDS( ){
        return this.ADDITIONAL_WORLDS;
    }
    
    public void addADDITIONAL_WORLDS(int ADDITIONAL_WORLDS){
        this.ADDITIONAL_WORLDS = this.ADDITIONAL_WORLDS + ADDITIONAL_WORLDS;
    }
    
    public void removeADDITIONAL_WORLDS(int ADDITIONAL_WORLDS){
        final int nextValue = this.ADDITIONAL_WORLDS - ADDITIONAL_WORLDS;
        this.ADDITIONAL_WORLDS = nextValue <= 0 ? 0 : nextValue;
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
