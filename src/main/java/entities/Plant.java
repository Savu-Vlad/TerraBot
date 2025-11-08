package entities;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Plant {
    private String name;
    private double mass;
    private List<Section> sections;
    private String type;
    private double oxygenLevel;
    private String ageStatus;
    private double maturityOxygenLevel;

    public Plant() {}

    public Plant(String name, double mass, String type) {
        this.name = name;
        this.mass = mass;
        this.type = type;
        this.ageStatus = "Young";
        this.maturityOxygenLevel = 0.2;
        switch (type) {
            case "FloweringPlant" -> this.oxygenLevel = 6.0;
            case "Mosses" -> this.oxygenLevel = 0.8;
            case "Algae" -> this.oxygenLevel = 0.5;
            //for default values to not duplicate because 2 types of plants have the same oxygen level
            default -> this.oxygenLevel = 0.0;
        };
    }
}

//class FloweringPlant extends Plant {
//    public FloweringPlant() {
//        this.oxygenLevel = 6.0;
//        this.ageStatus = "young";
//        this.type = "FloweringPlant";
//    }
//}
//
//class GymnospermsPlants extends Plant {
//    public GymnospermsPlants() {
//        this.oxygenLevel = 0.0;
//        this.ageStatus = "young";
//        this.type = "GymnospermsPlants";
//    }
//}

//class Ferns extends Plant {
//    public Ferns() {
//        this.oxygenLevel = 0.0;
//        this.ageStatus = "young";
//        this.type = "Ferns";
//    }
//}
//
//class Mosses extends Plant {
//    public Mosses() {
//        this.oxygenLevel = 0.8;
//        this.ageStatus = "young";
//        this.type = "Mosses";
//    }
//}
//
//class Algae extends Plant {
//    public Algae() {
//        this.oxygenLevel = 0.5;
//        this.ageStatus = "young";
//        this.type = "Algae";
//    }
