package fr.edouwarden.space.station;

import org.bukkit.Location;

public class Spot {
    private Location pos;
    private int rot;
    private short maxTier;
    private short bridge;
    private char station;
    public Spot(Location pos,int rot, short maxTier,short bridge, char station){
        this.pos = pos;
        this.rot = rot;
        this.maxTier = maxTier;
        this.bridge = bridge;
        this.station = station;
        //pos.
    }

    public Location getPos() {
        return pos;
    }

    public char getStation() {
        return station;
    }
}
