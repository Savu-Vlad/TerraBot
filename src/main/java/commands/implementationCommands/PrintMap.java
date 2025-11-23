package commands.implementationCommands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PrintMap implements CommandInterface {

    /**
     * Method that prints all the cells of the map and their entities count
     * and qualities.
     * */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
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


