package net.climaxmc.Donations.Commands;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.common.database.PlayerData;
import net.climaxmc.common.donations.Perk;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpectateCommand implements Perk, CommandExecutor {
    private ClimaxPvp plugin;

    public SpectateCommand(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player);

        if (!playerData.hasPerk(this)) {
            player.sendMessage(ChatColor.RED + "Please donate at https://donate.climaxmc.net for access to spectator mode!");
            return true;
        }

        if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
            plugin.respawn(player);
            player.setGameMode(GameMode.SPECTATOR);
            player.setFlySpeed(0.15F);
            player.sendMessage(ChatColor.GREEN + "You are now spectating");
        } else {
            plugin.respawn(player);
            player.sendMessage(ChatColor.GREEN + "You are no longer spectating");
        }

        return true;
    }

    @Override
    public String getDBName() {
        return "Spectator";
    }
}
