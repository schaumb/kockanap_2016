package communication;

/**
 * Created by qqcs on 25/11/16.
 */
public class Status {
    private final int playerId;
    private final int timeLeft;

    private Status(int playerId, int timeLeft) {
        this.playerId = playerId;
        this.timeLeft = timeLeft;
    }

    public static Status create(byte[] data) {
        return new Status(data[5], data[6]);
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    @Override
    public String toString() {
        return "Status{" +
                "playerId=" + playerId +
                ", timeLeft=" + timeLeft +
                '}';
    }
}
