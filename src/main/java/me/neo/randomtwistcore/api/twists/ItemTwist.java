package me.neo.randomtwistcore.api.twists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * An extension of {@link me.neo.randomtwistcore.api.twists.Twist} that is used to make twists containing a custom item.
 */
public abstract class ItemTwist extends Twist {
    public ItemTwist(String name, String description, int id, boolean grantItemOnBind, boolean soulbound) {
        super(name, description, id);
        customItem = buildCustomItem();
        customRecipe = buildShapedRecipe();
        Bukkit.addRecipe(customRecipe);
        this.grantItemOnBind = grantItemOnBind;
        this.soulbound = soulbound;
    }

    protected boolean soulbound;

    public boolean isSoulbound() {
        return soulbound;
    }

    /**
     * A boolean representing if the {@link #customItem} is automatically granted when a {@link org.bukkit.entity.Player} is bound to this twist.
     */
    protected boolean grantItemOnBind;

    /**
     * Returns if the item is granted when the player is bound.
     * @return If the item is granted.
     */
    public boolean isItemGrantedOnBind() {
        return grantItemOnBind;
    }

    /**
     * An {@link org.bukkit.inventory.ItemStack} that represents the item that this twist revolves around.
     */
    protected ItemStack customItem;
    /**
     * Returns the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @return The custom item.
     */
    public ItemStack getCustomItem() {
        return customItem;
    }

    /**
     * A {@link org.bukkit.inventory.ShapedRecipe} that represents the recipe of the {@link #customItem}.
     */
    protected ShapedRecipe customRecipe;
    /**
     * Returns the {@link org.bukkit.inventory.ShapedRecipe} used to craft the custom item.
     * @return The recipe.
     */
    public ShapedRecipe getCustomRecipe() {
        return customRecipe;
    }

    /**
     * This method is used to define the {@link #customItem} in an extending class.
     * @return The {@link org.bukkit.inventory.ItemStack} that is the custom item.
     */
    public abstract ItemStack buildCustomItem();

    /**
     * This method is used to define the {@link #customItem} in an extending class.
     * @return The {@link org.bukkit.inventory.ShapedRecipe} that is the custom item.
     */
    public abstract ShapedRecipe buildShapedRecipe();

    private boolean isCustomItem(ItemStack stack) {
        return stack.equals(customItem);
    }



    @EventHandler
    public void onCraft(CraftItemEvent ev) {
        if (!isCustomItem(ev.getRecipe().getResult()))
            return;
        Player player = (Player) ev.getWhoClicked();
        if (!isBound(player)) {
            ItemStack no = new ItemStack(Material.BARRIER);
            ItemMeta meta = no.getItemMeta();
            meta.setDisplayName("\u200B" + ChatColor.DARK_RED + "You do not have access to this recipe");
            meta.setLore(Arrays.asList(
                    ChatColor.DARK_RED + "You do not have the required twist",
                    ChatColor.DARK_RED + "to craft this item."
            ));
            no.setItemMeta(meta);
            ev.getInventory().setResult(no);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            ev.setCancelled(true);
        }
    }


    /**
     * Checks if the given {@link org.bukkit.inventory.ItemStack} is the custom item.
     * @param stack The {@link org.bukkit.inventory.ItemStack} to compare to
     * @return True if it is the custom item. False otherwise.
     */
    protected boolean check(ItemStack stack) {
        return stack.isSimilar(customItem);
    }

    /**
     * Checks if the display name is the custom item's display name.
     * This method is less reliable than the ItemStack method.
     * Only use this method if the properties of the {@link org.bukkit.inventory.ItemStack} are compromised.
     * @param displayName The display name of the item to check.
     * @return True if it is the custom item. False otherwise.
     */
    protected boolean check(String displayName) {
        return displayName.equals(customItem.getItemMeta().getDisplayName());
    }
}
