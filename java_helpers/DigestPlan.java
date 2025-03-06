import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class was developed to obtain a more compact representation of a plan obtained with ENHSP, when using the numerical planner.
 * 
 * The class was not tested extensively. Please feel free to adapt it however you like.
 */
public class DigestPlan {

  private static void printShortPlan(Scanner s) {
    System.out.println("Short plan:");

    Pattern enhspPattern = Pattern.compile("\\(([\\d\\w\\s]*)\\)");
    Pattern metricPattern = Pattern.compile("^[\\w\\s]+:\\s([\\w\\s]+)$");

    String cur;
    // Reach plan begin
    while (s.hasNextLine()) {
      cur = s.nextLine();
      if (cur.contains("Found Plan:\n") || cur.contains("legal plan")) break;
    }

    boolean isChange = false;
    int index = 0;
    while (s.hasNextLine()) {
      cur = s.nextLine();

      // BASE CASE == REACHED END OF PLAN
      if (cur.contains("Plan-Length") || cur.contains("time spent")) break;

      if (cur.toLowerCase().contains("change_value")) {
        isChange = true;
      }

      // Avoid jargon
      if (cur.toLowerCase().contains("move_automatons") || 
          cur.toLowerCase().contains("check_added_variables") ||
          cur.toLowerCase().contains("reach-goal")) {
        continue;
      }

      // Skip sync if we already printed about changing value
      if (cur.toLowerCase().contains("sync")) {
        if (isChange) {
          isChange = false;
          continue;
        }
      }

      Matcher m = enhspPattern.matcher(cur);
      if (!m.find()) {
        m = metricPattern.matcher(cur);
      }
      if (m.find()) {
        StringBuilder b = new StringBuilder(index++ + ": ");
        b.append(m.group(1));
        System.out.println(b);
      } else {
        System.out.println(cur);
      }
    }
  }

  public static void main(String[] args) {

    if (args.length != 1) {
      throw new Error("Pass as argument the file name of the file to digest!");
    }
 
    File file = new File(args[0]);
    try {
      Scanner s = new Scanner(file);
      printShortPlan(s);
      s.close();
    } catch (FileNotFoundException e) {
      System.err.println("Was unable to find file to parse!");
      e.printStackTrace();
    }
  }
}