package map;
import entities.*;
import input.Section;


public class Map {
    private int witdh;
    private int height;
    MapCell[][] grid;

    public class MapCell {
        private final int x;
        private final int y;

        public MapCell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}