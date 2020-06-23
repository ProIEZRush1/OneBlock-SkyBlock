package me.proiezrush.oneblock.oneblock;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OnePlayer {

    private final Player player;
    private int oneBlocks;
    private int maxOneBlocks;
    private List<OneBlock> blocks;
    public OnePlayer(Player player) {
        this.player = player;
        this.oneBlocks = 0;
        this.maxOneBlocks = 3;
        this.blocks = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public int getOneBlocks() {
        return oneBlocks;
    }

    public void setOneBlocks(int oneBlocks) {
        this.oneBlocks = oneBlocks;
    }

    public void addOneBlock() {
        this.oneBlocks++;
    }

    public void removeOneBlock() {
        this.oneBlocks--;
    }

    public int getMaxOneBlocks() {
        return maxOneBlocks;
    }

    public void setMaxOneBlocks(int maxOneBlocks) {
        this.maxOneBlocks = maxOneBlocks;
    }

    public void addBlock(OneBlock block) {
        if (!blocks.contains(block)) {
            blocks.add(block);
        }
    }

    public void removeBlock(OneBlock block) {
        blocks.remove(block);
    }

    public List<OneBlock> getBlocks() {
        return blocks;
    }
}
