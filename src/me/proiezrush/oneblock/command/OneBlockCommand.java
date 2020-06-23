package me.proiezrush.oneblock.command;

import me.proiezrush.oneblock.Main;
import me.proiezrush.oneblock.oneblock.OneBlock;
import me.proiezrush.oneblock.oneblock.OnePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class OneBlockCommand implements CommandExecutor {

    private Main m;
    public OneBlockCommand(Main m) {
        this.m = m;
    }
    private Player j;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("oneblock")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                this.j = p;
                if (p.hasPermission("oneblock.command")) {
                    if (args.length == 1) {
                        String a = args[0];
                        if (a.equalsIgnoreCase("add")) {
                            Block b = p.getTargetBlock((Set<Material>) null, 5);
                            Location loc = b.getLocation();
                            if (loc != null) {
                                OnePlayer onePlayer = m.getPlayerByName(p.getName());
                                if (m.getOneBlockByLocation(loc) != null) {
                                    p.sendMessage(m.getC().get(p, "oneblock-exist"));
                                    return true;
                                }
                                if (onePlayer.getOneBlocks() == onePlayer.getMaxOneBlocks()) {
                                    p.sendMessage(m.getC().get(p, "oneblock-limit"));
                                    return true;
                                }
                                OneBlock oneBlock = new OneBlock(m.getOneBlockList().size(), loc);
                                m.addOneBlock(oneBlock);
                                onePlayer.addBlock(oneBlock);
                                onePlayer.addOneBlock();
                                p.sendMessage(m.getC().get(p, "oneblock-setted"));
                            }
                        }
                        else if (a.equalsIgnoreCase("remove")) {
                            Block b = p.getTargetBlock((Set<Material>) null, 5);
                            Location loc = b.getLocation();
                            if (loc != null) {
                                OneBlock oneBlock = m.getOneBlockByLocation(loc);
                                OnePlayer onePlayer = m.getPlayerByName(p.getName());
                                if (oneBlock == null) {
                                    p.sendMessage(m.getC().get(p, "oneblock-not-exist"));
                                    return true;
                                }
                                if (!onePlayer.getBlocks().contains(oneBlock)) {
                                    p.sendMessage(m.getC().get(p, "oneblock-not-owner"));
                                    return true;
                                }
                                onePlayer.removeBlock(oneBlock);
                                onePlayer.removeOneBlock();
                                m.removeOneBlock(oneBlock);
                                p.sendMessage(m.getC().get(p, "oneblock-removed"));
                            }
                        }
                        else {
                            h();
                        }
                    }
                    else {
                        h();
                    }
                }
                else {
                    n();
                }
            }
        }
        return false;
    }

    private void n() {
        j.sendMessage(m.getC().get(j, "no-permission"));
    }

    private void h() {
        List<String> commandHelp = m.getC().getList("command-help");
        for (String s : commandHelp) {
            j.sendMessage(m.getAdm().parsePlaceholders(j, s));
        }
    }
}
