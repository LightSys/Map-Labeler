import java.awt.*;

/**
 * Created by jbern on 3/27/2018.
 */
public class Options {
    public static double padXScale = 1.2;
    public static double padYScale = 1.5;
    public static boolean newLine = false;
    public static Font font = new Font("Times New Roman", Font.PLAIN,16);
    public static String outputFile = "out.gif";

    public Options() throws InstantiationException{
        throw new InstantiationException("Cannot create instance of Options");
    }
    public void setFontPlain(){font = new Font(font.getName(), Font.PLAIN, font.getSize());}

    public void setFontBold(){font = new Font(font.getName(), Font.BOLD, font.getSize());}

    public void setFontItalic(){font = new Font(font.getName(), Font.ITALIC, font.getSize());}

    public void setFontSize(int size){font = new Font(font.getName(), font.getStyle(), size);}

    public void setFontName(String name){font = new Font(name, font.getStyle(), font.getSize());}
}
