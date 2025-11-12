package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Water extends Entity{
    private String type;
    private double salinity;
    private double ph;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;


    public Water() {}

    public Water(String name, String type, double mass, double salinity, double ph, double purity, double turbidity
    , double contaminantIndex, boolean isFrozen) {
        super(name, mass);
        this.salinity = salinity;
        this.ph = ph;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
        this.type = type;
    }
}