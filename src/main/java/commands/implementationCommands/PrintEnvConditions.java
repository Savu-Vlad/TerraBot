package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import entities.Water;
import fileio.CommandInput;
import map.Map;
import robot.Robot;

public class PrintEnvConditions implements CommandInterface {

    /**
     * Method that prints the entities in the current cell in which the robot is located.
     * */
    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();
        ObjectNode envOutput = MAPPER.createObjectNode();

        Map.MapCell currentCell = map.getMapCell(robot.getX(), robot.getY());

        result.put("command", "printEnvConditions");
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
        } else {
            if (currentCell.getSoil() != null) {
                envOutput.set("soil", entityToJsonNode(currentCell.getSoil()));
            }

            if (currentCell.getPlant() != null) {
                envOutput.set("plants", entityToJsonNode(currentCell.getPlant()));
            }

            if (currentCell.getAnimal() != null) {
                envOutput.set("animals", entityToJsonNode(currentCell.getAnimal()));
            }

            if (currentCell.getWater() != null) {
                Water water = currentCell.getWater();
                ObjectNode waterNode = MAPPER.createObjectNode();
                waterNode.put("mass", water.getMass());
                waterNode.put("name", water.getName());
                waterNode.put("type", water.getType());
                envOutput.set("water", waterNode);
            }

            if (currentCell.getAir() != null) {
                envOutput.set("air", entityToJsonNode(currentCell.getAir()));
            }

            result.set("output", envOutput);
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }

    private ObjectNode entityToJsonNode(final Object entity) {
        return MAPPER.valueToTree(entity);
    }
}
