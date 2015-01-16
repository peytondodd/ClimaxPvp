package net.climaxmc.KitPvp.Kits;

import net.climaxmc.KitPvp.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class WitchKit extends Kit {
    public WitchKit() {
        super("Witch", new ItemStack(Material.SOUL_SAND), "Potions FTW!", KitType.AMATEUR);
    }

    public void wear(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        player.getInventory().addItem(sword);
        addSoup(player.getInventory(), 3, 35);
    }
}