package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import map.Map;
import robot.Robot;

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
    protected final int one = 1;
    @JsonIgnore
    protected final int zero = 0;
    @JsonIgnore
    protected final int two = 2;
    @JsonIgnore
    protected final int eightyTwo = 82;
    @JsonIgnore
    protected final int eightyFour = 84;
    @JsonIgnore
    protected final double zeroPointFive = 0.5;
    @JsonIgnore
    protected final double zeroPointZeroOne = 0.01;
    @JsonIgnore
    protected final double zeroPointThree = 0.3;
    @JsonIgnore
    protected final double zeroPointSix = 0.6;
    @JsonIgnore
    protected final double zeroPointOne = 0.1;
    @JsonIgnore
    protected final int oneFortyTwo = 142;
    @JsonIgnore
    protected final double zeroPointZeroFive = 0.05;
    @JsonIgnore
    protected final double zeroPointTwo = 0.2;
    @JsonIgnore
    protected final double zeroPointSeven = 0.7;
    @JsonIgnore
    protected final int fifteen = 15;
    @JsonIgnore
    protected final int sixtyFive = 65;
    @JsonIgnore
    protected final int thirty = 30;
    @JsonIgnore
    protected final int oneThousand = 1000;
    @JsonIgnore
    protected final int threeHundredFifty = 350;
    @JsonIgnore
    protected final int seventyEight = 78;
    @JsonIgnore
    protected final double zeroPointFifteen = 0.15;
    @JsonIgnore
    protected final double sevenPointFive = 7.5;
    @JsonIgnore
    protected final double sixPointZero = 6.0;
    @JsonIgnore
    protected final double zeroPointNine = 0.9;
    @JsonIgnore
    protected final double zeroPointFour = 0.4;
    @JsonIgnore
    protected final double zeroPointEight = 0.8;
    @JsonIgnore
    protected final double zeroPointZero = 0.0;
    @JsonIgnore
    protected final double threePointZero = 3.0;
    @JsonIgnore
    protected final double onePointTwo = 1.2;
    @JsonIgnore
    protected final double onePointFive = 1.5;
    @JsonIgnore
    protected final double onePointOne = 1.1;
    @JsonIgnore
    protected final int eighty = 80;
    @JsonIgnore
    protected final double twoPointTwo = 2.2;
    @JsonIgnore
    protected final int five = 5;
    @JsonIgnore
    protected final int ten = 10;
    @JsonIgnore
    protected final int seventyFive = 75;
    @JsonIgnore
    protected final int fifty = 50;


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
    public abstract void updateMapWithScannedObject(Robot robot,
                                                    Map map,
                                                    Map.MapCell cell,
                                                    int timestamp);
}
