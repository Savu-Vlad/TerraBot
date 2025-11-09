package entities;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Water {
    private String name;
    private String type;
    private double mass;
    private double salinity;
    private double ph;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;


    public Water() {}

    public Water(String name, String type, double mass, double salinity, double ph, double purity, double turbidity
    , double contaminantIndex, boolean isFrozen) {
        this.name = name;
        this.mass = mass;
        this.salinity = salinity;
        this.ph = ph;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
        this.type = type;
    }
}