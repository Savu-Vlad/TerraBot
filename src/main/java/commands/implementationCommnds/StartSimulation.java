package commands.implementationCommnds;
import commands.Commands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;

public class StartSimulation implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map) {
        if (robot.isSimulationStarted()) {
            System.out.println("ERROR: Simulation already started. Cannot perform action");
        } else {
            robot.setSimulationStarted(true);
            System.out.println("Simulation has started.");
        }
    }
}