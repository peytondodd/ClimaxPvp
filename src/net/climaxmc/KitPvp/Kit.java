package net.climaxmc.KitPvp;

import java.util.ArrayList;

import lombok.Data;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Represents a kit
 * 
 * @author computerwizjared
 */
@Data
public abstract class Kit implements Listener, CommandExecutor {
	/**
	 * Name of the kit
	 */
	private String name = "";
	/**
	 * Item representing the kit
	 */
	private ItemStack item = new ItemStack(Material.AIR);
	/**
	 * Lore of the kit
	 */
	private String lore = "";
	/**
	 * Slot of the kit
	 */
	private int slot = 0;
	
	/**
	 * Defines a kit
	 * 
	 * @param name Name of the kit
	 * @param item Item representing the kit
	 * @param slot Slot of the kit
	 */
	public Kit(String name, ItemStack item, int slot) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("�eKit " + name);
		item.setItemMeta(meta);
		this.name = name;
		this.item = item;
		this.slot = slot;
		System.out.println("Kit Manager> Enabled kit " + name);
	}
	
	/**
	 * Defines a kit
	 * 
	 * @param name Name of the kit
	 * @param item Item representing the kit
	 * @param lore Lore of the kit
	 * @param slot Slot of the kit
	 */
	public Kit(String name, ItemStack item, String lore, int slot) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lores = new ArrayList<String>();
		lores.add(lore);
		meta.setLore(lores);
		meta.setDisplayName("�eKit " + name);
		item.setItemMeta(meta);
		this.name = name;
		this.item = item;
		this.lore = lore;
		this.slot = slot;
		System.out.println("Kit Manager> Enabled kit " + name);
	}
	
	/**
	 * Wear the kit
	 * 
	 * @param player Player to wear kit
	 */
	public abstract void wear(Player player);
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase(getName())) {
				if (player.hasPermission("ClimaxKits.Kit." + getName().replaceAll("\\s+", ""))) {
					if (!Main.inKit.contains(player.getUniqueId())) {
						Main.inKit.add(player.getUniqueId());
						for (PotionEffect effect : player.getActivePotionEffects()) {
							player.removePotionEffect(effect.getType());
						}
						player.getInventory().clear();
						wear(player);
						player.sendMessage("�6You have chosen �a" + getName());
					} else {
						player.sendMessage("�cYou have not died yet!");
					}
				} else {
					player.sendMessage("�cYou do not have permission for kit " + getName() + "�c!");
				}
			}
		}
		return false;
	}

	/**
	 * Gives a player specified amount of soups
	 * 
	 * @param inventory Inventory to give soups
	 * @param startslot Inventory slot to start giving soups
	 * @param finalslot Inventory slot to end giving soups
	 */
	public static void addSoup(Inventory inventory, int startslot, int finalslot) {
		for (int x = startslot; x <= finalslot; x++) {
			inventory.setItem(x, new ItemStack(Material.MUSHROOM_SOUP));
		}
	}
}