package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import map.Map;

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
    protected final int oneHundred = 100;
    @JsonIgnore
    protected final int zero = 0;

    public Entity() {}

    public Entity(final String name, final double mass, final String type) {
        this.type = type;
        this.name = name;
        this.mass = mass;
    }

    /**
    * Function that normalizes the score
    * */
    public final double normalizeScore(final double score) {
        return Math.max(zero, Math.min(oneHundred, score));
    }

    /**
    * function that wil round the score
    * */
    public final double roundScore(final double score) {
        return Math.round(score * normalizationFactor) / normalizationFactor;
    }

    /**
    * function that will be implemented in each subclass of Entity to update the environment
    * */
    public abstract void updateMapWithScannedObject(Map map, Map.MapCell cell, int timestamp);
}
