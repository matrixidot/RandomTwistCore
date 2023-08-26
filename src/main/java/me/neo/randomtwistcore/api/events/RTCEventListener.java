package me.neo.randomtwistcore.api.events;

import me.neo.randomtwistcore.api.twists.ItemTwist;
import me.neo.randomtwistcore.api.twists.NaturalTwist;
import me.neo.randomtwistcore.api.twists.Twist;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class RTCEventListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent ev) {
        Player player = ev.getPlayer();
        if (Boolean.TRUE.equals(ev.getPlayer().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)))
            return;
        List<ItemTwist> toReclaim = Twist.getSoulboundItemTwists(player);
        if (toReclaim.isEmpty())
            return;
        boolean doesStash = false;
        for (ItemTwist it : toReclaim) {
            if (player.getInventory().contains(it.getCustomItem()))
                continue;
            if (player.getInventory().firstEmpty() == -1) {
                Twist.addToStash(player, it.getCustomItem());
                doesStash = true;
            } else {
                player.getInventory().addItem(it.getCustomItem());
            }
        }
        if (doesStash)
            Twist.doAddStashText(player);
        player.sendMessage(ChatColor.RED + "You seem to have lost some twist-specific items!");
        player.sendMessage(ChatColor.GREEN + "Not to worry as some of the items may have been " + ChatColor.GOLD + ChatColor.BOLD + "SOULBOUND" + ChatColor.GREEN + " and returned to your inventory!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();
        for (Twist twist : Twist.twists) {
            if (twist instanceof NaturalTwist && !twist.isBound(player)) {
                Twist.tryBind(player, twist, true);
            }
        }
    }
}
