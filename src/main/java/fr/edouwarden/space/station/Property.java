package fr.edouwarden.space.station;

import fr.edouwarden.space.data.DataFile;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.resources.WorldGuardAPI;
import fr.edouwarden.space.variables.General;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Property {
    transient Station station;
    public UUID owner;
    private Integer rentDays;
    private Double price;
    private List doors;
    private Inventory inventory;
    private UUID uuid;
    public static HashMap<UUID,Property> propertys;

    //private region
    public Property(Station station, List doors, Double price,Inventory inventory){//TODO genration
        this.station = station;
        this.doors = doors;
        this.price = price;
        this.rentDays = 0;
        this.inventory = inventory;
        this.uuid = UUID.randomUUID();
        this.owner = null;
        YamlConfiguration config = General.propertysConfig;
        config.set(uuid.toString()+".station",station.letter);
        config.set(uuid.toString()+".owner",owner);
        config.set(uuid.toString()+".price",price);
        config.set(uuid.toString()+".rentDays",rentDays);
        config.set(uuid.toString()+".doors",doors);
        DataFile.saveConfig(config,General.propertysFile);
        propertys.put(uuid,this);//TODO for each at startup

        return;
    }

    public static Property getFromLoc(Location location) {
        String region= WorldGuardAPI.getRegionNameFromLoc("Property",location);
        if(region==null)return null;
        return getFromUUID(region.replace("Property.",""));
    }

    private static Property getFromUUID(String regionUUID) {
        return propertys.get(UUID.fromString(regionUUID));
    }


    public boolean pay(PlayerEntity buyer, int rentDays){
        if ( buyer.pay(price) && General.maxRentDays >= rentDays){//TODO has property ?, pay before check is empty (split canPay and pay)
            if(owner ==null){
                this.owner = buyer.bukkitPL.getUniqueId();
                buyer.addPropertys(this);
                this.rentDays = rentDays;
            }/*else if(owner.getBukkitPL().getUniqueid()== buyer.getBukkitPL().getUniqueid()){
    			this.rentDays += rentDays;
    		}*/else return false;
            return true;
        }
        return false;
    }
    public static boolean canUse(Block block, PlayerEntity player){
        UUID uuid = WorldGuardAPI.uuidOfProperty(block.getLocation());
        if(uuid !=null && player.getPropertys().contains(uuid))
            return true;
        return false;
    }
    public Inventory getInvetory(){
        return this.inventory;
    }

}
