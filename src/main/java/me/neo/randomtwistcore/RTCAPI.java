package me.neo.randomtwistcore;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.neo.randomtwistcore.api.twists.Twist;
import me.neo.randomtwistcore.api.twists.events.DeathListener;
import me.neo.randomtwistcore.commands.TwistCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The "Main" API class for RTC. This class has 3 methods that must be called in their respective spots and 1 utility method.
 */
public class RTCAPI {

    /**
     * Call this method in your plugin's onLoad method.
     * Just calls the CommandAPI's onLoad method.
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} that is calling this method.
     * @param <T> The {@link org.bukkit.plugin.java.JavaPlugin}
     */
    public static <T extends JavaPlugin> void onLoad(T plugin) {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).verboseOutput(true));
    }

    /**
     * Call this in your plugin's onEnable method AFTER you register all of your twists.
     * Registers the give and take twist commands along with start.
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} that is calling this method.
     * @param <T> The {@link org.bukkit.plugin.java.JavaPlugin}
     */
    public static <T extends JavaPlugin> void onEnable(T plugin) {
        new TwistCommands(plugin);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), plugin);
        for (String string : Twist.twistNames) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RTC | Registered]: " + string);
        }
    }

    /**
     * Call this in your plugin's onDisable method.
     * Just calls the CommandAPI's onDisable method.
     */
    public static void onDisable() {
        CommandAPI.onDisable();
    }

    /**
     * A helper method that can be used to generate {@link org.bukkit.NamespacedKey}s fast.
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} that this key's namespace will be under.
     * @param key The key of the {@link org.bukkit.NamespacedKey}
     * @param <T> The JavaPlugin that this {@link org.bukkit.NamespacedKey} will be under.
     * @return The {@link org.bukkit.NamespacedKey}.
     */
    public static <T extends JavaPlugin> NamespacedKey genKey(T plugin, String key) {
        return new NamespacedKey(plugin, key);
    }
}
