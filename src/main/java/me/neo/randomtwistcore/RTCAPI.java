package me.neo.randomtwistcore;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.neo.randomtwistcore.commands.Commands;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class RTCAPI {

    public static <T extends JavaPlugin> void onLoad(T plugin) {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).verboseOutput(true));
    }

    public static <T extends JavaPlugin> void onEnable(T plugin) {
        new Commands(plugin);
    }

    public static void onDisable() {
        CommandAPI.onDisable();
    }

    public static <T extends JavaPlugin> NamespacedKey genKey(T plugin, String key) {
        return new NamespacedKey(plugin, key);
    }
}
