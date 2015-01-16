package net.climaxmc.KitPvp.Kits;

import net.climaxmc.KitPvp.Kit;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArcherKit extends Kit {
	public ArcherKit() {
		super("Archer", new ItemStack(Material.BOW), "Snipe them up!", KitType.DEFAULT);
	}

	public void wear(Player player) {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
		helmetMeta.setColor(Color.WHITE);
		helmet.setItemMeta(helmetMeta);
		helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		helmet.addEnchantment(Enchantment.DURABILITY, 2);
		player.getInventory().setHelmet(helmet);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
		chestplateMeta.setColor(Color.WHITE);
		chestplate.setItemMeta(chestplateMeta);
		chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		chestplate.addEnchantment(Enchantment.DURABILITY, 2);
		player.getInventory().setChestplate(chestplate);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
		leggingsMeta.setColor(Color.WHITE);
		leggings.setItemMeta(leggingsMeta);
		leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		leggings.addEnchantment(Enchantment.DURABILITY, 2);
		player.getInventory().setLeggings(leggings);
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		player.getInventory().setBoots(boots);
		ItemStack sword = new ItemStack(Material.WOOD_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		sword.addEnchantment(Enchantment.DURABILITY, 2);
		player.getInventory().addItem(sword);
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		player.getInventory().addItem(bow);
		addSoup(player.getInventory(), 2, 34);
		player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
	}
}