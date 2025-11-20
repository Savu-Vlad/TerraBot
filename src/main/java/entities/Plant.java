package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import map.Map;

@Getter
@Setter
public class Plant extends Entity {
    @JsonIgnore
    private double oxygenLevel;
    @JsonIgnore
    private String ageStatus;
    @JsonIgnore
    private double maturityOxygenLevel;
    @JsonIgnore
    private double possibilityToGetStuckInPlant;
    @JsonIgnore
    private boolean scannedByRobot;
    @JsonIgnore
    private boolean deadPlant;
    @JsonIgnore
    private int timestampAtWhichItWasScanned;
    @JsonIgnore
    private double growthRate;
    @JsonIgnore
    private final double firstStageOxygenLevel = 0.2;
    @JsonIgnore
    private final double secondStageOxygenLevel = 0.7;
    @JsonIgnore
    private final double thirdStageOxygenLevel = 0.4;

    public Plant() {}

    public Plant(String name, double mass, String type) {
        super(name, mass, type);
        this.ageStatus = "Young";
        this.maturityOxygenLevel = firstStageOxygenLevel;
        switch (type) {
            case "FloweringPlants" -> {
                this.oxygenLevel = 6.0;
                this.possibilityToGetStuckInPlant = 0.9;
            }
            case "Mosses" -> {
                this.oxygenLevel = 0.8;
                this.possibilityToGetStuckInPlant = 0.4;
            }
            case "Algae" -> {
                this.oxygenLevel = 0.5;
                this.possibilityToGetStuckInPlant = 0.2;
            }
            case "GymnospermsPlants" ->  {
                this.oxygenLevel = 0.0;
                this.possibilityToGetStuckInPlant = 0.6;
            }
            case "Ferns" -> {
                this.oxygenLevel = 0.0;
                this.possibilityToGetStuckInPlant = 0.3;
            }
        };
    }

    public double calculateOxygenLevelToBeGivenToAir() {
        return oxygenLevel + maturityOxygenLevel;
    }

    public void checkIfPlantAged() {
        if (growthRate >= 3.0) {
            deadPlant = true;
            maturityOxygenLevel = zero;
            oxygenLevel = 0;
        } else if (growthRate >= 2.0) {
            ageStatus = "Old";
            maturityOxygenLevel = thirdStageOxygenLevel;
        } else if (growthRate >= 1.0) {
            ageStatus = "Mature";
            maturityOxygenLevel = secondStageOxygenLevel;
        }
    }

    @Override
    public void updateMapWithScannedObject(Map map, Map.MapCell cell, int timestamp) {
        if (cell.getPlant().getTimestampAtWhichItWasScanned() == timestamp) {
            return;
        }

        if (cell.getSoil() != null) {
            double previousGrowthRate = cell.getPlant().getGrowthRate();
            previousGrowthRate += 0.2;
            cell.getPlant().setGrowthRate(roundScore(previousGrowthRate));
        }

        cell.getPlant().checkIfPlantAged();

        double currentAirOxygenLevel = cell.getAir().getOxygenLevel();

        currentAirOxygenLevel += cell.getPlant().calculateOxygenLevelToBeGivenToAir();
        cell.getAir().setOxygenLevel(roundScore(currentAirOxygenLevel));

        Air tempAir = cell.getAir();
        double tempToxicity = tempAir.calculateToxicityAQ(tempAir.getAirQuality(), tempAir.getMaxScore());
        cell.getAir().setToxicityAQ(tempToxicity);
        double tempFinalProb = tempAir.calculateFinalResult(tempToxicity);

        cell.getAir().setPossibilityToGetDamagedByAir(tempFinalProb);
        cell.getAir().setAirQualityIndicator();
    }
}
