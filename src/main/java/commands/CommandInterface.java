package commands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public interface CommandInterface {
    ObjectMapper MAPPER = new ObjectMapper();

    /**
     * This will be overridden in each command class
     * and will execute a specific command.
     */
    void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command);
}
