package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public class ScanObject implements CommandInterface {
    private final int energyCostForScanObject = 7;

    /**
     *The scanned object is added to the robot's inventory and in the database inventory.
     * After an object is scanned, the interactions start to happen with that object
     * also the scanned animal starts to move and eat the plants, other animals if carnivore
     * or parasite and drinks water.
     */

    @Override
    public void execute(final Robot robot, final Map map, final ArrayNode output,
                        final int timestamp, final CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "scanObject");
        String errorMessage = robot.returnBasicErrors();

        if (errorMessage != null) {
            result.put("message", errorMessage);
        } else if (robot.getEnergy() - energyCostForScanObject < 0) {
            result.put("message", "ERROR: Not enough energy to perform action");
        } else {
            int x = robot.getX();
            int y = robot.getY();

            if (command.getColor().equals("none") && command.getSmell().equals("none")
                    &&
                    command.getSound().equals("none")) {
                if (map.getMapCell(x, y).getWater() != null) {
                    map.getMapCell(x, y).getWater().setScannedByRobot(true);
                    map.getMapCell(x, y).getWater().setX(x);
                    map.getMapCell(x, y).getWater().setY(y);
                    map.getMapCell(x,
                            y).getWater().
                            setTimestampAtWhichItWasScanned(command.getTimestamp());
                    robot.getInventory().add(map.getMapCell(x, y).getWater());
                    robot.getDatabaseInventory().add(map.getMapCell(x, y).getWater());

                    result.put("message", "The scanned object is water.");
                    robot.setEnergy(robot.getEnergy() - energyCostForScanObject);

                } else {
                    result.put("message", "ERROR: Object not found. Cannot perform action");
                }
            } else if (command.getSound().equals("none")) {
                if (map.getMapCell(x, y).getPlant() != null) {
                    map.getMapCell(x, y).getPlant().setScannedByRobot(true);
                    map.getMapCell(x, y).getPlant().setX(x);
                    map.getMapCell(x, y).getPlant().setY(y);
                    map.getMapCell(x,
                            y).getPlant().
                            setTimestampAtWhichItWasScanned(command.getTimestamp());
                    robot.getInventory().add(map.getMapCell(x, y).getPlant());
                    robot.getDatabaseInventory().add(map.getMapCell(x,
                            y).getPlant());
                    result.put("message", "The scanned object is a plant.");
                    robot.setEnergy(robot.getEnergy() - energyCostForScanObject);

                } else {
                    result.put("message", "ERROR: Object not found. Cannot perform action");
                }
            } else {
                if (map.getMapCell(x, y).getAnimal() != null) {
                    map.getMapCell(x, y).getAnimal().setScannedByRobot(true);
                    map.getMapCell(x, y).getAnimal().setX(x);
                    map.getMapCell(x, y).getAnimal().setY(y);
                    map.getMapCell(x,
                            y).getAnimal().
                            setTimestampAtWhichItWasScanned(command.getTimestamp());
                    robot.getInventory().add(map.getMapCell(x,
                            y).getAnimal());
                    robot.getDatabaseInventory().add(map.getMapCell(x,
                            y).getAnimal());
                    result.put("message", "The scanned object is an animal.");
                    robot.setEnergy(robot.getEnergy() - energyCostForScanObject);

                } else {
                    result.put("message", "ERROR: Object not found. Cannot perform action");
                }
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}
