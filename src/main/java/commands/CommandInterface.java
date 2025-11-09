package commands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import map.Map;
import robot.Robot;

public interface CommandInterface {
    void execute(Robot robot, Map map, ArrayNode output, int timestamp);
}