package com.example.strenghtSMP;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class DeathListener implements Listener {
    private final StrengthSMP plugin;
    private final PlayerDataManager dataManager;


    public DeathListener(StrengthSMP plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
// Reduce lives and handle consequences
        dataManager.handleDeath(dead);


        int lives = dataManager.getLives(dead);
        if (lives > 0) {
            dead.getPlayer().sendMessage("§cYou died. Remaining lives: " + lives);
        } else {
            dead.getPlayer().sendMessage("§cYou have no lives left.");
        }
    }
}
