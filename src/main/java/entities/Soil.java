package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import java.util.List;
import entities.QualityLevel;
import lombok.Getter;
import lombok.Setter;
import map.Map;

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

    public Soil() {}

    public Soil(String name, double mass, double nitrogen,
                double waterRetention, double soilpH, double organicMatter, String type) {
        super(name, mass, type);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }

    @Override
    public void updateMapWithScannedObject(Map map, Map.MapCell cell, int timestamp) {

    }

    public abstract double calculateSoilQuality();
    public abstract double calculatePossibilityToGetStuckInSoil();

    public void setSoilQualityIndicator() {
        double quality = calculateSoilQuality();
        this.soilQualityIndicator = QualityLevel.fromScore(quality).getIdentifier();
        this.soilQuality = quality;
    }

    public void increaseSoilWaterRetention(Map map, int x, int y) {
        this.waterRetention += 0.1;
        this.soilQuality = calculateSoilQuality();
        setSoilQualityIndicator();
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
    }
}

@Getter
@Setter
class ForestSoil extends Soil {
    private final double leafLitter;

    public ForestSoil(String name, double mass, double nitrogen, double waterRetention, double soilpH, double organicMatter, double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "ForestSoil");
        this.leafLitter = leafLitter;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    @Override
    public double calculateSoilQuality() {
        double forestQualityScore = (nitrogen * 1.2) + (organicMatter * 2) + (waterRetention * 1.5) + (leafLitter * 0.3);
        double normalizedQualityScore = normalizeScore(forestQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (waterRetention * 0.6 + leafLitter * 0.4) / 80 * 100;
    }
}
@Getter
@Setter
class SwampSoil extends Soil {
    private final double waterLogging;

    public SwampSoil(String name , double mass, double nitrogen, double waterRetention, double soilpH, double organicMatter, double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "SwampSoil");
        this.waterLogging = waterLogging;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    @Override
    public double calculateSoilQuality() {
        double SwampQualityScore = (nitrogen * 1.1) + (organicMatter * 2.2) - (waterLogging * 5);
        double normalizedQualityScore = normalizeScore(SwampQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return waterLogging * 10;
    }
}

@Getter
@Setter
class DesertSoil extends Soil {
    private final double salinity;

    public DesertSoil(String name, double mass, double nitrogen, double waterRetention, double soilpH, double organicMatter, double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "DesertSoil");
        this.salinity = salinity;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    @Override
    public double calculateSoilQuality() {
        double desertQualityScore = (nitrogen * 0.5) + (waterRetention * 0.3) - (salinity * 2);
        double normalizedQualityScore = normalizeScore(desertQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (100 - waterRetention + salinity) / 100 * 100;
    }
}

@Getter
@Setter
class GrasslandSoil extends Soil {
    private final double rootDensity;

    public GrasslandSoil(String name, double mass, double nitrogen, double waterRetention, double soilpH, double organicMatter, double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "GrasslandSoil");
        this.rootDensity = rootDensity;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    @Override
    public double calculateSoilQuality() {
        double grasslandQualityScore = (nitrogen * 1.3) + (organicMatter * 1.5) + (rootDensity * 0.8);
        double normalizedQualityScore = normalizeScore(grasslandQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return ((50 - rootDensity) + waterRetention * 0.5) / 75 * 100;
    }
}

@Getter
@Setter
class TundraSoil extends Soil {
    private final double permafrostDepth;

    public TundraSoil(String name, double mass, double nitrogen, double waterRetention, double soilpH, double organicMatter, double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter, "TundraSoil");
        this.permafrostDepth = permafrostDepth;
        this.possibilityToGetStuckInSoil = calculatePossibilityToGetStuckInSoil();
        setSoilQualityIndicator();
    }

    @Override
    public double calculateSoilQuality() {
        double tundraQualityScore = (nitrogen * 0.7) + (organicMatter * 0.5) - (permafrostDepth * 1.5);
        double normalizedQualityScore = normalizeScore(tundraQualityScore);
        return roundScore(normalizedQualityScore);
    }

    @Override
    public double calculatePossibilityToGetStuckInSoil() {
        return (50 - permafrostDepth) / 50 * 100;
    }
}

