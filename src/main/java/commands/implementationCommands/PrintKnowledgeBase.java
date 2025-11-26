package commands.implementationCommands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PrintKnowledgeBase implements CommandInterface {

    /**
     * Method that prints the facts that the robot has learned
     * */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("command", "printKnowledgeBase");
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
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
