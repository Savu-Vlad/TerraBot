package entities;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import robot.Robot;

@Getter
@Setter
public class Water extends Entity implements UpdatableInterface {
    private double salinity;
    private double ph;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;
    private int timestampAtWhichItWasScanned;
    private boolean scannedByRobot;
    private double waterQuality;
    private final double plantGrowthRateIncrease = 0.2;
    private final double airHumidityIncrease = 0.1;
    private final double soilWaterRetentionIncrease = 0.1;
    private final int zero = 0;
    private final int frequencyAtWhichInteractionHappens = 2;
    private final int one = 1;
    private final int oneHundred = 100;

    // Purity Score Constants
    private final int purityDivision = 100;
    private final double purityScoreMultiplier = 0.3;

    // pH Score Constants
    private final double pHScoreDivision = 7.5;
    private final double pHScoreSubstraction = 7.5;
    private final double pHScoreMultiplier = 0.2;

    // Salinity Score Constants
    private final int salinityDivision = 350;
    private final double salinityScoreMultiplier = 0.15;

    // Turbidity Score Constants
    private final int turbidityDivision = 100;
    private final double turbidityScoreMultiplier = 0.1;

    // Contaminant Score Constants
    private final int contaminantIndexDivision = 100;
    private final double contaminantScoreMultiplier = 0.15;

    // Frozen Score Constants
    private final double frozenScoreMultiplier = 0.2;

    public Water(final String name, final String type, final double mass, final double salinity,
                 final double ph, final double purity, final double turbidity,
                 final double contaminantIndex, final boolean isFrozen) {
        super(name, mass, type);
        this.salinity = salinity;
        this.ph = ph;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
        this.waterQuality = calculateWaterQuality();
    }

    /**
     * Method that calculates the water quality with a formula
     * */
    public double calculateWaterQuality() {
        double purityScore = purity / purityDivision;
        double pHScore = one - Math.abs(ph - pHScoreSubstraction) / pHScoreDivision;
        double salinityScore = one - (salinity / salinityDivision);
        double turbidityScore = one - (turbidity / turbidityDivision);
        double contaminantScore = one - (contaminantIndex / contaminantIndexDivision);
        int frozenScore = isFrozen ? zero : one;

        return (purityScoreMultiplier * purityScore
                +
                pHScoreMultiplier * pHScore + salinityScoreMultiplier * salinityScore
                +
                turbidityScoreMultiplier * turbidityScore
                + contaminantScoreMultiplier * contaminantScore
                +
                frozenScoreMultiplier * frozenScore) * oneHundred;
    }

    /**
     * Method that updates the map if water is scanned in that cell
     * It adds waterHumidity from Air, growthRate to plant and waterRetention to soil
     * */
    @Override
    public void updateMapWithScannedObject(final Robot robot,
                                           final Map map,
                                           final Map.MapCell cell,
                                           final int timestamp) {
        if (timestamp < cell.getWater().getTimestampAtWhichItWasScanned() + one) {
            return;
        }

        int differentiationBetweenTimestamps
                =
                timestamp - cell.getWater().getTimestampAtWhichItWasScanned();

        if (differentiationBetweenTimestamps % frequencyAtWhichInteractionHappens == zero) {
            if (cell.getSoil() != null) {
                cell.getSoil().setWaterRetention(roundScore(
                        cell.getSoil().getWaterRetention() + soilWaterRetentionIncrease));
            }

            if (cell.getAir() != null) {
                cell.getAir().setHumidity(roundScore(cell.getAir().getHumidity()
                        +
                        airHumidityIncrease));
                cell.getAir().setAirQualityIndicator();
            }

            if (cell.getPlant() != null) {
                cell.getPlant().setGrowthRate(roundScore(
                        cell.getPlant().getGrowthRate() + plantGrowthRateIncrease));
            }
        }
    }
}
