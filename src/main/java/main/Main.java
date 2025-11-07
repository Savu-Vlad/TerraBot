package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import robot.Robot;
import map.Map;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public class Main {

    private Main(){
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();


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

        output.add(MAPPER.valueToTree(simulations));
        output.add(MAPPER.valueToTree(commands));

        //in simulation params there are energy and size of map and the other entites!!!

        SimulationInput simulationParams = simulations.getFirst();

        String mapDimensions = simulationParams.territoryDim;
        int energyPoints = simulationParams.energyPoints;
        TerritorySectionParamsInput territoryParams = simulationParams.territorySectionParams;
        List<PlantInput> plants = territoryParams.plants;
        List<SoilInput> soils = territoryParams.soil;
        List<AnimalInput> animals = territoryParams.animals;
        List<WaterInput> waters = territoryParams.water;
        List<AirInput> airs = territoryParams.air;

        for (PlantInput plant : plants) {
            List<PairInput> position = plant.sections;
            String type = plant.type;
            double mass = plant.mass;

            for (PairInput pos : position) {
                int x = pos.x;
                int y = pos.y;
                // Add plant to the map at position (x, y)
                // Map.getInstance().addPlant(x, y, type, mass);
            }


        }

        int rowLength = Character.getNumericValue(mapDimensions.charAt(0));
        int columnLength = Character.getNumericValue(mapDimensions.charAt(2));
        Map map = Map.getinstance(rowLength, columnLength);


        Robot robot = Robot.getInstance(energyPoints, map, commands, 0, 0);









        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}