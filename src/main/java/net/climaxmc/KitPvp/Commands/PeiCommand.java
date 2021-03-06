package net.climaxmc.KitPvp.Commands;

import net.climaxmc.KitPvp.Utils.I;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PeiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        player.getInventory().addItem(new I(Material.PUMPKIN_PIE).name(ChatColor.GOLD + "Pei"));
        player.sendMessage(ChatColor.GREEN + "You have received a Pei!");

        return true;
    }
}
