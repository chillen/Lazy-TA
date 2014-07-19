import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.text.DecimalFormat;

////////////////////////////////////////////////////////////////////////////////
// Class:         Tester                                                      //
// Dependencies:  GradeItem, GradeSection, Problem, RubricParser              //
// Description:   This is a testing suite for TA's in COMP1406. Basically, it //
//                takes in a rubric (details for formatting in RubricParser)  //
//                and pulls out all relevant information and places it into   //
//                objects. Then it asks the user to put in a mark for the     //
//                grade item and any additional comments. It outputs this to  //
//                a text file.                                                //
// Usage:         Usage is simple. For a basic assignment with no automation, //
//                simply running the program with the rubric in the same      //
//                folder is enough. It will output to "comments.txt". If you  //
//                pass a command line argument, it will output to that        //
//                filename instead.                                           //
////////////////////////////////////////////////////////////////////////////////

public class Tester {
  // Mark and max for the entire assignment. These should be automated, don't touch
  private static double         max  = 0;
  private static double         mark = 0;
  private static DecimalFormat  df;
  private static String         additionalComments;
  
  // Args[0]: The first command line argument is the filename to output report
  public static void main(String[] args) {
    if (!RubricParser.init()) {
      RubricParser.systemMessage("Failed to initialize.");
      return;
    }
    testAll();
    if (args.length > 0)
      printReport(args[0]);
    else
      printReport();
  }
  
  /* This method runs through everything parsed from the rubric and tests it.
   * There is one check that looks to see if a section is flagged as "automated"
   * by the automated method. If it is, it continues through the loop. Otherwise,
   * the "automated" function will handle our marking for us. It then asks if
   * there are any additional comments by the TA.
   */
  public static void testAll() {
    for (Problem p : RubricParser.getProblems()) {
      System.out.println();
      System.out.println("  == Current Problem: " + p + " ==");
      for (GradeSection g : p.getSections()) {
        System.out.println("    = Currently Marking: " + g + " =");      
          if (automated(p, g))
            continue;
          for (GradeItem i : g.getGradeItems()) {
            mark += i.test();
            max  += i.getMax();
          }
      }
    }
    Scanner sc = new Scanner (System.in);
    String  temp;
    System.out.println();
    System.out.print("*** Marking is completed. Please type any additional comments you have here (leave blank for none): ");
    if (!(temp = sc.nextLine()).equals(""))
      additionalComments = temp;
  }
  
  // Displays the report to the screen
  public static void displayReport() {
    df = new DecimalFormat("#0.00");
    
    for (Problem p : RubricParser.getProblems()) {
      System.out.println("# " + p + " #");
      for (GradeSection g : p.getSections()) {
        System.out.println("## " + g + " ##");
        for (GradeItem i : g.getGradeItems()) 
          System.out.println(i);
      }
      System.out.println();
    }
    
    System.out.println("Total Mark: " + df.format(mark) + "/"+df.format(max)+ ", "+df.format((mark/max)*100)+"%");
    System.out.println();
    if (additionalComments != null) {
      System.out.println("Additional Comments:");
      System.out.println("  " + additionalComments);
    }
  }
  
  /* Prints out the results to a text file, using the input parameter name as the
   * filename. 
   */
  
  public static void printReport(String name) {
    try {
      PrintStream console = System.out; //Store for later
      FileOutputStream fos = null;
      fos = new FileOutputStream(name);
      PrintStream ps = new PrintStream(fos);
      
      System.setOut(ps);
      displayReport();
      System.setOut(console);
    }
    catch (Exception e) {
      RubricParser.systemMessage("ERROR: Exception occurred while printing report.");
      RubricParser.systemMessage("       " + e);
    }
  }
  
  // Print the report and default to the file name "comments.txt"
  public static void printReport() {
    printReport("comments.txt");
  }
  
  /* If any of your tests are automated, put them here. The format should be
   * relatively simple to follow.
   * Input: p, Problem. g, Grade Section. These two things combined should be
   * enough information to determine exactly what section you're dealing with.
   * Write an if statement to check if the trimmed p and g (don't use the input
   * parameters, they may have whitespace issues) match what you're looking for 
   * and RETURN TRUE if the test was indeed automated.
   * EXAMPLE:
   *   Let's say Problem 2, Correctness is automated through a TestProblem2 class.
   *   The main method of TestProblem2 will handle everything for us. Usage would be
   *   if (problem.getName().equals("Problem 2") && section.getName().equals("Correctness")) {
   *     TestProblem1.main(void);
   *     return true;
   *   }
   * NOTE: Soon, I hope to have a "test object" which we can use to unify interactions
   *       between tests. This way we can pull marks easily without having to hack things
   *       together.
   * Also NOTE: This function should do nothing but return false if there is no automation
   */
  public static boolean automated(Problem p, GradeSection s) {
    return false;
  }
}