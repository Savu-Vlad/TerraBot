package robot;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

//might transform this into a record later !!!

@Getter
@Setter
public class KnowledgeBase {
    private String topic;
    private ArrayList<String> facts;

    public KnowledgeBase(String topic) {
        this.topic = topic;
        this.facts = new ArrayList<>();
    }
}