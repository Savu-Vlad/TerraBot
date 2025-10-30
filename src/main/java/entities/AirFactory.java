package entities;
import input.Section;
import java.util.List;

public class AirFactory {
    public Air createAir(String type, String name, double mass, List<Section> sections) {
        Air air = switch (type) {
            case "DesertAir" -> new DesertAir();
            case "MountainAir" -> new MountainAir();
            case "TropicalAir" -> new TropicalAir();
            case "PolarAir" -> new PolarAir();
            case "TemperateAir" -> new TemperateAir();
            default -> throw new IllegalArgumentException("Unknown air type: " + type);
        };

        air.setName(name);
        air.setMass(mass);
        air.setSections(sections);

        return air;
    }
}