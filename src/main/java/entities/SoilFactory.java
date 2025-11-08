package entities;
import input.Section;
import java.util.List;

public class SoilFactory {
    public static Soil createSoil(String type, String name, double mass, double nitrogen, double waterRetention, double soilPh, double organicMatter,
                           Double leafLitter, Double waterLogging, Double permafrostDepth, Double rootDensity, Double salinity) {
        return switch (type) {
          case "DesertSoil" -> new DesertSoil(name, mass, nitrogen, waterRetention, soilPh, organicMatter, salinity);
          case "TundraSoil" -> new TundraSoil(name , mass, nitrogen, waterRetention, soilPh, organicMatter, permafrostDepth);
          case "GrasslandSoil" -> new GrasslandSoil(name , mass, nitrogen, waterRetention, soilPh, organicMatter, rootDensity);
          case "ForestSoil" -> new ForestSoil(name, mass, nitrogen, waterRetention, soilPh, organicMatter, leafLitter);
          case "SwampSoil" -> new SwampSoil(name, mass, nitrogen, waterRetention, soilPh, organicMatter, waterLogging);
          default -> throw new IllegalArgumentException("Unknown soil type: " + type);
        };
    }
}