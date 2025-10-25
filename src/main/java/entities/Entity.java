package entities;
import input.Section;
import java.util.List;

public abstract class Entity {
    protected String name;
    protected double mass;
    protected List<Section> sections;

    public Entity() {}

    public Entity(String name, double mass, List<Section> sections) {
        this.name = name;
        this.mass = mass;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}