package me.neo.randomtwistcore.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.neo.randomtwistcore.RandomTwistCore;
import me.neo.randomtwistcore.api.Twist;
import me.neo.randomtwistcore.util.RandomElements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Commands {
    private final RandomTwistCore plugin;
    public Commands(RandomTwistCore plugin) {
        this.plugin = plugin;
        StartCommand();
        GrantTwist();
        RevokeTwist();
    }



    public void StartCommand() {
        new CommandAPICommand("startTimer")
                .executes((sender, args) -> {
                    GiveTwists();
                    sender.sendMessage("Successfully started Random Twists");
                }).register();
    }

    public void GrantTwist() {
        new CommandAPICommand("giveTwist")
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(Twist.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = Twist.Get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        Twist.tryBind(sender, twist);
                    }
                }).register();
    }

    public void RevokeTwist() {
        new CommandAPICommand("takeTwist")
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(Twist.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = Twist.Get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        Twist.tryUnbind(sender, twist);
                    }
                }).register();
    }

    private void GiveTwists() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ArrayList<Twist> valid = new ArrayList<>();
                for (Twist twist : Twist.twists) {
                    if (twist.isBound(player))
                        continue;
                    valid.add(twist);
                }
                Twist.tryBind(player, RandomElements.randomItem(valid));
            }
        }, 0, 6000);
    }
}
