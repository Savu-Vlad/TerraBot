package entities;
import lombok.Getter;

@Getter
public enum QualityLevel {
    POOR(0, 40, "poor"),
    MODERATE(40, 70, "moderate"),
    GOOD(70, 100, "good");

    private final double minScore;
    private final double maxScore;
    private final String identifier;

    QualityLevel(final double minScore, final double maxScore, final String identifier) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.identifier = identifier;
    }

    /**
     * Method that returns the quality level from the score that is provided
     * */
    public static QualityLevel fromScore(final double score) {
        for (QualityLevel level : QualityLevel.values()) {
            if (score >= level.minScore && score < level.maxScore) {
                return level;
            }
        }

        return GOOD;
    }
}



