package net.climaxmc.KitPvp.Kits;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Kit;
import net.climaxmc.KitPvp.KitManager;
import net.climaxmc.KitPvp.KitPvp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrafeKit extends Kit {
    public StrafeKit() {
        super("Strafe", new ItemStack(Material.IRON_SWORD), "It's in the name! Speed Ftw!", ChatColor.BLUE);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.getInventory().clear();
        ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        ItemStack leggings = new ItemStack(Material.GOLD_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setLeggings(leggings);
        ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        player.getInventory().setBoots(boots);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        player.getInventory().addItem(sword);
        addSoup(player.getInventory(), 1, 35);
    }

    protected void wearNoSoup(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        regenResistance(player);
        player.getInventory().clear();
        ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        ItemStack leggings = new ItemStack(Material.GOLD_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setLeggings(leggings);
        ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        player.getInventory().setBoots(boots);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        player.getInventory().addItem(sword);
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        player.getInventory().addItem(rod);
    }

    /*@EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.FISHING_HOOK)) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player player = (Player) projectile.getShooter();
                if (KitManager.isPlayerInKit(player, this)) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 1));
                }
            }
        }
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) event.getDamager();
            if (KitManager.isPlayerInKit(player, this)) {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 1));
            }
        }
    }*/
}