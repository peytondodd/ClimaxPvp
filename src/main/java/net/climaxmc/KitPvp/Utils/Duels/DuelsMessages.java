package net.climaxmc.KitPvp.Utils.Duels;// AUTHOR: gamer_000 (11/10/2015)

import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Utils.TextComponentMessages;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class DuelsMessages {

    public static String duelChatStarter = (AQUA + "" + BOLD + "  Climax" + YELLOW + "" + BOLD + "Duels" + WHITE + "\u00BB");

    private ClimaxPvp plugin;

    public DuelsMessages(ClimaxPvp plugin) {
        this.plugin = plugin;
    }

    TextComponentMessages tcm = new TextComponentMessages();

    public void sendDuelRequestMessage(Player sender, Player target) {
        sender.playSound(sender.getLocation(), Sound.ITEM_PICKUP, 2F, 0.3F);
        sender.sendMessage(WHITE + "     \u00AB" + GREEN + " Your request to duel has been sent to " + YELLOW + target.getName() + WHITE + " \u00BB");

        target.playSound(target.getLocation(), Sound.ITEM_PICKUP, 2F, 0.3F);
        target.sendMessage(" ");
        target.sendMessage(GOLD + "     \u00AB" + WHITE + " ========================================" + GOLD + " \u00BB");
        target.sendMessage(" ");
        target.sendMessage(YELLOW + "             " + sender.getName() + AQUA + " has sent you a request to duel!");
        target.sendMessage(" ");
        BaseComponent component = tcm.centerTextSpacesLeft();
        component.addExtra(tcm.teamAcceptButton());
        component.addExtra(tcm.centerTextSpacesMiddle());
        component.addExtra(tcm.teamDenyButton());
        target.spigot().sendMessage(component);
        target.sendMessage(GOLD + "     \u00AB" + WHITE + " ========================================" + GOLD + " \u00BB");
        target.sendMessage(" ");
    }

    public void sendDuelAcceptMessage(Player target, Player sender) {
        sender.playSound(sender.getLocation(), Sound.VILLAGER_YES, 1, 1F);
        sender.sendMessage(GREEN + "     \u00AB " + AQUA + target.getName() + YELLOW + " has " + GREEN + "accepted " + YELLOW + "your request to duel!" + GREEN + " \u00BB");

        target.playSound(target.getLocation(), Sound.VILLAGER_YES, 1, 1F);
        target.sendMessage(GREEN + "     \u00AB " + YELLOW + "You have " + GREEN + "accepted " + AQUA + sender.getName() + YELLOW + "'s request to duel!" + GREEN + " \u00BB");
    }

    public void sendDuelDeclineMessage(Player target, Player sender) {
        sender.playSound(sender.getLocation(), Sound.VILLAGER_NO, 1, 0.75F);
        sender.sendMessage(RED + "     \u00AB " + AQUA + target.getName() + YELLOW + " has " + RED + "declined " + YELLOW + "your request to duel!" + RED + " \u00BB");

        target.playSound(target.getLocation(), Sound.VILLAGER_NO, 1, 0.75F);
        target.sendMessage(RED + "     \u00AB " + YELLOW + "You have " + RED + "declined " + AQUA + target.getName() + YELLOW + "'s request to duel!" + RED + " \u00BB");
    }

    public void sendDuelMessage(Player player1, Player player2, String msg) {
        player1.sendMessage(duelChatStarter + msg);
        player2.sendMessage(duelChatStarter + msg);
    }

}