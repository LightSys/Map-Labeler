import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jbern on 3/27/2018.
 */
public class Options {
    public enum InputType {SINGLE, DIRECTORY, CSV, INFO}
    public enum InfoType {HELP, FONT, EXTENSION}
    public static final String[] EXTENSIONS = {".csv", ".gif", ".jpg", ".png", ".svg", ".pdf", ".tiff", ".bmp"};

    public static double padXScale = 1.2;
    public static double padYScale = 1.5;
    public static boolean newLine = false;
    public static Font font = new Font("Times New Roman", Font.PLAIN,16);
    public static String outputFile = "out.gif";
    public static String inputFile = "us-map.gif";
    public static String text = "Sample Label";
    public static String fileExtension = "";
    public static InputType inputType = null;
    public static InfoType infoType = null;
    public static String errorMessage = null;

    public static boolean debug = false;

    public static ArrayList<String> argList;


    public Options() throws InstantiationException{
        throw new InstantiationException("Cannot create instance of Options!");
    }

    public static void setInputType(String extension) {
        switch (extension) {
            case "directory":
                inputType = InputType.DIRECTORY;
                break;
            case ".csv":
                inputType = InputType.CSV;
                break;
            default:
                inputType = InputType.SINGLE;
        }
    }

    public static void setOptions(String[] args) {
        argList = new ArrayList<>(Arrays.asList(args));
        processInfoFlag();
        if (infoType != null) return;

        if (argList.isEmpty()) {
            System.out.println("Labeler requires arguments\nUse the -help flag for more information");
            System.exit(0);
        }

        inputFile = argList.get(0);
        fileExtension = getExtension(inputFile);
        setInputType(fileExtension);

        setTextIfSingle();

        if (argsContainsFlag("-b")) { setFontBold(); }
        if (argsContainsFlag("-i")) { setFontItalic(); }
        if (argsContainsFlag("-n")) { newLine = true; }

        argList.add(""); // avoid index out of bounds
        if (argsContainsFlag("-s")) {
            int i = argList.indexOf("-s");
            setFontSize(argList.get(i+1));
        }
        if (argsContainsFlag("-f")) {
            int i = argList.indexOf("-f");
            setFontName(argList.get(i+1));
        }
        if (argsContainsFlag("-px")) {
            int i = argList.indexOf("-px");
            setXPaddingScale(argList.get(i+1));
        }
        if (argsContainsFlag("-py")) {
            int i = argList.indexOf("-py");
            setYPaddingScale(argList.get(i+1));
        }
        if (errorMessage != null) {
            inputType = InputType.INFO;
        }
    }


    private static void setFontPlain(){font = new Font(font.getName(), Font.PLAIN, font.getSize());}

    private static void setFontBold(){font = new Font(font.getName(), font.getStyle() | Font.BOLD, font.getSize());}

    private static void setFontItalic(){font = new Font(font.getName(), font.getStyle() | Font.ITALIC, font.getSize());}

    private static void setFontSize(int size){font = new Font(font.getName(), font.getStyle(), size);}

    public static String getExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            String extension = fileName.substring(i);
            for (String ext : EXTENSIONS) {
                if (ext.equals(extension)) {
                    System.out.println(fileName);
                    return extension;
                }
            }
            throw new IllegalArgumentException("Cannot recognize file extension: " + extension
                    + "\nFor a list of legal file extensions use the -ext flag.");
        }
        File f = new File(fileName);
        if (f.exists() && f.isDirectory()) return "directory";
        throw new IllegalArgumentException("Cannot find directory: " + fileName
                + "\nInput an existing directory name or a filename with a valid extension.");
    }

    public static void processInfoFlag() {
        if (argsContainsFlag("-help")){
            infoType = InfoType.HELP;
        } else if (argsContainsFlag("-font")){
            infoType = InfoType.FONT;
        } else if (argsContainsFlag("-ext")) {
            infoType = InfoType.EXTENSION;
        }

        if (infoType != null) {
            inputType = InputType.INFO;
        }
    }

    private static boolean argsContainsFlag(String flag) {
        if (flag.charAt(0) != '-') return false;
        for (int i = 0; i < argList.size(); i++) {
            if (argList.get(i).equals(flag) || argList.get(i).equals("-" + flag)){
                argList.set(i, flag);
                return true;
            }
        }
        return false;
    }

    public static void setTextIfSingle() {
        if (inputType == InputType.SINGLE) {
            text = argList.get(1);
        }
    }

    private static void setFontSize(String size){
        int chosenFontSize = 0;
        if (size.equals("")) {
            System.out.println("-s requires an integer argument");
        } else {
            try {
                chosenFontSize = Integer.parseInt(size);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set font size to " + size);
            }
        }
        if (chosenFontSize < 1){
            System.out.println("Using default font size");
        }
        else {
            setFontSize(chosenFontSize);
        }
    }

    private static void setFontName(String name){
        if (name.equals("")) {
            System.out.println("-f requires a text argument.");
        } else {
            boolean found = false;
            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            for (String f : fonts) {
                if (f.equals(name)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                font = new Font(name, font.getStyle(), font.getSize());
            } else {
                System.out.println("Cannot set font to " + name);
            }
        }
    }

    private static void setXPaddingScale(String scale){
        double chosenXPaddingScale = 0;
        if (scale.equals("")) {
            System.out.println("-s requires an integer argument");
        } else {
            try {
                chosenXPaddingScale = Double.parseDouble(scale);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set font size to " + scale);
            }
        }
        if (chosenXPaddingScale < 1.0){
            System.out.println("Using default font size");
        }
        else {
            padXScale = chosenXPaddingScale;
        }
    }

    private static void setYPaddingScale(String scale){
        double chosenYPaddingScale = 0;
        if (scale.equals("")) {
            System.out.println("-s requires an integer argument");
        } else {
            try {
                chosenYPaddingScale = Double.parseDouble(scale);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set font size to " + scale);
            }
        }
        if (chosenYPaddingScale < 1.0){
            System.out.println("Using default font size");
        }
        else {
            padYScale = chosenYPaddingScale;
        }
    }

}
