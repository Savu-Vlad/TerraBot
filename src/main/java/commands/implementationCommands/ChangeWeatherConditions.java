package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import entities.Air;
import fileio.CommandInput;
import map.Map;
import robot.Robot;

public class ChangeWeatherConditions implements CommandInterface {

    /**
     * Method that changes the weather conditions
     * by calling the updateMapWithChangeWeatherCondition
     * from the Map class.
     * outputs the result in the output array.
     * */
    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "changeWeatherConditions");
        String errorMessage = robot.returnBasicErrors();
        Air air = map.getMapCell(robot.getX(), robot.getY()).getAir();

        if (errorMessage != null) {
            result.put("message", errorMessage);
        } else {
            String status = air.changeWeatherConditions(command);

            if (status == null) {
                result.put("message", "The weather has changed.");
            } else {
                result.put("message", status);
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
