import java.awt.*;

/**
 * Created by Edric on 3/26/2018.
 */
public class Labeler {

    public static void main(String[] args){
        Options.setOptions(args);
        switch (Options.inputType){
            case INFO:
                printInfo();
                break;
            case SINGLE:
                System.out.println("Trying to open single file");
                labelPicture(Options.inputFile, Options.text);
                break;
            case CSV:
                System.out.println("Print CSV");
                //setup
                String csvName = Options.inputFile;
                String mapsLoc = "maps/";
                batchLabeler.ProcessCSV(csvName, mapsLoc);
                break;
            case DIRECTORY:
                System.out.println("Print directory");
                //setup
                //run many
                break;
        }
        System.out.println(Options.font.getSize());
        System.out.println(Options.font.getSize2D());
    }

    public static void labelPicture(String inFileName, String labelText) {
        Options.inputFile = inFileName;
        Options.text = labelText;
        Picture mypic = new Picture(Options.inputFile);
        System.out.println("Opened " + Options.inputFile);
        System.out.println("Labeling as " + Options.text);
        if (Options.centerLabel){
            mypic.writeCenteredLabel();
        } else {
            mypic.writeLabel();
        }

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
}
