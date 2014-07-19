import java.util.ArrayList;
/* A collection class for GradeSections, representative of a Problem */

public class Problem {
  private String                      name;
  private ArrayList<GradeSection> sections;
  
  public Problem (String n) {
    name = validate(n);
    sections = new ArrayList<GradeSection>();
  }
  
  // Prompts the user, asking if the section
  public void addGradeSection(String s) {
    sections.add(new GradeSection(s));
  }
  
  // Cuts off the [## Marks] at the end of unparsed problems
  public String validate(String n) {
    String[] broken = n.split(" ");
    String response = "";
    for (String s : broken) {
      if (s.charAt(0) == '[')
        break;
      response += s;
    }
    return response;
  }
  
  public boolean addGradeItemToLatestSection(String s) {
    try {
      sections.get(sections.size()-1).addGradeItem(s);
      return true;
    }
    catch (Exception e) { return false; }
  }
  
  public String toString() {
    return name;
  }
  
  public GradeSection[] getSections() {
    GradeSection[] g = new GradeSection[sections.size()];
    g = sections.toArray(g);
    return g;
  }
}