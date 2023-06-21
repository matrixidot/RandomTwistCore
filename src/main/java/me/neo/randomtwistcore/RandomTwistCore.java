package me.neo.randomtwistcore;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.neo.randomtwistcore.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class RandomTwistCore extends JavaPlugin {
    public static RandomTwistCore plugin;
    public static Random random;

    @Override
    public void onLoad() {
        super.onLoad();
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        plugin = this;
        random = new Random();
        getLogger().info("Random Twist Core by man_in_matrix#4484 aka N3E0 has been started.");
        new Commands(this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

    }

    public static void registerListener(Listener listener) {
        Bukkit.getLogger().info("Registering events for: " + listener.getClass().getTypeName());
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
