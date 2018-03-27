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

        String filename = args[0];
        System.out.println("Opening " + filename);

        // 1 open file
        Picture mypic = new Picture(filename);

        // choose font
        Font myFont = new Font("Times New Roman", Font.BOLD,16);

        // write label
        String text = args[1];
        Font font = myFont;
        String outputFileName = "out.gif";
        mypic.writeLabel(text, font, outputFileName, X_PAD_SCALE, Y_PAD_SCALE);

        /*
        // 2 find best box
        int origW = mypic.getDisplayWidth(args[1], myFont);
        int origH = 16;
        int w = (int) (X_PAD_SCALE * origW);
        int h = (int) (Y_PAD_SCALE * origH);


        ScorePoint sp = mypic.getBestBoxPosition(w, h);


        // 3 edit image

        //drawBox(mypic, sp.getP().getX(), sp.getP().getY(), w, h);

        int drawX = sp.getP().getX() + w/2;
        int drawY = sp.getP().getY() + h/2;

        mypic.drawString(args[1], myFont, drawX, drawY);

        // 4 write to new file
        mypic.write("out2.gif");
        */
        System.out.println("done?");
    }

    private static void drawBox(Picture pic, int x, int y, int w, int h) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pic.getPixel(x+i,y+j).setBlue(0);
                pic.getPixel(x+i,y+j).setRed(255);
                pic.getPixel(x+i,y+j).setGreen(0);
            }
        }
    }



}
