/**************************************************
 * Created by Will Kercher 3/30/18
 * ************************************************/

public class FactbookAutomaticLabeler {
    FactbookAutomaticLabeler(){}


    /**This Java application is designed as an automated process
     * for labeling all the maps in the CIA World Factbook.
     * Output: A CSV with filename of a map and the name of that country.
     *         and a folder with shiny new labeled maps
     * @param args Commandline input arguments, args[0] should be location of factbook.zip,
     *             and args[1] should be location of output folder
     */
    public static void main(String[] args) throws Exception {
        boolean factbookFound = false;
        String factbookLocation = "";
        String outputDirectory = ""; // current directory

        //parsing Commandline Input cases
        int numInputs = args.length;
        switch(numInputs){
            case 1: //One Input
            case 2: //Two Input
                for (String arg : args) {
                    if (arg.contains(".zip")) {
                        factbookLocation = arg;
                        factbookFound = true;
                    }
                    else{
                        outputDirectory = arg + "/";
                    }
                }
                break;

            //Incoherent Input Cases
            case 0:
                System.out.println("No Input was given!");
            default:
                printInputError();
                return;
        }
        // check to make sure you were passed in factbook
        if(!factbookFound){
            System.out.println("Factbook Not Found in input");
            printInputError();
            return;
        }
        // Make Output Dir
        MakeDirectory.makeNewDir(outputDirectory);
        // Process World Factbook
        String[] input = {factbookLocation, outputDirectory};
        String[] CSVAndMapDir = FileNameToCountryName.runProcess(input);
        // Run BatchLabeler
        String CSVName = CSVAndMapDir[0];// the name of the CSV
        String unlabeledMapsDir = CSVAndMapDir[1]; //name of the Unlabeled maps folder
        if (CSVAndMapDir != null){
            BatchLabeler.ProcessCSV(CSVName,unlabeledMapsDir);
        }
        else{
            Logger.addLog("Error with returned string from FileNameToCountryName: ");
            return;
        }
    }

    static void printInputError(){
        System.out.println("Input Error");
        System.out.println("Input should be: file/path/factbook.zip, and an optional output folder name!");
        System.out.println("If no output folder is given, output will be placed where application is run");
    }
}
