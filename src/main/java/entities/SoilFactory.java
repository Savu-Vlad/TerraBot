package entities;
import input.Section;
import java.util.List;

public class SoilFactory {
    public Soil createSoil(String type, String name, double mass, List<Section> sections) {
        Soil soil = switch (type) {
          case "DesertSoil" -> new DesertSoil();
          case "TundraSoil" -> new TundraSoil();
          case "GrasslandSoil" -> new GrasslandSoil();
          case "ForestSoil" -> new ForestSoil();
          case "SwampSoil" -> new SwampSoil();
          default -> throw new IllegalArgumentException("Unknown soil type: " + type);
        };
//aici ar trebui sa pun si parametrii la constructori, si sa dau tot in factory builder ca sa pot sa folosesc doar ce ma intereseaza in functie de tip!!!
        soil.setName(name);
        soil.setMass(mass);
        soil.setSections(sections);

        return soil;
    }
}