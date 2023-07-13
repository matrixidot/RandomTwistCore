package me.neo.randomtwistcore.api.twists.customevents;

import me.neo.randomtwistcore.api.twists.Twist;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


/**
 * Fired immediately before a {@link me.neo.randomtwistcore.api.twists.Twist} is unregistered.
 * Event can be cancelled
 */
public class UnregisterTwistEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;

    private final Twist twist;

    public UnregisterTwistEvent(Twist twist) {
        cancelled = false;
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
     * Returns the {@link me.neo.randomtwistcore.api.twists.Twist} being unregistered.
     * @return The twist being unregistered.
     */
    public Twist getTwist() {
        return twist;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you do not want the twist to be unregistered.
     * @return If the event is cancelled or not.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be executed in the server, but will still pass to other plugins.
     * Cancelling this event will prevent the twist from being unregistered.
     * This may cause unintended behaviour so be cautious when cancelling this event.
     * @param cancel True if you want to cancel this event.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
