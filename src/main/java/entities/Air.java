package entities;
import input.Section;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public abstract class Air {
    protected String name;
    protected double mass;
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected String type;


    public Air() {}

    public Air(String name, double mass, String type, double humidity,
               double temperature, double oxygenLevel) {
        this.name = name;
        this.mass = mass;
        this.type = type;
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

}

class TropicalAir extends Air {
    private double co2Level;

    public TropicalAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double co2Level) {
        super(name, mass, "TropicalAir", humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }
}

class PolarAir extends Air {
    private double iceCrystalConcentration;

    public PolarAir(String name, double mass, double humidity,
                    double temperature, double oxygenLevel, double iceCrystalConcentration) {
        super(name, mass, "PolarAir", humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }
}

class TemperateAir extends Air {
    private double pollenLevel;

    public TemperateAir(String name , double mass, double humidity,
                        double temperature, double oxygenLevel, double pollenLevel) {
        super(name, mass, "TemperateAir", humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
    }
}

class DesertAir extends Air {
    private double dustParticles;

    public DesertAir(String name, double mass, double humidity,
                     double temperature, double oxygenLevel, double dustParticles) {
        super(name, mass, "DesertAir", humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
    }
}

class MountainAir extends Air {
    private double altitude;

    public MountainAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double altitude) {
        super(name, mass, "MountainAir", humidity, temperature, oxygenLevel);
        this.altitude = altitude;
    }
}

