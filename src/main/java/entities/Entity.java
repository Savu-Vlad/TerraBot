package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {
    protected String name;
    protected double mass;
    protected String type;
    @JsonIgnore
    protected int x;
    @JsonIgnore
    protected int y;
    @JsonIgnore
    protected final double normalizationFactor = 100.0;
    @JsonIgnore
    private final int lowerBound = 0;
    @JsonIgnore
    private final int upperBound = 100;

    public Entity() {

    }

    public Entity(final String name, final double mass, final String type) {
        this.type = type;
        this.name = name;
        this.mass = mass;
    }

    /**
    * Function that normalizes the score
    * */
    public final double normalizeScore(final double score) {
        return Math.max(lowerBound, Math.min(upperBound, score));
    }

    /**
    * function that wil round the score
    * */
    public final double roundScore(final double score) {
        return Math.round(score * normalizationFactor) / normalizationFactor;
    }
}
