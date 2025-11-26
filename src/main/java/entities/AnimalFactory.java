package entities;

public final class AnimalFactory {
    private AnimalFactory() {

    }

    /**
     * Factory method to instantiate different type of Animal objects based on type
     * */
    public static Animal createAnimal(final String type, final String name, final double mass) {
        return switch (type) {
            case "Herbivores" -> new Herbivores(name, mass);
            case "Carnivores" -> new Carnivores(name, mass);
            case "Omnivores" -> new Omnivores(name, mass);
            case "Detritivores" -> new Detritivores(name, mass);
            case "Parasites" -> new Parasites(name, mass);
            default -> throw new IllegalArgumentException("Unknown animal type: " + type);
        };
    }
}
