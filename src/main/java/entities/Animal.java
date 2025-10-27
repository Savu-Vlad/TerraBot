package entities;
import input.Section;
import java.util.List;

public abstract class Animal extends Entity {
    protected String type;

    public Animal() {
    }

    public Animal(String name, double mass, List<Section> sections, String type) {
        super(name, mass, sections);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class Herbivores extends Animal {
    public Herbivores() {
        this.type = "Herbivores";
    }
}

class Carnivores extends Animal {
    public Carnivores() {
        this.type = "Carnivores";
    }
}

class Omnivores extends Animal {
    public Omnivores() {
        this.type = "Omnivores";
    }
}

class Detritivores extends Animal {
    public Detritivores() {
        this.type = "Detritivores";
    }
}

class Parasites extends Animal {
    public Parasites() {
        this.type = "Parasites";
    }
}
