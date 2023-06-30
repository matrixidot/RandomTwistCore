package me.neo.randomtwistcore.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.neo.randomtwistcore.api.twists.Twist;
import me.neo.randomtwistcore.util.RTCRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * This class and it's accompanying methods are not to be used anywhere in the code manually.
 * It is handled automatically in the {@link me.neo.randomtwistcore.RTCAPI} class.
 */
public class Commands {
    private final JavaPlugin plugin;

    public Commands(JavaPlugin plugin) {
        this.plugin = plugin;
        startCommand();
        giveTwist();
        takeTwist();
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
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(Twist.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = Twist.Get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        boolean success = Twist.tryBind(sender, twist);
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

    private void twistTimer() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ArrayList<Twist> valid = new ArrayList<>();
                for (Twist twist : Twist.twists) {
                    if (twist.isBound(player))
                        continue;
                    valid.add(twist);
                }
                Twist.tryBind(player, RTCRandom.randomItem(valid));
            }
        }, 0, 6000);
    }

}

