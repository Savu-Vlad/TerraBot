package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import map.Map;

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

    public Water() {}

    public Water(String name, String type, double mass, double salinity, double ph, double purity, double turbidity
    , double contaminantIndex, boolean isFrozen) {
        super(name, mass, type);
        this.salinity = salinity;
        this.ph = ph;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
        this.waterQuality = calculateWaterQuality();
    }

    public double calculateWaterQuality() {
        double purityScore = purity / 100;
        double pHScore = 1 - Math.abs(ph - 7.5) / 7.5;
        double salinityScore = 1 - (salinity / 350);
        double turbidityScore = 1 - (turbidity / 100);
        double contaminantScore = 1 - (contaminantIndex / 100);
        int frozenScore = isFrozen ? 0 : 1;

        return (0.3 * purityScore + 0.2 * pHScore + 0.15 * salinityScore + 0.1 * turbidityScore
                + 0.15 * contaminantScore + 0.2 * frozenScore) * 100;
    }

    @Override
    public void updateMapWithScannedObject(Map map, Map.MapCell cell, int timestamp) {
        if (timestamp < cell.getWater().getTimestampAtWhichItWasScanned() + 1) {
            return;
        }

        int differentiationBetweenTimestamps = timestamp - cell.getWater().getTimestampAtWhichItWasScanned();

        if (differentiationBetweenTimestamps % 2 == 0) {
            if (cell.getSoil() != null) {
                cell.getSoil().setWaterRetention(roundScore(cell.getSoil().getWaterRetention() + 0.1));
            }

            if (cell.getAir() != null) {
                cell.getAir().setHumidity(roundScore(cell.getAir().getHumidity() + 0.1));
                cell.getAir().setAirQualityIndicator();
            }

            if (cell.getPlant() != null) {
                cell.getPlant().setGrowthRate(roundScore(cell.getPlant().getGrowthRate() + 0.2));
            }
        }
    }
}