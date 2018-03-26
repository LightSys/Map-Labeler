/**
 * Created by Edric on 3/26/2018.
 */
public class LabelerMain {
    public static void main(String[] args){
        //args[0] is the file
        //args[1] is the text of the label

        String filename = args[0];
        System.out.println("Opening " + filename);

        // open file
        Picture mypic = new Picture(filename);

        // find best box
        int w = mypic.getDisplayWidth(args[1]);
        int h = 16;

        int score = mypic.getScore(0, 0, w, h);
        int bestX = 0;
        int bestY = 0;
        for (int y = 0; y < mypic.getHeight() - h; y++) {
            for (int x = 0; x < mypic.getWidth() - w; x++) {
                int newScore = mypic.getScore(x, y, w, h);
                if (score < newScore){
                    score = newScore;
                    bestX = x;
                    bestY = y;
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

        mypic.drawString(args[1], bestX, bestY);

        // write to new file
        mypic.write("D:\\git\\Map-Labeler\\out.gif");
        System.out.println("done?");
    }
}
