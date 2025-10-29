package entities;
import input.Section;
import java.util.List;

public abstract class Soil extends Entity {
    protected double nitrogen;
    protected double waterRetention;
    double soilPh;
    double organicMatter;
    protected String type;

    public Soil() {}

    public Soil(String name, double mass, List<Section> sections, double nitrogen,
                double waterRetention, double soilPh, double organicMatter, String type) {
        super(name, mass, sections);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilPh = soilPh;
        this.organicMatter = organicMatter;
        this.type = type;
    }

    public double getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(double nitrogen) {
        this.nitrogen = nitrogen;
    }

    public double getWaterRetention() {
        return waterRetention;
    }

    public void setWaterRetention(double waterRetention) {
        this.waterRetention = waterRetention;
    }

    public double getSoilPh() {
        return soilPh;
    }

    public void setSoilPh(double soilPh) {
        this.soilPh = soilPh;
    }

    public double getOrganicMatter() {
        return organicMatter;
    }

    public void setOrganicMatter(double organicMatter) {
        this.organicMatter = organicMatter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class ForestSoil extends Soil {
    public ForestSoil() {
        this.type = "ForestSoil";
    }
}

class SwampSoil extends Soil {
    public SwampSoil() {
        this.type = "SwampSoil";
    }
}

class DesertSoil extends Soil {
    public DesertSoil() {
        this.type = "DesertSoil";
    }
}

class GrasslandSoil extends Soil {
    public GrasslandSoil() {
        this.type = "GrasslandSoil";
    }
}

class TundraSoil extends Soil {
    public TundraSoil() {
        this.type = "TundraSoil";
    }
}

