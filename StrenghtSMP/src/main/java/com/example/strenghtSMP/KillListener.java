package com.example.strenghtSMP;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


public class KillListener implements Listener {
    private final StrengthSMP plugin;
    private final PlayerDataManager dataManager;


    public KillListener(StrengthSMP plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }


    // We'll listen to PlayerDeathEvent to catch kills
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Player killer = dead.getKiller();
        if (killer == null) return;


// Killer gains a kill -> increase strength
        dataManager.addKill(killer);


        int kills = dataManager.getKills(killer);
        killer.sendMessage("§aYou killed " + dead.getName() + ". Total kills: " + kills + " — Strength increased!");
    }
}