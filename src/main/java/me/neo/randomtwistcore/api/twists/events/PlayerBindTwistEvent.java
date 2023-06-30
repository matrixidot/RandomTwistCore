package me.neo.randomtwistcore.api.twists.events;

import me.neo.randomtwistcore.api.twists.Twist;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fired immediately before a {@link org.bukkit.entity.Player} is bound to a {@link me.neo.randomtwistcore.api.twists.Twist}.
 * Event can be cancelled
 */
public class PlayerBindTwistEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;

    private final Player player;
    private final Twist twist;

    public PlayerBindTwistEvent(Player player, Twist twist) {
        cancelled = false;
        this.player = player;
        this.twist = twist;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Returns the {@link org.bukkit.entity.Player} being bound.
     * @return Player being bound.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the {@link me.neo.randomtwistcore.api.twists.Twist} the player is being bound to.
     * @return Twist player being bound to.
     */
    public Twist getTwist() {
        return twist;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you do not want the player to be bound.
     * @return If the event is cancelled or not.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be executed in the server, but will still pass to other plugins.
     * Cancelling this event will prevent the player from being bound to the specified twist.
     * @param cancel True if you want to cancel this event.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
