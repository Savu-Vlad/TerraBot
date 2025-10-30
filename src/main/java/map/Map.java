package map;
import entities.*;
import input.Section;


public class Map {
    private static Map instance = null;
    private int rowLength;
    private int columnLength;
    MapCell[][] grid;

    public class MapCell {
        private final int x;
        private final int y;
        private Animal animal;
        private Plant plant;
        private Soil soil;
        private Water water;
        private Air air;

        public MapCell(int x, int y, Animal animal, Plant plant, Soil soil, Water water, Air air) {
            this.animal = animal;
            this.plant = plant;
            this.soil = soil;
            this.water = water;
            this.air = air;
            this.x = x;
            this.y = y;
        }
    }

    //making it private to implement singleton pattern

    private Map(int rowLength, int columnLength) {
        this.rowLength = rowLength;
        this.columnLength = columnLength;
        this.grid = new MapCell[rowLength][columnLength];
    }

    public static Map getinstance(int rowLength, int columnLength) {
        if (instance == null) {
            return new Map(rowLength, columnLength);
        }

        return instance;
    }

}