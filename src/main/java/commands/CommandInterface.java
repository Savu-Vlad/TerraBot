package commands;
import map.Map;
import robot.Robot;

public interface CommandInterface {
    void execute(Robot robot, Map map);
}