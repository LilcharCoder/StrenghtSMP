package com.example.strenghtSMP;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    private final StrengthSMP plugin;
    private final PlayerDataManager dataManager;

    public Commands(StrengthSMP plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eStrengthSMP commands:");
            sender.sendMessage("/sslives <player?> - show your lives or another player's lives");
            sender.sendMessage("/sslives set <player> <num> - set player's lives (admin)");
            sender.sendMessage("/sslives setkills <player> <num> - set kills (admin)");
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("strengthsmp.admin")) {
                sender.sendMessage("§cNo permission");
                return true;
            }
            if (args.length != 3) return false;
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) { sender.sendMessage("Player not found"); return true; }
            try {
                int num = Integer.parseInt(args[2]);
                dataManager.setLives(target, num);
                sender.sendMessage("Set lives of " + target.getName() + " to " + num);
                dataManager.applyEffectsIfAlive(target);
            } catch (NumberFormatException e) { sender.sendMessage("Invalid number"); }
            return true;
        }

        if (args[0].equalsIgnoreCase("setkills")) {
            if (!sender.hasPermission("strengthsmp.admin")) {
                sender.sendMessage("§cNo permission");
                return true;
            }
            if (args.length != 3) return false;
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) { sender.sendMessage("Player not found"); return true; }
            try {
                int num = Integer.parseInt(args[2]);
                dataManager.setKills(target, num);
                sender.sendMessage("Set kills of " + target.getName() + " to " + num);
                dataManager.applyEffectsIfAlive(target);
            } catch (NumberFormatException e) { sender.sendMessage("Invalid number"); }
            return true;
        }

        // show lives
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) { sender.sendMessage("Player not found"); return true; }
            sender.sendMessage(target.getName() + " has " + dataManager.getLives(target) + " lives and " + dataManager.getKills(target) + " kills.");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> res = new ArrayList<>();
        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) res.add(p.getName());
        }
        return res;
    }
}
