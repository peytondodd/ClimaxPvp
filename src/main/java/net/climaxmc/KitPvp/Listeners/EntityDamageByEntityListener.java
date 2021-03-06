package net.climaxmc.KitPvp.Listeners;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.KitPvp;
import net.climaxmc.KitPvp.Utils.Duels.DuelUtils;
import net.climaxmc.KitPvp.Utils.Fair.FairUtils;
import net.climaxmc.KitPvp.Utils.Fair.MatchManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class EntityDamageByEntityListener implements Listener {
    private ClimaxPvp plugin;

    public EntityDamageByEntityListener(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    @SuppressWarnings("deprecation")
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            Player target = (Player) event.getEntity();
            Player player = null;

            if (event.getEntityType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {
                player = (Player) event.getDamager();
            } else if (event.getDamager().getType().equals(EntityType.ARROW) || event.getDamager().getType().equals(EntityType.FISHING_HOOK)) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    player = (Player) projectile.getShooter();
                }
            }

            if (plugin.isWithinProtectedRegion(target.getLocation())) {
                event.setCancelled(true);
                return;
            }

            if (player != null) {
                if (plugin.isWithinProtectedRegion(player.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (target.getGameMode().equals(GameMode.CREATIVE) && ClimaxPvp.deadPeoples.contains(target)) {
                event.setCancelled(true);
                return;
            }
            if (ClimaxPvp.isSpectating != null) {
                if (player != null) {
                    if (ClimaxPvp.isSpectating.contains(player.getUniqueId())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if (ClimaxPvp.deadPeoples != null) {
                if (ClimaxPvp.deadPeoples.contains(player)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (player != null && ((KitPvp.currentTeams.containsKey(player.getName())
                    && KitPvp.currentTeams.get(player.getName()).equals(target.getName()))
                    || (KitPvp.currentTeams.containsKey(target.getName())
                    && KitPvp.currentTeams.get(target.getName()).equals(player.getName())))) {
                event.setCancelled(true);
                return;
            }

            if (plugin.getWarpLocation("Fair") != null) {
                if (target.getLocation().distance(plugin.getWarpLocation("Fair")) <= 50) {
                    if (!MatchManager.isInMatch(player.getUniqueId())) {
                        return;
                    }
                }
            }
            if (event.isCancelled()) {
                return;
            }
            if (player != null) {
                if (!MatchManager.isInMatch(player.getUniqueId()) && MatchManager.isInMatch(target.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }

                if (MatchManager.isInMatch(player.getUniqueId()) && !MatchManager.isInMatch(target.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (target.getNoDamageTicks() <= 0) {

                float knockbackStrength;

                if (!plugin.lastHitType.containsKey(player)) {
                    plugin.lastHitType.put(player, "Walk");
                }

                if (player != null) {
                    if (player.isSprinting() && plugin.lastHitType.get(player).equals("Walk")) {
                        knockbackStrength = -0.28F;
                        plugin.lastHitType.put(player, "Sprint");
                    } else {
                        knockbackStrength = -0.1F;
                    }

                    if (player.isOnGround()) {
                        knockbackStrength -= -0.2F;
                    }

                    if (player.getVelocity().length() >= 0.08) {
                        knockbackStrength -= (float)((knockbackStrength * target.getVelocity().length()) / 18);
                    }

                    double y = -0.19;
                    if (player.isOnGround()) {
                        y = -0.19;
                    }

                    //Bukkit.broadcastMessage("str: " + knockbackStrength + " y: " + y + " vel length: " + target.getVelocity().length());

                    target.setVelocity(new Vector().setY(0).setX(0).setZ(0).zero());
                    target.setVelocity(target.getLocation().toVector().subtract(player.getLocation().toVector()).setY(0).normalize().multiply(knockbackStrength).setY(y));
                }
            }
        }
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.PLAYER) || !event.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }
        Player player = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        if (player.getItemInHand().getType().equals(Material.DIAMOND_AXE)) {
            if (player.getItemInHand().getItemMeta().getDisplayName() != null && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Duel Axe " + ChatColor.AQUA + "(Punch a player!)")) {
                if (ClimaxPvp.hasRequest.containsKey(target) && ClimaxPvp.hasRequest.get(target).equals(player)) {
                    DuelUtils duelUtils = new DuelUtils(plugin);
                    duelUtils.acceptRequest(target);
                    event.setCancelled(true);
                    return;
                }
                if (ClimaxPvp.hasRequest.containsKey(player)) {
                    player.sendMessage(ChatColor.WHITE + "\u00BB " + ChatColor.RED + "You have already sent a duel request to this player!");
                    event.setCancelled(true);
                    return;
                } else {
                    if (ClimaxPvp.inDuel.contains(target)) {
                        player.sendMessage(ChatColor.WHITE + "\u00BB " + ChatColor.RED + "That player is already in a duel!");
                        event.setCancelled(true);
                        return;
                    } else {
                        DuelUtils duelUtils = new DuelUtils(plugin);
                        duelUtils.openInventory(player);
                        event.setCancelled(true);

                        ClimaxPvp.initialRequest.put(player, target);
                        ClimaxPvp.duelRequestReverse.put(target, player);
                         Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                             @Override
                             public void run() {
                                 ClimaxPvp.initialRequest.remove(player);
                                 ClimaxPvp.duelRequestReverse.remove(target);
                                 ClimaxPvp.duelsKit.remove(player);
                             }
                         }, 20L * 15);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                Player damager = (Player) player.getLastDamageCause().getEntity();
                player.damage(1000, damager);
            }
        }
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            Player player = (Player) event.getEntity();
            if (plugin.isWithinProtectedRegion(player.getLocation())) {
                //event.setCancelled(true);
            }
            /*if (player.getLocation().distance(plugin.getWarpLocation("Duel")) <= 50) {
                event.setCancelled(true);
            }*/
        }
    }
}
