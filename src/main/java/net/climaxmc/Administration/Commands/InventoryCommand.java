package net.climaxmc.Administration.Commands;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.common.Rank;
import net.climaxmc.common.database.CachedPlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class InventoryCommand implements CommandExecutor {
    private ClimaxPvp plugin;

    public InventoryCommand(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        CachedPlayerData playerData = plugin.getPlayerData(player);

        if (!playerData.hasRank(Rank.HELPER)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute that command!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/" + label + " <player>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online!");
            return true;
        }

        player.openInventory(target.getInventory());

        return true;
    }
}