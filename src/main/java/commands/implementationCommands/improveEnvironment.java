package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import entities.Air;
import entities.Plant;
import map.Map;
import robot.KnowledgeBase;
import robot.Robot;
import fileio.CommandInput;
import entities.Entity;

public class improveEnvironment implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "improveEnvironment");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            boolean foundScannedEntity = false;

            for (Entity entity : robot.getDatabaseInventory()) {
                if (entity.getName().contains(command.getName()) && entity.getType().contains(command.getType())) {
                    foundScannedEntity = true;
                    break;
                }
            }

            boolean foundKnowledgeBaseFact = false;

            for (KnowledgeBase knowledgeBase : robot.getKnowledgeBase()) {
                if (knowledgeBase.getTopic().equals(command.getName())) {
                    foundKnowledgeBaseFact = true;
                    break;
                }
            }

            if (!foundScannedEntity) {
                result.put("message", "ERROR: Subject not yet saved. Cannot perform action");
            } else if (!foundKnowledgeBaseFact) {
                result.put("message", "ERROR: Fact not yet saved. Cannot perform action");
            }

            if (robot.getEnergy() - 10 < 0) {
                result.put("message", "ERROR: Not enough battery left. Cannot perform action");
            } else {
                if (command.getImprovementType().equals("plantVegetation")) {
                    robot.setEnergy(robot.getEnergy() - 10);
                    result.put("message", "The " + command.getName() + " was planted successfully.");
                    Map.MapCell cellToBeImproved = map.getMapCell(robot.getX(), robot.getY());
                    Air entityToBeImproved = cellToBeImproved.getAir();
                    entityToBeImproved.setOxygenLevel(entityToBeImproved.getOxygenLevel() + 0.3);
                    entityToBeImproved.setAirQuality(entityToBeImproved.calculateAirQuality());
                    //asta e bine dar trebuie sa recalculez calitatea !!
                    //ar trebui sa modific print env condition ca sa mi calculeze calitatea aerului la momentul respectiv

                    for (Entity entity : robot.getInventory()) {
                        if (entity.getName().equals(command.getName()) && entity.getType().equals(command.getType())) {
                            robot.getDatabaseInventory().remove(entity);
                            break;
                        }
                    }
                }
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}