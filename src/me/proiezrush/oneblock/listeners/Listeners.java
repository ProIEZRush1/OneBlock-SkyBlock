package me.proiezrush.oneblock.listeners;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.events.IslandNewEvent;
import me.proiezrush.oneblock.Cuboid;
import me.proiezrush.oneblock.Main;
import me.proiezrush.oneblock.breaker.Breaker;
import me.proiezrush.oneblock.oneblock.OneBlock;
import me.proiezrush.oneblock.oneblock.OnePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;


public class Listeners implements Listener {

    private Main m;
    public Listeners(Main m) {
        this.m = m;
    }

    @EventHandler
    public void onIsCreate(IslandNewEvent e) {
        Location loc1 = new Location(e.getIslandLocation().getWorld(), e.getIslandLocation().getX() - 10, e.getIslandLocation().getY() - 10, e.getIslandLocation().getZ() - 10);
        Location loc2 = new Location(e.getIslandLocation().getWorld(), e.getIslandLocation().getX() + 10, e.getIslandLocation().getY() + 10, e.getIslandLocation().getZ() + 10);
        Cuboid cuboid = new Cuboid(loc1, loc2);
        for (int i=0;i<cuboid.getBlocks().size();i++) {
            if (cuboid.getBlocks().get(i).getType() == Material.valueOf(m.getC().getConfig().getString("block-to-change"))) {
                OneBlock oneBlock = new OneBlock(m.getOneBlockList().size(), cuboid.getBlocks().get(i).getLocation());
                OnePlayer onePlayer = m.getOnePlayerList().get(e.getPlayer());
                m.addOneBlock(oneBlock);
                onePlayer.addBlock(oneBlock);
                onePlayer.addOneBlock();
            }
        }
    }

    private Collection<ItemStack> x;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (m.isBlock(b)) {
            if (b.getType() == Material.CHEST && p.hasPermission("oneblock.luckychest")) {
                luckyChest(p, m);
            }
            x = b.getDrops();
            Bukkit.getScheduler().scheduleSyncDelayedTask(m, () -> {
                Breaker.breakBlock(p, b, x, m, e);
            }, 1L);
        }
    }

    private static void luckyChest(Player p, Main m) {
        ConfigurationSection a = m.getC().getConfig().getConfigurationSection("luckychest-contents");
        int random = m.getRandomNumberInRange(0, a.getKeys(false).size());
        List<String> luckyChest = m.getC().getList("luckychest-contents." + random);
        for (String s : luckyChest) {
            String[] value = s.split(":");
            String material = value[0];
            int data = Integer.parseInt(value[1]);
            int amount = Integer.parseInt(value[2]);
            ItemStack item = new ItemStack(Material.valueOf(material), amount, (short) data);
            p.getInventory().addItem(item);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!m.getOnePlayerList().containsKey(p)) {
            OnePlayer player = new OnePlayer(p);
            m.addOnePlayer(player);
        }
    }
}
