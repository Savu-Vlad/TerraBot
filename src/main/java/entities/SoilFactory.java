package entities;

public final class SoilFactory {
    private SoilFactory() {

    }

    /**
     * Factory method to instantiate different type of Soil objects based on type
     */
    public static Soil createSoil(final String type, final String name, final double mass,
                                  final double nitrogen, final double waterRetention,
                                  final double soilPh, final double organicMatter,
                                  final Double leafLitter,
                                  final Double waterLogging, final Double permafrostDepth,
                                  final Double rootDensity, final Double salinity) {
        return switch (type) {
          case "DesertSoil" -> new DesertSoil(name, mass, nitrogen,
                  waterRetention, soilPh, organicMatter, salinity);
          case "TundraSoil" -> new TundraSoil(name, mass, nitrogen,
                  waterRetention, soilPh, organicMatter, permafrostDepth);
          case "GrasslandSoil" -> new GrasslandSoil(name, mass, nitrogen,
                  waterRetention, soilPh, organicMatter, rootDensity);
          case "ForestSoil" -> new ForestSoil(name, mass, nitrogen,
                  waterRetention, soilPh, organicMatter, leafLitter);
          case "SwampSoil" -> new SwampSoil(name, mass, nitrogen,
                  waterRetention, soilPh, organicMatter, waterLogging);
          default -> throw new IllegalArgumentException("Unknown soil type: " + type);
        };
    }
}
