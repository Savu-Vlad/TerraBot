package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import robot.Robot;

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

    public Plant() {

    }

    public Plant(final String name, final double mass, final String type) {
        super(name, mass, type);
        this.ageStatus = "Young";
        this.maturityOxygenLevel = firstStageOxygenLevel;
        switch (type) {
            case "FloweringPlants" -> {
                this.oxygenLevel = sixPointZero;
                this.possibilityToGetStuckInPlant = zeroPointNine;
            }
            case "Mosses" -> {
                this.oxygenLevel = zeroPointEight;
                this.possibilityToGetStuckInPlant = zeroPointFour;
            }
            case "Algae" -> {
                this.oxygenLevel = zeroPointFive;
                this.possibilityToGetStuckInPlant = zeroPointTwo;
            }
            case "GymnospermsPlants" ->  {
                this.oxygenLevel = zeroPointZero;
                this.possibilityToGetStuckInPlant = zeroPointSix;
            }
            case "Ferns" -> {
                this.oxygenLevel = zeroPointZero;
                this.possibilityToGetStuckInPlant = zeroPointThree;
            }
            default -> {
                this.oxygenLevel = zeroPointZero;
                this.possibilityToGetStuckInPlant = zeroPointZero;
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
        if (growthRate >= threePointZero) {
            deadPlant = true;
            maturityOxygenLevel = zero;
            oxygenLevel = zero;
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
            previousGrowthRate += zeroPointTwo;
            cell.getPlant().setGrowthRate(roundScore(previousGrowthRate));
            if (cell.getPlant().getGrowthRate() > threePointZero) {
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
        double tempToxicity = tempAir.calculateToxicityAQ(tempAir.getAirQuality(),
                tempAir.getMaxScore());
        cell.getAir().setToxicityAQ(tempToxicity);
        double tempFinalProb = tempAir.calculateFinalResult(tempToxicity);

        cell.getAir().setPossibilityToGetDamagedByAir(tempFinalProb);
        cell.getAir().setAirQualityIndicator();
    }
}
