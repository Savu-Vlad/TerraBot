package entities;
import input.Section;
import java.util.List;

public class AirFactory {
    public static Air createAir(String type, String name, double mass, double humidity, double temperature, double oxygenLevel,
                         double co2Level, double iceCrystalConcentration, double pollenLevel, double dustParticles, double altitude) {
        return switch (type) {
            case "DesertAir" -> new DesertAir(name, mass, humidity,temperature, oxygenLevel, dustParticles);
            case "MountainAir" -> new MountainAir(name, mass, humidity, temperature, oxygenLevel, altitude);
            case "TropicalAir" -> new TropicalAir(name, mass, humidity, temperature, oxygenLevel, co2Level);
            case "PolarAir" -> new PolarAir(name, mass, humidity, temperature, oxygenLevel, iceCrystalConcentration);
            case "TemperateAir" -> new TemperateAir(name, mass, humidity, temperature, oxygenLevel, pollenLevel);
            default -> throw new IllegalArgumentException("Unknown air type: " + type);
        };
    }
}