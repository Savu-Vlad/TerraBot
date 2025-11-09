package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public abstract class Air extends Entity {
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double airQuality;
    protected String type;
    @JsonIgnore
    protected String airQualityIndicator;


    public Air() {}

    public Air(String name, double mass, String type, double humidity,
               double temperature, double oxygenLevel) {
        super(name, mass);
        this.type = type;
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    public abstract double calculateAirQuality();

    public void setAirQualityIndicator() {
        double quality = calculateAirQuality();
        this.airQualityIndicator = QualityLevel.fromScore(quality).getIdentifier();
        this.airQuality = quality;
    }

}

class TropicalAir extends Air {
    private final double co2Level;

    public TropicalAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double co2Level) {
        super(name, mass, "TropicalAir", humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double TropicalAirQualityScore = (oxygenLevel * 2) + (humidity * 0.5) - (co2Level * 0.01);
        double normalizedQualityScore = normalizeScore(TropicalAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

}

class PolarAir extends Air {
    private final double iceCrystalConcentration;

    public PolarAir(String name, double mass, double humidity,
                    double temperature, double oxygenLevel, double iceCrystalConcentration) {
        super(name, mass, "PolarAir", humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double PolarAirQualityScore = (oxygenLevel * 2) + (100 - Math.abs(temperature)) - (iceCrystalConcentration * 0.05);
        double normalizedQualityScore = normalizeScore(PolarAirQualityScore);
        return roundScore(normalizedQualityScore);
    }
}

class TemperateAir extends Air {
    private final double pollenLevel;

    public TemperateAir(String name, double mass, double humidity,
                        double temperature, double oxygenLevel, double pollenLevel) {
        super(name, mass, "TemperateAir", humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double TemperateAirQualityScore = (oxygenLevel * 2) + (humidity * 0.7) - (pollenLevel * 0.01);
        double normalizedQualityScore = normalizeScore(TemperateAirQualityScore);
        return roundScore(normalizedQualityScore);
    }
}

class DesertAir extends Air {
    private final double dustParticles;

    public DesertAir(String name, double mass, double humidity,
                     double temperature, double oxygenLevel, double dustParticles) {
        super(name, mass, "DesertAir", humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double DesertAirQualityScore = (oxygenLevel * 2) - (dustParticles * 0.2) - (temperature * 0.3);
        double normalizedQualityScore = normalizeScore(DesertAirQualityScore);
        return roundScore(normalizedQualityScore);
    }
}

class MountainAir extends Air {
    private final double altitude;

    public MountainAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double altitude) {
        super(name, mass, "MountainAir", humidity, temperature, oxygenLevel);
        this.altitude = altitude;
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double oxygenFactor = oxygenLevel - (altitude/1000 * 0.05);
        double MountainAirQualityScore = (oxygenFactor * 2) + (humidity * 0.6);
        double normalizedQualityScore = normalizeScore(MountainAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

}

