package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import map.Map;
import robot.KnowledgeBase;
import robot.Robot;
import fileio.CommandInput;
import entities.Entity;

import java.util.ArrayList;

public class learnFact implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();
        Entity foundEntity = null;

        result.put("command", "learnFact");
        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else if (robot.getEnergy() - 2 < 0) {
            result.put("message", "ERROR: Not enough battery left. Cannot perform action");
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
            robot.setEnergy(robot.getEnergy() - 2);
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


            //aici sa adaug in knowledge base

            result.put("message", "The fact has been successfully saved in the database.");
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}