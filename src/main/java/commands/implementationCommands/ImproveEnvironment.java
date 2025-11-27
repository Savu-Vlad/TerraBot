package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import entities.Air;
import entities.Plant;
import map.Map;
import robot.KnowledgeBase;
import robot.Robot;
import fileio.CommandInput;
import entities.Entity;

public class ImproveEnvironment implements CommandInterface {
    private final int energyLevelCost = 10;

    /**
     * Method that improves the environment by looking through the robot's
     * database inventory and knowledge base to see if the item that is imposed
     * to use is present in both.
     * If it is, the robot improves the environment by planting vegetation, adding water etc.
     * */
    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "improveEnvironment");
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
        } else {
            boolean foundScannedEntity = false;

            for (Entity entity : robot.getDatabaseInventory()) {
                if (entity.getName().contains(command.getName())
                        &&
                        entity.getType().contains(command.getType())) {
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

            int energyLevelAtWhichTheRobotCannotPerformAction = 0;
            if (robot.getEnergy() - energyLevelCost
                    <
                    energyLevelAtWhichTheRobotCannotPerformAction) {
                result.put("message",
                        "ERROR: Not enough battery left. Cannot perform action");
                result.put("timestamp", timestamp);
                output.add(result);
                return;
            }

            if (!foundScannedEntity) {
                result.put("message",
                        "ERROR: Subject not yet saved. Cannot perform action");
                result.put("timestamp", timestamp);
                output.add(result);
                return;
            } else if (!foundKnowledgeBaseFact) {
                result.put("message",
                        "ERROR: Fact not yet saved. Cannot perform action");
                result.put("timestamp", timestamp);
                output.add(result);
                return;
            }


            if (command.getImprovementType().equals("plantVegetation")) {
                robot.setEnergy(robot.getEnergy() - energyLevelCost);
                result.put("message",
                        "The " + command.getName() + " was planted successfully.");
                robot.improveAirByPlantingPlant(map, command.getName(), command.getType());
            }

            if (command.getImprovementType().equals("increaseMoisture")) {
                robot.setEnergy(robot.getEnergy() - energyLevelCost);
                result.put("message",
                        "The moisture was successfully increased using " + command.getName());
                robot.improveSoilByAddingWaterBody(map, command.getName(), command.getType());
            }

            if (command.getImprovementType().equals("increaseHumidity")) {
                robot.setEnergy(robot.getEnergy() - energyLevelCost);
                result.put("message",
                        "The humidity was successfully increased using " + command.getName());
                robot.improveAirByAddingWaterBody(map, command.getName(), command.getType());
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
