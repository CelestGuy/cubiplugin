package net.ddns.lagarderie.racingplugin.game;

import java.util.ArrayList;

import static net.ddns.lagarderie.racingplugin.utils.RacingGameUtils.loadPowerBlocks;

public class PowerBlocksManager {
    private static PowerBlocksManager instance = null;
    private final ArrayList<PowerBlock> powerBlocks;

    private PowerBlocksManager() {
        powerBlocks = loadPowerBlocks();
    }

    public static PowerBlocksManager getInstance() {
        if (instance == null) {
            instance = new PowerBlocksManager();
        }

        return instance;
    }

    public boolean exists(PowerBlock powerBlock) {
        for (PowerBlock p : powerBlocks) {
            if (powerBlock.getId() == p.getId() || powerBlock == p) {
                return true;
            }
        }

        return false;
    }

    public boolean addPowerBlock(PowerBlock powerBlock) {
        if (!exists(powerBlock)) {
            powerBlocks.add(powerBlock);
            return true;
        }

        return false;
    }

    public boolean removePowerBlock(int id) {
        for (PowerBlock p : powerBlocks) {
            if (id == p.getId()) {
                powerBlocks.remove(id);
                return true;
            }
        }

        return false;
    }

    public ArrayList<PowerBlock> getPowerBlocks() {
        return powerBlocks;
    }

    public int getMaxId() {
        int maxId = -1;

        for (PowerBlock p : powerBlocks) {
            maxId = Math.max(p.getId(), maxId);
        }

        return maxId;
    }
}
