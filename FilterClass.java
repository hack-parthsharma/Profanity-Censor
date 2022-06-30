import java.io.BufferedReader; // SelfExplainatory
import java.io.FileReader; // SelfExplainatory
import java.io.IOException; // SelfExplainatory
import java.util.ArrayList; // SelfExplainatory
import java.util.Arrays; // SelfExplainatory
import java.util.HashMap; // SelfExplainatory
import java.util.Map; // SelfExplainatory

public class FilterClass {
  // Create a map that help in finding the bad words
  private static int largestWordLength = 0;
  private static Map<String, String[]> allBadWords = new HashMap<String, String[]>();

  // Responsible for getting the lyrics censored
  public static String getCensoredText(final String input) {
    // Loads the bad words
    loadBadWords();
    if (input == null) {
      // Dont do this, its too simple
      return "";
    }
    // Used to covert all to lower case for wasy finding
    String modifiedInput = input;
    modifiedInput = modifiedInput.toLowerCase().replaceAll("[^a-zA-Z]", "");
    ArrayList<String> badWordsFound = new ArrayList<>();
    // Array list is formed here
    for (int start = 0; start < modifiedInput.length(); start++) {
      for (int offset = 1; offset < (modifiedInput.length() + 1 - start) && offset < largestWordLength; offset++) {
        String wordToCheck = modifiedInput.substring(start, start + offset);
        // Blah blah blah, the n^2 complex iteration for finding the pattern is done
        // here...
        if (allBadWords.containsKey(wordToCheck)) {
          // Now comes the main part. If any of them match, then
          String[] ignoreCheck = allBadWords.get(wordToCheck);
          // It is copied into a new string
          boolean ignore = false;
          // Ignore the word turn false which is like an alert flag
          for (int stringIndex = 0; stringIndex < ignoreCheck.length; stringIndex++) {
            if (modifiedInput.contains(ignoreCheck[stringIndex])) {
              // Is being compared to all the words in the CSV file
              ignore = true;
              break;
            }
          }
          if (!ignore) {
            // Based on the value of the ignore bool, the word is added into the Array list
            badWordsFound.add(wordToCheck);
          }
        }
      }
    }
    // God, am tired adding comments :(
    String inputToReturn = input;
    for (String swearWord : badWordsFound) {
      // This is used to replace the words with *
      char[] charsStars = new char[swearWord.length()];
      Arrays.fill(charsStars, '*');
      // Here's where that happs
      final String stars = new String(charsStars);
      // This produces a new string that has the line censored lines
      inputToReturn = inputToReturn.replaceAll("(?i)" + swearWord, stars);
      // This is again used to replace all unknown characters(slang) with *
    }
    return inputToReturn;
  }

  // This is the function that loads the CSV file
  private static void loadBadWords() {
    int CounterFlag = 0;
    try {
      // The try here is used to pass the exceptions to the catch while accessing the
      // CSV file
      FileReader fr = new FileReader("/home/mudigonda/Documents/Semester-3/OOPS/Assignments/Project/Database.csv");
      // This is where the path to the CSV is specified
      BufferedReader reader = new BufferedReader(fr);
      // This is converted to the bufferInput for reading it.
      String CurrentLine = "";
      // Explained before
      while ((CurrentLine = reader.readLine()) != null) {
        CounterFlag++;
        String[] content = null;
        try {
          if (1 == CounterFlag) {
            continue;
          }
          // Self Explainatory
          content = CurrentLine.split(",");
          if (content.length == 0) {
            continue;
          }

          final String Word = content[0];

          if (Word.startsWith("-----")) {
            continue;
          }

          // This is just to collect the largest word
          if (Word.length() > largestWordLength) {
            largestWordLength = Word.length();
          }

          String[] IgnoreWordCombos = new String[] {};
          if (content.length > 1) {
            IgnoreWordCombos = content[1].split("_");
          }

          // Self Explainatory
          allBadWords.put(Word.replaceAll(" ", "").toLowerCase(), IgnoreWordCombos);
        } catch (Exception exc) {
          // The excepetions are handled here
          exc.printStackTrace();
          System.out.println("Error: inner try");
        }
      }
      // I'll close the streams and buffers
      fr.close();
      reader.close();
    } catch (IOException exc) {
      // The excepetions are handled here
      exc.printStackTrace();
      System.out.println("Error: outer try");
    }
  }

}
