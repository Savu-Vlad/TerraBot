package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import robot.Robot;
import map.Map;
import java.util.List;
import entities.*;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();
        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         *
         * ObjectNode objectNode = MAPPER.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = MAPPER.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */
        ArrayList<SimulationInput> simulations = inputLoader.getSimulations();
        ArrayList<CommandInput> commands = inputLoader.getCommands();

        //in simulation params there are energy and size of map and the other entites!!!

        SimulationInput simulationParams = simulations.getFirst();

        String mapDimensions = simulationParams.getTerritoryDim();
        int energyPoints = simulationParams.getEnergyPoints();
        TerritorySectionParamsInput territoryParams = simulationParams.getTerritorySectionParams();

        List<PlantInput> plants = territoryParams.getPlants();
        List<SoilInput> soils = territoryParams.getSoil();
        List<AnimalInput> animals = territoryParams.getAnimals();
        List<WaterInput> waters = territoryParams.getWater();
        List<AirInput> airs = territoryParams.getAir();

        int rowLength = Character.getNumericValue(mapDimensions.charAt(0));
        int columnLength = Character.getNumericValue(mapDimensions.charAt(2));
        Map map = new Map(rowLength, columnLength);

        Robot robot = new Robot(energyPoints, map, commands, 0, 0);

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

        for (AnimalInput animal : animals) {
            List<PairInput> position = animal.getSections();
            String type = animal.getType();
            String name = animal.getName();
            double mass = animal.getMass();
            //for the carnivore and parasite I need to implement the feedAnimal method in a way that it would first eat an animal
            // and then a plant if there is no anima;

            for (PairInput pos : position) {
                int x = pos.getX();
                int y = pos.getY();
                Animal animalFactory = AnimalFactory.createAnimal(type, name, mass);
                map.getGrid()[x][y].setAnimal(animalFactory);
            }
        }

        for (WaterInput water : waters) {
            List<PairInput> position = water.getSections();
            String name = water.getName();
            String type = water.getType();
            double mass = water.getMass();
            double salinity = water.getSalinity();
            double turbidity = water.getTurbidity();
            double purity = water.getPurity();
            double contaminantIndex = water.getContaminantIndex();
            double ph = water.getPH();
            boolean isFrozen = water.isFrozen();

            for (PairInput pos : position) {
                int x = pos.getX();
                int y = pos.getY();
                Water waterMap = new Water(name, type, mass, salinity, ph, purity, turbidity, contaminantIndex, isFrozen);
                map.getGrid()[x][y].setWater(waterMap);
            }
        }

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
                Soil soilMap = SoilFactory.createSoil(type, name, mass, nitrogen, waterRetention, soilPH, organicMatter,
                        leafLitter, waterLogging, permafrostDepth, rootDensity, salinity);
                map.getGrid()[x][y].setSoil(soilMap);
            }
        }

        for (AirInput air : airs) {
            List <PairInput> positions = air.getSections();
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
                Air airMap = AirFactory.createAir(type, name, mass, humidity, temperature, oxygenLevel,
                         co2Level, iceCrystalConcentration, pollenLevel, dustParticles, altitude);
                map.getGrid()[x][y].setAir(airMap);
            }
        }
        //nu mai merge ca am schimbat aia la entity sa fie la super !!!
        for (CommandInput command : commands) {
            robot.executeCommand(command.getCommand(), map, output, command.getTimestamp(), command);
        }


        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
