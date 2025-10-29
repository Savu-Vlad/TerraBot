package entities;
import input.Section;
import java.util.List;

public abstract class Water extends Entity {
    protected double salinity;
    protected double pH;
    protected double purity;
    protected int turbidity;
    protected double contaminantIndex;
    protected boolean isFrozen;


    public Water() {}

    public Water(String name, double mass, List<Section> sections, double salinity, double pH, double purity, int turbidity
    , double contaminantIndex, boolean isFrozen) {
        super(name, mass, sections);
        this.salinity = salinity;
        this.pH = pH;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }

    public double getSalinity() {
        return salinity;
    }

    public void setSalinity(double salinity) {
        this.salinity = salinity;
    }

    public double getPH() {return pH;
    }

    public void setPH(double pH) {
        this.pH = pH;
    }

    public double getPurity() {
        return purity;
    }

    public void setPurity(double purity) {
        this.purity = purity;
    }

    public int getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(int turbidity) {
        this.turbidity = turbidity;
    }

    public double getContaminantIndex() {
        return contaminantIndex;
    }

    public void setContaminantIndex(double contaminantIndex) {
        this.contaminantIndex = contaminantIndex;
    }

    public boolean isFrozen() {
        return isFrozen;
    }
}