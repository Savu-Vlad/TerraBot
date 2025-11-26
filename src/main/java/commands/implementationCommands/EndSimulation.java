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
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
        } else {
            robot.setSimulationStarted(false);
            result.put("message", "Simulation has ended.");
        }
        result.put("timestamp", timestamp);
        output.add(result);
    }
}
