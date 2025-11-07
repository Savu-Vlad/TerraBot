package entities;
import input.Section;
import java.util.List;

public class Plant {
    private String name;
    private double mass;
    private List<Section> sections;
    protected String type;
    protected double oxygenLevel;
    protected String ageStatus;

    public Plant() {}

    public Plant(String name, double mass, List<Section> sections, String type,
                 double oxygenLevel, String ageStatus) {
        this.name = name;
        this.mass = mass;
        this.sections = sections;
        this.type = type;
        this.ageStatus = ageStatus;
        switch (type) {
            case "FloweringPlant" -> this.oxygenLevel = 6.0;
            case "Mosses" -> this.oxygenLevel = 0.8;
            case "Algae" -> this.oxygenLevel = 0.5;
            default -> this.oxygenLevel = 0.0;//for default values to not duplicate because 2 types of plants have the same oxygen level
        };
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public String getAgeStatus() {
        return ageStatus;
    }

    public void setAgeStatus(String ageStatus) {
        this.ageStatus = ageStatus;
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
