package entities;

public class AnimalFactory {
    public static Animal createAnimal(String type, String name, double mass) {
        Animal animal = switch(type) {
            case "Herbivores" -> new Herbivores(name, mass);
            case "Carnivores" -> new Carnivores(name, mass);
            case "Omnivores" -> new Omnivores(name, mass);
            case "Detritivores" -> new Detritivores(name, mass);
            case "Parasites" -> new Parasites(name, mass);
            default -> throw new IllegalArgumentException("Unknown animal type: " + type);
        };

        return animal;
    }
}