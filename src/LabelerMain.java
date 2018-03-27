import java.awt.*;
import java.security.cert.PKIXRevocationChecker;


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

        Picture mypic = new Picture(filename);
        mypic.writeLabel(text);

        System.out.println("done");
    }

    private static void setOptions(String[] args){
        Options.outputFile = "out.gif";
        Options.newLine = true;
        Options.padXScale = 1.2;
        Options.padYScale = 1.5;
    }
}
