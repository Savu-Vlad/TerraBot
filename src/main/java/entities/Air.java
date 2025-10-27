package entities;
import input.Section;
import java.util.List;

public abstract class Air extends Entity {
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected String type;

    public Air() {}

    public Air(String name, double mass, List<Section> sections, String type, double humidity,
               double temperature, double oxygenLevel) {
        super(name, mass, sections);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getOxigenLevel() {
        return oxygenLevel;
    }

    public void setOxigenLevel(double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class TropicalAir extends Air {
    public TropicalAir() {
        this.type = "TropicalAir";
    }
}

class PolarAir extends Air {
    public PolarAir() {
        this.type = "PolarAir";
    }
}

class TemperateAir extends Air {
    public TemperateAir() {
        this.type = "TemperateAir";
    }
}

class DesertAir extends Air {
    public DesertAir() {
        this.type = "DesertAir";
    }
}

class MountainAir extends Air {
    public MountainAir() {
        this.type = "MountainAir";
    }
}

