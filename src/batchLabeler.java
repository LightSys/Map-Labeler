import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Created by Will Kercher 2/29/18
The goal of this application is to take in the csv file that has the map names and
 */
public class batchLabeler {
    public batchLabeler(){}
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
                    LabelerMain.labelPicture(mapsLoc+fileName,countryName);
                }
                else{
                    System.out.println("Was not able to find both FileName and CountryName from line: " + line);
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error looks like File can't be read");
        }
    }
    public static void main(String[] args){
        String csvName = "maps.csv";
        String mapsLoc = "maps/";
//        batchLabeler(csvName, mapsLoc);
        //todo mkdir for output use other method from FileNameToCountrNAme

    }
}
