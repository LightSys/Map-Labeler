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
    public static Color targetColor = null;

    public static boolean centerLabel = false;

    public static final Color FB_LAND_COLOR = new Color(255, 240, 222);


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
            Logger.addLog("Labeler requires arguments\nUse the -help flag for more information");
            System.exit(0);
        }

        inputFile = argList.get(0);
        fileExtension = getExtension(inputFile);
        setInputType(fileExtension);

        setTextIfSingle();

        if (argsContainsFlag("-watermark")) { //gets overridden by everything
            setFontBold();
            setFontName("SansSerif");
            setFontSize(42);
            setAlpha(".5");
        }
        if (argsContainsFlag("-b")) {
            setFontBold();
        }
        if (argsContainsFlag("-i")) {
            setFontItalic();
        }
        if (argsContainsFlag("-n")) {
            newLine = true;
        }
        if (argsContainsFlag("-factbook")) { //gets overridden by target color
            targetColor = Options.FB_LAND_COLOR;
        }
        if (argsContainsFlag("-center")) { //gets overridden by target color
            centerLabel = true;
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
        if (argsContainsFlag("-a")) {
            int i = argList.indexOf("-a");
            setAlpha(argList.get(i + 1));
        }
        if (argsContainsFlag("-tc")) {
            int i = argList.indexOf("-tc");
            setTargetColor(argList.get(i + 1));
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
        System.out.println("Font Alpha set to " + getAlpha());
        System.out.println("Padding X Scale set to " + padXScale);
        System.out.println("Padding Y Scale set to " + padYScale);

        if (locationY != -1){System.out.println("Location Y set to " + locationY);}
        else {System.out.println("No specific Location Y set");}
        if (locationX != -1){System.out.println("Location X set to " + locationX);}
        else {System.out.println("No specific Location X set");}
        System.out.println("Target Color set to " + targetColor);
    }

    private static double getAlpha() {
        return Math.round(color.getAlpha()/255.0 * 100)/100.0;
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

    public static void setFontSize(int size) {
        font = new Font(font.getName(), font.getStyle(), size);
    }

    public static void shrinkFontToFit(Picture pic, String displayText, int numStrings) {
        int origW = pic.getDisplayWidth(displayText, font);
        int origH = font.getSize();
        int w = (int) (Options.padXScale * origW);
        int h = (int) (Options.padYScale * origH);
        int yPadding = h - origH;
        h = (origH * numStrings) + yPadding;
        while (w >= pic.getWidth() || (h >= pic.getHeight())){
            setFontSize(font.getSize()-1);
            origW = pic.getDisplayWidth(displayText, font);
            origH = font.getSize();
            w = (int) (Options.padXScale * origW);
            h = (int) (Options.padYScale * origH);
            yPadding = h - origH;
            h = (origH * numStrings) + yPadding;
        }
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
            System.out.println("Could not set font size because -s requires an integer argument");
            Logger.addLog("Could not set font size because -s requires an integer argument");
        } else {
            try {
                chosenFontSize = Integer.parseInt(size);
            } catch (NumberFormatException e) {
                Logger.addLog("Could not set font size to" + size);
                System.out.println("Cannot set font size to " + size);
            }
        }
        if (chosenFontSize < 1) {
            Logger.addLog("Using default font size");
            System.out.println("Using default font size");
        } else {
            setFontSize(chosenFontSize);
        }
    }

    private static void setFontName(String name) {
        if (name.equals("")) {
            Logger.addLog("Could not set font name because -f requires a text argument.");
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
                Logger.addLog("Could not set font to " + name + "use -font to get a list of valid fonts");
                System.out.println("Cannot set font to " + name);
            }
        }
    }

    private static void setXPaddingScale(String scale) {
        double chosenXPaddingScale = 0;
        if (scale.equals("")) {
            Logger.addLog("Could not set x padding scale because -px requires an integer argument");
            System.out.println("-px requires an integer argument");
        } else {
            try {
                chosenXPaddingScale = Double.parseDouble(scale);
            } catch (NumberFormatException e) {
                Logger.addLog("Could not set x padding scale to " + scale);
                System.out.println("Cannot set x padding scale to " + scale);
            }
        }
        if (chosenXPaddingScale < 1.0) {
            Logger.addLog("Using default x padding scale");
            System.out.println("Using default x padding scale");
        } else {
            padXScale = chosenXPaddingScale;
        }
    }

    private static void setYPaddingScale(String scale) {
        double chosenYPaddingScale = 0;
        if (scale.equals("")) {
            Logger.addLog("Could not set y padding scale because -py requires an integer argument");
            System.out.println("-py requires an integer argument");
        } else {
            try {
                chosenYPaddingScale = Double.parseDouble(scale);
            } catch (NumberFormatException e) {
                Logger.addLog("Could not set y padding scale to " + scale);
                System.out.println("Cannot set y padding scale to " + scale);
            }
        }
        if (chosenYPaddingScale < 1.0) {
            Logger.addLog("Using default y padding scale");
            System.out.println("Using default y padding scale");
        } else {
            padYScale = chosenYPaddingScale;
        }
    }

    private static void setColor(String strColor) {
        color = stringToColor(strColor);
        if (color == null) color = Color.black;
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
                return null;
            }
        }
    }

    private static void setXLocation(String loc) {
        int chosenXLoc = -1;
        if (loc.equals("")) {
            Logger.addLog("Could not set location x because -lx requires an integer argument");
            System.out.println("-lx requires an integer argument");
        } else {
            try {
                chosenXLoc = Integer.parseInt(loc);
            } catch (NumberFormatException e) {
                Logger.addLog("Could not set location x to " + loc);
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
            Logger.addLog("Could not set location y -ly requires an integer argument");
            System.out.println("-ly requires an integer argument");
        } else {
            try {
                chosenYLoc = Integer.parseInt(loc);
            } catch (NumberFormatException e) {
                Logger.addLog("Could not set location y to " + loc);
                System.out.println("Cannot set location y to " + loc);
            }
        }
        if (chosenYLoc >= 0) {
            locationY = chosenYLoc;
        }
    }
    private static void setAlpha(String strAlpha) {
        float chosenAlpha = -1;
        if (strAlpha.equals("")) {
            Logger.addLog("Could not set alpha because -a requires a float argument");
            System.out.println("-a requires a float argument");
        } else {
            try {
                chosenAlpha = Float.parseFloat(strAlpha);
            } catch (NumberFormatException e) {
                Logger.addLog("Could not set alpha to " + strAlpha);
                System.out.println("Cannot set alpha to " + strAlpha);
            }
        }
        if (chosenAlpha < 0 && chosenAlpha > 1) {
            Logger.addLog("Using default alpha");
            System.out.println("Using default alpha");
        } else {
            Options.color = new Color(Options.color.getRed(), Options.color.getGreen(), Options.color.getBlue(), (int)(chosenAlpha*255));
        }
    }
    private static void setTargetColor(String strColor) {
        targetColor = stringToColor(strColor);
    }
}
