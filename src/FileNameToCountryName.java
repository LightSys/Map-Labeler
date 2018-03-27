import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
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
                    abbreviation = abbreviation.replaceAll(".*/",""); // removes prefix
                    abbreviation = abbreviation.replaceAll(".html","");
                    long size = entry.getSize();
                    if (size > 0) {
                        //System.out.println("Length is " + size);
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(factbook.getInputStream(entry)));
                        String line;
                        //process each line of the document
                        while ((line = br.readLine()) != null) {
                            if(line.contains("countryname")){
                                countryName++;
                                line = line.replaceAll(".*countryname=\"","");
                                line = line.replaceAll("\"","");
                                String csvline = abbreviation.concat("-map.gif,").concat(line).concat("\n");
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
                theDir.mkdir();
                result = true;
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
            if(entry.getName().contains("-map.gif") && entry.getName().contains("factbook/graphics/maps/newmaps/")){
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

    private static void createMapzip() {
        //create new mapZip file

        //iterate through maps and add each to zip


    }

    public static void main(String[] args) throws Exception {
        //check File
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
        //make csv for XX-map.gif -> country name
        createCSV(factbook);
        //todo zip up maps
        createMapzip();
    }
}
