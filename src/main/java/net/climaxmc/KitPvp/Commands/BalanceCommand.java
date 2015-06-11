package net.climaxmc.KitPvp.Commands;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.common.database.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    private ClimaxPvp plugin;

    public BalanceCommand(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player);
        player.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.RED + "$" + playerData.getBalance());
        player.sendMessage(ChatColor.GREEN + "Levels: " + ChatColor.RED + playerData.getKills());

        return true;
    }
}
