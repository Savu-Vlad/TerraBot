package entities;
import input.Section;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class Animal extends Entity {
    protected String type;

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
    }
}

class Carnivores extends Animal {
    public Carnivores(String name, double mass) {
        super(name, mass, "Carnivores");
    }
}

class Omnivores extends Animal {
    public Omnivores(String name, double mass) {
        super(name, mass, "Omnivores");
    }
}

class Detritivores extends Animal {
    public Detritivores(String name, double mass) {
        super(name, mass, "Detritivores");
    }
}

class Parasites extends Animal {
    public Parasites(String name, double mass) {
        super(name, mass, "Parasites");
    }
}
