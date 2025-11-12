package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class Animal extends Entity {
    protected String type;
    @JsonIgnore
    protected double possibilityToAttackRobot;

    public double calculatePossibilityToAttackRobot(double attackPossibility) {
        return (100 - attackPossibility) / 10.0;
    }

    public Animal() {
    }

    public Animal(String name, double mass, String type) {
        super(name, mass);
        this.type = type;
    }

}

class Herbivores extends Animal {
    public Herbivores(String name, double mass) {
        super(name, mass, "Herbivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(85);
    }
}

class Carnivores extends Animal {
    public Carnivores(String name, double mass) {
        super(name, mass, "Carnivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(30);
    }
}

class Omnivores extends Animal {
    public Omnivores(String name, double mass) {
        super(name, mass, "Omnivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(60);
    }
}

class Detritivores extends Animal {
    public Detritivores(String name, double mass) {
        super(name, mass, "Detritivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(90);
    }
}

class Parasites extends Animal {
    public Parasites(String name, double mass) {
        super(name, mass, "Parasites");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(10);
    }
}
