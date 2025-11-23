package entities;


public final class AirFactory {
    private AirFactory() {

    }

    /**
     * Factory method to instantiate different type of Air objects based on type
     */
    public static Air createAir(final String type, final String name, final double mass,
                                final double humidity, final double temperature,
                                final double oxygenLevel,
                                final double co2Level,
                                final double iceCrystalConcentration,
                                final double pollenLevel,
                                final double dustParticles, final double altitude) {
        return switch (type) {
            case "DesertAir" -> new DesertAir(name, mass, humidity,
                    temperature, oxygenLevel, dustParticles);
            case "MountainAir" -> new MountainAir(name, mass, humidity,
                    temperature, oxygenLevel, altitude);
            case "TropicalAir" -> new TropicalAir(name, mass, humidity,
                    temperature, oxygenLevel, co2Level);
            case "PolarAir" -> new PolarAir(name, mass, humidity,
                    temperature, oxygenLevel, iceCrystalConcentration);
            case "TemperateAir" -> new TemperateAir(name, mass, humidity,
                    temperature, oxygenLevel, pollenLevel);
            default -> throw new IllegalArgumentException("Unknown air type: " + type);
        };
    }
}
