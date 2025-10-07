package com.example.strenghtSMP;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

public class PlayerDataManager {
    private final JavaPlugin plugin;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int getKills(Player p) {
        PersistentDataContainer c = p.getPersistentDataContainer();
        Integer v = c.get(StrengthSMP.getInstance().killsKey, PersistentDataType.INTEGER);
        return v == null ? 0 : v;
    }

    public void setKills(Player p, int kills) {
        p.getPersistentDataContainer().set(StrengthSMP.getInstance().killsKey, PersistentDataType.INTEGER, kills);
        p.getPersistentDataContainer().set(StrengthSMP.getInstance().strengthKey, PersistentDataType.INTEGER, calcStrengthLevel(kills));
    }

    public int getLives(Player p) {
        PersistentDataContainer c = p.getPersistentDataContainer();
        Integer v = c.get(StrengthSMP.getInstance().livesKey, PersistentDataType.INTEGER);
        if (v == null) {
            int defaultLives = plugin.getConfig().getInt("default-lives", 3);
            setLives(p, defaultLives);
            return defaultLives;
        }
        return v;
    }

    public void setLives(Player p, int lives) {
        p.getPersistentDataContainer().set(StrengthSMP.getInstance().livesKey, PersistentDataType.INTEGER, lives);
    }

    public int getStrengthLevelFromKills(Player p) {
        int kills = getKills(p);
        return calcStrengthLevel(kills);
    }

    private int calcStrengthLevel(int kills) {
        int amplifier = kills;
        int cap = plugin.getConfig().getInt("max-strength-amplifier", 3);
        if (amplifier > cap) amplifier = cap;
        return amplifier;
    }

    public void addKill(Player p) {
        int kills = getKills(p) + 1;
        setKills(p, kills);
        applyEffectsIfAlive(p);
    }

    public void applyEffectsIfAlive(Player p) {
        int lives = getLives(p);
        if (lives <= 0) {
            removeAllBuffs(p);
            boolean spectatorOnZero = plugin.getConfig().getBoolean("spectator-on-zero-lives", true);
            if (spectatorOnZero) {
                p.setGameMode(GameMode.SPECTATOR);
            }
            return;
        }

        int kills = getKills(p);
        if (kills <= 0) {
            removeAllBuffs(p);
            return;
        }

        int amplifier = getStrengthLevelFromKills(p);
        int potionAmp = Math.max(0, amplifier - 1);
        int durationTicks = Integer.MAX_VALUE;

        // ✅ Updated for modern Bukkit API
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, durationTicks, potionAmp, false, false, true));
    }

    public void removeAllBuffs(Player p) {
        // ✅ Updated for modern Bukkit API
        if (p.hasPotionEffect(PotionEffectType.STRENGTH))
            p.removePotionEffect(PotionEffectType.STRENGTH);
    }

    public void handleDeath(Player p) {
        int lives = getLives(p) - 1;
        setLives(p, lives);

        if (lives <= 0) {
            removeAllBuffs(p);
            boolean ban = plugin.getConfig().getBoolean("ban-on-zero-lives", false);
            if (ban) {
                try {
                    Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(
                            p.getName(),
                            "You ran out of lives on the Strength SMP.",
                            null,
                            "StrengthSMP"
                    );
                    p.kickPlayer("You ran out of lives and have been banned.");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to ban player on zero lives", e);
                }
            } else {
                boolean spectator = plugin.getConfig().getBoolean("spectator-on-zero-lives", true);
                if (spectator) {
                    p.setGameMode(GameMode.SPECTATOR);
                }
            }
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> applyEffectsIfAlive(p), 2L);
        }
    }

    public void saveAll() {
        // No external file saving needed; data is persistent automatically.
    }
}
