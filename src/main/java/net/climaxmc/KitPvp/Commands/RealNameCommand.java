package net.climaxmc.KitPvp.Commands;

import net.climaxmc.ClimaxPvp;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RealNameCommand implements CommandExecutor {
    private ClimaxPvp plugin;

    public RealNameCommand(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/realname [nickname]");
            return true;
        }

        plugin.getServer().getOnlinePlayers().stream().filter(players -> StringUtils.containsIgnoreCase(players.getDisplayName(), args[0]))
                .forEach(players -> player.sendMessage(ChatColor.WHITE + "\u00BB " + ChatColor.GRAY + players.getDisplayName() + ChatColor.GRAY + "'s real name is " + ChatColor.YELLOW + players.getName() + "."));

        return true;
    }
}
