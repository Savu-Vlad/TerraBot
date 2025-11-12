package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Plant {
    private String name;
    private double mass;
    private String type;
    @JsonIgnore
    private double oxygenLevel;
    @JsonIgnore
    private String ageStatus;
    @JsonIgnore
    private double maturityOxygenLevel;
    @JsonIgnore
    private double possibilityToGetStuckInPlant;

    public Plant() {}

    public Plant(String name, double mass, String type) {
        this.name = name;
        this.mass = mass;
        this.type = type;
        this.ageStatus = "Young";
        this.maturityOxygenLevel = 0.2;
        switch (type) {
            case "FloweringPlants" -> {
                this.oxygenLevel = 6.0;
                this.possibilityToGetStuckInPlant = 0.9;
            }
            case "Mosses" -> {
                this.oxygenLevel = 0.8;
                this.possibilityToGetStuckInPlant = 0.4;
            }
            case "Algae" -> {
                this.oxygenLevel = 0.5;
                this.possibilityToGetStuckInPlant = 0.2;
            }
            case "GymnospermsPlants" ->  {
                this.oxygenLevel = 0.0;
                this.possibilityToGetStuckInPlant = 0.6;
            }
            case "Ferns" -> {
                this.oxygenLevel = 0.0;
                this.possibilityToGetStuckInPlant = 0.3;
            }
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
