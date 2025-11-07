package robot;
import fileio.CommandInput;
import map.Map;
import java.util.List;
import commands.Commands;
import input.Section;
import java.util.ArrayList;

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

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public ArrayList<CommandInput> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<CommandInput> commands) {
        this.commands = commands;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public boolean isSimulationStarted() {
        return isSimulationStarted;
    }

    public void setSimulationStarted(boolean isSimulationStarted) {
        this.isSimulationStarted = isSimulationStarted;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}