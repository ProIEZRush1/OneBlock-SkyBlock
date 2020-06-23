package me.proiezrush.oneblock.dependencies;

import me.proiezrush.oneblock.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DependsADM {

    private Main m;
    public DependsADM(Main m) {
        this.m = m;
    }

    public String parsePlaceholders(Player p, String s) {
        return ChatColor.translateAlternateColorCodes('&', PAPI.parsePlaceholders(p, s));
    }

}
