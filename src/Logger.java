import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;

/**
Created by Will Kercher 2/29/18
Purpose: This class is to handle logging process flags, warnings and errors.
 */
public class Logger {

    Logger() throws InstantiationException {
        throw new InstantiationException("Cannot create instance of Logger!");
    }


    /**
     * Method to append a string to the end of the log file
     * If nothing has been logged today will create a new file named <today's date(yyyy-mm-ddd)>.log
     */

    public static void addLog(String newLog){
        MakeDirectory.makeNewDir("logs");// make sure there is a logs directory
        String logName = "logs/" + java.time.LocalDate.now().toString() + ".log";
        createFile(logName);
        //format line to be added to log "time logMessage \n"
        newLog = Instant.now().atZone(ZoneId.of("GMT")) + "  " + newLog + "\n";
        try {
            Files.write(Paths.get(logName), newLog.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.out.println("Warning: Failed to write to log file");
        }

    }

    /**
     * Method to make file if it doesn't exist
     */
    private static void createFile(String fileName){
        File file = new File(fileName);
        // if the directory does not exist, create it
        if (!file.exists()) {
            System.out.println("creating file: " + file.getName());
            boolean result = false;
            try{
                result = file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if(!result) {
                System.out.println("Failed to create file: " + file.getName());
            } else {
                System.out.println("File created");
            }
        }
    }

}
