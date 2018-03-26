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

        // open file
        Picture mypic = new Picture(filename);

        Font myFont = new Font("Rockwell Extra Bold", Font.BOLD,16);

        // find best box
        int origW = mypic.getDisplayWidth(args[1], myFont);
        int origH = 16;
        int w = (int) (X_PAD_SCALE * origW);
        int h = (int) (Y_PAD_SCALE * origH);

        double score = mypic.getScore(0, 0, w, h);
        int bestX = 0;
        int bestY = 0;
        for (int y = 0; y < mypic.getHeight() - h; y++) {
            for (int x = 0; x < mypic.getWidth() - w; x++) {
                double newScore = mypic.getScore(x, y, w, h);
                if (newScore > score) {
                    score = newScore;
                    bestX = x;
                    bestY = y;
                    //System.out.println(mypic.getPointsForCenter(x, y, w, h));
                    //System.out.println(mypic.getPointsForNoise(x,y,w,h));
                }
            }
        }

        // edit image
        /*
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                mypic.getPixel(bestX+i,bestY+j).setBlue(0);
                mypic.getPixel(bestX+i,bestY+j).setRed(255);
                mypic.getPixel(bestX+i,bestY+j).setGreen(0);
            }
        }
        */
        int xStrPadding = (w - origW)/2;
        int yStrPadding = (h - origH)/2;

        mypic.drawString(args[1], myFont, bestX + xStrPadding, bestY + yStrPadding);

        // write to new file
        mypic.write("D:\\git\\Map-Labeler\\out.gif");
        System.out.println("done?");
    }
}
