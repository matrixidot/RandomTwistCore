package me.neo.randomtwistcore.api.twists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public abstract class ItemTwist extends Twist {
    public ItemTwist(String name, String description, int id, boolean grantItemOnBind) {
        super(name, description, id);
        customItem = buildCustomItem();
        customRecipe = buildShapedRecipe();
        Bukkit.addRecipe(customRecipe);
        this.grantItemOnBind = grantItemOnBind;
    }

    public boolean grantItemOnBind;

    protected ItemStack customItem;

    public ItemStack getCustomItem() {
        return customItem;
    }

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

    /**
     * Checks if the item is the custom item.
     * @param stack The item stack to compare to
     * @return True if it is the custom item. False otherwise.
     */
    protected boolean check(ItemStack stack) {
        return stack.isSimilar(customItem);
    }

    /**
     * Checks if the display name is the custom item's display name.
     * This method is less reliable than the ItemStack method.
     * Only use this method if the properties of the itemstack were comprimised.
     * @param displayName The display name of the item to check.
     * @return True if it is the custom item. False otherwise.
     */
    protected boolean check(String displayName) {
        return displayName.equals(customItem.getItemMeta().getDisplayName());
    }
}
