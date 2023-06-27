package me.neo.randomtwistcore.api.twists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class Twist implements Listener {
    /**
     * A List of registered Twists.
     */
    public static ArrayList<Twist> twists = new ArrayList<>();

    /**
     * A List of registered Twist's names.
     */
    public static ArrayList<String> twistNames = new ArrayList<>();

    /**
     * A List of players bound to a specific twist.
     */
    protected ArrayList<Player> boundPlayers = new ArrayList<>();

    /**
     * The Twist's name.
     */
    protected final String name;

    /**
     * The Twist's description.
     */
    protected final String description;

    /**
     * The Twist's unique id.
     */
    protected final int id;

    public Twist(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    /**
     * Checks if the player is bound to the specified twist.
     * @param player The player to check.
     * @param twist The twist to check.
     * @return True if the player is bound to the twist. False otherwise.
     */
    public static boolean isBound(Player player, Twist twist) {
        return twist.boundPlayers.contains(player);
    }

    public boolean isBound(Player player) {
        return boundPlayers.contains(player);
    }

    /**
     * Tries to get the twist associated with the class.
     * @param t The Twist's runtime class.
     * @param <T> The Twist's runtime class.
     * @return The twist attached to the class or null if none was found.
     */
    @Nullable
    public static <T extends Twist> Twist Get(Class<T> t) {
        for (Twist twist : twists) {
            if (t == twist.getClass())
                return twist;
        }
        return null;
    }

    /**
     * Tries to get the twist associated with the name.
     * @param name The name of the twist.
     * @return The twist associated with the name or null if none was found.
     */
    @Nullable
    public static Twist Get(String name) {
        for (Twist twist : twists) {
            if (twist.name.equalsIgnoreCase(name))
                return twist;
        }
        return null;
    }

    /**
     * Tries to get the twist associated with the id.
     * @param id The id of the twist.
     * @return The twist associated with the id or null if none was found.
     */
    @Nullable
    public static Twist Get(int id) {
        for (Twist twist : twists) {
            if (twist.id == id)
                return twist;
        }
        return null;
    }

    /**
     * Tries to register a Twist.
     * @param twist The twist to register.
     * @param plugin The plugin that this twist is being registered from.
     * @return True if it registered. False otherwise.
     */
    public static boolean tryRegister(Twist twist, Plugin plugin) {
        if (twists.contains(twist)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Register]: " + twist.name + " is already registered");

            return false;
        }

        twists.add(twist);
        twistNames.add(twist.name);
        twist.onRegister();
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Reg Events]: " + "Registering events for: " + twist.getClass().getTypeName());

        Bukkit.getPluginManager().registerEvents(twist, plugin);
        return true;
    }

    /**
     * Tries to unregister a Twist.
     * @param twist The twist to unregister.
     * @return True if the twist was unregistered. False otherwise.
     */
    public static boolean tryUnregister(Twist twist) {
        if (!twists.contains(twist)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Unregister]: " + twist.name + " is not registered");

            return false;
        }

        twists.remove(twist);
        twistNames.remove(twist.name);
        twist.onUnregister();
        return true;
    }

    /**
     * Tries to bind a player to a specified twist.
     * @param player The player to bind.
     * @param twist The type of twist to bind the player to.
     * @return True if the player was successfully bound. False otherwise.
     */
    public static boolean tryBind(Player player, Twist twist) {
        if (twist == null) {
            Bukkit.getLogger().severe("Internal error occurred while binding player, twist is null");
            return false;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Bind]: Attempting to bind " + player.getName() + " to: " + twist.name);

        if (twist.boundPlayers.contains(player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Bind]: " + player.getName() + " is already bound to: " + twist.name + ".");
            return false;
        }

        twist.boundPlayers.add(player);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Bind]: Successfully bound " + player.getName() + " from: " + twist.name);

        player.sendMessage(ChatColor.GREEN + "Got new twist: " + twist.name + "!");
        player.sendMessage(ChatColor.AQUA + twist.description);
        if (twist instanceof ItemTwist itemTwist) {
            if (itemTwist.customRecipe != null)
                player.discoverRecipe(itemTwist.customRecipe.getKey());
            if (itemTwist.grantItemOnBind)
                player.getInventory().addItem(itemTwist.getCustomItem());
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 4f);
        return true;
    }

    /**
     * Tries to unbind a player from a specified twist.
     * @param player The player to unbind.
     * @param twist The type of twist to unbind the player from.
     * @return True if the player was successfully unbound. False otherwise.
     */
    public static boolean tryUnbind(Player player, Twist twist) {
        if (twist == null) {
            Bukkit.getLogger().severe("Internal error occurred while unbinding player, twist is null");
            return false;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unbind]: Attempting to unbind " + player.getName() + " from: " + twist.name);

        if (!twist.boundPlayers.contains(player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Unbind]: " + player.getName() + " is not bound to: " + twist.name + ".");
            return false;
        }

        twist.boundPlayers.remove(player);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Unbind]: Successfully unbound " + player.getName() + " from: " + twist.name);
        player.sendMessage(ChatColor.RED + "Lost twist: " + twist.name + "!");
        if (twist instanceof ItemTwist itemTwist)
            if (itemTwist.customRecipe != null)
                player.undiscoverRecipe(itemTwist.customRecipe.getKey());

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
        return true;
    }

    /**
     * Called when a twist is registered.
     * Mainly used for debugging.
     */
    protected void onRegister() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Register]: " + name + " has been registered");
        for (String string : Twist.twistNames) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RTC | All Registered]: " + string);
        }
    }

    /**
     * Called when a twist is unregistered.
     * Mainly used for debugging.
     */
    protected void onUnregister() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Unregister]: " + name + " has been unregistered");
        for (String string : Twist.twistNames) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RTC | All Registered]: " + string);
        }
    }
}
