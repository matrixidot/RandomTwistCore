package me.neo.randomtwistcore.api.twists.events;

import me.neo.randomtwistcore.api.twists.ItemTwist;
import me.neo.randomtwistcore.api.twists.Twist;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class RTCEventListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent ev) {
        Player player = ev.getPlayer();
        if (Boolean.TRUE.equals(ev.getPlayer().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)))
            return;
        List<ItemTwist> toReclaim = Twist.getSoulboundItemTwists(player);
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
        
    }
}
