package net.ddns.lagarderie.racingplugin.game;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class PowerBlock {
    private final int id;
    private final Material blockMaterial;
    private final String potionEffectId;
    private final int duration;
    private final int defaultAmplifier;

    private final boolean speedAdaptative;


    public PowerBlock(int id, Material blockMaterial, String potionEffectId, int duration, int defaultAmplifier, boolean speedAdaptative) {
        this.id = id;
        this.blockMaterial = blockMaterial;
        this.potionEffectId = potionEffectId;
        this.duration = duration;
        this.defaultAmplifier = defaultAmplifier;
        this.speedAdaptative = speedAdaptative;
    }

    @Override
    public String toString() {
        return "PowerBlock " + blockMaterial.toString().toLowerCase() + " - " + potionEffectId.toLowerCase() + " - " + (1 / 20) + "s, " + defaultAmplifier + ", speedAdaptative: " + speedAdaptative;
    }

    public int getId() {
        return id;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public PotionEffect getEffect(int speed) {
        PotionEffectType potionEffectType = PotionEffectType.getByName(potionEffectId);

        if (potionEffectType != null) {
            if (speedAdaptative) {
                int amplifier = Math.min(speed + defaultAmplifier, 255);
                return new PotionEffect(potionEffectType, duration, amplifier, true);
            }
            return new PotionEffect(potionEffectType, duration, defaultAmplifier, true);
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof PowerBlock that) {
            return Objects.equals(that.potionEffectId, potionEffectId) && blockMaterial == that.blockMaterial;
        }

        return false;
    }
}
