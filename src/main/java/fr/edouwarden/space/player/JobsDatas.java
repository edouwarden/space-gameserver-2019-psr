package fr.edouwarden.space.player;

import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.jobs.Jobs;

import java.io.Serializable;
import java.time.LocalDate;


public class JobsDatas implements Serializable {
    private LocalDate switchDate;
    private Jobs job ;
    private Byte woodcutter;
    private Byte cook;
    private Byte merchant;
    private Byte minners;
    private Byte patrol;
    private Byte blacksmith;
    private Byte farmer;
    private Station station;

    public JobsDatas(){
        switchDate = LocalDate.now().plusDays(10);
        job = null;
        woodcutter =0;
        cook = 0;
        merchant = 0;
        minners = 0;
        patrol=0;
        blacksmith =0;
        farmer=0;
    }


    public LocalDate getJobSwitchDate() {
        return switchDate;
    }
    public Jobs getJob() {
        return job;
    }
    public boolean haveJob() {return job!=null;}
    public Byte getWoodcutterLevel() {return woodcutter;}
    public Byte getCookLevel() {return cook;}
    public Byte getMerchantLevel() {
        return merchant;
    }
    public Byte getMinersLevel() {
        return minners;
    }
    public Byte getPatrolLevel() {
        return patrol;
    }
    public Byte getBlacksmithsLevel() {
        return blacksmith;
    }
    public Byte getFarmerLevel() {
        return farmer;
    }

    public boolean setNewJob(Jobs job, boolean forced){
        if(forced){
            this.job = job;
            return true;
        } else if (LocalDate.now().isAfter(switchDate)){
            this.job = job;
            return true;
        }
        return false;
    }
    public void incrementcookLevel(){
        this.cook++;
    }
    public void incrementMerchantLevel(){
        this.merchant++;
    }
    public void incrementCookLevel(){
        this.cook++;
    }
    public void incrementMinersLevel(){
        this.minners++;
    }
    public void incrementWoodcutterLevel(){this.woodcutter++;}
    public void incrementPatrolLevel(){
        this.patrol++;
    }
    public void incrementBlacksmithLevel(){
        this.blacksmith++;
    }
    public void incrementFarmerLevel(){
        this.farmer++;
    }

    public Station getMayorStation() { return this.station;
    }
    public void setMayorStation(Station station) { this.station = station;
    }
}



