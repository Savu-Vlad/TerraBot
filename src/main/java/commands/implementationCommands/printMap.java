package commands.implementationCommands;
import commands.Commands;
import commands.CommandInterface;
import entities.Air;
import entities.Soil;
import entities.Water;
import map.Map;
import robot.Robot;
import fileio.CommandInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Section;

public class printMap implements CommandInterface {

    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("command", "printMap");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            ArrayNode cellArray = MAPPER.createArrayNode();

            for (int i = 0; i < map.getRowLength(); i++) {
                for (int j = 0; j < map.getColumnLength(); j++) {
                    ObjectNode cellNode = MAPPER.createObjectNode();
                    Map.MapCell currentCell = map.getMapCell(j, i);
                    ArrayNode section = MAPPER.createArrayNode();
                    section.add(j);
                    section.add(i);
                    cellNode.set("section", section);
                    cellNode.put("totalNrOfObjects", currentCell.getEntitiesCount());

                    cellNode.put("airQuality", currentCell.getAir().getAirQualityIndicator());
                    cellNode.put("soilQuality", currentCell.getSoil().getSoilQualityIndicator());
                    cellArray.add(cellNode);
                }
            }
            result.set("output", cellArray);
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }

    private ObjectNode entityToJsonNode(Object entity) {
        return MAPPER.valueToTree(entity);
    }
}


