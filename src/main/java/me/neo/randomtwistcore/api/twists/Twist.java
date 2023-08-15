package me.neo.randomtwistcore.api.twists;

import me.neo.randomtwistcore.api.customevents.PlayerBindTwistEvent;
import me.neo.randomtwistcore.api.customevents.PlayerUnbindTwistEvent;
import me.neo.randomtwistcore.api.customevents.RegisterTwistEvent;
import me.neo.randomtwistcore.api.customevents.UnregisterTwistEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
/**
 * This class encompasses all backend logic of RTC and can also be extended to make custom twists.
 */
@SuppressWarnings("unused")
public abstract class Twist implements Listener {
    /**
     * An {@link java.util.ArrayList} of registered twists.
     */
    public static ArrayList<Twist> twists = new ArrayList<>();

    /**
     * An {@link java.util.ArrayList} of registered twists' names.
     */
    public static ArrayList<String> twistNames = new ArrayList<>();

    /**
     * A stash for player's unclaimed twist items.
     */
    public static HashMap<UUID, List<ItemStack>> itemStash = new HashMap<>();

    /**
     * An {@link java.util.ArrayList} of {@link org.bukkit.entity.Player}s that are bound to a given twist..
     */
    protected ArrayList<UUID> boundPlayers = new ArrayList<>();

    /**
     * The name of the twist.
     */
    protected final String name;

    /**
     * The description of the twist.
     */
    protected final String description;

    /**
     * The id of the twist.
     */
    protected final int id;

    public Twist(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    /**
     * Checks if the player is bound to the specified twist.
     * @param player The {@link org.bukkit.entity.Player} to check.
     * @param twist The {@link me.neo.randomtwistcore.api.twists.Twist} to check.
     * @return True if the player is bound to the twist. False otherwise.
     */
    public static boolean isBound(Player player, Twist twist) {
        return twist.boundPlayers.contains(player.getUniqueId());
    }

    /**
     * Returns if the player is bound to the twist.
     * This method is to be used in classes that extend twist.
     * @param player The {@link org.bukkit.entity.Player} to check.
     * @return If the player is bound.
     */
    public boolean isBound(Player player) {
        return boundPlayers.contains(player.getUniqueId());
    }

    /**
     * Tries to get the {@link me.neo.randomtwistcore.api.twists.Twist} associated with the class.
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
     * Tries to get the {@link me.neo.randomtwistcore.api.twists.Twist} associated with the name.
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
     * Tries to get the {@link me.neo.randomtwistcore.api.twists.Twist} associated with the id.
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
     * Adds an item to the player's item Stash.
     * Will create the stash if the player does not have one.
     * @param player The {@link org.bukkit.entity.Player}'s stash to add the item to.
     * @param toAdd The {@link org.bukkit.inventory.ItemStack} to add to the stash.
     */
    public static void addToStash(Player player, ItemStack toAdd) {
        if (!itemStash.containsKey(player.getUniqueId()))
            itemStash.put(player.getUniqueId(), new ArrayList<>());
        itemStash.get(player.getUniqueId()).add(toAdd);
    }

    /**
     * Removes the item at the index from the player's stash.
     * Will create the stash if the player does not have one.
     * @param player The {@link org.bukkit.entity.Player}'s stash to remove the item from.
     * @param index The (0-indexed) index of the item to remove.
     */
    public static void removeFromStash(Player player, int index) {
        if (!itemStash.containsKey(player.getUniqueId()))
            itemStash.put(player.getUniqueId(), new ArrayList<>());
        itemStash.get(player.getUniqueId()).remove(index);
    }

    /**
     * Removes the item from the player's stash.
     * Will create the stash if the player does not have one.
     * @param player The {@link org.bukkit.entity.Player}'s stash to remove the item from.
     * @param stack The {@link org.bukkit.inventory.ItemStack} to remove from the stash.
     */
    public static void removeFromStash(Player player, ItemStack stack) {
        if (!itemStash.containsKey(player.getUniqueId()))
            itemStash.put(player.getUniqueId(), new ArrayList<>());
        itemStash.get(player.getUniqueId()).remove(stack);
    }

    /**
     * Removes the item at the index from the player's stash.
     * Will create the stash if the player does not have one.
     * @param player The {@link org.bukkit.entity.Player}'s stash to remove the item from.
     * @param stacks The {@link org.bukkit.inventory.ItemStack}s to remove.
     */
    public static void removeFromStash(Player player, ItemStack... stacks) {
        if (!itemStash.containsKey(player.getUniqueId()))
            itemStash.put(player.getUniqueId(), new ArrayList<>());
        for (ItemStack stack : stacks) {
            itemStash.get(player.getUniqueId()).remove(stack);
        }
    }

    /**
     * Gets the item at the specified index from the player's stash.
     * Will create the stash if the player does not have one.
     * Returns null if there is no item at the index.
     * @param player The {@link org.bukkit.entity.Player}'s stash to get the item from.
     * @param index The (0-indexed) index of the item to get.
     * @return The {@link org.bukkit.inventory.ItemStack} at the specified index or null.
     */
    public static ItemStack getFromStash(Player player, int index) {
        if (!itemStash.containsKey(player.getUniqueId()))
            itemStash.put(player.getUniqueId(), new ArrayList<>());
        return itemStash.get(player.getUniqueId()).get(index);

    }

    /**
     * Gets all twists the player is bound to.
     * @param player The player to check.
     * @return A {@link java.util.List} of {@link me.neo.randomtwistcore.api.twists.Twist}s the player is bound to.
     */
    public static List<Twist> getAllBoundTwists(Player player) {
        List<Twist> bound = new ArrayList<>();
        for (Twist twist : twists) {
            if (twist.isBound(player))
                bound.add(twist);
        }
        return bound;
    }

    /**
     * Gets all Non ItemTwists the player is bound to.
     * @param player The player to check.
     * @return A {@link java.util.List} of {@link me.neo.randomtwistcore.api.twists.Twist}s the player is bound to.
     */
    public static List<Twist> getBoundAbilityTwists(Player player) {
        List<Twist> bound = new ArrayList<>();
        for (Twist twist : twists) {
            if (twist instanceof ItemTwist)
                continue;
            if (twist.isBound(player))
                bound.add(twist);
        }
        return bound;
    }

    /**
     * Gets all the ItemTwists the player is bound to.
     * @param player The player to check.
     * @return A {@link java.util.List} of {@link me.neo.randomtwistcore.api.twists.ItemTwist}s the player is bound to.
     */
    public static List<ItemTwist> getBoundItemTwists(Player player) {
        List<ItemTwist> bound = new ArrayList<>();
        for (Twist twist : twists) {
            if (twist instanceof ItemTwist it && it.isBound(player))
                bound.add(it);
        }
        return bound;
    }

    /**
     * Gets all the soulbound ItemTwists the player is bound to.
     * @param player The player to check.
     * @return A {@link java.util.List} of {@link me.neo.randomtwistcore.api.twists.ItemTwist}s the player is bound to that are soulbound.
     */
    public static List<ItemTwist> getSoulboundItemTwists(Player player) {
        List<ItemTwist> bound = new ArrayList<>();
        for (Twist twist : twists) {
            if (twist instanceof ItemTwist it && it.isBound(player) && it.isSoulbound())
                bound.add(it);
        }
        return bound;
    }

    /**
     * Calls {@link RegisterTwistEvent} and tries to register a twist.
     * If the event was cancelled the twist will not be registered.
     * If a twist was registered after the plugin was enabled the twist will not show up inside the command pool.
     * The functionality, however, will still persist.
     * @param twist The {@link me.neo.randomtwistcore.api.twists.Twist} to register.
     * @param plugin The {@link org.bukkit.plugin.Plugin} that this twist is being registered from.
     * @return True if it registered. False otherwise.
     */
    public static boolean tryRegister(Twist twist, Plugin plugin) {
        if (twists.contains(twist)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Register]: " + twist.name + " is already registered.");

            return false;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Register Event]: Calling Register Event for: " + twist.name + "...");
        RegisterTwistEvent event = new RegisterTwistEvent(twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Register Event]: " + "Registering Event for: " + twist.name + " has been cancelled!");
            return false;
        } else {
            twists.add(twist);
            twistNames.add(twist.name);
            twist.onRegister();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Register]: " + "Registering Twist: " + twist.name + "...");
            Bukkit.getPluginManager().registerEvents(twist, plugin);
            return true;
        }
    }

    /**
     * Calls {@link UnregisterTwistEvent} and tries to unregister a twist.
     * If the event was cancelled the twist will not be unregistered.
     * This will not remove the twist from the command pool as those are statically generated upon enabling the plugin.
     * Unregistering a twist will automatically unbind all players from the twist but will not call PlayerUnbindEvent as the twist has been removed.
     * This will call UnregisterTwistEvent
     * @param twist The {@link me.neo.randomtwistcore.api.twists.Twist} to unregister.
     * @return True if the twist was unregistered. False otherwise.
     */
    public static boolean tryUnregister(Twist twist) {
        if (!twists.contains(twist)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Unregistering]: " + twist.name + " is not registered.");

            return false;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unregistering]: Calling Unregister event for: " + twist.name + "...");
        UnregisterTwistEvent event = new UnregisterTwistEvent(twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Unregistering]: " + "Unregistering event for: " + twist.name + " has been cancelled!");
            return false;
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unregistering]: unbinding bound players...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (twist.isBound(player))
                    Twist.tryUnbind(player, twist, true);
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unregistering]: " + twist.name + "...");
            twists.remove(twist);
            twistNames.remove(twist.name);
            twist.onUnregister();
            return true;
        }


    }

    /**
     * Calls {@link PlayerBindTwistEvent} and tries to bind a player to a twist.
     * If the event was cancelled the player will not be bound.
     * @param player The {@link org.bukkit.entity.Player} to bind.
     * @param twist The {@link me.neo.randomtwistcore.api.twists.Twist} to bind the player to.
     * @param silent Whether to send a message to the player if the binding was successful.
     * @return True if the player was successfully bound. False otherwise.
     */
    public static boolean tryBind(Player player, Twist twist, boolean silent) {
        if (twist == null) {
            Bukkit.getLogger().severe("Internal error occurred while binding player, twist is null.");
            return false;
        }


        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Binding]: Calling Bind event for: " + player.getName() + " to: " + twist.name + "...");
        PlayerBindTwistEvent event = new PlayerBindTwistEvent(player, twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Binding]: Binding event for: " + player.getName() + " to: " + twist.name + " was cancelled!");
            return false;
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Binding]: " + player.getName() + " to: " + twist.name + "...");

            if (twist.isBound(player)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Binding]: " + player.getName() + " is already bound to: " + twist.name + ".");
                return false;
            }

            twist.boundPlayers.add(player.getUniqueId());
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Bound]: " + player.getName() + " to: " + twist.name + "!");
            if (!silent) {
                player.sendMessage(ChatColor.GREEN + "Got new twist: " + twist.name + "!");
                player.sendMessage(ChatColor.AQUA + twist.description);
            }
            if (twist instanceof ItemTwist itemTwist) {
                if (!itemStash.containsKey(player.getUniqueId()))
                    itemStash.put(player.getUniqueId(), new ArrayList<>());
                if (itemTwist.getCustomRecipe() != null) {
                    player.discoverRecipe(itemTwist.getCustomRecipe().getKey());
                    if (!silent)
                        player.sendMessage(ChatColor.GREEN + "You have discovered a new recipe for: " + twist.name + "!");
                }

                if (itemTwist.isItemGrantedOnBind()) {
                    if (player.getInventory().firstEmpty() == -1) {
                        if (!itemStash.get(player.getUniqueId()).contains(itemTwist.getCustomItem()))
                            itemStash.get(player.getUniqueId()).add(itemTwist.getCustomItem());
                        doAddStashText(player);
                    } else {
                        player.getInventory().addItem(itemTwist.getCustomItem());
                    }
                }
            }

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 4f);
            return true;
        }
    }


    /**
     * Calls {@link PlayerBindTwistEvent} and tries to unbind a player from a twist.
     * If the event was cancelled the player will not be unbound.
     * @param player The {@link org.bukkit.entity.Player} to unbind.
     * @param twist The {@link me.neo.randomtwistcore.api.twists.Twist} to unbind the player from.
     * @param unregistered If the players were unbound because a twist was unregistered.
     * @return True if the player was successfully unbound. False otherwise.
     */
    public static boolean tryUnbind(Player player, Twist twist, boolean unregistered) {
        if (twist == null) {
            Bukkit.getLogger().severe("Internal error occurred while unbinding player, twist is null.");
            return false;
        }

        if (unregistered) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Unbound]: " + player.getName() + " from: " + twist.name + "!" + " This was because " + twist.name + " is going to be unregistered");
            player.sendMessage(ChatColor.RED + "Lost twist: " + twist.name + "!" + " This was because " + twist.name + " is going to be unregistered");
            if (twist instanceof ItemTwist itemTwist)
                if (itemTwist.getCustomRecipe() != null)
                    player.undiscoverRecipe(itemTwist.getCustomRecipe().getKey());

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
            return true;

        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unbinding]: Calling Unbind event for: " + player.getName() + " from: " + twist.name + "...");
            PlayerUnbindTwistEvent event = new PlayerUnbindTwistEvent(player, twist);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unbinding]: Unbind event for: " + player.getName() + " from: " + twist.name + " was cancelled!");

                return false;
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[RTC | Unbinding]: " + player.getName() + " from: " + twist.name + "...");

                if (!twist.isBound(player)) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RTC | Unbinding]: " + player.getName() + " is not bound to: " + twist.name + ".");
                    return false;
                }

                twist.boundPlayers.remove(player.getUniqueId());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Unbound]: " + player.getName() + " from: " + twist.name + "!");
                player.sendMessage(ChatColor.RED + "Lost twist: " + twist.name + "!");

                if (twist instanceof ItemTwist itemTwist)
                    if (itemTwist.getCustomRecipe() != null)
                        player.undiscoverRecipe(itemTwist.getCustomRecipe().getKey());

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
                return true;
            }
        }
    }

    /**
     * Called when a twist is registered.
     * Mainly used for debugging.
     */
    protected void onRegister() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Registered]: " + name + "!");
    }

    /**
     * Called when a twist is unregistered.
     * Mainly used for debugging.
     */
    protected void onUnregister() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RTC | Unregistered]: " + name + "!");
    }

    public static void doAddStashText(Player player) {
        player.sendMessage(ChatColor.GREEN + "Your inventory is full so the item was added to your stash!");
        player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + itemStash.get(player.getUniqueId()).size() + " in your stash!");

        TextComponent filler = new TextComponent("§eClick ");

        TextComponent clickable = new TextComponent("§6§lHERE");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ct"));

        TextComponent filler1 = new TextComponent(" §eto claim the items, or run /ct.");

        player.spigot().sendMessage(filler, clickable, filler1);
    }


    public static void doRemoveStashText(Player player, int itemsClaimed) {
        player.sendMessage(ChatColor.RED + "Your inventory was full, but you claimed " + ChatColor.RED + itemsClaimed + " items!");
        player.sendMessage(ChatColor.YELLOW + "You still have " + ChatColor.RED + itemStash.get(player.getUniqueId()).size() + " items left!");

        TextComponent filler = new TextComponent("§eClick ");

        TextComponent clickable = new TextComponent("§6§lHERE");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ct"));

        TextComponent filler1 = new TextComponent(" §eto claim the items, or run /ct.");

        player.spigot().sendMessage(filler, clickable, filler1);
    }
}
