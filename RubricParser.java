import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

////////////////////////////////////////////////////////////////////////////////
// Class:         RubricParser                                                //
// Dependencies:  GradeItem, GradeSection, Problem, RubricParser              //
// Description:   This takes in a rubric file for an assignment and searches  //
//                through looking for specific patterns and pulling out the   //
//                information such as a grade item, section, or problem. It   //
//                then stores this information in a way we can use for testing//
//                purposes.                                                   //
// Usage:         Simply drop a rubric into the folder with this and in your  //
//                program, use the static method getProblems() to get an array//
//                of all of the problems from the rubric. It will parse if it //
//                needs to. There's also a handy system message function if   //
//                you want to use that, it was originally just for debugging. //
// Terms:         There are some terms used throughout this worth defining.   //
//                Grade Item:    A grade item is the lowest level entity. It  //
//                               has a mark, a max, a description. It is what //
//                               the student is being marked for and the mark //
//                               received.                                    //
//                Grade Section: The grade section is the category the grade  //
//                               items are kept and acts as a collection. It  //
//                               stores the grade items under it and has some //
//                               helpers. This might be "style" or            //
//                               "efficiency" or "correctness" or something   //
//                Problem:       A problem is an overall collection of        //
//                               sections. One problem might be made up of    //
//                               multiple sections.                           //
// Rubric Format: The rubric has to be formatted in a specific way, but it's  //
//                hopefully general enough for most purposes.                 //
//                File Name:     rubric##.txt. Eg: rubric01.txt rubric99.txt  //
//                Problem:       The first word of the line is "Problem". It  //
//                               also skips over any "Problem 0" in case that //
//                               is a thing.                                  //
//                Grade Section: All sections must end with "mark}" or "marks}//
//                               This means you might have something like     //
//                               Correctness {10 marks}                       //
//                Grade Item:    This is more specific. It must be:           //
//                               _/## : Description or /## : Description or   //
//                               __/## : Description                          //
// Modifying:     If you would like to extend or modify this to fit your      //
//                rubric structure better, make sure you double check that    //
//                the GradeSection, and Problem validate functions still      //
//                work. In GradeItem, this is findMax and findDescription.    //
////////////////////////////////////////////////////////////////////////////////

public class RubricParser {

  static private File                rubricFile;
  static private ArrayList<Problem>  problems; // The problems collection just simplifies interactions
  
  public static boolean init() {
  
    // Step 1: Make sure there is a rubric for RubricParser to parse
    systemMessage("Loading rubric...");
    if (!establishRubric()) 
      return false;
    // Step 2: Parse the rubric
    try {
      systemMessage("Parsing rubric...");
      parse();
    }
    catch(Exception e) {
      systemMessage("ERROR: Failed to parse rubric. Exception thrown.");
      systemMessage("       " + e);
      return false;
    }
    systemMessage("Rubric parsed successfully.");
    return true;
  }
  
  /* Takes in a message and prints it to the screen following system format */
  public static void systemMessage(String m) {
    System.out.println("+ SYSTEM MESSAGE: "+m);
  } 
  
  /* Loops through possible rubric names until it finds one and sets
   * the static FileReader accordingly. It will find the very first rubric
   * that matches. Searches for files named rubric01.txt ... rubric99.txt.
   * Returns true if it could find the rubric and false if it can't.
   */
  private static boolean establishRubric() {
    for (int i = 0; i < 100; i++) {
      try {
        rubricFile = new File(String.format("rubric%02d.txt", i)); // Format to include leading 0's for 1-9
        if (rubricFile.exists()) {
          systemMessage("File [" + rubricFile.getName() + "] has been loaded successfully.");
          return true;
        }
      }
      catch (Exception e) {} // We really don't care what happens here. We expect it to fail 98/99 times after all
    }
    
    systemMessage("Rubric could not be loaded. Is it in the right directory and does it follow proper naming convention (rubric##.txt)?");
    return false;
  }
  
  /* THE MEAT: The rubric parser.
   * This function does a few things to accomplish its goal:
   * 1. Initialize the problems list so it's nice and free to add away.
   * 2. Look for problems. A problem is a line which contains the string "problem" at position 0 and ! '0' at position 1 (rubric problem)
   * 3. Add the problem to the array of problems
   * 4. Search under the problem for sections defined as having the last 5 characters, not including whitespace, be "marks}"
   * 5. Find all GradeItems underneath. It will skip anything until it finds at least one. A grade item starts with /
   */
  public static void parse() throws Exception {
    FileReader fileReader = new FileReader(rubricFile);
    BufferedReader reader = new BufferedReader(fileReader);
    String           line; 
    // Let's reset/initialize the problems list. Start clean.
    problems = new ArrayList<Problem>();
    
    // Let's check out the file!
    while ((line = reader.readLine()) != null) {
      if (isProblem(line))
        problems.add(new Problem(line));
      else if (isGradeSection(line))
        problems.get(problems.size()-1).addGradeSection(line);
      else if (isGradeItem(line))
        problems.get(problems.size()-1).addGradeItemToLatestSection(line);
    }
    
    
  }

  // Takes in a string, breaks it up, and returns true if it's a problem or not
  private static boolean isProblem (String s) {
    String[] broken = s.split(" ");
    return broken.length > 2 && broken[0].equals("Problem") && !broken[1].equals("0");
  }
  
  // Takes in a string, breaks it up, and returns true if it's a grade section or not
  private static boolean isGradeSection (String s) {
    String[] broken = s.split(" ");
    return broken.length > 2 && (broken[broken.length-1].equals("marks}") || broken[broken.length-1].equals("mark}"));
  }
  
  // Takes in a string, breaks it up, and returns true if it's a grade item or not
  private static boolean isGradeItem (String s) {
    String[] broken = s.trim().split(" ");
    return broken.length > 0 && broken[0].length() > 0 
          && (broken[0].charAt(0) == '/'
          ||  (broken[0].length() > 1 && broken[0].charAt(0) == '_' && broken[0].charAt(1) == '/')
          ||  (broken[0].length() > 2 && broken[0].charAt(1) == '_' && broken[0].charAt(2) == '/'));
  }
  
  // Enforce a singleton-esque approach to this file
  // If problems hasn't been initiated, parse the rubric and get it. Otherwise
  // Pass the user the previously parsed copy of it in an array
  public static Problem[] getProblems() {
    if (problems == null) 
      init();
    
    Problem[] response = new Problem[problems.size()];
    response = problems.toArray(response);
    return response;
  }
}