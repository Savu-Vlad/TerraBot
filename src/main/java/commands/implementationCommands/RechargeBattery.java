package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public class RechargeBattery implements CommandInterface {

    /**
     * Method that recharges the battery of the robot, all the interactions happen
     * in the time that the robot is charging
     * also prints the result of the command execution in the output.
     * */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "rechargeBattery");
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
        } else {
            robot.setEnergy(robot.getEnergy() + command.getTimeToCharge());
            robot.setTimeAtWhichRechargingIsDone(command.getTimeToCharge() + timestamp);
            result.put("message", "Robot battery is charging.");

            for (int i = timestamp + 1;
                 i < timestamp + robot.getTimeAtWhichRechargingIsDone();
                 i++) {
                map.updateMapWithScan(robot, i);
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
