package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import entities.Water;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public class rechargeBattery implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "rechargeBattery");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        }else {
            robot.setEnergy(robot.getEnergy() + command.getTimeToCharge());
            robot.setTimeAtWhichRechargingIsDone(command.getTimeToCharge() + timestamp);
            result.put("message", "Robot battery is charging.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}