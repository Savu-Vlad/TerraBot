package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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
    @JsonIgnore
    protected final double forestSoilQualityNitrogenBase = 1.2;
    @JsonIgnore
    protected final double forestSoilQualityOrganicMatterBase = 2.0;
    @JsonIgnore
    protected final double forestSoilQualityWaterRetentionBase = 1.5;
    @JsonIgnore
    protected final double forestSoilQualityLeafLitterBase = 0.3;
    @JsonIgnore
    protected final double forestSoilStuckWaterRetentionBase = 0.6;
    @JsonIgnore
    protected final double forestSoilStuckLeafLitterBase = 0.4;
    @JsonIgnore
    protected final double forestSoilStuckDivisor = 80.0;
    @JsonIgnore
    protected final double swampSoilQualityNitrogenBase = 1.1;
    @JsonIgnore
    protected final double swampSoilQualityOrganicMatterBase = 2.2;
    @JsonIgnore
    protected final double swampSoilQualityWaterLoggingBase = 5.0;
    @JsonIgnore
    protected final double swampSoilStuckWaterLoggingBase = 10.0;
    @JsonIgnore
    protected final double desertSoilQualityNitrogenBase = 0.5;
    @JsonIgnore
    protected final double desertSoilQualityWaterRetentionBase = 0.3;
    @JsonIgnore
    protected final double desertSoilQualitySalinityBase = 2.0;
    @JsonIgnore
    protected final double grasslandSoilQualityNitrogenBase = 1.3;
    @JsonIgnore
    protected final double grasslandSoilQualityOrganicMatterBase = 1.5;
    @JsonIgnore
    protected final double grasslandSoilQualityRootDensityBase = 0.8;
    @JsonIgnore
    protected final double grasslandSoilStuckRootDensityBase = 50.0;
    @JsonIgnore
    protected final double grasslandSoilStuckWaterRetentionBase = 0.5;
    @JsonIgnore
    protected final double grasslandSoilStuckDivisor = 75.0;
    @JsonIgnore
    protected final double tundraSoilQualityNitrogenBase = 0.7;
    @JsonIgnore
    protected final double tundraSoilQualityOrganicMatterBase = 0.5;
    @JsonIgnore
    protected final double tundraSoilQualityPermafrostBase = 1.5;
    @JsonIgnore
    protected final double tundraSoilStuckPermafrostDivisor = 50.0;
    @JsonIgnore
    protected final int oneHundredDivisor = 100;
    @JsonIgnore
    protected final double oneHundredPointZeroDivisor = 100.0;
    @JsonIgnore
    protected final int oneHundred = 100;
    @JsonIgnore
    protected final int fifty = 50;

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

    /**
     * The quality is calculated differently for each soil type
     * */
    public abstract double calculateSoilQuality();
    /**
     * The possibility to get stuck is calculated differently for each soil type
     * */
    public abstract double calculatePossibilityToGetStuckInSoil();


    /**
     * Method that sets the air quality indicator based on the boundaries in the QualityLevel enum
     * 0-40 -> poor
     * 40-70 -> moderate
     * 70-100 -> good
     * */
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
                (nitrogen * forestSoilQualityNitrogenBase)
                        +
                        (organicMatter * forestSoilQualityOrganicMatterBase)
                        +
                        (waterRetention * forestSoilQualityWaterRetentionBase)
                        +
                        (leafLitter * forestSoilQualityLeafLitterBase);
        double normalizedQualityScore = normalizeScore(forestQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (waterRetention * forestSoilStuckWaterRetentionBase
                +
                leafLitter * forestSoilStuckLeafLitterBase)
                /
                forestSoilStuckDivisor * oneHundredDivisor;
    }
}
@Getter
@Setter
class SwampSoil extends Soil {
    private final double waterLogging;

    SwampSoil(final String name, final double mass,
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
        double swampQualityScore = (nitrogen * swampSoilQualityNitrogenBase)
                +
                (organicMatter * swampSoilQualityOrganicMatterBase)
                -
                (waterLogging * swampSoilQualityWaterLoggingBase);
        double normalizedQualityScore = normalizeScore(swampQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return waterLogging * swampSoilStuckWaterLoggingBase;
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
        double desertQualityScore = (nitrogen * desertSoilQualityNitrogenBase)
                +
                (waterRetention * desertSoilQualityWaterRetentionBase)
                -
                (salinity * desertSoilQualitySalinityBase);
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
                oneHundredDivisor * oneHundredDivisor;
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
        double grasslandQualityScore = (nitrogen * grasslandSoilQualityNitrogenBase)
                +
                (organicMatter * grasslandSoilQualityOrganicMatterBase)
                +
                (rootDensity * grasslandSoilQualityRootDensityBase);
        double normalizedQualityScore = normalizeScore(grasslandQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return ((grasslandSoilStuckRootDensityBase - rootDensity)
                +
                waterRetention * grasslandSoilStuckWaterRetentionBase)
                /
                grasslandSoilStuckDivisor * oneHundredDivisor;
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
        double tundraQualityScore = (nitrogen * tundraSoilQualityNitrogenBase)
                +
                (organicMatter * tundraSoilQualityOrganicMatterBase)
                -
                (permafrostDepth * tundraSoilQualityPermafrostBase);
        double normalizedQualityScore = normalizeScore(tundraQualityScore);
        return roundScore(normalizedQualityScore);
    }

    /**
     * Method to calculate possibility to get stuck in soil
     * */
    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (fifty - permafrostDepth)
                /
                tundraSoilStuckPermafrostDivisor * oneHundredDivisor;
    }
}

