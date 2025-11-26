package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import map.Map;
import robot.KnowledgeBase;
import robot.Robot;
import fileio.CommandInput;
import entities.Entity;

import java.util.ArrayList;

public class LearnFact implements CommandInterface {
    private final int energyCostForLearningFact = 2;

    /**
     * Method that learns facts about a specific entity that is already in the robot's inventory.
     * The facts will be used in the improveEnvironment command.
     * */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();
        Entity foundEntity = null;

        result.put("command", "learnFact");
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
            result.put("timestamp", timestamp);
            output.add(result);
            return;
        } else if (robot.getEnergy() - energyCostForLearningFact <= 0) {
            result.put("message", "ERROR: Not enough battery left. Cannot perform action");
            result.put("timestamp", timestamp);
            output.add(result);
            return;
        } else {
            String component = command.getComponents();

            for (Entity entity : robot.getInventory()) {
                if (entity.getName().equals(component)) {
                    foundEntity = entity;
                }
            }
        }

        if (foundEntity == null) {
            result.put("message", "ERROR: Subject not yet saved. Cannot perform action");
        } else {
            robot.setEnergy(robot.getEnergy() - energyCostForLearningFact);
            ArrayList<Entity> inventory = robot.getInventory();
            ArrayList<KnowledgeBase> knowledgeBase = robot.getKnowledgeBase();

            boolean foundKnowledgeBase = false;

            for (KnowledgeBase base : knowledgeBase) {
                if (base.getTopic().equals(foundEntity.getName())) {
                    foundKnowledgeBase = true;
                    break;
                }
            }

            if (!foundKnowledgeBase) {
                KnowledgeBase newBase = new KnowledgeBase(foundEntity.getName());
                newBase.getFacts().add(command.getSubject());
                knowledgeBase.add(newBase);
            } else {
                for (KnowledgeBase base : knowledgeBase) {
                    if (base.getTopic().equals(foundEntity.getName())) {
                        base.getFacts().add(command.getSubject());
                        break;
                    }
                }
            }

            result.put("message", "The fact has been successfully saved in the database.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
