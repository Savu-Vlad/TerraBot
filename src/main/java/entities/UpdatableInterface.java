package entities;
import map.Map;
import robot.Robot;

public interface UpdatableInterface {
    /**
     * Interface for entities that can be scanned and need to start interactions
     * The entities that can be scanned are : Water, Plant, Animal
     * In these classes, the method updateMapWithScannedObject is implemented.
     * */
    void updateMapWithScannedObject(Robot robot, Map map, Map.MapCell cell, int timestamp);
}
