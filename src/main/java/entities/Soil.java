package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import robot.Robot;

@Getter
@Setter
public abstract class Soil extends Entity {
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;
    protected double soilQuality;
    @JsonIgnore
    protected double possibilityToGetStuckInSoil;
    @JsonIgnore
    protected String soilQualityIndicator;

    public Soil() {

    }

    public Soil(final String name, final double mass, final double nitrogen,
                final double waterRetention, final double soilpH,
                final double organicMatter, final String type) {
        super(name, mass, type);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }

    @Override
    public void updateMapWithScannedObject(final Robot robot, final Map map,
                                           final Map.MapCell cell, final int timestamp) {

    }


    /**
     * The quality is calculated differently for each soil type
     * */
    public abstract double calculateSoilQuality();
    /**
     * The possibility to get stuck is calculated differently for each soil type
     * */
    public abstract double calculatePossibilityToGetStuckInSoil();

    public void setSoilQualityIndicator() {
        double quality = calculateSoilQuality();
        this.soilQualityIndicator = QualityLevel.fromScore(quality).getIdentifier();
        this.soilQuality = quality;
    }
}

@Getter
@Setter
class ForestSoil extends Soil {
    private final double leafLitter;

    ForestSoil(final String name, final double mass,
                      final double nitrogen, final double waterRetention, final double soilpH,
                      final double organicMatter, final double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "ForestSoil");
        this.leafLitter = leafLitter;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    /**
     * Method that calculates the soil quality based on formula
     * */
    @Override
    public double calculateSoilQuality() {
        double forestQualityScore
                =
                (nitrogen * onePointTwo)
                        +
                        (organicMatter * two)
                        +
                        (waterRetention * onePointFive)
                        +
                        (leafLitter * zeroPointThree);
        double normalizedQualityScore = normalizeScore(forestQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (waterRetention * zeroPointSix + leafLitter * zeroPointFour)
                /
                eighty * oneHundred;
    }
}
@Getter
@Setter
class SwampSoil extends Soil {
    private final double waterLogging;

    SwampSoil(final String name , final double mass,
                     final double nitrogen, final double waterRetention,
                     final double soilpH, final double organicMatter, final double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "SwampSoil");
        this.waterLogging = waterLogging;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    /**
     * Method that calculates the soil quality based on formula
     * */
    @Override
    public double calculateSoilQuality() {
        double SwampQualityScore = (nitrogen * onePointOne)
                +
                (organicMatter * twoPointTwo) - (waterLogging * five);
        double normalizedQualityScore = normalizeScore(SwampQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return waterLogging * ten;
    }
}

@Getter
@Setter
class DesertSoil extends Soil {
    private final double salinity;

    DesertSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "DesertSoil");
        this.salinity = salinity;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    /**
     * Method that calculates the soil quality based on formula
     * */
    @Override
    public double calculateSoilQuality() {
        double desertQualityScore = (nitrogen * zeroPointFive)
                +
                (waterRetention * zeroPointThree) - (salinity * two);
        double normalizedQualityScore = normalizeScore(desertQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (oneHundred - waterRetention + salinity)
                /
                oneHundred * oneHundred;
    }
}

@Getter
@Setter
class GrasslandSoil extends Soil {
    private final double rootDensity;

    GrasslandSoil(final String name, final double mass,
                         final double nitrogen, final double waterRetention, final double soilpH,
                         final double organicMatter, final double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "GrasslandSoil");
        this.rootDensity = rootDensity;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    /**
     * Method that calculates the soil quality based on formula
     * */
    @Override
    public double calculateSoilQuality() {
        double grasslandQualityScore = (nitrogen * 1.3) + (organicMatter * onePointFive) + (rootDensity * zeroPointEight);
        double normalizedQualityScore = normalizeScore(grasslandQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return ((fifty - rootDensity) + waterRetention * zeroPointFive)
                /
                seventyFive * oneHundred;
    }
}

@Getter
@Setter
class TundraSoil extends Soil {
    private final double permafrostDepth;

    TundraSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "TundraSoil");
        this.permafrostDepth = permafrostDepth;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    /**
     * Method that calculates the soil quality based on formula
     * */
    @Override
    public double calculateSoilQuality() {
        double tundraQualityScore = (nitrogen * zeroPointSeven) + (organicMatter * zeroPointFive)
                -
                (permafrostDepth * onePointFive);
        double normalizedQualityScore = normalizeScore(tundraQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (fifty - permafrostDepth) / fifty * oneHundred;
    }
}

