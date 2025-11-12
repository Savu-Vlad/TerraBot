package robot;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.implementationCommands.StartSimulation;
import fileio.CommandInput;
import map.Map;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import commands.implementationCommands.*;

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


    public Robot(int energy, Map map, ArrayList<CommandInput> commands, int x, int y) {
        this.commands = commands;
        this.map = map;
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.timeAtWhichRechargingIsDone = -1;
    }

    public boolean checkIfOutOfBounds(int xToMoveRobot, int yToMoveRobot) {
        return xToMoveRobot >= 0 && xToMoveRobot < map.getRowLength() && yToMoveRobot >= 0 && yToMoveRobot < map.getColumnLength();
    }
    //in loc de string command name as putea sa dau ca parametru command input direct si sa mi iau parametrii de acolo!!!
    public void executeCommand(String commandName, Map map, ArrayNode output, int timestamp, CommandInput command) {
        switch (commandName) {
            case "startSimulation" -> {
                new StartSimulation().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
            case "endSimulation" -> {
                new endSimulation().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
            case "printEnvConditions" -> {
                new printEnvConditions().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
            case "printMap" -> {
                new printMap().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
            case "moveRobot" ->{
                new moveRobot().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
            case "getEnergyStatus" -> {
                new getEnergyStatus().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
            case "rechargeBattery" -> {
                new rechargeBattery().execute(this, map, output, timestamp, command);
                map.setMapTimestamp(timestamp);
                map.updateMapWithoutScan(timestamp);
            }
        }
    }
}