package map;
import entities.*;
import input.Section;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Map {
    private static Map instance = null;
    private int rowLength;
    private int columnLength;
    MapCell[][] grid;

    public MapCell getMapCell(int x, int y) {
        return grid[x][y];
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

    //making it private to implement singleton pattern

    private Map(int rowLength, int columnLength) {
        this.rowLength = rowLength;
        this.columnLength = columnLength;
        this.grid = new MapCell[rowLength][columnLength];

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                grid[i][j] = new MapCell();
            }
        }
    }

    public static Map getinstance(int rowLength, int columnLength) {
        if (instance == null) {
            return new Map(rowLength, columnLength);
        }

        return instance;
    }

}