package input;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Section {
    private int x;
    private int y;

    public Section(int x, int y) {
        this.x = x;
        this.y = y;
    }
}