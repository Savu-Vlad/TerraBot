package map;
import entities.*;
import lombok.Getter;
import lombok.Setter;
import fileio.CommandInput;
import robot.Robot;

@Getter
@Setter
public class Map {
    private final int rowLength;
    private final int columnLength;
    private int mapTimestamp;
    private MapCell[][] grid;

    /**
     * Returns the MapCell from the coordinates passed as parameters.
    * */

    public MapCell getMapCell(final int x, final int y) {
        return grid[x][y];
    }

    /**
     * Method that updates the weather conditions.
     * The changeWeatherCondition is an abstract method from Air.
     * Implemented in all Air subclasses.
    * */

    public void updateMapWithChangeWeatherCondition(final CommandInput command) {
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (grid[i][j].getAir() != null) {
                    grid[i][j].getAir().changeWeatherConditions(command);
                }
            }
        }
    }

    /**
     * Method that makes the interactions between the scanned objects.
     * It goes through the entire map and checks for scanned objects.
     */
    public void updateMapWithScan(final Robot robot, final int timestamp) {
        if (robot.getInventory().isEmpty()) {
            return;
        }
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (grid[i][j].getWater() != null && grid[i][j].getWater().isScannedByRobot()) {
                    Water water = grid[i][j].getWater();
                    water.updateMapWithScannedObject(robot, this, grid[i][j], timestamp);
                }
                if (grid[i][j].getPlant() != null && grid[i][j].getPlant().isScannedByRobot()) {
                    Plant plant = grid[i][j].getPlant();
                    plant.updateMapWithScannedObject(robot, this, grid[i][j], timestamp);
                }
                if (grid[i][j].getAnimal() != null && grid[i][j].getAnimal().isScannedByRobot()) {
                    Animal animal = grid[i][j].getAnimal();
                    animal.updateMapWithScannedObject(robot, this, grid[i][j], timestamp);
                }
            }
        }

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (grid[i][j].getAnimal() != null && grid[i][j].getAnimal().isScannedByRobot()) {
                    grid[i][j].getAnimal().setProcessedForThisTimestamp(false);
                }
            }
        }
    }

    @Getter
    @Setter
    public static final class MapCell {
        private Animal animal;
        private Plant plant;
        @Setter
        private Soil soil;
        private Water water;
        @Setter
        private Air air;
        private int entitiesCount;

        /**
         * Method that calculates the sum probability of all entities
         * that have a chance to damage/attack the robot.
         */
        public int calculateSumProbability() {
            double sum = 0.0;
            int countForProbabilities = 0;

            if (animal != null) {
                sum += animal.getPossibilityToAttackRobot();
                countForProbabilities++;
            }

            if (plant != null) {
                sum += plant.getPossibilityToGetStuckInPlant();
                countForProbabilities++;
            }

            if (soil != null) {
                sum += soil.getPossibilityToGetStuckInSoil();
                countForProbabilities++;
            }

            if (air != null) {
                sum += air.getPossibilityToGetDamagedByAir();
                countForProbabilities++;
            }

            double mean = Math.abs(sum / countForProbabilities);

            return (int) Math.round(mean);
        }

        /**
         * Setter method for Animal that also increases the entitiesCount
         * */
        public void setAnimal(final Animal animal) {
            this.animal = animal;
            if (animal != null) {
                this.entitiesCount++;
            } else {
                this.entitiesCount--;
            }
        }

        /**
         * Setter method for Plant that also increases the entitiesCount
         * */
        public void setPlant(final Plant plant) {
            this.plant = plant;
            if (plant != null) {
                this.entitiesCount++;
            } else {
                this.entitiesCount--;
            }
        }

        /**
         * Setter method for Water that also increases the entitiesCount
         * */
        public void setWater(final Water water) {
            this.water = water;
            if (water != null) {
                this.entitiesCount++;
            } else {
                this.entitiesCount--;
            }
        }
    }

    public Map(final int rowLength, final int columnLength) {
        this.rowLength = rowLength;
        this.columnLength = columnLength;
        this.grid = new MapCell[rowLength][columnLength];

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                grid[i][j] = new MapCell();
            }
        }
    }
}
