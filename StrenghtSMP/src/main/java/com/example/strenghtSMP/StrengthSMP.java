package com.example.strenghtSMP;


import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;


public class StrengthSMP extends JavaPlugin {
    private static StrengthSMP instance;
    private PlayerDataManager dataManager;
    public NamespacedKey killsKey;
    public NamespacedKey livesKey;
    public NamespacedKey strengthKey;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();


        killsKey = new NamespacedKey(this, "ss_kills");
        livesKey = new NamespacedKey(this, "ss_lives");
        strengthKey = new NamespacedKey(this, "ss_strength");


        dataManager = new PlayerDataManager(this);
        getServer().getPluginManager().registerEvents(new KillListener(this, dataManager), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this, dataManager), this);


        getCommand("sslives").setExecutor(new Commands(this, dataManager));


// Apply effects for players already online (e.g., reload)
        Bukkit.getOnlinePlayers().forEach(p -> dataManager.applyEffectsIfAlive(p));


        getLogger().info("StrengthSMP enabled");
    }


    @Override
    public void onDisable() {
        dataManager.saveAll();
        getLogger().info("StrengthSMP disabled");
    }


    public static StrengthSMP getInstance() {
        return instance;
    }
}