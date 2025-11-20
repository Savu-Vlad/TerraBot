package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import fileio.CommandInput;
import map.Map;
import robot.Robot;

public class changeWeatherConditions implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "changeWeatherConditions");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            map.updateMapWithChangeWeatherCondition(command);
            result.put("message", "The weather has changed.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}