package commands.implementationCommands;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandInterface;
import fileio.CommandInput;
import map.Map;
import map.MapCardinalPoints;
import robot.Robot;

public class moveRobot implements CommandInterface {

    @Override
    public void execute(Robot robot, Map map, ArrayNode output, int timestamp, CommandInput command) {
        ObjectNode result = MAPPER.createObjectNode();

        result.put("command", "moveRobot");

        if (!robot.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (command.getTimestamp() < robot.getTimeAtWhichRechargingIsDone()) {
            result.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {

            if (robot.getEnergy() < 0) {
                result.put("message", "ERROR: Not enough battery left. Cannot perform action");
                result.put("timestamp", timestamp);
                output.add(result);
                return;
            }

            int[] probabilities = new int[4];

            for (MapCardinalPoints direction : MapCardinalPoints.values()) {
                if (direction == MapCardinalPoints.NORTH) {
                    int newY = robot.getY() + 1;

                    if (robot.checkIfOutOfBounds(robot.getX(), newY)) {
                        probabilities[MapCardinalPoints.NORTH.ordinal()] = map.getMapCell(robot.getX(), newY).calculateSumProbability();
                    } else {
                        probabilities[MapCardinalPoints.NORTH.ordinal()] = Integer.MAX_VALUE;
                    }
                }

                if (direction == MapCardinalPoints.EAST) {
                    int newX = robot.getX() + 1;

                    if (robot.checkIfOutOfBounds(newX, robot.getY())) {
                        probabilities[MapCardinalPoints.EAST.ordinal()] = map.getMapCell(newX, robot.getY()).calculateSumProbability();
                    } else {
                        probabilities[MapCardinalPoints.EAST.ordinal()] = Integer.MAX_VALUE;
                    }
                }

                if (direction == MapCardinalPoints.SOUTH) {
                    int newY = robot.getY() - 1;

                    if (robot.checkIfOutOfBounds(robot.getX(), newY)) {
                        probabilities[MapCardinalPoints.SOUTH.ordinal()] = map.getMapCell(robot.getX(), newY).calculateSumProbability();
                    } else {
                        probabilities[MapCardinalPoints.SOUTH.ordinal()] = Integer.MAX_VALUE;
                    }
                }

                if (direction == MapCardinalPoints.WEST) {
                    int newX = robot.getX() - 1;

                    if (robot.checkIfOutOfBounds(newX, robot.getY())) {
                        probabilities[MapCardinalPoints.WEST.ordinal()] = map.getMapCell(newX, robot.getY()).calculateSumProbability();
                    } else {
                        probabilities[MapCardinalPoints.WEST.ordinal()] = Integer.MAX_VALUE;
                    }
                }
            }

            double minProbability = Double.MAX_VALUE;

            for (double probability : probabilities) {
                if (probability < minProbability) {
                    minProbability = probability;
                }
            }

            int index = -1;

            for (int i = 0; i < probabilities.length; i++) {
                if (probabilities[i] == minProbability) {
                    index = i;
                    break;
                }
            }

            if (robot.getEnergy() - probabilities[index] >= 0) {

                if (index == MapCardinalPoints.NORTH.ordinal()) {
                    robot.setY(robot.getY() + 1);
                } else if (index == MapCardinalPoints.EAST.ordinal()) {
                    robot.setX(robot.getX() + 1);
                } else if (index == MapCardinalPoints.SOUTH.ordinal()) {
                    robot.setY(robot.getY() - 1);
                } else {
                    robot.setX(robot.getX() - 1);
                }

                robot.setEnergy(robot.getEnergy() - probabilities[index]);

                result.put("message", "The robot has successfully moved to position (" + robot.getX() + ", " + robot.getY() + ").");
            } else {
                result.put("message", "ERROR: Not enough battery left. Cannot perform action");
            }
        }

        result.put("timestamp", timestamp);
        output.add(result);
    }
}