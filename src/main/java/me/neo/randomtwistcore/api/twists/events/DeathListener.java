package me.neo.randomtwistcore.api.twists.events;

import me.neo.randomtwistcore.api.twists.ItemTwist;
import me.neo.randomtwistcore.api.twists.Twist;
import me.neo.randomtwistcore.util.Cooldown;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Duration;
import java.util.List;

public class DeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent ev) {
        if (Boolean.TRUE.equals(ev.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)))
            return;
        List<ItemTwist> twists = Twist.getReclaimableBoundItemTwists(ev.getEntity());
        if (twists.isEmpty())
            return;
        Twist.doReclaimText(ev.getEntity());
        Cooldown.getCooldown(ev.getEntity(), "RTC.reclaimCommand.CooldownFeature.Internal").updateCooldown(Duration.ZERO);
    }
}
