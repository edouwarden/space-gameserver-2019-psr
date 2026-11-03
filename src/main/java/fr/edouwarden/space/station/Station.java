package fr.edouwarden.space.station;

import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public enum Station implements Serializable {
    GATEWAY('G',new ArrayList<>(),new ArrayList<>(),null,null),
    SECTOR('S',new ArrayList<>(),new ArrayList<>(),null,null),
    COMMERCE('C',new ArrayList<>(),new ArrayList<>(),null,null),
    FORGE('F',new ArrayList<>(),new ArrayList<>(),null,null),
    ARRAY('A',new ArrayList<>(),new ArrayList<>(),null,null);

    public final List<Spot> spots;
    public final UUID townHall;
    public List<Location> path;
    public final Character letter;
    public static HashMap<Character, Station> stations = new HashMap<>();
    public Location mid;
    public char taxesPercent;//
    public char dutyPercent;//
    public float totalTaxes;//
    public float totalDuty;//
    public UUID owner;//

    public static Station getFromChar(char stationChar){
        return stations.get(stationChar);
    }

    Station(Character letter,List<Spot> spots,List<Location>path,Location mid, UUID townHall) {
        this.letter = letter;
        this.spots = spots;
        this.path = path;
        this.mid = mid;
        this.townHall = townHall;
    }

    public static void saveStation() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(General.stationsFile))) {
            oos.writeObject(stations);
        } catch (IOException e) {
            General.console.log(java.util.logging.Level.SEVERE, "Cannot save stations", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void registerSetup() {
        if (!General.stationsFile.exists() || General.stationsFile.length() == 0L) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(General.stationsFile))) {
            stations = (HashMap<Character, Station>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            General.console.log(java.util.logging.Level.SEVERE, "Cannot load stations", e);
        }
    }

    public void addTaxesFund(float totalRent){
        totalTaxes += totalRent*(taxesPercent/100);
    }
    public void addDutyFund(float totalPayed){
        totalDuty+= totalPayed*(dutyPercent/100);
    }
    public void payOwnerTaxes(){
        PlayerEntity.getPlayerEntity(Bukkit.getPlayer(owner)).pay(totalTaxes);
        totalTaxes =0;
    }
    public void payOwnerDuty(){
        PlayerEntity.getPlayerEntity(Bukkit.getPlayer(owner)).pay(totalDuty);
        totalDuty=0;
    }


    public static Station getFromLoc(Location loc) {
        String stationName = getStationRegionName(loc);
        char character = valueOf(stationName).letter;//To get vanilla station data
        return getFromChar(character);//To get right datas
    }

    @NotNull
    private static String getStationRegionName(Location loc) {
        String region =WorldGuardAPI.getRegionNameFromLoc("station_", loc);
        String regionFormated =region.replace("station_","").toUpperCase();
        return regionFormated;
    }

    public static List<String> setup(Player player, Location location, List<String> data) {
        if(data ==null&& player.isSneaking()) player.sendMessage(TChat.Head()+TChat.Info()+" X:" + location.getBlockX() +" - Z:"+ location.getBlockZ());//TODO change to TChat
        else if(data ==null){
            player.sendMessage(TChat.Head()+TChat.Info()+"Middle set to X:" + location.getBlockX() +" - Z:"+ location.getBlockZ());
            data = new ArrayList<>();
            data.add(location.getBlockX()+":"+location.getBlockZ());
        }else if (data.size()>=1 && player.isSneaking()){
            String[] midStr = data.get(0).split(":");
            Location mid = new Location(location.getWorld(),Double.parseDouble(midStr[0]),0,Double.parseDouble(midStr[1]));
            Station station = Station.valueOf(Station.getStationRegionName(mid));//Station.getFromLoc(mid);
            Station existed = Station.getFromChar(station.letter);
            if(existed!=null)station = existed;
            List<Location> path = new ArrayList<>();
            String[] pathStrs = data.get(1).split("/");
            Location loc =new Location(location.getWorld(),0,0,0);
            for (String pathStr:pathStrs){
                loc.setX(Double.parseDouble(pathStr.split(":")[0]));
                loc.setZ(Double.parseDouble(pathStr.split(":")[1]));
                path.add(loc);
            }
            station.mid = mid;
            station.path = path;
            stations.put(station.letter,station);
            player.sendMessage(TChat.Head()+TChat.Info()+"Station:"+station.name()+" added");
            return null;
        } else {
            if (data.size() ==1)data.add( location.getBlockX()+":"+location.getBlockZ());
            else
            data.set(1, data.get(1)+"/"+location.getBlockX()+":"+location.getBlockZ());
            player.sendMessage(TChat.Head()+TChat.Info()+"Path set to X:" + location.getBlockX() +" - Z:"+ location.getBlockZ());
        }
        return data;
    }


    public static void addSpot(Spot spot,char letter) {
        Station station = stations.get(letter);
        station.spots.add(spot);
        stations.put(letter,station);

    }
    public static Spot getSpot(Location loc,char letter) {
        Station station = stations.get(letter);
        for (Spot spot : station.spots)
            if (spot.getPos().getBlockX() == loc.getBlockX() &&spot.getPos().getBlockZ() == loc.getBlockZ() ) return spot;

        return null;
    }
    public void removeSpotfromLoc(Location loc) {
        for (Spot spot : spots)
            if(spot.getPos()==loc){
                spots.remove(spot);
                break;
            }
    }

    public static void removeSpot(Spot spot) {
        Station station = stations.get(spot.getStation());
        station.spots.remove(spot);
        stations.put(spot.getStation(),station);
    }
}
