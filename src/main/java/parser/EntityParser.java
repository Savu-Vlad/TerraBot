package parser;
import entities.*;
import fileio.*;
import java.util.List;
import map.Map;

public class EntityParser {
    private void parsePlants(final List<PlantInput> plants, final Map map) {
        for (PlantInput plant : plants) {
            List<PairInput> position = plant.getSections();
            String type = plant.getType();
            String name = plant.getName();
            double mass = plant.getMass();

            for (PairInput pos : position) {
                int x = pos.getX();
                int y = pos.getY();
                Plant plantMap = new Plant(name, mass, type);
                map.getGrid()[x][y].setPlant(plantMap);
            }
        }
    }

    private void parseAnimals(final List<AnimalInput> animals, final Map map) {
        for (AnimalInput animal : animals) {
            List<PairInput> position = animal.getSections();
            String type = animal.getType();
            String name = animal.getName();
            double mass = animal.getMass();

            for (PairInput pos : position) {
                int x = pos.getX();
                int y = pos.getY();
                Animal animalFactory = AnimalFactory.createAnimal(type, name, mass);
                map.getGrid()[x][y].setAnimal(animalFactory);
            }
        }
    }

    private void parseSoils(final List<SoilInput> soils, final Map map) {
        for (SoilInput soil : soils) {
            List<PairInput> positions = soil.getSections();
            String name = soil.getName();
            String type = soil.getType();
            double mass = soil.getMass();
            double soilPH = soil.getSoilpH();
            double nitrogen = soil.getNitrogen();
            double waterRetention = soil.getWaterRetention();
            double organicMatter = soil.getOrganicMatter();
            Double leafLitter = soil.getLeafLitter();
            Double waterLogging = soil.getWaterLogging();
            Double permafrostDepth = soil.getPermafrostDepth();
            Double rootDensity = soil.getRootDensity();
            Double salinity = soil.getSalinity();

            for (PairInput pos : positions) {
                int x = pos.getX();
                int y = pos.getY();
                Soil soilMap = SoilFactory.createSoil(type, name, mass, nitrogen,
                        waterRetention, soilPH, organicMatter,
                        leafLitter, waterLogging, permafrostDepth, rootDensity, salinity);
                map.getGrid()[x][y].setSoil(soilMap);
            }
        }
    }

    private void parseAirs(final List<AirInput> airs, final Map map) {
        for (AirInput air : airs) {
            List<PairInput> positions = air.getSections();
            String name = air.getName();
            String type = air.getType();
            double mass = air.getMass();
            double humidity = air.getHumidity();
            double temperature = air.getTemperature();
            double oxygenLevel = air.getOxygenLevel();
            double co2Level = air.getCo2Level();
            double altitude = air.getAltitude();
            double pollenLevel = air.getPollenLevel();
            double iceCrystalConcentration = air.getIceCrystalConcentration();
            double dustParticles = air.getDustParticles();

            for (PairInput pos : positions) {
                int x = pos.getX();
                int y = pos.getY();
                Air airMap = AirFactory.createAir(type, name, mass,
                        humidity, temperature, oxygenLevel,
                        co2Level, iceCrystalConcentration, pollenLevel, dustParticles, altitude);
                map.getGrid()[x][y].setAir(airMap);
            }
        }
    }

    private void parseWaters(final List<WaterInput> waters, final Map map) {
        for (WaterInput water : waters) {
            List<PairInput> positions = water.getSections();
            String name = water.getName();
            String type = water.getType();
            double mass = water.getMass();
            double salinity = water.getSalinity();
            double ph = water.getPH();
            double purity = water.getPurity();
            double turbidity = water.getTurbidity();
            double contaminantIndex = water.getContaminantIndex();
            boolean isFrozen = water.isFrozen();

            for (PairInput pos : positions) {
                int x = pos.getX();
                int y = pos.getY();
                Water waterMap = new Water(name, type, mass,
                        salinity, ph, purity, turbidity,
                        contaminantIndex, isFrozen);
                map.getGrid()[x][y].setWater(waterMap);
            }
        }
    }

    /**
     * Method that parses all the entities from the json using all of the above helper methods
     * It sets all the entities in the map grid
     * There can only be 1 type of entity per cell, no duplicates
     * The setter methods also increment the entity count in the cell
     * */
    public void parseAllEntities(final SimulationInput simulationParams, final Map map) {
        parseAirs(simulationParams.getTerritorySectionParams().getAir(), map);
        parseWaters(simulationParams.getTerritorySectionParams().getWater(), map);
        parsePlants(simulationParams.getTerritorySectionParams().getPlants(), map);
        parseSoils(simulationParams.getTerritorySectionParams().getSoil(), map);
        parseAnimals(simulationParams.getTerritorySectionParams().getAnimals(), map);
    }
}
