package robot;
import map.Map;
import java.util.List;

public class Robot {
    private static Robot instance = null;
    private float energy;


    private Robot(float energy) {
        this.energy = energy;
    }

    public static Robot getInstance(float energy) {
        if (instance == null) {
            instance = new Robot(energy);
        }

        return instance;
    }


}