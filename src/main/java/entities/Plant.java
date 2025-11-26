package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import robot.Robot;

@Getter
@Setter
public class Plant extends Entity implements UpdatableInterface {
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
    @JsonIgnore
    private final int basicPlantValue = 0;
    @JsonIgnore
    private final double basicPlantValueDecimal = 0.0;
    @JsonIgnore
    private final double growthRatePlant = 0.2;
    @JsonIgnore
    private final double floweringPlantsPossibility = 0.9;
    @JsonIgnore
    private final double mossesPlantsPossibility = 0.4;
    @JsonIgnore
    private final double algaePlantsPossibility = 0.2;
    @JsonIgnore
    private final double gymnospermsPlantsPossibility = 0.6;
    @JsonIgnore
    private final double fernsPlantsPossibility = 0.3;
    @JsonIgnore
    private final double floweringPlantsOxygenLevel = 6.0;
    @JsonIgnore
    private final double mossesPlantsOxygenLevel = 0.8;
    @JsonIgnore
    private final double algaePlantsOxygenLevel = 0.5;
    @JsonIgnore
    private final double gymnospermsPlantsOxygenLevel = 0.0;
    @JsonIgnore
    private final double fernsPlantsOxygenLevel = 0.0;
    @JsonIgnore
    private final double upperBoundGrowthRate = 3.0;


    public Plant() {

    }

    public Plant(final String name, final double mass, final String type) {
        super(name, mass, type);
        this.ageStatus = "Young";
        this.maturityOxygenLevel = firstStageOxygenLevel;
        switch (type) {
            case "FloweringPlants" -> {
                this.oxygenLevel = floweringPlantsOxygenLevel;
                this.possibilityToGetStuckInPlant = floweringPlantsPossibility;
            }
            case "Mosses" -> {
                this.oxygenLevel = mossesPlantsOxygenLevel;
                this.possibilityToGetStuckInPlant = mossesPlantsPossibility;
            }
            case "Algae" -> {
                this.oxygenLevel = algaePlantsOxygenLevel;
                this.possibilityToGetStuckInPlant = algaePlantsPossibility;
            }
            case "GymnospermsPlants" ->  {
                this.oxygenLevel = gymnospermsPlantsOxygenLevel;
                this.possibilityToGetStuckInPlant = gymnospermsPlantsPossibility;
            }
            case "Ferns" -> {
                this.oxygenLevel = fernsPlantsOxygenLevel;
                this.possibilityToGetStuckInPlant = fernsPlantsPossibility;
            }
            default -> {
                this.oxygenLevel = basicPlantValueDecimal;
                this.possibilityToGetStuckInPlant = basicPlantValueDecimal;
            }
        }
    }

    /**
     * Method that calculates the oxygen level to be given to air
     * The oxygen from the plant itself and the maturity oxygen level
     * Based on the age it can give more or less oxygen
     * */
    public double calculateOxygenLevelToBeGivenToAir() {
        return oxygenLevel + maturityOxygenLevel;
    }

    /**
     * Method that checks if the plant aged, if the plant is above 1.0 growthRate
     * the oxygen level given to air increases, then at 2.0 it decreases and at
     * 3.0 the plant dies
     * */
    public void checkIfPlantAged() {
        if (growthRate >= upperBoundGrowthRate) {
            maturityOxygenLevel = basicPlantValue;
            oxygenLevel = basicPlantValue;
        } else if (growthRate >= 2.0) {
            ageStatus = "Old";
            maturityOxygenLevel = thirdStageOxygenLevel;
        } else if (growthRate >= 1.0) {
            ageStatus = "Mature";
            maturityOxygenLevel = secondStageOxygenLevel;
        }
    }

    /**
     * Method that gives oxygen to air when the plant is scanned
     * */
    @Override
    public void updateMapWithScannedObject(final Robot robot, final Map map,
                                           final Map.MapCell cell,
                                           final int timestamp) {
        if (cell.getPlant() == null) {
            return;
        }
        if (cell.getPlant().getTimestampAtWhichItWasScanned() == timestamp) {
            return;
        }

        if (cell.getSoil() != null) {
            double previousGrowthRate = cell.getPlant().getGrowthRate();
            previousGrowthRate += growthRatePlant;
            cell.getPlant().setGrowthRate(roundScore(previousGrowthRate));
            if (cell.getPlant().getGrowthRate() > upperBoundGrowthRate) {
                cell.setPlant(null);
            }
        }

        if (cell.getPlant() != null) {
            cell.getPlant().checkIfPlantAged();
        } else {
            return;
        }

        double currentAirOxygenLevel = cell.getAir().getOxygenLevel();

        currentAirOxygenLevel += cell.getPlant().calculateOxygenLevelToBeGivenToAir();
        cell.getAir().setOxygenLevel(roundScore(currentAirOxygenLevel));

        Air tempAir = cell.getAir();
        double airQuality = tempAir.calculateAirQuality();
        double tempToxicity = tempAir.calculateToxicityAQ(airQuality,
                tempAir.getMaxScore());
        cell.getAir().setToxicityAQ(tempToxicity);
        double tempFinalProb = tempAir.calculateFinalResult(tempToxicity);

        cell.getAir().setPossibilityToGetDamagedByAir(tempFinalProb);
        cell.getAir().setAirQualityIndicator();
    }
}
