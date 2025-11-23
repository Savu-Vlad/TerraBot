package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import fileio.CommandInput;
import map.Map;
import robot.Robot;

public class GetEnergyStatus implements CommandInterface {

    /**
     * Method that returns the energy of the robot and writes in the outputs
     * the aftermath of the command execution.
     * */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "getEnergyStatus");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            result.put("message", "TerraBot has " + robot.getEnergy() + " energy points left.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
