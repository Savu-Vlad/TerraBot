package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public class StartSimulation implements CommandInterface {

    /**
     * Starts the simulation by setting the field in the robot to true
     * and writes in the output the result of the command execution.
     * */
    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "startSimulation");

        if (robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation already started. Cannot perform action");
        } else {
            robot.setSimulationStarted(true);
            result.put("message", "Simulation has started.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
