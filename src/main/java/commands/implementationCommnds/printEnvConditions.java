package commands.implementationCommnds;
import commands.Commands;
import commands.CommandInterface;
import map.Map;
import robot.Robot;

public class printEnvConditions implements CommandInterface {
    @Override
    public void execute(Robot robot, Map map) {
        if (!robot.isSimulationStarted()) {
            System.out.println("ERROR: Simulation not started. Cannot perform action");
        } else {

        }
    }
}