package me.neo.randomtwistcore.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.neo.randomtwistcore.api.twists.Twist;
import me.neo.randomtwistcore.util.RTCRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * This class and it's accompanying methods are not to be used anywhere in the code manually.
 * It is handled automatically in the {@link me.neo.randomtwistcore.RTCAPI} class.
 */
public class TwistCommands {
    private final JavaPlugin plugin;

    public TwistCommands(JavaPlugin plugin) {
        this.plugin = plugin;
        startCommand();
        giveTwist();
        takeTwist();
        claimStash();
    }

    public void startCommand() {
        new CommandAPICommand("startTimer")
                .executes((sender, args) -> {
                    twistTimer();
                    sender.sendMessage("Successfully started Random Twists");
                }).register();
    }

    public void giveTwist() {
        new CommandAPICommand("giveTwist")
                .withAliases("gt")
                .withArguments(new BooleanArgument("silent"), new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(Twist.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(1);
                    boolean silent = (boolean) args.get(0);
                    Twist twist = Twist.Get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        boolean success = Twist.tryBind(sender, twist, silent);
                        if (!success)
                            sender.sendMessage(ChatColor.RED + "Internal Occurred while trying to bind you to: " + twistName);
                    }
                }).register();
    }

    public void takeTwist() {
        new CommandAPICommand("takeTwist")
                .withAliases("tt")
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(Twist.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = Twist.Get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        boolean success = Twist.tryUnbind(sender, twist, false);
                        if (!success)
                            sender.sendMessage(ChatColor.RED + "Internal Occurred while trying to bind you to: " + twistName);
                    }
                }).register();
    }

    public void claimStash() {
        new CommandAPICommand("claimStash")
                .withAliases("ct")
                .executesPlayer((sender, args) -> {
                    if (!Twist.itemStash.containsKey(sender))
                        Twist.itemStash.put(sender, new ArrayList<>());
                    int itemsClaimed = 0;
                    ArrayList<ItemStack> itemsToRemove = new ArrayList<>();
                    if (Twist.itemStash.get(sender).size() <= 0) {
                        sender.sendMessage(ChatColor.GREEN + "You have no items in your stash!");
                        return;
                    }
                    for (int i = 0; i < Twist.itemStash.get(sender).size(); i++) {
                        if (sender.getInventory().firstEmpty() == -1) {
                            Twist.doRemoveStashText(sender, itemsClaimed);
                            break;
                        }
                        ItemStack toGrant = Twist.getFromStash(sender, i);
                        sender.getInventory().addItem(toGrant);
                        itemsToRemove.add(toGrant);
                        sender.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
                        itemsClaimed++;
                    }
                    for (ItemStack stack : itemsToRemove) {
                        Twist.removeFromStash(sender, stack);
                    }
                    if (Twist.itemStash.get(sender).size() <= 0) {
                        sender.sendMessage(ChatColor.GREEN + "All items have been claimed!");
                    }
                }).register();
    }
    private void twistTimer() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ArrayList<Twist> valid = new ArrayList<>();
                for (Twist twist : Twist.twists) {
                    if (twist.isBound(player))
                        continue;
                    valid.add(twist);
                }
                Twist.tryBind(player, RTCRandom.randomItem(valid), false);
            }
        }, 0, 6000);
    }

}

