package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public class StartSimulation implements CommandInterface {

    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "startSimulation");

        if (robot.isSimulationStarted()) {
            result.put("error", "ERROR: Simulation already started. Cannot perform action");
        } else {
            robot.setSimulationStarted(true);
            result.put("message", "Simulation has started.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}