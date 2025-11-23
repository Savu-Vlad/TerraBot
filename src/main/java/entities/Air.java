package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import fileio.CommandInput;
import robot.Robot;

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

    /**
     * Method that calculates the toxicity of the air based on that formula
     */
    public double calculateToxicityAQ(final double airQualityScore, final double maxScoreValue) {
        return oneHundred * (1 - airQualityScore / maxScoreValue);
    }

    /**
     * Method that return the final attack probability that can harm the robot
     */
    public double calculateFinalResult(final double toxicityAQValue) {
        double normalizedToxicity = normalizeScore(toxicityAQValue);
        return Math.round(normalizedToxicity * normalizationFactor) / normalizationFactor;
    }

    public Air(final String name, final double mass, final String type, final double humidity,
               final double temperature, final double oxygenLevel) {
        super(name, mass, type);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    /**
     * Method that calculates air quality scode in each subclass with its own formula
     * */

    public abstract double calculateAirQuality();

    /**
     * Method that sets air quality and its indicator based on specific bounds
     * */
    public void setAirQualityIndicator() {
        double quality = calculateAirQuality();
        this.airQualityIndicator = QualityLevel.fromScore(quality).getIdentifier();
        this.airQuality = quality;
    }

    /**
     * Method tht increases air humidity when water is in the map cell
     * */

    public void increaseAirHumidity(final Map map, final int x, final int y) {

        if (map.getMapCell(x, y).getWater() != null) {

            this.humidity += zeroPointOne;
            this.airQuality = calculateAirQuality();
            setAirQualityIndicator();
            this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
            this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
        }
    }

    @Override
    public void updateMapWithScannedObject(final Robot robot, final Map map,
                                           final Map.MapCell cell, final int timestamp) {

    }

    /**
     * Method that changes weather conditions based on command input
     * it is overridden in each subclass of Air
     * */
    public abstract void changeWeatherConditions(CommandInput command);
}

@Getter
class TropicalAir extends Air {
    private final double co2Level;

    TropicalAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double co2Level) {
        super(name, mass, "TropicalAir", humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
        setAirQualityIndicator();
        this.maxScore = eightyTwo;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double tropicalAirQualityScore = (oxygenLevel * two)
                +
                (humidity * zeroPointFive)
                -
                (co2Level * zeroPointZeroOne);
        double normalizedQualityScore = normalizeScore(tropicalAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(final CommandInput command) {
        this.airQuality += (command.getRainfall() * zeroPointThree);
    }
}

@Getter
class PolarAir extends Air {
    private final double iceCrystalConcentration;

    PolarAir(final String name, final double mass, final double humidity,
             final double temperature,
             final double oxygenLevel, final double iceCrystalConcentration) {
        super(name, mass, "PolarAir", humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
        this.maxScore = oneFortyTwo;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double polarAirQualityScore = (oxygenLevel * two)
                +
                (oneHundred - Math.abs(temperature))
                -
                (iceCrystalConcentration * zeroPointZeroFive);
        double normalizedQualityScore = normalizeScore(polarAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(final CommandInput command) {
        this.airQuality -= (command.getWindSpeed() * zeroPointTwo);
    }
}

@Getter
class TemperateAir extends Air {
    private final double pollenLevel;

    TemperateAir(final String name, final double mass, final double humidity,
                        final double temperature, final double oxygenLevel,
                        final double pollenLevel) {
        super(name, mass, "TemperateAir", humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
        setAirQualityIndicator();
        this.maxScore = eightyFour;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double temperateAirQualityScore = (oxygenLevel * two)
                +
                (humidity * zeroPointSeven)
                -
                (pollenLevel * zeroPointOne);
        double normalizedQualityScore = normalizeScore(temperateAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(final CommandInput command) {
        /* Check if the season is null and the method calls on a type of air that isn't TemperateAir
            * to avoid the program having a null pointer exception !!
            * should be ok for the other methods because all the data
            * in the json if not specified is 0 or false
            * so for the other methods it would just do air quality - 0 or + 0 so it is safe
         */
        if (command.getSeason() == null) {
            return;
        }

        double seasonPenalty = command.getSeason().equalsIgnoreCase("Spring") ? fifteen : zero;
        this.airQuality -= seasonPenalty;
    }
}

@Getter
class DesertAir extends Air {
    @JsonIgnore
    private final double dustParticles;
    private boolean desertStorm;

    DesertAir(final String name, final double mass, final double humidity,
              final double temperature,
              final double oxygenLevel, final double dustParticles) {
        super(name, mass, "DesertAir", humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
        setAirQualityIndicator();
        this.maxScore = sixtyFive;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double desertAirQualityScore = (oxygenLevel * two)
                -
                (dustParticles * zeroPointTwo)
                -
                (temperature * zeroPointThree);
        double normalizedQualityScore = normalizeScore(desertAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(final CommandInput command) {
        if (command.isDesertStorm()) {
            this.desertStorm = true;
        }

        this.airQuality -= (command.isDesertStorm() ? thirty : zero);

    }
}

@Getter
class MountainAir extends Air {
    private final double altitude;

    MountainAir(final String name, final double mass, final double humidity,
                final double temperature, final double oxygenLevel, final double altitude) {
        super(name, mass, "MountainAir", humidity, temperature, oxygenLevel);
        this.altitude = altitude;
        setAirQualityIndicator();
        this.maxScore = seventyEight;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double oxygenFactor = oxygenLevel - (altitude / oneThousand * zeroPointFive);
        double mountainAirQualityScore = (oxygenFactor * two) + (humidity * zeroPointSix);
        double normalizedQualityScore = normalizeScore(mountainAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public void changeWeatherConditions(final CommandInput command) {
        this.airQuality -= (command.getNumberOfHikers() * zeroPointOne);
    }

}

