import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by Will Kercher on 2/29/18.
 */

public class BatchLabeler {
    public BatchLabeler(){}

    /**
     * Method to process a CSV formatted <image.xxx><label> and label the images
     * @param mapsLoc directory that contains the image files that you want to label
     */
    public static void ProcessCSV(String csvName, String mapsLoc){
        //ReadCSV
        try {
            BufferedReader in = new BufferedReader(new FileReader(csvName));
            Pattern selectFileName = Pattern.compile(".+?(?=,)"); // regex to just select before the comma "," in the csv
            Pattern selectCountryName = Pattern.compile("(?<=,).*"); // regex to select after the comma "," in the csv
            String line;
            System.out.println("Reading CSV: " + csvName);
            while ((line = in.readLine()) != null) {
                //parse line for filename
                Matcher fileNameMatcher = selectFileName.matcher(line);
                //parse line for country name
                Matcher countryNameMatcher = selectCountryName.matcher(line);
                if(fileNameMatcher.find() && countryNameMatcher.find()){
                    String fileName = fileNameMatcher.group(0);
                    String countryName = countryNameMatcher.group(0);
                    try {
                        Labeler.labelPicture(mapsLoc + fileName, countryName);
                    }
                    catch(IllegalArgumentException e){
                        e.printStackTrace();
                        Logger.addLog("could not label" + fileName + "the label was too big to fit the picture");
                    }
                }
                else{
                    Logger.addLog("Was not able to find both FileName and CountryName from line: " + line);
                }
            }
        }
        catch (IOException e) {
            Logger.addLog("Error looks like " + csvName + " can't be read");
        }
    }

    /**
     * Method to process a directory containing image files and label the images with their names
     */
    public static void ProcessDir(String directory){
        File dir = new File(directory);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String fileName = child.getName();
                try {
                    String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");
                    Labeler.labelPicture(directory + fileName, fileNameWithOutExt);
                }
                catch(IllegalArgumentException e){
                    e.printStackTrace();
                    Logger.addLog(fileName + ": " + e.getMessage());
                }
            }
        } else {
            String message = "Could not open dir " + directory;
            System.out.println(message);
            Logger.addLog(message);
        }
    }
}
