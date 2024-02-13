package com.corosus.mobtimizations;

public interface MobtimizationEntityFields {
    
    long getlastWanderTime();

    void setlastWanderTime(long mobtimizations_lastWanderTime);

    long getlastPlayerScanTime();

    void setlastPlayerScanTime(long mobtimizations_lastPlayerScanTime);

    boolean isplayerInRange();

    void setplayerInRange(boolean mobtimizations_playerInRange);
    
}
