package me.proiezrush.oneblock.breaker;

import me.proiezrush.oneblock.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Breaker {

    private static List<String> materials;

    public static void breakBlock(Player p, Block b, Collection<ItemStack> drops, Main m, BlockBreakEvent e) {
        if (p.getLocation().getBlock().equals(b)) {
            p.teleport(p.getLocation().add(new Vector(0D, 1D, 0D)));
        }
        addMaterials();
        checkAutoPickup(p, b, drops, m, e);
        setType(b, m);
    }

    private static void addMaterials() {
        materials = new ArrayList<>();
        for (Material mat : Material.values()) {
            if (mat.isBlock() && mat.isSolid()) {
                materials.add(mat.toString());
            }
        }
    }

    private static void checkAutoPickup(Player p, Block b, Collection<ItemStack> drops, Main m, BlockBreakEvent e) {
        boolean a = m.getC().getBoolean("autopickup");
        if (a && p.hasPermission("oneblock.autopick")) {
            if (drops != null) {
                for (ItemStack i : drops) {
                    if (!invFull(p) || hasItem(p, i)) {
                        p.getInventory().addItem(i);
                    }
                    else {
                        p.getWorld().dropItem(b.getLocation().add(0D, 1D, 0D), i);
                    }
                }
            }
        }
        else {
            for (ItemStack item : b.getDrops()) {
                b.getWorld().dropItem(b.getLocation().add(new Vector(0D,1.5,0D)), item);
            }
        }
    }

    private static void setType(Block b, Main m) {
        int random = m.getRandomNumberInRange(0, materials.size()-1);
        if (isBlacklistEnabled(m)) {
            List<String> blacklistedBlocks = m.getC().getList("blacklisted-blocks");
            if (!blacklistedBlocks.contains(materials.get(random))) {
                if (Material.valueOf(materials.get(random)).isSolid() && Material.valueOf(materials.get(random)).isBlock()) {
                    b.setType(Material.valueOf(materials.get(random)));
                }
                else {
                    b.setType(b.getType());
                }
            }
            else {
                b.setType(b.getType());
            }
        }
        else if (isWhitelistEnabled(m)) {
            List<String> whitelistedBlocks = m.getC().getList("only-this-blocks-will-popup");
            random = m.getRandomNumberInRange(0, whitelistedBlocks.size()-1);
            if (Material.valueOf(whitelistedBlocks.get(random)).isSolid() && Material.valueOf(whitelistedBlocks.get(random)).isBlock()) {
                b.setType(Material.valueOf(whitelistedBlocks.get(random)));
            }
            else {
                b.setType(b.getType());
            }
        }
        else {
            if (Material.valueOf(materials.get(random)).isSolid() && Material.valueOf(materials.get(random)).isBlock()) {
                b.setType(Material.valueOf(materials.get(random)));
            }
            else {
                b.setType(b.getType());
            }
        }
    }

    private static boolean isBlacklistEnabled(Main m) {
        return m.getC().getBoolean("blacklisted-blocks-enabled");
    }

    private static boolean isWhitelistEnabled(Main m) {
        return m.getC().getBoolean("only-this-blocks-will-popup-enabled");
    }

    private static boolean invFull(Player p){
        Inventory inv = p.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasItem(Player p, ItemStack i) {
        Inventory inv = p.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item.getType() == i.getType()) {
                return true;
            }
        }
        return false;
    }
}
