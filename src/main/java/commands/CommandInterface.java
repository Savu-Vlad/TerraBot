package commands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public interface CommandInterface {
    static final ObjectMapper MAPPER = new ObjectMapper();
    void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command);
}