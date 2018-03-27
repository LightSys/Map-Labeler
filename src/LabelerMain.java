import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Edric on 3/26/2018.
 */
public class LabelerMain {
    private static final double X_PAD_SCALE = 1.2;
    private static final double Y_PAD_SCALE = 1.5;

    public static void main(String[] args){
        //args[0] is the file
        //args[1] is the text of the label

        setOptions(args);

        String filename = args[0];
        String text = args[1];
        System.out.println("Opening " + filename);

        Picture mypic = new Picture(Options.inputFile);
        mypic.writeLabel();

        System.out.println("done");
    }

    private static void setOptions(String[] args){
        parseArguments(args);
        Options.outputFile = "out.gif";
        Options.newLine = false;
        Options.padXScale = 1.5;
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

    private static void checkFontSize(ArrayList<String> argList){
        int argIndex = checkForFlag(argList, "-s");
        if (argIndex == -1) return;
        int chosenFontSize = 0;
        try {
            chosenFontSize = Integer.parseInt(argList.get(argIndex));
            argList.remove(argIndex);
        } catch (NumberFormatException e) {
            System.out.println("Cannot set font size to " + argList.get(argIndex));
        } catch (IndexOutOfBoundsException e){
            System.out.println("-s requires an integer argument");
        }

        if (chosenFontSize < 1){
            System.out.println("Using default font size");
        }
        else {
            Options.setFontSize(chosenFontSize);
        }
    }

    private static int checkForFlag(ArrayList<String> argList, String flag){
        for (int i = 0; i < argList.size() ; i++) {
            if (argList.get(i).equals(flag)) {
                argList.remove(i);
                return i;
            }
        }
        return -1;
    }
}
