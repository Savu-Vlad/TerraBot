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

    public MapCell getMapCell(int x, int y) {
        return grid[x][y];
    }

    public void updateMapWithChangeWeatherCondition(CommandInput command) {
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (grid[i][j].getAir() != null) {
                    grid[i][j].getAir().changeWeatherConditions(command);
                }
            }
        }
    }

    //aici o sa am robotul si o sa actualizez tot ceea ce este in inventarul robotului
    public void updateMapWithScan(Robot robot, Map map, int timestamp) {
        if (robot.getInventory().isEmpty()) {
            return;
        }
        //sa verific daca e egal timestamp ul de la care s a primit scan object cu cel curent !
        for (Entity entity : robot.getInventory()) {
            entity.updateMapWithScannedObject(map, grid[entity.getX()][entity.getY()], timestamp) ;
        }
    }

    public void updateMapWithoutScan(int timestamp) {
        int differenceBetweenTimestamps = timestamp - mapTimestamp;
//        //sa fac un fel de if 2 iterations has passed!!!
//        if (mapTimestamp % 2 != 0 && mapTimestamp != 1) {
//            for (int i = 0; i < rowLength; i++) {
//                for (int j = 0; j < columnLength; j++) {
//                    if (grid[i][j].getAir() != null) {
//                        grid[i][j].getAir().increaseAirHumidity(this, i, j);
//                    }
//
//                    //need to also add for the soil !
//                    if (grid[i][j].getSoil() != null) {
//                        grid[i][j].getSoil().increaseSoilWaterRetention(this, i, j);
//                    }
//                }
//            }
//        }

    }

    @Getter
    @Setter
    public static class MapCell {
        private Animal animal;
        private Plant plant;
        @Setter
        private Soil soil;
        private Water water;
        @Setter
        private Air air;
        private int entitiesCount;

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

            return (int)Math.round(mean);
        }

        public void setAnimal(Animal animal) {
            this.animal = animal;
            this.entitiesCount++;
        }

        public void setPlant(Plant plant) {
            this.plant = plant;
            this.entitiesCount++;
        }

        public void setWater(Water water) {
            this.water = water;
            this.entitiesCount++;
        }
    }

    public Map(int rowLength, int columnLength) {
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
