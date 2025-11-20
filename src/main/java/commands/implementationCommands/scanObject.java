package commands.implementationCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Commands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;
import fileio.CommandInput;

public class scanObject implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "scanObject");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else if (robot.getEnergy() - 7 < 0) {
            result.put("message", "ERROR: Not enough battery left. Cannot perform action");
        } else {

            if (command.getColor().equals("none") && command.getSmell().equals("none") && command.getSound().equals("none")) {
                if (map.getMapCell(robot.getX(), robot.getY()).getWater() != null) {
                    map.getMapCell(robot.getX(), robot.getY()).getWater().setScannedByRobot(true);
                    map.getMapCell(robot.getX(), robot.getY()).getWater().setX(robot.getX());
                    map.getMapCell(robot.getX(), robot.getY()).getWater().setY(robot.getY());
                    map.getMapCell(robot.getX(), robot.getY()).getWater().setTimestampAtWhichItWasScanned(command.getTimestamp());
                    robot.getInventory().add(map.getMapCell(robot.getX(), robot.getY()).getWater());
                    robot.getDatabaseInventory().add(map.getMapCell(robot.getX(), robot.getY()).getWater());

                    result.put("message", "The scanned object is water.");
                    robot.setEnergy(robot.getEnergy() - 7);

                } else {
                    result.put("message", "ERROR: Object not found. Cannot perform action");
                }
            } else if (command.getSound().equals("none")) {
                if(map.getMapCell(robot.getX(), robot.getY()).getPlant() != null) {
                    map.getMapCell(robot.getX(), robot.getY()).getPlant().setScannedByRobot(true);
                    map.getMapCell(robot.getX(), robot.getY()).getPlant().setX(robot.getX());
                    map.getMapCell(robot.getX(), robot.getY()).getPlant().setY(robot.getY());
                    map.getMapCell(robot.getX(), robot.getY()).getPlant().setTimestampAtWhichItWasScanned(command.getTimestamp());
                    robot.getInventory().add(map.getMapCell(robot.getX(), robot.getY()).getPlant());
                    robot.getDatabaseInventory().add(map.getMapCell(robot.getX(), robot.getY()).getPlant());
                    result.put("message", "The scanned object is a plant.");
                    robot.setEnergy(robot.getEnergy() - 7);

                } else {
                    result.put("message", "ERROR: Object not found. Cannot perform action");
                }
            } else {
                if (map.getMapCell(robot.getX(), robot.getY()).getAnimal() != null) {
                    map.getMapCell(robot.getX(), robot.getY()).getAnimal().setScannedByRobot(true);
                    map.getMapCell(robot.getX(), robot.getY()).getAnimal().setX(robot.getX());
                    map.getMapCell(robot.getX(), robot.getY()).getAnimal().setY(robot.getY());
                    map.getMapCell(robot.getX(), robot.getY()).getAnimal().setTimestampAtWhichItWasScanned(command.getTimestamp());
                    robot.getInventory().add(map.getMapCell(robot.getX(), robot.getY()).getAnimal());
                    robot.getDatabaseInventory().add(map.getMapCell(robot.getX(), robot.getY()).getAnimal());
                    result.put("message", "The scanned object is an animal.");
                    robot.setEnergy(robot.getEnergy() - 7);

                } else {
                    result.put("message", "ERROR: Object not found. Cannot perform action");
                }
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}