package net.climaxmc.Donations.Commands;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.common.database.Rank;
import net.climaxmc.common.database.PlayerData;
import net.climaxmc.common.donations.Perk;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class NicknameCommand implements Perk, CommandExecutor {
    private ClimaxPvp plugin;

    public NicknameCommand(ClimaxPvp plugin) {
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
            player.sendMessage(ChatColor.RED + "Please donate at https://donate.climaxmc.net for access to this perk!");
            return true;
        }

        if (args.length == 2) {
            if (playerData.hasRank(Rank.MODERATOR)) {
                Player target = plugin.getServer().getPlayer(args[0]);
                PlayerData targetData = plugin.getPlayerData(player);

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "That player is not online!");
                    return true;
                }

                if (args[1].equalsIgnoreCase("off")) {
                    player.sendMessage(ChatColor.GREEN + target.getName() + "'s nickname has been disabled!");
                    targetData.setNickname(null);
                    target.setDisplayName(null);
                    return true;
                }

                String nickname = ChatColor.translateAlternateColorCodes('&', args[1]);
                target.setDisplayName(nickname);
                targetData.setNickname(nickname);
                player.sendMessage(ChatColor.GREEN + target.getName() + "'s nickname has been set to " + nickname + ChatColor.GREEN + "!");

                return true;
            }
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/" + label + " <nickname/off>");
            return true;
        }

        if (args[0].length() >= 32) {
            player.sendMessage(ChatColor.RED + "You cannot have a nickname longer than 32 characters!");
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            player.sendMessage(ChatColor.GREEN + "Your nickname has been disabled!");
            player.setDisplayName(null);
            playerData.setNickname(null);
            return true;
        }

        String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);
        player.setDisplayName(nickname);
        playerData.setNickname(nickname);
        player.sendMessage(ChatColor.GREEN + "Your nickname has been set to " + nickname + ChatColor.GREEN + "!");

        return true;
    }

    @Override
    public String getDBName() {
        return "Nickname";
    }
}
