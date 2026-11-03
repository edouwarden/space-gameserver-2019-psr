package fr.edouwarden.space.ship;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import fr.edouwarden.space.station.Station;
import fr.edouwarden.space.player.PlayerEntity;
import fr.edouwarden.space.station.Spot;
import fr.edouwarden.space.resources.WorldEditAPI;
import fr.edouwarden.space.resources.WorldGuardAPI;
import fr.edouwarden.space.sign.ShipSign;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.TChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static fr.edouwarden.space.resources.WorldEditAPI.extremePoint;
import static fr.edouwarden.space.variables.General.item;

public class ShipEntity implements Serializable {
    private final World world;
    private Spot spot;
    //private String name;
    public int id;
    public static ArrayList<ShipEntity> ids = new ArrayList<>();
    private Player owner;
    private int rot;
    private ArrayList<Player> players;
    private Location loc;

    private int type;
    private boolean inSpace;
    private boolean exist;
    private ProtectedCuboidRegion regionWG;
    private Player navigator;

    private float vlocX;
    private float vlocZ;
    private float speed = 1;
    private short vrot;

    public ShipEntity(Spot spot, Integer rotation, Player player,int shiptype) {
        this.loc = spot.getPos();
        this.world = spot.getPos().getWorld();
        this.rot = rotation;
        this.vrot = normalizeHeading(rotation);
        this.owner = player;
        this.type = shiptype;
        this.exist = false;
        this.spot = spot;
        ids.add(this);
        this.id = ids.indexOf(this);//TODO change
        Station.removeSpot(spot);
        spawn();

    }

    public static void clearAll() {
        for(ShipEntity ship : ids){
            ship.remove();
        }

    }

    public void move(Spot newSpot, Integer rotation){
        Station.addSpot(this.spot,spot.getStation());
        remove();
        //WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(this.loc.getWorld()), -1).undo(editSession);
        this.loc = newSpot.getPos();
        this.spot = newSpot;
        this.rot = rotation;
        this.vrot = normalizeHeading(rotation);
        spawn();
    }
    public void spawn(){
        Location spawnloc = this.loc;
        int rotation = this.rot;
        Player player = this.owner;
        int shiptype = this.type;

        Clipboard clipboard = null;
        try {
            clipboard = WorldEditAPI.paste(General.ship(shiptype), rotation, spawnloc);
        } catch (IOException | MaxChangedBlocksException e) {
            General.console.log(java.util.logging.Level.SEVERE, "Cannot remove ship schematic", e);
        }
        if(clipboard==null) return;
        //clipboard.undo(clipboard);

        //this.origin = vecSpawnLoc.add(0,0,1);


        BlockVector3 origin= clipboard.getOrigin().add(0,0,-1);

        //WorldGuard
        BlockVector3 min = origin.subtract(clipboard.getMinimumPoint());
        BlockVector3 max = origin.subtract(clipboard.getMaximumPoint());
        if (inSpace){
            min = origin.add(-min.getBlockX(), -min.getBlockY(), min.getBlockZ());
            max =origin.add(-max.getBlockX(), -max.getBlockY(), max.getBlockZ());
        }
        min = extremePoint(rotation, min,spawnloc.toVector());
        max  =extremePoint(rotation, max,spawnloc.toVector());
        BlockVector minVector = new BlockVector(min.getX(),min.getY(),min.getZ());
        BlockVector maxVector = new BlockVector(max.getX(),max.getY(),max.getZ());
        this.regionWG = WorldGuardAPI.addRegion(player,minVector,maxVector,player.getWorld());
        //this.regionWG.setFlag(WorldGuard.getInstance()..GREET_MESSAGE, "HI there");

        this.exist = true;
    }


    public void map(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "Maps");
        for (int i = 0; i < 53; i++) {
            inv.setItem(i,item(Material.BLUE_STAINED_GLASS_PANE, "Space"));
        }
        player.openInventory(inv);
        new BukkitRunnable() {

            YamlConfiguration frontierMap = General.frontierMap;
            boolean water =false;
            int item = 0;
            char[] line;
            @Override
            public void run() {
                for (int i = Math.round(vlocZ)-2; i < Math.round(vlocZ)+3; i++) {
                    if(!frontierMap.contains(String.valueOf(i))) water=true;
                    else line=frontierMap.getString(String.valueOf(i)).toCharArray();
                    for (int j = Math.round(vlocX)-4; j < Math.round(vlocX)+4; j++) {
                        if(water || line == null || j < 0 || j >= line.length)
                            inv.setItem(item,item(Material.BLUE_STAINED_GLASS_PANE,"Space"));
                        else{
                            char c = line[j];
                            if (c=='X')inv.setItem(item,item(Material.BLUE_STAINED_GLASS_PANE,"Space"));
                            else inv.setItem(item,item(Material.GRASS_BLOCK,"Space"));

                        }
                        item++;

                    }
                    water =false;
                }
                item=0;
                inv.setItem(27,item(Material.OAK_BOAT, "Ship"));
                if(player.getOpenInventory()==null){
                    this.cancel();
                    return;
                }
                player.updateInventory();

            }
        }.runTaskTimerAsynchronously(General.plugin,5, (long) (General.seconds * 2));


    }
    public void movetoSpace(){

        Location previous = this.loc;
        ArrayList<Player> playersInShip = WorldGuardAPI.playersInRegion(this.loc.getWorld(),"ship_" + this.owner.getUniqueId());
        this.players = playersInShip;
        Vector vec = spawnLoc(General.spaceWorld);
        if (vec==null){
            navigator.sendMessage(TChat.spaceFull());
            return;
        }

        General.spaceWorld.getBlockAt(vec.getBlockX(),81,vec.getBlockZ()).setType(Material.GOLD_BLOCK);



        // Spawn ship in space
        this.loc = new Location(General.spaceBukkitWorld,vec.getBlockX(),vec.getBlockY(),vec.getBlockZ());
        this.exist = false; // avoid remove previous one
        this.inSpace = true;
        spawn();
        // TP all the crew
        for (Player p : playersInShip) {
            PlayerEntity.getPlayerEntity(p).setInSpace(true);
            p.teleport(General.spaceWorld.getBlockAt(vec.getBlockX() - 5, 51, vec.getBlockZ()).getLocation());
        }
        // remove the previous ship
        remove(previous,false); // remove previous one

         char station = spot.getStation();
        YamlConfiguration frontierMap = General.frontierMap;
        int i;
        for ( i = 0; i < 99; i++) {
            if(frontierMap.getString(String.valueOf(i)).contains(String.valueOf(station))){
                break;
            }
        }
        this.vlocZ = i;
        this.vlocX = frontierMap.getString(String.valueOf(i)).indexOf(String.valueOf(station));


        new BukkitRunnable() {

            float tempVlocX = vlocX;
            float tempVlocZ = vlocZ;
            @Override
            public void run() {
                // vrot is the voyage heading in degrees (0 = +Z, 90 = +X);
                // sin/cos expect radians.
                double heading = Math.toRadians(vrot);
                tempVlocX += (speed * Math.sin(heading));
                tempVlocZ += (speed * Math.cos(heading));
                if (tempVlocX < 0) tempVlocX = 0;
                else if (tempVlocX > 99) tempVlocX = 99;
                if (tempVlocZ < 0) tempVlocZ = 0;
                else if (tempVlocZ > 98) tempVlocZ = 98;

                // The voyage map is addressed row = Z, column = X, like the GUI and
                // the station lookup above.
                String row = frontierMap.getString(String.valueOf(Math.round(tempVlocZ)));
                int column = Math.round(tempVlocX);
                char cell = row == null || column < 0 || column >= row.length()
                        ? 'X' : row.charAt(column);

                vlocZ = tempVlocZ;
                vlocX = tempVlocX;

                if (cell != 'X') {
                    final char arrival = cell;
                    // Arrival pastes a schematic, moves the crew and edits regions:
                    // it has to run on the main thread.
                    Bukkit.getScheduler().runTask(General.plugin, () -> endTravel(arrival));
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(General.plugin,5, (long) (General.seconds * 5));

        //regionWG.setFlag(DefaultFlag.EXIT, StateFlag.State.DENY);

    }
    private Boolean compareNearest(Boolean pos, int tested, int old, int mid){
        if(pos==null){
            int opposite = mid - (old-mid);
            if(old>tested && tested>opposite){
                return true;
            }
        }else if(pos && tested>old){
            return true;
        } else if(tested<old) {
            return true;
        }
        return false;
    }
    private void endTravel(char charAt){
        //on station
        Station station = Station.getFromChar(charAt);
        //true = left , null = mid , false = right
        Boolean side=null;
        //true = Top , null = mid , false = bottom
        Boolean high=null;
        if(-67.5<vrot&&vrot<67.5)
            side=true;
        else if(-112.5>vrot||vrot>112.5)
            side=false;
        if(22.5<vrot&&vrot<157.5)
            high=true;
        else if(-157.5<vrot&&vrot<22.5)
            high=false;
        List<Spot> spots =station.spots;
        Spot spot = spots.get(0);
        Location loc;
        int currentX = spot.getPos().getBlockX();
        int currentZ = spot.getPos().getBlockZ();
        Boolean testedX;
        Boolean testedZ;
        int midX = station.mid.getBlockX();
        int midZ = station.mid.getBlockZ();
        for (Spot find:spots){
            loc = find.getPos();
            testedX = compareNearest(side,loc.getBlockX(),currentX,midX);
            testedZ = compareNearest(side,loc.getBlockZ(),currentZ,midZ);
            if(testedX&& testedZ) spot=find;
        }
        this.spot = spot;
        for (Player p : this.players) {
            p.teleport(spot.getPos());//modify to station
        }
        remove();
        spawn();
    }
    public float getVlocX(){return vlocX;}
    public float getVlocZ(){return vlocZ;}
    public void addPlayer(Player player){
        this.players.add(player);
    }
    public void remove() {
        remove(this.loc,inSpace);
    }
    private void remove(Location location,Boolean inSpace) {
        WorldGuardAPI.getRegions(loc).remove(regionWG);
        Location loc = location;
        try {
            WorldEditAPI.paste(General.undoShip(this.type), this.rot, loc);
        } catch (IOException | MaxChangedBlocksException e) {
            General.console.log(java.util.logging.Level.SEVERE, "Cannot remove ship schematic", e);
            return;
        }
        if (inSpace) {
            General.spaceBukkitWorld.getBlockAt(loc.getBlockX(), 81, loc.getBlockZ()).setType(Material.AIR);
            this.inSpace = false;
        } else {
            Station.addSpot(spot,spot.getStation());
            org.bukkit.block.Block sign = General.frontierBukkitWorld.getBlockAt(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
            if (sign.getType() == Material.OAK_SIGN)
                ShipSign.Clear(sign);
        }
        this.exist = false;

    }


    private static Vector spawnLoc(World world){
        int startx = General.startx;
        int startz = General.startz;
        int spacingX = General.spacingX;
        int spacingZ = General.spacingZ;
        int spacingXMax = General.spacingXMax;
        int spacingZMax = General.spacingZMax;

        while (world.getBlockAt(startx,81,startz).getType() == Material.GOLD_BLOCK){
            if (startx>=spacingXMax){
                startx=General.startx;
                if(startz > spacingZMax)
                    startz=startz-spacingZ;
                else return null;
            } else startx=spacingX+startx;
        }
        return new Vector(startx, 50, startz);
    }

    public void setNavigator(Player player) {
        this.navigator = player;
    }
    public Player getNavigator() {
        return this.navigator;
    }


    public static ShipEntity getShip(Integer id){
        return ids.get(id);
    }

    /** Maps a schematic rotation of 0/90/180/270° into [-180, 180). */
    private static short normalizeHeading(int degrees) {
        return (short) (((degrees % 360) + 540) % 360 - 180);
    }

    /**
     * Turns the voyage heading by {@code i} quarter-turns (normally -1 or +1).
     * Headings are degrees in [-180, 180), 0 = +Z and 90 = +X, matching the
     * movement step; the schematic rotation in {@link #rot} is separate.
     */
    public void vRotAddition(int i) {
        int temprot = vrot + i * 90;
        while (temprot >= 180) temprot -= 360;
        while (temprot < -180) temprot += 360;
        this.vrot = (short) temprot;
    }

    public boolean isInSpace() { return inSpace;
    }
}
