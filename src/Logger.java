import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;

/*
Created by Will Kercher 2/29/18
Purpose: This class is to handle logging process flags, warnings and errors.
 */
public class Logger {

    Logger(){    }
    public static void main(String[] args){
        addLog("Test LOG OINK!!!!");
    }
    //Takes in a string and appends that to the end of the log file!
    public static void addLog(String newLog){
        makeDirectory.makeNewDir("logs");// make sure there is a logs directory
        String logName = "logs/" + java.time.LocalDate.now().toString() + ".log"; // make sure you have a log for that day
        System.out.println(logName);
        //create logfile
        createLogFile(logName);
        //format line to be added to log "time logMessage \n"
        newLog = Instant.now().atZone(ZoneId.of("GMT")) + "  " + newLog + "\n";
        try {
            Files.write(Paths.get(logName), newLog.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

    //Checks if Logfile exists, if it doesn't it makes it.
    static void createLogFile(String logName){
        File logFile = new File(logName);
        // if the directory does not exist, create it
        if (!logFile.exists()) {
            System.out.println("creating log: " + logFile.getName());
            boolean result = false;
            try{
                result = logFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if(result) {
                System.out.println("Log created: " + logName);
            }
        }
    }

}
