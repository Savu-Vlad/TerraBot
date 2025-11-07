package commands;

public class Commands {
    final private int timestamp;
    final private String command;

    public Commands(final int timestamp, final String command) {
        this.timestamp = timestamp;
        this.command = command;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getCommand() {
        return command;
    }
}