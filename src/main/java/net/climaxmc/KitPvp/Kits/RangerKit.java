package net.climaxmc.KitPvp.Kits;

import java.util.ArrayList;

import net.climaxmc.KitPvp.Kit;
import net.climaxmc.KitPvp.KitManager;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class RangerKit extends Kit {
    public RangerKit() {
        super("Ranger", new ItemStack(Material.ARROW), "Equipped with a Combat Bow so you can rek dem all!", ChatColor.GOLD);
    }

    protected void wear(Player player) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(Color.ORANGE);
        helmet.setItemMeta(helmetMeta);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setHelmet(helmet);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setColor(Color.WHITE);
        chestplate.setItemMeta(chestplateMeta);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setChestplate(chestplate);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(Color.ORANGE);
        leggings.setItemMeta(leggingsMeta);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setLeggings(leggings);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        player.getInventory().setBoots(boots);
        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(sword);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowmeta = bow.getItemMeta();
        bowmeta.setDisplayName(ChatColor.RED + "Combat Bow");
        ArrayList<String> lores = new ArrayList<>();
        lores.add("Knocks you back when fired, Shift to avoid Knockback!");
        bowmeta.setLore(lores);
        bow.setItemMeta(bowmeta);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
        player.getInventory().addItem(bow);
        addSoup(player.getInventory(), 2, 33);
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
    }
    
    protected void wearNoSoup(Player player){
    	ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(Color.ORANGE);
        helmet.setItemMeta(helmetMeta);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setHelmet(helmet);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setColor(Color.WHITE);
        chestplate.setItemMeta(chestplateMeta);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setChestplate(chestplate);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(Color.ORANGE);
        leggings.setItemMeta(leggingsMeta);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setLeggings(leggings);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        player.getInventory().setBoots(boots);
        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(sword);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowmeta = bow.getItemMeta();
        bowmeta.setDisplayName(ChatColor.RED + "Combat Bow");
        ArrayList<String> lores = new ArrayList<>();
        lores.add("Knocks you back when fired, Shift to avoid Knockback!");
        bowmeta.setLore(lores);
        bow.setItemMeta(bowmeta);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
        player.getInventory().addItem(bow);
        ItemStack fishingrod = new ItemStack(Material.FISHING_ROD);
        fishingrod.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        
        if (!KitManager.isPlayerInKit(player, this)) {
        	return;
        }

        if (player.isSneaking()) {
            return;
        }

        double vel = event.getProjectile().getVelocity().length() * (0.1D + 0.1D * 5);
        // Knock player back
        velocity(player, player.getLocation().getDirection().multiply(-1), vel,
                false, 0.0D, 0.2D, 0.8D, true);
    }

    private void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax, boolean groundBoost) {
        if ((Double.isNaN(vec.getX())) || (Double.isNaN(vec.getY())) || (Double.isNaN(vec.getZ())) || (vec.length() == 0.0D)) {
            return;
        }

        if (ySet) {
            vec.setY(yBase);
        }

        vec.normalize();
        vec.multiply(str);

        vec.setY(vec.getY() + yAdd);

        if (vec.getY() > yMax) {
            vec.setY(yMax);
        }

        if (groundBoost) {
            vec.setY(vec.getY() + 0.2D);
        }

        ent.setFallDistance(0.0F);
        ent.setVelocity(vec);
    }
}