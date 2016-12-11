package net.climaxmc.KitPvp.Listeners;

import net.climaxmc.ClimaxPvp;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * Created by Joshua on 11/9/2016.
 * This class is for better/more smooth potion throwing. It works well :D
 */
public class ProjectileLaunchListener implements Listener{
    private ClimaxPvp plugin;

    public ProjectileLaunchListener(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(ProjectileLaunchEvent event) {
        if (event.getEntity().getType() == EntityType.SPLASH_POTION) {
            event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(0.83));
        }
    }
    /*@EventHandler
    public void onEntityHitGround(PotionSplashEvent event) {
        if (event.getPotion().getShooter() instanceof Player) {
            Player player = (Player) event.getPotion().getShooter();
            player.setHealth(player.getHealth() + 3);
        }
    }*/
}
