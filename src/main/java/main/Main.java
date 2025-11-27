package main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.SimulationInput;
import fileio.InputLoader;
import fileio.CommandInput;
import robot.Robot;
import map.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import parser.EntityParser;

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

        SimulationInput simulationParams = simulations.getFirst();
        SimulationInput simulationParamsSecond = simulations.getLast();

        String mapDimensions = simulationParams.getTerritoryDim();
        String mapDimensionsSecond = simulationParamsSecond.getTerritoryDim();

        int energyPoints = simulationParams.getEnergyPoints();
        int energyPointsSecond = simulationParamsSecond.getEnergyPoints();

        int rowLength = Character.getNumericValue(mapDimensions.charAt(0));
        int columnLength = Character.getNumericValue(mapDimensions.charAt(2));
        int rowLengthSecond = Character.getNumericValue(mapDimensionsSecond.charAt(0));
        int columnLengthSecond = Character.getNumericValue(mapDimensionsSecond.charAt(2));

        Map map = new Map(rowLength, columnLength);
        Map mapSecond = new Map(rowLengthSecond, columnLengthSecond);

        Robot robot = new Robot(energyPoints, map, commands, 0, 0);

        EntityParser entityParser = new EntityParser();
        EntityParser entityParserSecond = new EntityParser();

        entityParser.parseAllEntities(simulationParams, map);
        entityParserSecond.parseAllEntities(simulationParamsSecond, mapSecond);

        for (CommandInput command : commands) {
            if (command.getCommand().equals("startSimulation") && !robot.isSimulationStarted()) {
                robot.setNumberOfSimulations(robot.getNumberOfSimulations() + 1);
            }
            if (robot.getNumberOfSimulations() == 2 && !robot.isSimulationStarted()) {
                robot = new Robot(energyPointsSecond, mapSecond, commands, 0, 0);
                robot.setDatabaseInventory(new ArrayList<>());
                robot.setInventory(new ArrayList<>());
                map = mapSecond;
            }
            robot.executeCommand(command.getCommand(), map, output,
                    command.getTimestamp(), command);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
