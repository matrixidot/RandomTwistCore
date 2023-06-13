package me.neo.randomtwistcore.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public abstract class ItemTwist extends Twist {
    public ItemTwist(String name, String description, int id) {
        super(name, description, id);
        customItem = buildCustomItem();
        customRecipe = buildShapedRecipe();
        Bukkit.addRecipe(customRecipe);

    }

    protected ItemStack customItem;

    protected ShapedRecipe customRecipe;

    public abstract ItemStack buildCustomItem();

    public abstract ShapedRecipe buildShapedRecipe();

    protected boolean isCustomItem(ItemStack stack) {
        return stack.equals(customItem);
    }

    @EventHandler
    public void onCraft(CraftItemEvent ev) {
        if (!isCustomItem(ev.getRecipe().getResult()))
            return;
        Player player = (Player) ev.getWhoClicked();
        if (!isBound(player)) {
            ev.getInventory().getResult().setType(Material.AIR);
            ev.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You have not unlocked this recipe.");
        }

    }

}
