package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
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
    @JsonIgnore
    protected final int toxicityMultiplier = 100;
    @JsonIgnore
    protected final double humidityIncreaseRate = 0.1;
    @JsonIgnore
    protected final int tropicalAirMaxScore = 82;
    @JsonIgnore
    protected final int polarAirMaxScore = 142;
    @JsonIgnore
    protected final int temperateAirMaxScore = 84;
    @JsonIgnore
    protected final int desertAirMaxScore = 65;
    @JsonIgnore
    protected final int mountainAirMaxScore = 78;
    @JsonIgnore
    protected final int basicValueForChangeWeather = 0;
    @JsonIgnore
    protected final int desertStormValue = 30;
    @JsonIgnore
    protected final int springValue = 15;
    @JsonIgnore
    protected final int altitudeDivisor = 1000;
    @JsonIgnore
    protected final double altitudeMultiplier = 0.5;
    @JsonIgnore
    protected final int temperatureOneHundred = 100;
    @JsonIgnore
    protected final int tropicalAirOxygenMultiplier = 2;
    @JsonIgnore
    protected final double tropicalAirHumidityMultiplier = 0.5;
    @JsonIgnore
    protected final double tropicalAirCo2Multiplier = 0.01;
    @JsonIgnore
    protected final double tropicalAirRainfallMultiplier = 0.3;
    @JsonIgnore
    protected final int polarAirOxygenMultiplier = 2;
    @JsonIgnore
    protected final double polarAirTemperatureDifferenceMultiplier = 1.0;
    @JsonIgnore
    protected final double polarAirIceCrystalMultiplier = 0.05;
    @JsonIgnore
    protected final double polarAirWindSpeedMultiplier = 0.2;
    @JsonIgnore
    protected final int temperateAirOxygenMultiplier = 2;
    @JsonIgnore
    protected final double temperateAirHumidityMultiplier = 0.7;
    @JsonIgnore
    protected final double temperateAirPollenMultiplier = 0.1;
    @JsonIgnore
    protected final double temperateAirSeasonMultiplier = 1.0;
    @JsonIgnore
    protected final int desertAirOxygenMultiplier = 2;
    @JsonIgnore
    protected final double desertAirDustParticlesMultiplier = 0.2;
    @JsonIgnore
    protected final double desertAirTemperatureMultiplier = 0.3;
    @JsonIgnore
    protected final int mountainAirOxygenMultiplier = 2;
    @JsonIgnore
    protected final double mountainAirHumidityMultiplier = 0.6;
    @JsonIgnore
    protected final double mountainAirHikersMultiplier = 0.1;
    @JsonIgnore
    protected boolean alreadyProcessedWeatherChange;


    /**
     * Method that calculates the toxicity of the air based on that formula
     */
    public double calculateToxicityAQ(final double airQualityScore, final double maxScoreValue) {
        return toxicityMultiplier * (1 - airQualityScore / maxScoreValue);
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
     * Method that changes weather conditions based on command input
     * it is overridden in each subclass of Air
     * */
    public abstract String changeWeatherConditions(CommandInput command);
}

@Getter
class TropicalAir extends Air {
    private final double co2Level;

    TropicalAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double co2Level) {
        super(name, mass, "TropicalAir", humidity, temperature, oxygenLevel);
        this.co2Level = roundScore(co2Level);
        setAirQualityIndicator();
        this.maxScore = tropicalAirMaxScore;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double tropicalAirQualityScore = (oxygenLevel * tropicalAirOxygenMultiplier)
                +
                (humidity * tropicalAirHumidityMultiplier)
                -
                (co2Level * tropicalAirCo2Multiplier);
        double normalizedQualityScore = normalizeScore(tropicalAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public String changeWeatherConditions(final CommandInput command) {
        if (!command.getType().equals("rainfall")) {
            return "ERROR: The weather change does not affect the environment. Cannot perform action";
        }
        this.airQuality += (command.getRainfall() * tropicalAirRainfallMultiplier);
        return null;
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
        this.maxScore = polarAirMaxScore;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
        setAirQualityIndicator();
    }

    @Override
    public double calculateAirQuality() {
        double polarAirQualityScore = (oxygenLevel * polarAirOxygenMultiplier)
                +
                (temperatureOneHundred - Math.abs(temperature))
                -
                (iceCrystalConcentration * polarAirIceCrystalMultiplier);
        double normalizedQualityScore = normalizeScore(polarAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public String changeWeatherConditions(final CommandInput command) {
        if (!command.getType().equals("polarStorm")) {
            return "ERROR: The weather change does not affect the environment. Cannot perform action";
        }
        this.airQuality -= (command.getWindSpeed() * polarAirWindSpeedMultiplier);
        return null;
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
        this.maxScore = temperateAirMaxScore;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double temperateAirQualityScore = (oxygenLevel * temperateAirOxygenMultiplier)
                +
                (humidity * temperateAirHumidityMultiplier)
                -
                (pollenLevel * temperateAirPollenMultiplier);
        double normalizedQualityScore = normalizeScore(temperateAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public String changeWeatherConditions(final CommandInput command) {
        if (command.getType().equals("newSeason")) {
            return "ERROR: The weather change does not affect the environment. Cannot perform action";
        }

        if (command.getSeason() == null) {
            return "ERROR: The weather change does not affect the environment. Cannot perform action";
        }

        double seasonPenalty = command.getSeason().equalsIgnoreCase("Spring") ? springValue
                :
                basicValueForChangeWeather;
        this.airQuality -= seasonPenalty;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        return null;
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
        this.maxScore = desertAirMaxScore;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double desertAirQualityScore = (oxygenLevel * desertAirOxygenMultiplier)
                -
                (dustParticles * desertAirDustParticlesMultiplier)
                -
                (temperature * desertAirTemperatureMultiplier);
        double normalizedQualityScore = normalizeScore(desertAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public String changeWeatherConditions(final CommandInput command) {
        if (!command.getType().equals("desertStorm")) {
            return "ERROR: The weather change does not affect the environment. Cannot perform action";
        }
        if (command.isDesertStorm()) {
            this.desertStorm = true;
        }

        this.airQuality -= (command.isDesertStorm() ? desertStormValue
                :
                basicValueForChangeWeather);
        return null;

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
        this.maxScore = mountainAirMaxScore;
        this.toxicityAQ = calculateToxicityAQ(airQuality, maxScore);
        this.possibilityToGetDamagedByAir = calculateFinalResult(toxicityAQ);
    }

    @Override
    public double calculateAirQuality() {
        double oxygenFactor = oxygenLevel - (altitude / altitudeDivisor * altitudeMultiplier);
        double mountainAirQualityScore = (oxygenFactor * mountainAirOxygenMultiplier)
                +
                (humidity * mountainAirHumidityMultiplier);
        double normalizedQualityScore = normalizeScore(mountainAirQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public String changeWeatherConditions(final CommandInput command) {
        if (!command.getType().equals("peopleHiking")) {
            return "ERROR: The weather change does not affect the environment. Cannot perform action";
        }
        this.airQuality -= (command.getNumberOfHikers() * mountainAirHikersMultiplier);
        return null;
    }
}

