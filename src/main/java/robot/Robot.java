package robot;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.implementationCommands.*;
import entities.Air;
import entities.Soil;
import fileio.CommandInput;
import map.Map;
import entities.Entity;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Robot {
    private static Robot instance = null;
    private int energy;
    private ArrayList<CommandInput> commands;
    private Map map;
    private boolean isSimulationStarted = false;
    private int x;
    private int y;
    private int timeAtWhichRechargingIsDone;
    private ArrayList<Entity> inventory = new ArrayList<>();
    private final double zeroPointTree = 0.3;

    /**
     * The databaseInventory is for the interactions to continue
     * to happen if they are removed from the robot's main inventory.
     * The entities are removed from the databaseInventory and
     * the scanned objects are kept in inventory.
     * For the interactions to still happen.
     * */

    private ArrayList<Entity> databaseInventory = new ArrayList<>();
    private ArrayList<KnowledgeBase> knowledgeBase = new ArrayList<>();
    public Robot(final int energy, final Map map,
                 final ArrayList<CommandInput> commands, final int x, final int y) {
        this.commands = commands;
        this.map = map;
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.timeAtWhichRechargingIsDone = -1;
    }

    /**
     * Method that checks if the robot is out of bounds
     * when the moveRobot command is called.
     * */

    public boolean checkIfOutOfBounds(final int xToMoveRobot, final int yToMoveRobot) {
        return xToMoveRobot >= 0 && xToMoveRobot < map.getRowLength()
                &&
                yToMoveRobot >= 0 && yToMoveRobot < map.getColumnLength();
    }

    /**
     * Method that removes an item from the databaseInventory after
     * being "used" to improve the environment.
     * */

    public void removeItemFromDataBaseInventory(final String itemName, final String itemType) {
        for (int i = 0; i < databaseInventory.size(); i++) {
            if (databaseInventory.get(i).getName().equals(itemName)
                    &&
                    databaseInventory.get(i).getType().equals(itemType)) {
                databaseInventory.remove(i);
                break;
            }
        }
    }

    /**
     * Method that improves the iar quality.
     * */
    public void improveAirByPlantingPlant(final Map map,
                                          final String plantName, final String plantType) {
        Map.MapCell cellToBeImproved = map.getMapCell(this.x, this.y);
        Air entityToBeImproved = cellToBeImproved.getAir();
        entityToBeImproved.setOxygenLevel(entityToBeImproved.getOxygenLevel() + zeroPointTree);
        entityToBeImproved.setAirQuality(entityToBeImproved.calculateAirQuality());
        removeItemFromDataBaseInventory(plantName, plantType);
    }

    /**
     * Method that improves the soil quality.
     * */
    public void improveSoilByAddingWaterBody(final Map map,
                                             final String waterBodyName,
                                             final String waterBodyType) {
        Map.MapCell cellToBeImproved = map.getMapCell(this.x, this.y);
        Soil entityToBeImproved = cellToBeImproved.getSoil();
        entityToBeImproved.setWaterRetention(entityToBeImproved.getWaterRetention()
                + zeroPointTree);
        entityToBeImproved.setSoilQuality(entityToBeImproved.calculateSoilQuality());
        entityToBeImproved.setSoilQualityIndicator();
        removeItemFromDataBaseInventory(waterBodyName, waterBodyType);
    }

    /**
     *Primary method that executes the commands based on their name.
     * This is trying to follow the Command design pattern.
     */
    public void executeCommand(final String commandName, final Map map,
                               final ArrayNode output, final int timestamp,
                               final CommandInput command) {
        this.map.setMapTimestamp(timestamp);
        //aparent se poate da sout println si printeaza la testul de la output !!!
        this.map.updateMapWithScan(this, timestamp);
        switch (commandName) {
            case "startSimulation" -> {
                new StartSimulation().execute(this, map, output, timestamp, command);
            }
            case "endSimulation" -> {
                new EndSimulation().execute(this, map, output, timestamp, command);
            }
            case "printEnvConditions" -> {
                new PrintEnvConditions().execute(this, map, output, timestamp, command);
            }
            case "printMap" -> {
                new PrintMap().execute(this, map, output, timestamp, command);
            }
            case "moveRobot" -> {
                new MoveRobot().execute(this, map, output, timestamp, command);
            }
            case "getEnergyStatus" -> {
                new GetEnergyStatus().execute(this, map, output, timestamp, command);
            }
            case "rechargeBattery" -> {
                new RechargeBattery().execute(this, map, output, timestamp, command);
            }
            case "changeWeatherConditions" -> {
                new ChangeWeatherConditions().execute(this, map, output, timestamp, command);
            }
            case "scanObject" -> {
                new ScanObject().execute(this, map, output, timestamp, command);
            }
            case "learnFact" -> {
                new LearnFact().execute(this, map, output, timestamp, command);
            }
            case "printKnowledgeBase" -> {
                new PrintKnowledgeBase().execute(this, map, output, timestamp, command);
            }
            case "improveEnvironment" -> {
                new ImproveEnvironment().execute(this, map, output, timestamp, command);
            }
        }
    }
}
