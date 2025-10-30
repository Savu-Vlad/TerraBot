//package entities;
//import java.util.List;
//import input.Section;
//
//public class PlantFactory {
//    public Plant createPlant(String type, String name, double mass, List<Section> sections) {
//        Plant plant = switch (type) {
//            case "FloweringPlant" -> new FloweringPlant();
//            case "GymnospermsPlants" -> new GymnospermsPlants();
//            case "Ferns" -> new Ferns();
//            case "Mosses" -> new Mosses();
//            case "Algae" -> new Algae();
//            default -> throw new IllegalArgumentException("Unknown plant type: " + type);
//        };
//
//        plant.setName(name);
//        plant.setMass(mass);
//        plant.setSections(sections);
//
//        return plant;
//    }
//}