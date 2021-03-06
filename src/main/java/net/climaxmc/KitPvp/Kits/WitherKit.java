package net.climaxmc.KitPvp.Kits;


import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Kit;
import net.climaxmc.KitPvp.KitManager;
import net.climaxmc.KitPvp.Utils.Ability;
import net.climaxmc.antinub.AntiNub;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class WitherKit extends Kit {

    private final int cooldown = 4;
    private ItemStack ability = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);

    private Ability witherblast = new Ability("Wither Blast", 1, cooldown, TimeUnit.SECONDS);

    private ClimaxPvp plugin;

    public WitherKit(ClimaxPvp plugin) {
        super("Wither", new ItemStack(Material.SKULL_ITEM, 1, (byte) 1), "Launch wither skulls at people to murder them!", ChatColor.RED);
        this.plugin = plugin;
    }

    protected void wear(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().addItem(sword);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        player.getInventory().setHelmet(helmet);
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        chest.addEnchantment(Enchantment.DURABILITY, 3);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        LeatherArmorMeta chestmeta = (LeatherArmorMeta) chest.getItemMeta();
        chestmeta.setColor(Color.BLACK);
        chest.setItemMeta(chestmeta);
        player.getInventory().setChestplate(chest);
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        legs.addEnchantment(Enchantment.DURABILITY, 3);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        LeatherArmorMeta legsmeta = (LeatherArmorMeta) legs.getItemMeta();
        legsmeta.setColor(Color.BLACK);
        legs.setItemMeta(legsmeta);
        player.getInventory().setLeggings(legs);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 3);
        player.getInventory().setBoots(boots);

        ItemMeta abilitymeta = ability.getItemMeta();
        abilitymeta.setDisplayName(ChatColor.AQUA + "Wither Blast \u00A7f» \u00A78[\u00A76" + cooldown + "\u00A78]");
        ability.setItemMeta(abilitymeta);
        player.getInventory().addItem(ability);

        addSoup(player.getInventory(), 2, 34);
    }

    protected void wearNoSoup(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        regenResistance(player);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().addItem(sword);
        if (!ClimaxPvp.getInstance().spawnSoupTrue.containsKey(player)) {
            ClimaxPvp.getInstance().spawnSoupTrue.put(player, false);
        }
        if (!ClimaxPvp.getInstance().spawnSoupTrue.get(player)) {
            ItemStack rod = new ItemStack(Material.FISHING_ROD);
            player.getInventory().addItem(rod);
        }
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        player.getInventory().setHelmet(helmet);
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        chest.addEnchantment(Enchantment.DURABILITY, 3);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        LeatherArmorMeta chestmeta = (LeatherArmorMeta) chest.getItemMeta();
        chestmeta.setColor(Color.BLACK);
        chest.setItemMeta(chestmeta);
        player.getInventory().setChestplate(chest);
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        legs.addEnchantment(Enchantment.DURABILITY, 3);
        legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        LeatherArmorMeta legsmeta = (LeatherArmorMeta) legs.getItemMeta();
        legsmeta.setColor(Color.BLACK);
        legs.setItemMeta(legsmeta);
        player.getInventory().setLeggings(legs);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 3);
        player.getInventory().setBoots(boots);

        ItemMeta abilitymeta = ability.getItemMeta();
        abilitymeta.setDisplayName(ChatColor.AQUA + "Wither Blast \u00A7f» \u00A78[\u00A76" + cooldown + "\u00A78]");
        ability.setItemMeta(abilitymeta);
        player.getInventory().addItem(ability);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (KitManager.isPlayerInKit(player, this)) {
            if (player.getInventory().getItemInHand().getType() == Material.SKULL_ITEM) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (!witherblast.tryUse(player)) {
                        return;
                    }
                    player.sendMessage(ChatColor.GOLD + "You used the " + ChatColor.AQUA + "Wither Blast" + ChatColor.GOLD + " Ability!");
                    WitherSkull ws = (WitherSkull) player.launchProjectile(WitherSkull.class);
                    ws.setVelocity(player.getLocation().getDirection());
                    ws.setVelocity(ws.getVelocity().multiply(2));
                    ws.setIsIncendiary(true);
                    ws.setYield(5.0F);

                    witherblast.startCooldown(player, this, cooldown, ability);
                }
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (KitManager.isPlayerInKit(player, this)) {
                if (event.getDamager().getType() == EntityType.WITHER_SKULL) {
                    event.setCancelled(true);
                    player.setVelocity(player.getVelocity().setY(0.7));

                    AntiNub.alertsEnabled.put(player.getUniqueId(), false);
                    ClimaxPvp.getInstance().getServer().getScheduler().runTaskLater(ClimaxPvp.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            AntiNub.alertsEnabled.put(player.getUniqueId(), true);
                        }
                    }, 20L * 5);
                }
            }
        }
    }
}
