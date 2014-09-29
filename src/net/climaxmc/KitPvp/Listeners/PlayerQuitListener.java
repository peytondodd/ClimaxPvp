package net.climaxmc.KitPvp.Listeners;

import net.climaxmc.KitPvp.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
	Main plugin;
	
	public PlayerQuitListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Main.inKit.contains(player.getUniqueId())) {
			Main.inKit.remove(player.getUniqueId());
		}
		event.setQuitMessage("�cQuit�8� " + player.getName());
	}
}