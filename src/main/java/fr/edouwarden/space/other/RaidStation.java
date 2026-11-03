package fr.edouwarden.space.other;

import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.variables.General;
import org.bukkit.Bukkit;

public class RaidStation {
    static int taskID;
    static int seconds;
    public static void startLoop(int i){
        seconds = i;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(General.plugin, new Runnable() {
            int duration = 10;
            @Override
            public void run() {
                if (!General.raidEnable||seconds<=0) Bukkit.getScheduler().cancelTask(taskID);
                seconds = seconds-1;

                for(Station station :Station.stations.values()){
                    //if (station.isRaid()){

                    //}
                    //station.townHall
                }

            }
        }, 5, General.seconds);
    }
}
