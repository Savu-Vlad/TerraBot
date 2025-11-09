package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;

public class printEnvConditions implements CommandInterface {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp) {
        ObjectNode result = MAPPER.createObjectNode();
        ObjectNode envOutput = MAPPER.createObjectNode();

        Map.MapCell currentCell = map.getMapCell(robot.getX(), robot.getY());

        if (!robot.isSimulationStarted()) {
            result.put("error", "ERROR: Simulation not started. Cannot perform action");
        } else {
            if (currentCell.getSoil() != null) {
                envOutput.set("soil", entityToJsonNode(currentCell.getSoil()));
            }

            if (currentCell.getPlant() != null) {
                envOutput.set("plant", entityToJsonNode(currentCell.getPlant()));
            }

            if (currentCell.getAnimal() != null) {
                envOutput.set("animal", entityToJsonNode(currentCell.getAnimal()));
            }

            if (currentCell.getWater() != null) {
                envOutput.set("water", entityToJsonNode(currentCell.getWater()));
            }

            if (currentCell.getAir() != null) {
                envOutput.set("air", entityToJsonNode(currentCell.getAir()));
            }

            result.put("Command", "printEnvConditions");
            result.set("output", envOutput);
            result.put("timestamp", timestamp);
            output.add(result);
        }
    }

    private ObjectNode entityToJsonNode(Object entity) {
        return MAPPER.valueToTree(entity);
    }
}