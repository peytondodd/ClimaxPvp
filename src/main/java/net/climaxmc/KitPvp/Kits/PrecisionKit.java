package net.climaxmc.KitPvp.Kits;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Kit;
import net.climaxmc.KitPvp.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

public class PrecisionKit extends Kit {
    public PrecisionKit() {
        super("Precision", new ItemStack(Material.ARROW), "Op Sword, but damages you if you miss!", ChatColor.BLUE);
    }

    protected void wear(Player player) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordmeta = sword.getItemMeta();
        swordmeta.setDisplayName(ChatColor.AQUA + "Precision Sword");
        sword.setItemMeta(swordmeta);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        player.getInventory().addItem(sword);
        player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        player.getInventory().setBoots(boots);
        addSoup(player.getInventory(), 1, 35);
    }

    protected void wearNoSoup(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        regenResistance(player);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordmeta = sword.getItemMeta();
        swordmeta.setDisplayName(ChatColor.AQUA + "Precision Sword");
        sword.setItemMeta(swordmeta);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        player.getInventory().addItem(sword);
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        player.getInventory().addItem(rod);
        player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        player.getInventory().setBoots(boots);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (KitManager.isPlayerInKit(player, this)) {
            if (player.getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (getTarget(player) != null) {
                        return;
                    }

                    player.damage(4);
                }
            }
        }
    }

    private Entity getTarget(final Player player) {
        try {
            BlockIterator iterator = new BlockIterator(player.getWorld(), player
                    .getLocation().toVector(), player.getEyeLocation()
                    .getDirection(), 0, 5);
            while (iterator.hasNext()) {
                Block item = iterator.next();
                for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                    int acc = 2;
                    for (int x = -acc; x < acc; x++)
                        for (int z = -acc; z < acc; z++)
                            for (int y = -acc; y < acc; y++)
                                if (entity.getLocation().getBlock()
                                        .getRelative(x, y, z).equals(item)) {
                                    return entity;
                                }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}
