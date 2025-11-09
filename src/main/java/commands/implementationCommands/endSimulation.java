package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;

public class endSimulation implements CommandInterface {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("Command", "endSimulation");

        if (!robot.isSimulationStarted()) {
            result.put("error", "ERROR: Simulation not started. Cannot perform action");
        } else {
            robot.setSimulationStarted(false);
            result.put("message", "Simulation has ended.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}