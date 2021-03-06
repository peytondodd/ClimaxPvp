package net.climaxmc.KitPvp.Kits;

import me.xericker.disguiseabilities.DisguiseAbilities;
import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Kit;
import net.climaxmc.KitPvp.KitManager;
import net.climaxmc.KitPvp.Utils.Ability;
import net.climaxmc.KitPvp.Utils.Particles.TNTParticle;
import net.climaxmc.KitPvp.Utils.Settings.SettingsFiles;
import net.climaxmc.antinub.AntiNub;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;

public class BomberKit extends Kit {

    public BomberKit() {
        super("Bomber", new ItemStack(Material.TNT), "Explode in people's faces :D!", ChatColor.GOLD);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        LeatherArmorMeta chestmeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestmeta.setColor(Color.ORANGE);
        chestplate.setItemMeta(chestmeta);
        player.getInventory().setChestplate(chestplate);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        LeatherArmorMeta legmeta = (LeatherArmorMeta) leggings.getItemMeta();
        legmeta.setColor(Color.ORANGE);
        leggings.setItemMeta(legmeta);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().addItem(sword);

        ItemStack ability = new ItemStack(Material.TNT, 2);
        ItemMeta abilitymeta = ability.getItemMeta();
        abilitymeta.setDisplayName(ChatColor.AQUA + "Throwing TNT");
        ability.setItemMeta(abilitymeta);
        player.getInventory().addItem(ability);
        player.updateInventory();
    }

    protected void wearNoSoup(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        regenResistance(player);
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        LeatherArmorMeta chestmeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestmeta.setColor(Color.ORANGE);
        chestplate.setItemMeta(chestmeta);
        player.getInventory().setChestplate(chestplate);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        LeatherArmorMeta legmeta = (LeatherArmorMeta) leggings.getItemMeta();
        legmeta.setColor(Color.ORANGE);
        leggings.setItemMeta(legmeta);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().addItem(sword);
        if (!ClimaxPvp.getInstance().spawnSoupTrue.containsKey(player)) {             ClimaxPvp.getInstance().spawnSoupTrue.put(player, false);         }
        if (!ClimaxPvp.getInstance().spawnSoupTrue.get(player)) {
            ItemStack rod = new ItemStack(Material.FISHING_ROD);
            player.getInventory().addItem(rod);
        }
        ItemStack ability = new ItemStack(Material.TNT, 2);
        ItemMeta abilitymeta = ability.getItemMeta();
        abilitymeta.setDisplayName(ChatColor.AQUA + "Throwing TNT");
        ability.setItemMeta(abilitymeta);
        player.getInventory().addItem(ability);
        player.updateInventory();
    }

    /*@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (KitManager.isPlayerInKit(player, this)) {
            if (player.getInventory().getItemInHand().getType() == Material.TNT) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (!explode.tryUse(player)) {
                        return;
                    }
                    player.sendMessage(ChatColor.GOLD + "You used the " + ChatColor.AQUA + "Explode" + ChatColor.GOLD + " Ability!");
                    DisguiseAbilities.activateAbility(player, DisguiseAbilities.Ability.DETONATE);
                }
            }
        }
    }*/


    @EventHandler
    public void onPlayerClickDropTNT(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!KitManager.isPlayerInKit(player, this) || event.getItem() == null || !event.getItem().getType().equals(Material.TNT)) {
            return;
        }
        if (player.getInventory().getItemInHand().getType() == Material.TNT) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                spawnTNT(player);
                //for (int i = 0; i <= 2; i++) {
                    Bukkit.getServer().getScheduler().runTaskLater(ClimaxPvp.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            if (player.getInventory().contains(Material.TNT, 2) || !KitManager.isPlayerInKit(player, BomberKit.this)) {
                                return;
                            } else {
                                ItemStack ability = new ItemStack(Material.TNT);
                                ItemMeta abilitymeta = ability.getItemMeta();
                                abilitymeta.setDisplayName(ChatColor.AQUA + "Throwing TNT");
                                ability.setItemMeta(abilitymeta);
                                player.getInventory().addItem(ability);
                                player.updateInventory();
                            }
                        }
                    }, 20L * 5);
                //}
            }
        }
    }

    private void spawnTNT(Player player) {
        player.playSound(player.getLocation(), Sound.FUSE, 1, 1);
        ItemStack tntInInv = player.getItemInHand();
        tntInInv.setAmount(tntInInv.getAmount() - 1);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), tntInInv);
        player.updateInventory();
        TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);
        tnt.setFuseTicks(14);
        velocity(tnt, player.getLocation().getDirection().multiply(2), 0.5, false, 0.0, 0.1, 10.0, false);
        velocity(player, player.getLocation().getDirection().multiply(-1), tnt.getVelocity().length() + 0.02, false, 0.0, 0.2, 0.8, true);
        new TNTParticle(tnt);

        AntiNub.alertsEnabled.put(player.getUniqueId(), false);
        ClimaxPvp.getInstance().getServer().getScheduler().runTaskLater(ClimaxPvp.getInstance(), new Runnable() {
            @Override
            public void run() {
                AntiNub.alertsEnabled.put(player.getUniqueId(), true);
            }
        }, 20L * 5);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.setCancelled(true); // This should fix the bomber issue! It should also enable damage from tnt!

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.PRIMED_TNT)) {
            if (event.getEntity().getType().equals(EntityType.PLAYER)) {

                AntiNub.alertsEnabled.put(event.getEntity().getUniqueId(), false);
                ClimaxPvp.getInstance().getServer().getScheduler().runTaskLater(ClimaxPvp.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        AntiNub.alertsEnabled.put(event.getEntity().getUniqueId(), true);
                    }
                }, 20L * 5);
            }
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof TNTPrimed) {
            event.setCancelled(true);
            event.getEntity().getLocation().getWorld().createExplosion(event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ(), 2, false, false);
        }
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