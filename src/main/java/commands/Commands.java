package commands;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commands {
    final private int timestamp;
    final private String command;

    public Commands(final int timestamp, final String command) {
        this.timestamp = timestamp;
        this.command = command;
    }
}