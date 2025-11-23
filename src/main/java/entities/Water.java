package entities;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import robot.Robot;

@Getter
@Setter
public class Water extends Entity {
    private double salinity;
    private double ph;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;
    private int timestampAtWhichItWasScanned;
    private boolean scannedByRobot;
    private double waterQuality;

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
        double purityScore = purity / oneHundred;
        double pHScore = one - Math.abs(ph - sevenPointFive) / sevenPointFive;
        double salinityScore = one - (salinity / threeHundredFifty);
        double turbidityScore = one - (turbidity / oneHundred);
        double contaminantScore = one - (contaminantIndex / oneHundred);
        int frozenScore = isFrozen ? zero : one;

        return (zeroPointThree * purityScore
                +
                zeroPointTwo * pHScore + zeroPointFifteen * salinityScore
                +
                zeroPointOne * turbidityScore
                + zeroPointFifteen * contaminantScore + zeroPointTwo * frozenScore) * oneHundred;
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

        if (differentiationBetweenTimestamps % two == zero) {
            if (cell.getSoil() != null) {
                cell.getSoil().setWaterRetention(roundScore(
                        cell.getSoil().getWaterRetention() + zeroPointOne));
            }

            if (cell.getAir() != null) {
                cell.getAir().setHumidity(roundScore(cell.getAir().getHumidity() + zeroPointOne));
                cell.getAir().setAirQualityIndicator();
            }

            if (cell.getPlant() != null) {
                cell.getPlant().setGrowthRate(roundScore(
                        cell.getPlant().getGrowthRate() + zeroPointTwo));
            }
        }
    }
}
