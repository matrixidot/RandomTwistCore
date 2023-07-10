package me.neo.randomtwistcore.util;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Cooldown {
    public static List<Cooldown> cooldowns = new ArrayList<>();
    private final Player player;

    private final String identifier;

    private Instant time;

    private Cooldown(Player player, String identifier, Duration duration) {
        this.player = player;
        this.identifier = identifier;
        this.time = Instant.now().plus(duration);
    }

    public static void newCooldown(Player player, String identifier, Duration duration) {
        cooldowns.add(new Cooldown(player, identifier, duration));
    }

    public static Cooldown getCooldown(Player player, String identifier) {
        for (Cooldown cooldown : cooldowns) {
            if (cooldown.player.equals(player) && cooldown.identifier.equals(identifier)) {
                return cooldown;
            }
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Instant getTime() {
        return time;
    }

    public void updateCooldown(Duration duration) {
        time = Instant.now().plus(duration);
    }

    public boolean hasCooldown() {
        return Instant.now().isBefore(time);
    }

    public Long getRemainingCooldown() {
        if (time != null && !hasCooldown()) {
            return Duration.between(Instant.now(), time).getSeconds();
        } else {
            return 0L;
        }
    }

}
