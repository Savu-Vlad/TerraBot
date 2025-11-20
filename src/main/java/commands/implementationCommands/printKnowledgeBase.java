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

public class printKnowledgeBase implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("command", "printKnowledgeBase");
        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            ArrayNode knowledgeBaseArray = MAPPER.createArrayNode();

            for (int i = 0; i < robot.getKnowledgeBase().size(); i++) {
                ObjectNode knowledgeBaseNode = MAPPER.createObjectNode();
                knowledgeBaseNode.put("topic", robot.getKnowledgeBase().get(i).getTopic());
                ArrayNode factsArray = MAPPER.createArrayNode();

                for (String fact : robot.getKnowledgeBase().get(i).getFacts()) {
                    factsArray.add(fact);
                }

                knowledgeBaseNode.set("facts", factsArray);
                knowledgeBaseArray.add(knowledgeBaseNode);
            }

            result.set("output", knowledgeBaseArray);
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}