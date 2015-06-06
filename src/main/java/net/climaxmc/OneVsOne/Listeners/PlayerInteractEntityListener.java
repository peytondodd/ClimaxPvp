package net.climaxmc.OneVsOne.Listeners;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Kit;
import net.climaxmc.KitPvp.Kits.PvpKit;
import net.climaxmc.OneVsOne.OneVsOne;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

public class PlayerInteractEntityListener implements Listener {
    private ClimaxPvp plugin;
    private OneVsOne instance;

    public PlayerInteractEntityListener(ClimaxPvp plugin, OneVsOne instance) {
        this.plugin = plugin;
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event) {
        Kit kit = new PvpKit();
        Location arena1Spawn1 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Arenas.1.Spawns.1.World")), plugin.getConfig().getInt("Arenas.1.Spawns.1.X"), plugin.getConfig().getInt("Arenas.1.Spawns.1.Y"), plugin.getConfig().getInt("Arenas.1.Spawns.1.Z"));
        Location arena1Spawn2 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Arenas.1.Spawns.2.World")), plugin.getConfig().getInt("Arenas.1.Spawns.2.X"), plugin.getConfig().getInt("Arenas.1.Spawns.2.Y"), plugin.getConfig().getInt("Arenas.1.Spawns.2.Z"));
        Location arena2Spawn1 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Arenas.2.Spawns.1.World")), plugin.getConfig().getInt("Arenas.2.Spawns.1.X"), plugin.getConfig().getInt("Arenas.2.Spawns.1.Y"), plugin.getConfig().getInt("Arenas.2.Spawns.1.Z"));
        Location arena2spawn2 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Arenas.2.Spawns.2.World")), plugin.getConfig().getInt("Arenas.2.Spawns.2.X"), plugin.getConfig().getInt("Arenas.2.Spawns.2.Y"), plugin.getConfig().getInt("Arenas.2.Spawns.2.Z"));
        Location arena3spawn1 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Arenas.3.Spawns.1.World")), plugin.getConfig().getInt("Arenas.3.Spawns.1.X"), plugin.getConfig().getInt("Arenas.3.Spawns.1.Y"), plugin.getConfig().getInt("Arenas.3.Spawns.1.Z"));
        Location arena3spawn2 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Arenas.3.Spawns.2.World")), plugin.getConfig().getInt("Arenas.3.Spawns.2.X"), plugin.getConfig().getInt("Arenas.3.Spawns.2.Y"), plugin.getConfig().getInt("Arenas.3.Spawns.2.Z"));

        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            if (player.getInventory().getItemInHand().getType() == Material.STICK) {
                if (instance.getChallenged().containsKey(player.getUniqueId()) && instance.getChallenged().containsValue(target.getUniqueId())) {
                    int random = new Random().nextInt(3);
                    switch (random) {
                        case 0:
                            player.teleport(arena1Spawn1);
                            target.teleport(arena1Spawn2);
                            player.getInventory().clear();
                            target.getInventory().clear();
                            for (PotionEffect effect : player.getActivePotionEffects()) {
                                player.removePotionEffect(effect.getType());
                                target.removePotionEffect(effect.getType());
                            }
                            kit.wear(player);
                            kit.wear(target);
                            player.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GRAY + "You have entered a regular 1v1 with " + target.getName());
                            target.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GRAY + "You have entered a regular 1v1 with " + player.getName());
                            return;
                        case 1:
                            player.teleport(arena2Spawn1);
                            target.teleport(arena2spawn2);
                            player.getInventory().clear();
                            target.getInventory().clear();
                            for (PotionEffect effect : player.getActivePotionEffects()) {
                                player.removePotionEffect(effect.getType());
                                target.removePotionEffect(effect.getType());
                            }
                            kit.wear(player);
                            kit.wear(target);
                            player.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GRAY + "You have entered a regular 1v1 with " + target.getName());
                            target.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GRAY + "You have entered a regular 1v1 with " + player.getName());
                            return;
                        case 2:
                            player.teleport(arena3spawn1);
                            target.teleport(arena3spawn2);
                            player.getInventory().clear();
                            target.getInventory().clear();
                            for (PotionEffect effect : player.getActivePotionEffects()) {
                                player.removePotionEffect(effect.getType());
                                target.removePotionEffect(effect.getType());
                            }
                            kit.wear(player);
                            kit.wear(target);
                            player.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GRAY + "You have entered a regular 1v1 with " + target.getName());
                            target.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GRAY + "You have entered a regular 1v1 with " + player.getName());
                    }
                } else {
                    instance.getChallenged().put(player.getUniqueId(), target.getUniqueId());
                    player.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GREEN + "You have challenged " + target.getName() + " to a regular 1v1!");
                    target.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "1v1" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.GREEN + "You have been challenged by " + player.getName() + " to a Regular 1v1! Right click them to accept!");
                }
            }
        }
    }
}
