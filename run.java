import java.util.ArrayList; // SelfExplainatory
import java.io.*; // SelfExplainatory
import java.sql.Connection; // SelfExplainatory
import java.sql.DriverManager; // SelfExplainatory
import java.sql.Statement; // SelfExplainatory

/**
 * @author- Mudigonda Himansh This is the "SWEAR WORD DETECTION APP" This app is
 *          built to consor the lyrics and clean up the explicit content based
 *          on the ever growing database oof swear words. This API can be
 *          converted and code-corrected to suit your needs.
 */

/*
 * INPUT taken from the file POST INPUT pushed into the MySQL Database's
 * "UncensoredLyrics" column PREPROCESS then the lyrics is sliced with ' ' as
 * the delimitter. PROCESS word comparision with the elements in "Database.cvs"
 * OUTPUT printed out in the terminal and is pushed into the MySQL Database's
 * "ConsoredLyrics" column.
 */

public class run extends FilterClass {
    static String uncensoredLyrics = "";

    public static void main(String[] args) throws Exception {
        /**
         * Now lets see what happens in the main function
         */
        FileReader fr;
        BufferedReader input;
        // Starting with a try that help to handle exceptions incase there are any
        // problems while reading or locating the file
        try {
            fr = new FileReader("/home/mudigonda/Documents/Semester-3/OOPS/Assignments/Project/Lyrics.txt");
            // This is the path to the file that has the lyrics
            input = new BufferedReader(fr);
            // We convert it to the BufferedReader input to process the lyrics line by line
            String currentLine = "";
            // Initializing the current line to NULL
            while ((currentLine = input.readLine()) != null) {
                // While there are more lines in the file
                uncensoredLyrics = " " + currentLine;
                // This is just a statement that helps us in printing out the lyrics in the
                // final call
                runCheck(currentLine);
                // Censor happens here
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            // Stack trace is printed to better dubug when the program fails
            System.out.println(
                    "Looks like there's some error in the path to the file that you entered. \nCheck and try again!");
            // Custom output indicating what could be the error in inglesh :P
        }
    }

    public static void runCheck(String input) {

        String censoredLyrics = FilterClass.getCensoredText(input);
        // The final censored lyrics is collected as the getCensoredText returns a
        // String as a return type
        // System.out.println(uncensoredLyrics); !!! NVM tthis is jusst for debugging my
        // own code !!!
        System.out.println(censoredLyrics);
        // I just print out the censored lyrics
        try { // We use the try catch to again hadle the exception that would be raised during
              // the connection with the MySQL database using the 3306 port!
            Class.forName("java.sql.DriverManager");
            // We are just initiating
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/OOPS_Project",
                    "mudigonda", "password@sql");
            // Connecting to the database with the following details
            /**
             * Connection type -> JDBC Backend Database -> MySQL Type of database -> RDBMS
             * PORT -> 3306 (auto allocated) uname -> mudigonda password -> password@sql
             */
            Statement stmt = (Statement) con.createStatement();
            // This helps us write the command(query in the DATABASE terminology) that is to
            // be executed, explicitly specified to be a statement.
            String query = "insert into LyricsDatabase values('" + uncensoredLyrics + "','" + censoredLyrics + "');";
            // Here we build the query that goes into the connection as a statement
            stmt.executeUpdate(query);
            // Executing the query
        } catch (Exception e) {
            // Helps in catching the exceptions
            e.printStackTrace();
            // Prints out the stackTrace
            System.out.println("Dude there's some problem! Am not able to connect to the database!");
        }

    }
}
