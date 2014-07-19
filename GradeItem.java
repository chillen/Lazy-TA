import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
/* This class represents the individual grade item within a section */

public class GradeItem {
  private double              max;
  private double              mark;
  private String              description;
  private ArrayList<String>   comments; // Any comments which accrue over time
  
  // If you just receive a string, we'll assume it needs to be parsed
  public GradeItem(String s) {
    description = findDescription(s);
    max = findMax(s);
    mark = 0;
    comments = new ArrayList<String>();
  }
  
  public GradeItem(String d, double m) {
    description = d;
    max = m;
    mark = 0;
    comments = new ArrayList<String>();
  }
  
  // Goes through the original line from the rubric and pulls out the description
  // Description is the 3rd -> end words
  private static String findDescription(String s) {
    String response = "";
    String[] broken = s.trim().split(" ");
    
    try {
      for (int i = 3; i < broken.length; i++) 
        response+=broken[i]+" ";
    }
    catch (Exception e) { return s; }
    return response;
  }
  
  private static double findMax(String s) {
    String response = "";
    String[] broken = s.trim().split(" ");
    
    try {
      for (int i = 0; i < broken[0].length(); i++)  {
        try {
          double d = Double.parseDouble(""+broken[0].charAt(i));
          response+=broken[0].charAt(i);
        }
        catch(Exception e) {}
      }
     }   
    catch (Exception e) { return Double.parseDouble(s); }
    return Double.parseDouble(response);
  }
  
  // Currently, this does not allow for bonus marks. It takes in the number and
  // sets the mark, up to the maximum. Returns the mark that was added.
  public double addMark(double d) {
    if (d > max)
      mark = max;
    else
      mark = d;
    return mark;
  }
  
  // Returns a string of all comments accrued over time. Checks if there's a period. If not, adds one.
  public String getCommentString() {
    String s = "";
    try {
      if (comments.isEmpty())
        throw new IndexOutOfBoundsException();
      for (String c : comments) {
        if (c.trim().charAt(c.length()-1) != '.') //If there isn't a period at the end, add one
          c += ".";
        s+=c;
      }
    }
    // No comments, return the description
    catch (Exception e) {
      return description;
    }
    return s;
  }
  
  // Adds a comment to the list of comments
  public void addComment(String s) {
    comments.add(s);
  }
  
  public String toString() {
    DecimalFormat df = new DecimalFormat("0.00");
    return String.format("%s/%s : %s", df.format(mark), df.format(max), getCommentString());
  }
  
  // Tests the grade item and returns the grade received
  public double test() {
    Scanner   sc = new Scanner(System.in);
    double    m;
    String    comment;
    
    while(true) {
      System.out.print("      " + this + ". Enter your mark: " );
      try {
        m = sc.nextDouble();
        break;
      }
      catch (Exception e) {
        RubricParser.systemMessage("Invalid input. Try again.");
        sc.nextLine();
      }
    }
    System.out.print("      Please type any comments you have (leave blank for rubric description): " );
    comment = sc.nextLine();
    comment = sc.nextLine();
    
    comments.add(comment);
    return addMark(m);
  }
  
  public double getMax() { return max; }
}