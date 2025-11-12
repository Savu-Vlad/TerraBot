package entities;
import input.Section;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {
    protected String name;
    protected double mass;

    public Entity() {}

    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }

    double normalizeScore(double score) {
        return Math.max(0, Math.min(100, score));
    }

    double roundScore(double score) {
        return Math.round(score * 100.0) / 100.0;
    }
}