package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import fileio.CommandInput;
import map.Map;
import robot.Robot;

public class endSimulation implements CommandInterface {

    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "endSimulation");

        if (!robot.isSimulationStarted()) {
            result.put("error", "ERROR: Simulation not started. Cannot perform action");
            result.put("timestamp", timestamp);
            output.add(result);
        } else {
            robot.setSimulationStarted(false);
            result.put("message", "Simulation has ended.");
            result.put("timestamp", timestamp);
            output.add(result);
        }
    }
}