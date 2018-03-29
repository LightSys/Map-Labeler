import java.awt.*;


/**
 * Created by Edric on 3/26/2018.
 */
public class LabelerMain {
    private static final double X_PAD_SCALE = 1.2;
    private static final double Y_PAD_SCALE = 1.5;

    public static void main(String[] args){
        //args[0] is the file
        //args[1] is the text of the label
        Options.setOptions(args);
        switch (Options.inputType){
            case INFO:
                printInfo();
                break;
            case SINGLE:
                System.out.println("Trying to open single file");
                labelPicture(Options.inputFile, Options.text);
//                Picture mypic = new Picture(Options.inputFile);
//                mypic.writeLabel();
//                mypic.write(Options.outputFile);
                break;
            case CSV:
                System.out.println("I udnerstand that this is CSV");

                //setup
                String csvName = Options.inputFile;
                String mapsLoc = "maps/";
                batchLabeler.ProcessCSV(csvName, mapsLoc);
                break;
            case DIRECTORY:
                //setup
                //run many
                break;
        }
        System.out.println("done");
    }

    public static void labelPicture(String inFileName, String labelText) {
        Options.inputFile = inFileName;
        Options.text = labelText;
        Picture mypic = new Picture(Options.inputFile);
        mypic.writeLabel();
        mypic.write("output/" + inFileName);

//        mypic.write(Options.outputFile);
    }

    public static void printInfo() {
        switch (Options.infoType){
            case HELP:
                printHelp();
                break;
            case FONT:
                printFonts();
                break;
            case EXTENSION:
                printExtensions();
                break;
        }
    }

    public static void printHelp() {
        System.out.println("View the README at https://github.com/LightSys/Map-Labeler/blob/master/README.md");
    }

    public static void printFonts() {
        System.out.println("Your machine can use the following fonts:");
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String font : fonts) {
            System.out.println(font);
        }
    }

    public static void printExtensions() {
        System.out.println("This program handles the following file extensions:");
        for (String ext : Options.EXTENSIONS) {
            System.out.println(ext);
        }
    }
/*
    private static void setOptions(String[] args){
        parseArguments(args);
        Options.outputFile = "out.gif";
        Options.newLine = true;
        Options.padXScale = 1.2;
        Options.padYScale = 1.5;
    }

    private static void parseArguments(String[] args){
        if (args.length < 2){
            //TODO figure out what the heck to do here
            System.out.println("you are the worst period");
            System.exit(0);
            return;
        }
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        setFileName(argList.get(0));
        argList.remove(0);
        setText(argList.get(0));
        argList.remove(0);
        checkAllArguments(argList);
    }

    private static void checkAllArguments(ArrayList<String> argList){
        checkNewLineArg(argList);
        checkPaddingXArg(argList);
        checkPaddingYArg(argList);
        checkFontName(argList);
        checkBoldArg(argList);
        checkItalicArg(argList);
        checkFontSize(argList);
        if (argList.size() > 0) {
            String errorMessage = "Unepexected arguments: ";
            for (String arg : argList) {
                errorMessage += arg + ", ";
            }
            System.out.println(errorMessage);
        }
    }

    private static void setFileName(String fileName){Options.inputFile = fileName;}

    private static void setText(String text){Options.text = text;}

    private static void checkNewLineArg(ArrayList<String> argList){
        if (checkForFlag(argList, "-n") != -1){
            Options.newLine = true;
        }
    }

    private static void checkPaddingXArg(ArrayList<String> argList){
        int argIndex = checkForFlag(argList, "-px");
        double padX = 0;
        if (argIndex != -1){
            try {
                padX = Double.parseDouble(argList.get(argIndex));
                argList.remove(argIndex);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set x padding to " + argList.get(argIndex));
            } catch (IndexOutOfBoundsException e){
                System.out.println("-px requires a number argument");
            }

            if (padX < 1){
                System.out.println("Using default x padding");
            }
            else {
                Options.padXScale = padX;
            }
        }
    }

    private static void checkPaddingYArg(ArrayList<String> argList){
        int argIndex = checkForFlag(argList, "-py");
        double padY = 0;
        if (argIndex != -1){
            try {
                padY = Double.parseDouble(argList.get(argIndex));
                argList.remove(argIndex);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set y padding to " + argList.get(argIndex));
            } catch (IndexOutOfBoundsException e){
                System.out.println("-py requires a number argument");
            }

            if (padY < 1){
                System.out.println("Using default y padding");
            }
            else {
                Options.padYScale = padY;
            }
        }
    }

    private static void checkFontName(ArrayList<String> argList){
        int argIndex = checkForFlag(argList, "-f");
        if (argIndex == -1) return;
        String chosenFont = null;
        if (argIndex >= argList.size()) {
            System.out.println("-f requires a text argument");
        } else {
            chosenFont = argList.get(argIndex);
            argList.remove(argIndex);
            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            boolean found = false;
            for (String f : fonts) {
                if (f.equals(chosenFont)) {
                    found = true;
                    break;
                }
            }
            if (!found){
                System.out.println("Cannot set font to " + chosenFont);
            }
        }
        if (chosenFont != null){
            System.out.println("Using default font");
            System.out.println("To get a list of available fonts use the -fonts flag");
        }
        Options.setFontName(chosenFont);
    }

    private static void checkBoldArg(ArrayList<String> argList){
        if (checkForFlag(argList, "-b") != -1){
            Options.setFontBold();
        }
    }

    private static void checkItalicArg(ArrayList<String> argList){
        if (checkForFlag(argList, "-i") != -1){
            Options.setFontItalic();
        }
    }



    private static int checkForFlag(ArrayList<String> argList, String flag){
        for (int i = 0; i < argList.size() ; i++) {
            if (argList.get(i).equals(flag)) {
                return i;
            }
        }
        return -1;
    }
    */
}
