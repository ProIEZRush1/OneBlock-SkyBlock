package me.proiezrush.oneblock;

import me.proiezrush.oneblock.command.OneBlockCommand;
import me.proiezrush.oneblock.dependencies.DependsADM;
import me.proiezrush.oneblock.listeners.Listeners;
import me.proiezrush.oneblock.oneblock.OneBlock;
import me.proiezrush.oneblock.oneblock.OnePlayer;
import me.proiezrush.oneblock.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class Main extends JavaPlugin {

    private Settings config, blocks;
    private DependsADM adm;
    private HashMap<Location, OneBlock> oneBlockList;
    private HashMap<Player, OnePlayer> onePlayerList;

    @Override
    public void onEnable() {
        File f = new File("plugins/OneBlock");
        if (!f.exists()) {
            f.mkdir();
        }
        this.config = new Settings(this, "config", false, true);
        this.blocks = new Settings(this, "blocks", false ,false);
        this.adm = new DependsADM(this);
        this.oneBlockList = new HashMap<>();
        this.onePlayerList = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        this.getCommand("oneblock").setExecutor(new OneBlockCommand(this));
        lData();
    }

    @Override
    public void onDisable() {
        sData();
    }

    public DependsADM getAdm() {
        return adm;
    }

    public Settings getC() {
        return config;
    }

    public Settings getBlocks() {
        return blocks;
    }

    public void addOneBlock(OneBlock oneBlock) {
        oneBlockList.putIfAbsent(oneBlock.getLocation(), oneBlock);
    }

    public void removeOneBlock(OneBlock oneBlock) {
        oneBlockList.remove(oneBlock.getLocation());
    }

    public HashMap<Location, OneBlock> getOneBlockList() {
        return oneBlockList;
    }

    public OneBlock getOneBlockByLocation(Location location) {
        return oneBlockList.get(location);
    }

    public boolean isBlock(Block block) {
        return oneBlockList.get(block.getLocation()) != null;
    }

    public void addOnePlayer(OnePlayer onePlayer) {
        onePlayerList.putIfAbsent(onePlayer.getPlayer(), onePlayer);
    }

    public void removeOnePlayer(OnePlayer onePlayer) {
        onePlayerList.remove(onePlayer.getPlayer());
    }

    public HashMap<Player, OnePlayer> getOnePlayerList() {
        return onePlayerList;
    }

    public OnePlayer getPlayerByName(String name) {
        return onePlayerList.get(Bukkit.getPlayer(name));
    }

    private void lData() {
        ConfigurationSection a = blocks.getConfig().getConfigurationSection("OneBlocks");
        if (a != null) {
            for (String k : a.getKeys(false)) {
                World w = Bukkit.getWorld(a.getString(k + ".World"));
                double x = a.getDouble(k + ".X");
                double y = a.getDouble(k + ".Y");
                double z = a.getDouble(k + ".Z");
                OneBlock oneBlock = new OneBlock(oneBlockList.size(), new Location(w, x, y, z));
                addOneBlock(oneBlock);
            }
        }

        ConfigurationSection b = blocks.getConfig().getConfigurationSection("OnePlayers");
        if (b != null) {
            for (String k : b.getKeys(false)) {
                ConfigurationSection c = b.getConfigurationSection(k + ".OneBlocks");
                OnePlayer onePlayer = new OnePlayer(Bukkit.getPlayer(k));
                addOnePlayer(onePlayer);
                if (c != null) {
                    for (String j : c.getKeys(false)) {
                        World w = Bukkit.getWorld(c.getString(j + ".World"));
                        double x = c.getDouble(j + ".X");
                        double y = c.getDouble(j + ".Y");
                        double z = c.getDouble(j + ".Z");
                        OneBlock oneBlock = getOneBlockByLocation(new Location(w ,x ,y ,z));
                        onePlayer.addBlock(oneBlock);
                        onePlayer.addOneBlock();
                    }
                }
                int maxOneBlocks = b.getInt(k + ".MaxOneBlocks");
                onePlayer.setMaxOneBlocks(maxOneBlocks);
            }
        }
    }

    private void sData() {
        if (oneBlockList != null && !oneBlockList.isEmpty()) {
            for (OneBlock oneBlock : oneBlockList.values()) {
                blocks.set("OneBlocks." + oneBlock.getID() + ".World", oneBlock.getLocation().getWorld().getName());
                blocks.set("OneBlocks." + oneBlock.getID() + ".X", oneBlock.getLocation().getX());
                blocks.set("OneBlocks." + oneBlock.getID() + ".Y", oneBlock.getLocation().getY());
                blocks.set("OneBlocks." + oneBlock.getID() + ".Z", oneBlock.getLocation().getZ());
            }
            blocks.save();
        }

        if (onePlayerList != null && !onePlayerList.isEmpty()) {
            for (OnePlayer onePlayer : onePlayerList.values()) {
                for (OneBlock oneBlock : onePlayer.getBlocks()) {
                    blocks.set("OnePlayers." + onePlayer.getPlayer().getName() + ".OneBlocks." + oneBlock.getID() + ".World", oneBlock.getLocation().getWorld().getName());
                    blocks.set("OnePlayers." + onePlayer.getPlayer().getName() + ".OneBlocks." + oneBlock.getID() + ".X", oneBlock.getLocation().getX());
                    blocks.set("OnePlayers." + onePlayer.getPlayer().getName() + ".OneBlocks." + oneBlock.getID() + ".Y", oneBlock.getLocation().getY());
                    blocks.set("OnePlayers." + onePlayer.getPlayer().getName() + ".OneBlocks." + oneBlock.getID() + ".Z", oneBlock.getLocation().getZ());
                }
                //blocks.set("OnePlayers." + onePlayer.getPlayer().getName() + ".MaxOneBlocks", onePlayer.getMaxOneBlocks());
            }
            blocks.save();
        }
    }

    public int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
