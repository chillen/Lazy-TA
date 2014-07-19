import java.util.ArrayList;

/* This class represents a specific grade section in the rubric such as Style, 
 * Design, or Correctness
 */
public class GradeSection {
  private String                name;
  private ArrayList<GradeItem>  children;  // All sub grade items within the section
  
  // Constructor validates the string input before setting the name
  public GradeSection(String n) {
    name = validate(n);
    children = new ArrayList<GradeItem>();
  }
  
  // Converts strings with "marks}" or "mark}" in it to just the name
  private static String validate(String n) {
    String[] broken = n.split(" ");
    String valid = "";
    
    try {
      for (int i = 0; i < broken.length; i++) {
        if (broken[i].charAt(0) == '{')
          break;
        valid += broken[i] + " "; // The spaces were removed, so add a new one
      }
    }
    catch (Exception e) {}
    
    return valid;
    
  }
  
  public void addGradeItem(String s) {
    children.add(new GradeItem(s));
  }
  
  public String toString() {
    return name;
  }
  
  public GradeItem[] getGradeItems() {
    GradeItem[] items = new GradeItem[children.size()];
    items = children.toArray(items);
    return items;
  }
  
}