package entities;
import input.Section;
import java.util.List;

public class AnimalFactory {
    public static Animal createAnimal(String type, String name, double mass) {
        Animal animal = switch(type) {
            case "Herbivores" -> new Herbivores();
            case "Carnivores" -> new Carnivores();
            case "Omnivores" -> new Omnivores();
            case "Detritivores" -> new Detritivores();
            case "Parasites" -> new Parasites();
            default -> throw new IllegalArgumentException("Unknown animal type: " + type);
        };

        animal.setName(name);
        animal.setMass(mass);

        return animal;
    }
}