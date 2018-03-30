import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jbern on 3/27/2018.
 */
public class Options {
    public enum InputType {SINGLE, DIRECTORY, CSV, INFO}

    public enum InfoType {HELP, FONT, EXTENSION}

    public static final String[] EXTENSIONS = {".csv", ".gif", ".jpg", ".png", ".bmp"};

    public static double padXScale = 1.2;
    public static double padYScale = 1.5;
    public static boolean newLine = false;
    public static Font font = new Font("Arial", Font.PLAIN, 14);
    public static String outputFile = "out.gif";
    public static String inputFile = "us-map.gif";
    public static String text = "Sample Label";
    public static String fileExtension = "";
    public static InputType inputType = null;
    public static InfoType infoType = null;
    public static String errorMessage = null;
    public static Color color = Color.black;

    public static boolean debug = false;

    public static ArrayList<String> argList;
    public static int locationX = -1;
    public static int locationY = -1;


    public Options() throws InstantiationException {
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

        if (argsContainsFlag("-b")) {
            setFontBold();
        }
        if (argsContainsFlag("-i")) {
            setFontItalic();
        }
        if (argsContainsFlag("-n")) {
            newLine = true;
        }

        argList.add(""); // avoid index out of bounds
        if (argsContainsFlag("-s")) {
            int i = argList.indexOf("-s");
            setFontSize(argList.get(i + 1));
        }
        if (argsContainsFlag("-f")) {
            int i = argList.indexOf("-f");
            setFontName(argList.get(i + 1));
        }
        if (argsContainsFlag("-px")) {
            int i = argList.indexOf("-px");
            setXPaddingScale(argList.get(i + 1));
        }
        if (argsContainsFlag("-py")) {
            int i = argList.indexOf("-py");
            setYPaddingScale(argList.get(i + 1));
        }
        if (argsContainsFlag("-c")) {
            int i = argList.indexOf("-c");
            setColor(argList.get(i + 1));
        }
        if (argsContainsFlag("-lx")) {
            int i = argList.indexOf("-lx");
            setXLocation(argList.get(i + 1));
        }
        if (argsContainsFlag("-ly")) {
            int i = argList.indexOf("-ly");
            setYLocation(argList.get(i + 1));
        }
        if (errorMessage != null) {
            inputType = InputType.INFO;
        }
        //TODO notify user about unknown flags
        printAllOptions();
    }

    public static void printAllOptions() {
        if ((font.getStyle() & 1) == 1) {
            System.out.println("Font Style set to Bold.");
        }
        if ((font.getStyle() & 2) == 2) {
            System.out.println("Font Style set to Italic.");
        }
        if (font.getStyle() == 0) {
            System.out.println("Font Style set to Plain.");
        }

        System.out.println("Font Size set to " + font.getSize());
        System.out.println("Font set to " + font.getName());
        System.out.println("Font Color set to " + color);
        System.out.println("Padding X Scale set to " + padXScale);
        System.out.println("Padding Y Scale set to " + padYScale);

        if (locationY != -1){System.out.println("Location Y set to " + locationY);}
        if (locationX != -1){System.out.println("Location X set to " + locationX);}
    }

    private static void setFontPlain() {
        font = new Font(font.getName(), Font.PLAIN, font.getSize());
    }

    private static void setFontBold() {
        font = new Font(font.getName(), font.getStyle() | Font.BOLD, font.getSize());
    }

    private static void setFontItalic() {
        font = new Font(font.getName(), font.getStyle() | Font.ITALIC, font.getSize());
    }

    private static void setFontSize(int size) {
        font = new Font(font.getName(), font.getStyle(), size);
    }

    public static String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            String extension = fileName.substring(i);
            for (String ext : EXTENSIONS) {
                if (ext.equals(extension)) {
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
        if (argsContainsFlag("-help")) {
            infoType = InfoType.HELP;
        } else if (argsContainsFlag("-font")) {
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
            if (argList.get(i).equals(flag) || argList.get(i).equals("-" + flag)) {
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

    private static void setFontSize(String size) {
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
        if (chosenFontSize < 1) {
            System.out.println("Using default font size");
        } else {
            setFontSize(chosenFontSize);
        }
    }

    private static void setFontName(String name) {
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

    private static void setXPaddingScale(String scale) {
        double chosenXPaddingScale = 0;
        if (scale.equals("")) {
            System.out.println("-px requires an integer argument");
        } else {
            try {
                chosenXPaddingScale = Double.parseDouble(scale);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set x padding scale to " + scale);
            }
        }
        if (chosenXPaddingScale < 1.0) {
            System.out.println("Using default font size");
        } else {
            padXScale = chosenXPaddingScale;
        }
    }

    private static void setYPaddingScale(String scale) {
        double chosenYPaddingScale = 0;
        if (scale.equals("")) {
            System.out.println("-py requires an integer argument");
        } else {
            try {
                chosenYPaddingScale = Double.parseDouble(scale);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set y padding scale to " + scale);
            }
        }
        if (chosenYPaddingScale < 1.0) {
            System.out.println("Using default font size");
        } else {
            padYScale = chosenYPaddingScale;
        }
    }

    private static void setColor(String strColor) {
        color = stringToColor(strColor);
    }

    public static Color stringToColor(final String value) {
        if (value == null) {
            return Color.black;
        }
        try {
            // get color by hex or octal value
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            // if we can't decode lets try to get it by name
            try {
                // try to get a color by name using reflection
                final Field f = Color.class.getField(value);

                return (Color) f.get(null);
            } catch (Exception ce) {
                // if we can't get any color return black
                return Color.black;
            }
        }
    }

    private static void setXLocation(String loc) {
        int chosenXLoc = -1;
        if (loc.equals("")) {
            System.out.println("-lx requires an integer argument");
        } else {
            try {
                chosenXLoc = Integer.parseInt(loc);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set location x to " + loc);
            }
        }
        if (chosenXLoc >= 0) {
            locationX = chosenXLoc;
        }
    }
    private static void setYLocation(String loc) {
        int chosenYLoc = -1;
        if (loc.equals("")) {
            System.out.println("-ly requires an integer argument");
        } else {
            try {
                chosenYLoc = Integer.parseInt(loc);
            } catch (NumberFormatException e) {
                System.out.println("Cannot set location y to " + loc);
            }
        }
        if (chosenYLoc >= 0) {
            locationY = chosenYLoc;
        }
    }
}
