package me.neo.randomtwistcore;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class RTC extends JavaPlugin {
    public static RTC plugin;
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
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
