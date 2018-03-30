import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/*
Created by Will Kercher 2/26/18
The goal of this application is to create a dictionary that takes the image name for a map from the CIA world factbook,
and to return the country name for that map.
For example the file name "al-map.gif" would return "ALBANIA"
 */
public class FileNameToCountryName {
    //fixSplitName(String name) takes a string input, removes comma split, and corrects name
    //ex: "Bahamas, The" -> "The Bahamas" and "Congo, Republic of the" -> "The Republic of the Congo"
    private static String fixSplitName(String name){
        System.out.println("Comma in country name: " + name);
        String[] parts = name.split(",\\s"); // Array splits name
        String a = parts[1];//switch sides
        String b = parts[0];//switch sides
        System.out.println("Part a: " + a);
        System.out.println("Part b: " + b);
        System.out.println("a+b: " + a + b);
        return a + b;
    }
    private static void createCSV( ZipFile factbook) throws IOException {
        ArrayList<String> filenameToCountryName = new ArrayList<>();
        try {
            Enumeration entries = factbook.entries();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            int countryName = 0;
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                //System.out.println("Read " + entry.getName() + "?");
                //String inputLine = input.readLine();
                //check html for each country example: XX.html
                if (entry.getName().matches(".*/[a-z][a-z].html")) { //is a country html
                    String abbreviation = entry.getName();
                    abbreviation = abbreviation.replaceAll(".*/","");  // removes prefix
                    abbreviation = abbreviation.replaceAll(".html","");// removes suffix
                    long size = entry.getSize();
                    if (size > 0) {
                        //System.out.println("Length is " + size);
                        BufferedReader br = new BufferedReader(new InputStreamReader(factbook.getInputStream(entry)));
                        String line;
                        //process each line of the document
                        while ((line = br.readLine()) != null) {
                            //check if line of html has countryname
                            if(line.contains("countryname")){
                                //System.in.read();
                                countryName++;
                                line = line.replaceAll(".*countryname=\"","");//remove title up to first "
                                line = line.replaceAll("\"",""); //Remove closing quote "
                                //fix countries with commas
                                int numCommas = line.length() - line.replaceAll(",","").length(); //Count number of commas in line
                                if(numCommas == 1){ //That means country name needs to be switched
                                    line = fixSplitName(line);
                                }

                                String csvline = abbreviation.concat("-map.gif,").concat(line).concat("\n"); // add line to csv in format "xx-map.gif,countryName"
                                //System.out.println(csvline);
                                filenameToCountryName.add(csvline);
                                break;
                            }
                        }
                        br.close();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter writer = new FileWriter("maps.csv");
        for(String str: filenameToCountryName) {
            writer.write(str);
        }
        writer.close();
        System.out.println("CSV Created");
    }
    private static void makeMapsDir() {
        File theDir = new File("maps");
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;
            try{
                result = theDir.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }
    private static void extractFile(String fileName, String zipPath) throws Exception{
        // remove all the directory information from name and put it in maps
        String newFileName = fileName.replaceAll(".*/","maps/");
        //check if file already exists!
        File f = new File(newFileName);
        if(f.exists()){
            //If it does exist just return and don't extract
            //System.out.println("File already Exists!");
            return;
        }
        OutputStream out = new FileOutputStream(newFileName);
        FileInputStream fileInputStream = new FileInputStream(zipPath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream );
        ZipInputStream zin = new ZipInputStream(bufferedInputStream);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.getName().equals(fileName)) {
                byte[] buffer = new byte[9000];
                int len;
                while ((len = zin.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.close();
                break;
            }
        }
        zin.close();
        System.out.println("Map extracted: " + newFileName);
    }
    private static void unzipMaps(ZipFile factbook) throws Exception {
        System.out.println("List of Map Names Entered");
        System.out.println("path name: " + factbook.getName());
        System.out.println("size: " + factbook.size());

        int numMaps = 0;
        // Reads through each file in the zip
        Enumeration<? extends ZipEntry> entries = factbook.entries();
        long timeStart = System.nanoTime();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            // Checks if file is a map and add it to the list of maps
            if(entry.getName().contains("-map.gif") && entry.getName().contains("factbook/graphics/maps/")){
                extractFile(entry.getName(), factbook.getName());
                numMaps++;
                //System.out.println("Num Maps: " + numMaps);
                long timeSoFar = System.nanoTime();
                //System.out.print("Runtime seconds: ");
                //System.out.println((timeSoFar-timeStart)/1000000000.0);
            }
        }
        long timeEnd = System.nanoTime();
        System.out.print("Total Runtime seconds for maps extract: ");
        System.out.println((timeEnd-timeStart)/1000000000.0);
        System.out.println("Number of Map images: " + numMaps);
    }

    public static void main(String[] args) throws Exception {
        //check input line File
        if(args.length == 0){
            System.out.println("No File was given!");
            System.out.println("Please input location of factbook.zip");
            return;
        }
        // Print Input
        System.out.println("Input: " + args[0]);
        // Declare factbook as the input given for the factbook zip
        ZipFile factbook = new ZipFile(args[0]);
        // Make /maps dir for map .gif's to be extracted to
        makeMapsDir();
        // Get the maps from the factbook and unzip them to /maps
        unzipMaps(factbook);
        //todo break createCSV() into it's own class
        //make csv for XX-map.gif -> country name
        createCSV(factbook);
        //test CSV with extracted Maps :)
        String csvName = "maps.csv";
        testCSV(csvName);
    }

    private static void testCSV(String csvName) {
        System.out.println("TEST CSV BEING RAN");
        //list files in maps/
        System.out.println("Printing Folder maps/ Contents:");
        ArrayList<String> mapsWithoutNames = new ArrayList<String>();
        final File folder = new File("maps/");
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            mapsWithoutNames.add(fileEntry.getName());
            System.out.println(fileEntry.getName());
        }
        //read csv
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
                    //check if map from folder has a map in the CSV
                    if(mapsWithoutNames.contains(fileName)){//It has a name in the CSV :)
                        System.out.println("There exists a name for: " + fileName + " that name is: " + countryName );
                        //remove filename from mapsWithoutNames
                        mapsWithoutNames.remove(fileName);
                    }
                    else{//It Doesn't have a name in the CSV :(
                        System.out.println(fileName + " Has no Name in " + csvName);
                    }
                }
                else{
                    System.out.println("Was not able to find both FileName and CountryName from line: " + line);
                }
            }
            System.out.println("Number of Maps not matched with a name: " + mapsWithoutNames.size());
            for (String homelessMap : mapsWithoutNames) {
                System.out.println(homelessMap + " has no home :'(");
            }
        }
        catch (IOException e) {
            System.out.println("Error looks like File can't be read");
        }
    }
}
