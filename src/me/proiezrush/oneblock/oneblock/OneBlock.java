package me.proiezrush.oneblock.oneblock;

import org.bukkit.Location;

public class OneBlock {

    private int ID;
    private Location location;

    public OneBlock(int ID, Location location) {
        this.ID = ID;
        this.location = location;
    }

    public int getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
