package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import map.Map;
import fileio.CommandInput;

@Getter
@Setter
public abstract class Air extends Entity {
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double airQuality;
    @JsonIgnore
    protected String airQualityIndicator;
    @JsonIgnore
    protected double toxicityAQ;
    @JsonIgnore
    protected double possibilityToGetDamagedByAir;
    @JsonIgnore
    protected int maxScore;

    public double calculateToxicityAQ(double airQualityScore, double maxScore) {
        return 100 * (1 - airQualityScore / maxScore);
    }

    public double calculateFinalResult(double toxicityAQ) {
        double normalizedToxicity = normalizeScore(toxicityAQ);
        return Math.round(normalizedToxicity * normalizationFactor) / normalizationFactor;
    }

    public Air() {}

    public Air(String name, double mass, String type, double humidity,
               double temperature, double oxygenLevel) {
        super(name, mass, type);
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

    //if there are 2 timestamp passed, the humidity increases, thus the probability and other values are recalculated
    public void increaseAirHumidity(Map map, int x, int y) {

        if (map.getMapCell(x, y).getWater() != null) {

            this.humidity += 0.1;
            this.airQuality = calculateAirQuality();
            setAirQualityIndicator();
            this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
            this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
        }
    }

    @Override
    public void updateMapWithScannedObject(Map map, Map.MapCell cell, int timestamp) {

    }

    public abstract void changeWeatherConditions(CommandInput command);
        //this doesn't do anything, it is intended to be overridden in each air subclass, doing this to not use instanceof!!
}

@Getter
class TropicalAir extends Air {
    private final double co2Level;

    public TropicalAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double co2Level) {
        super(name, mass, "TropicalAir", humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
        setAirQualityIndicator();
        this.maxScore = 82;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double TropicalAirQualityScore = (oxygenLevel * 2) + (humidity * 0.5) - (co2Level * 0.01);
        double normalizedQualityScore = normalizeScore(TropicalAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(CommandInput command) {
        this.airQuality += (command.getRainfall() * 0.3);
    }
}

@Getter
class PolarAir extends Air {
    private final double iceCrystalConcentration;

    public PolarAir(String name, double mass, double humidity,
                    double temperature, double oxygenLevel, double iceCrystalConcentration) {
        super(name, mass, "PolarAir", humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
        this.maxScore = 142;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double PolarAirQualityScore = (oxygenLevel * 2) + (100 - Math.abs(temperature)) - (iceCrystalConcentration * 0.05);
        double normalizedQualityScore = normalizeScore(PolarAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(CommandInput command) {
        this.airQuality -= (command.getWindSpeed() * 0.2);
    }
}

@Getter
class TemperateAir extends Air {
    private final double pollenLevel;

    public TemperateAir(String name, double mass, double humidity,
                        double temperature, double oxygenLevel, double pollenLevel) {
        super(name, mass, "TemperateAir", humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
        setAirQualityIndicator();
        this.maxScore = 84;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double TemperateAirQualityScore = (oxygenLevel * 2) + (humidity * 0.7) - (pollenLevel * 0.1);
        double normalizedQualityScore = normalizeScore(TemperateAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(CommandInput command) {
        /* Check if the season is null and the method calls on a type of air that isn't TemperateAir
            * to avoid the program having a null pointer exception !!
            * should be ok for the other methods because all the data in the json if not specified is 0 or false
            * so for the other methods it would just do air quality - 0 or + 0 so it is safe
         */
        if (command.getSeason() == null) {
            return;
        }

        double seasonPenalty = command.getSeason().equalsIgnoreCase("Spring") ? 15 : 0;
        this.airQuality -= seasonPenalty;
    }
}

@Getter
class DesertAir extends Air {
    @JsonIgnore
    private final double dustParticles;
    private boolean desertStorm ;

    public DesertAir(String name, double mass, double humidity,
                     double temperature, double oxygenLevel, double dustParticles) {
        super(name, mass, "DesertAir", humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
        setAirQualityIndicator();
        this.maxScore = 65;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double DesertAirQualityScore = (oxygenLevel * 2) - (dustParticles * 0.2) - (temperature * 0.3);
        double normalizedQualityScore = normalizeScore(DesertAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(CommandInput command) {
        if (command.isDesertStorm()) {
            this.desertStorm = true;
        }

        this.airQuality -= (command.isDesertStorm() ? 30 : 0);

    }
}

@Getter
class MountainAir extends Air {
    private final double altitude;

    public MountainAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double altitude) {
        super(name, mass, "MountainAir", humidity, temperature, oxygenLevel);
        this.altitude = altitude;
        setAirQualityIndicator();
        this.maxScore = 78;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double oxygenFactor = oxygenLevel - (altitude/1000 * 0.5);
        double MountainAirQualityScore = (oxygenFactor * 2) + (humidity * 0.6);
        double normalizedQualityScore = normalizeScore(MountainAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(CommandInput command) {
        this.airQuality -= (command.getNumberOfHikers() * 0.1);
    }

}

