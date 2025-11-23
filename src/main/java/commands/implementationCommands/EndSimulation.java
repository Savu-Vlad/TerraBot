package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import fileio.CommandInput;
import map.Map;
import robot.Robot;

public class EndSimulation implements CommandInterface {

    /**
     * Ends the simulation by setting the filed in the robot to false
     * and writes in the output the result of the command execution.
     * */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
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
