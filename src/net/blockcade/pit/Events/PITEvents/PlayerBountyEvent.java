package net.blockcade.pit.Events.PITEvents;

import net.blockcade.pit.Utils.PITPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBountyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public enum BountyReason {KILLSTREAK, CONTRACT}

    private PITPlayer player;
    private BountyReason reason;
    private double bounty;

    public PlayerBountyEvent(PITPlayer player, BountyReason reason){
        this.player=player;
        this.bounty=player.getBounty();
        this.reason=reason;
    }

    public PITPlayer getPlayer() {
        return player;
    }

    public BountyReason getReason() {
        return reason;
    }

    public double getBounty() {
        return bounty;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
