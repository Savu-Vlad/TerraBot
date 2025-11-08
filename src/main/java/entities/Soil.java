package entities;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Soil {
    protected String name;
    protected double mass;
    protected double nitrogen;
    protected double waterRetention;
    protected double soilPh;
    protected double organicMatter;
    protected String type;

    public Soil() {}

    public Soil(String name, double mass, double nitrogen,
                double waterRetention, double soilPh, double organicMatter, String type) {
        this.name = name;
        this.mass = mass;
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilPh = soilPh;
        this.organicMatter = organicMatter;
        this.type = type;
    }
}

class ForestSoil extends Soil {
    private double leafLitter;

    public ForestSoil(String name, double mass, double nitrogen, double waterRetention, double soilPh, double organicMatter, double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilPh, organicMatter, "ForestSoil");
        this.leafLitter = leafLitter;
    }
}

class SwampSoil extends Soil {
    private double waterLogging;

    public SwampSoil(String name , double mass, double nitrogen, double waterRetention, double soilPh, double organicMatter, double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilPh, organicMatter, "SwampSoil");
        this.waterLogging = waterLogging;
    }
}

class DesertSoil extends Soil {
    private double salinity;

    public DesertSoil(String name, double mass, double nitrogen, double waterRetention, double soilPh, double organicMatter, double salinity) {
        super(name, mass, nitrogen, waterRetention, soilPh, organicMatter, "DesertSoil");
        this.salinity = salinity;
    }
}

class GrasslandSoil extends Soil {
    private double rootDensity;

    public GrasslandSoil(String name, double mass, double nitrogen, double waterRetention, double soilPh, double organicMatter, double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilPh, organicMatter, "GrasslandSoil");
        this.rootDensity = rootDensity;
    }
}

class TundraSoil extends Soil {
    private double permafrostDepth;

    public TundraSoil(String name, double mass, double nitrogen, double waterRetention, double soilPh, double organicMatter, double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilPh, organicMatter, "TundraSoil");
        this.permafrostDepth = permafrostDepth;
    }
}

