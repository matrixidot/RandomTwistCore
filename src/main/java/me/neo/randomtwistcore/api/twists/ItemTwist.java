package me.neo.randomtwistcore.api.twists;

import me.neo.randomtwistcore.RTCAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
/**
 * An extension of {@link me.neo.randomtwistcore.api.twists.Twist} that is used to make twists containing a custom item.
 */
@SuppressWarnings("unused")
public abstract class ItemTwist extends Twist {
    public ItemTwist(String name, String description, int id, boolean grantItemOnBind, boolean soulbound) {
        super(name, description, id);
        customItem = configureItem();
        customRecipe = buildShapedRecipe(customItem);
        Bukkit.addRecipe(customRecipe);
        this.grantItemOnBind = grantItemOnBind;
        this.soulbound = soulbound;
    }

    private final boolean soulbound;
    public boolean isSoulbound() {
        return soulbound;
    }

    private final boolean grantItemOnBind;
    public boolean isItemGrantedOnBind() {
        return grantItemOnBind;
    }


    private ItemStack customItem;
    public ItemStack getCustomItem() {
        return customItem;
    }


    private ShapedRecipe customRecipe;
    public ShapedRecipe getCustomRecipe() {
        return customRecipe;
    }


    public abstract ItemStack buildCustomItem();
    private ItemStack configureItem() {
        ItemStack customItem = buildCustomItem();
        ItemMeta meta = customItem.getItemMeta();
        meta.getPersistentDataContainer().set(RTCAPI.internalKey, PersistentDataType.STRING, RTCAPI.internalString + description + name + id);
        customItem.setItemMeta(meta);
        return customItem;
    }
    public abstract ShapedRecipe buildShapedRecipe(ItemStack customItem);

    public boolean check(ItemStack stack) {
        if (stack == null)
            return false;
        if (!stack.hasItemMeta())
            return false;
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        if (!PDC.has(RTCAPI.internalKey, PersistentDataType.STRING))
            return false;
        return PDC.get(RTCAPI.internalKey, PersistentDataType.STRING).equals(RTCAPI.internalString + description + name + id);
    }



    @EventHandler
    public void onCraft(CraftItemEvent ev) {
        if (!check(ev.getRecipe().getResult()))
            return;

        Player player = (Player) ev.getWhoClicked();
        if (!isBound(player)) {
            ItemStack no = new ItemStack(Material.BARRIER);
            ItemMeta meta = no.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_RED + "Recipe Locked");
            meta.setLore(List.of(
                    ChatColor.DARK_RED + "You do not have the required twist to craft this item"
            ));
            no.setItemMeta(meta);
            ev.getInventory().setResult(no);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            ev.setCancelled(true);
        }
    }


    public abstract void rightClickAbility(Player player, ItemStack stack, boolean isSneaking, EquipmentSlot hand);
    public abstract void leftClickAbility(Player player, ItemStack stack, boolean isSneaking, EquipmentSlot hand);
    public abstract void rightClickBlockAbility(Player player, ItemStack stack, boolean isSneaking, EquipmentSlot hand, Block clickedBlock, BlockFace clickedFace);
    public abstract void leftClickBlockAbility(Player player, ItemStack stack, boolean isSneaking, EquipmentSlot hand, Block clickedBlock, BlockFace clickedFace);
    public abstract void rightClickEntityAbility(Player player, ItemStack stack, boolean isSneaking, EquipmentSlot hand, Entity clickedEntity);

    @EventHandler
    public void onInteract(PlayerInteractEvent ev) {
        if (!ev.hasItem())
            return;
        if (!check(ev.getItem()))
            return;

        switch (ev.getAction()) {
            case RIGHT_CLICK_AIR -> rightClickAbility(ev.getPlayer(), ev.getItem(), ev.getPlayer().isSneaking(), ev.getHand());
            case LEFT_CLICK_AIR -> leftClickAbility(ev.getPlayer(), ev.getItem(), ev.getPlayer().isSneaking(), ev.getHand());
            case RIGHT_CLICK_BLOCK -> rightClickBlockAbility(ev.getPlayer(), ev.getItem(), ev.getPlayer().isSneaking(), ev.getHand(), ev.getClickedBlock(), ev.getBlockFace());
            case LEFT_CLICK_BLOCK -> leftClickBlockAbility(ev.getPlayer(), ev.getItem(), ev.getPlayer().isSneaking(), ev.getHand(), ev.getClickedBlock(), ev.getBlockFace());
            default -> {}
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent ev) {
        if (!check(ev.getPlayer().getInventory().getItem(ev.getHand())))
            return;
        rightClickEntityAbility(ev.getPlayer(), ev.getPlayer().getInventory().getItem(ev.getHand()), ev.getPlayer().isSneaking(), ev.getHand(), ev.getRightClicked());
    }
}
