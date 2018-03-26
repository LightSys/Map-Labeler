/**
 * Created by Edric on 3/26/2018.
 */
public class LabelerMain {
    public static void main(String[] args){
        String filename = args[0];
        System.out.println("Opening " + filename);

        // open file
        Picture mypic = new Picture(filename);


        // edit image

        int w = mypic.getDisplayWidth("chungis");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < 16; j++) {
                mypic.getPixel(100+i,200+j).setBlue(0);
                mypic.getPixel(100+i,200+j).setRed(255);
                mypic.getPixel(100+i,200+j).setGreen(0);
            }
        }

        mypic.addMessage("chungis", 100, 200+16);

        // write to new file
        mypic.write("D:\\git\\Map-Labeler\\out.gif");
        System.out.println("done?");
    }
}
