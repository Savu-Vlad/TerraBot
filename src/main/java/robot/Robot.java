package robot;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.implementationCommands.StartSimulation;
import fileio.CommandInput;
import map.Map;
import java.util.List;
import commands.Commands;
import input.Section;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import commands.*;
import commands.implementationCommands.*;

@Getter
@Setter
public class Robot {
    private static Robot instance = null;
    private float energy;
    private ArrayList<CommandInput> commands;
    private Map map;
    private boolean isSimulationStarted = false;
    private int x;
    private int y;


    private Robot(float energy, Map map, ArrayList<CommandInput> commands, int x, int y) {
        this.commands = commands;
        this.map = map;
        this.x = x;
        this.y = y;
        this.energy = energy;
    }

    public static Robot getInstance(float energy, Map map, ArrayList<CommandInput> commands, int x, int y) {
        if (instance == null) {
            instance = new Robot(energy, map, commands, x, y);
        }

        return instance;
    }

    public void executeCommand(String commandName, Map map, ArrayNode output, int timestamp) {
        switch (commandName) {
            case "startSimulation" -> new StartSimulation().execute(this, map, output, timestamp);
            case "endSimulation" -> new endSimulation().execute(this, map, output, timestamp);
            case "printEnvConditions" -> new printEnvConditions().execute(this, map, output, timestamp);
        }
    }
}