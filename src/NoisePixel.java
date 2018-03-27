/**
 * Created by Edric on 3/27/2018.
 */
import java.awt.Color;

public class NoisePixel extends NoisePoint{

    //check 4 cardinal directions
    public NoisePixel(Point p, Picture pic){
        super(p);
        this.setNoise(calculateNoise(p, pic));
    }

    public NoisePixel(Point p, double noise){
        super(p, noise);
    }

    public NoisePixel(int x, int y, double noise){
        super(new Point(x, y), noise);
    }

    public static double calculateNoise(Point p, Picture pic) {
        int pw = pic.getWidth();
        int ph = pic.getHeight();
        double noise = 0.0;

        Color myColor = pic.getPixel(p).getColor();
        int count = 0;
        if (p.left().inRange(0, 0, pw, ph)) {
            Color colorL = pic.getPixel(p.left()).getColor();
            noise += Pixel.colorDistance(myColor, colorL);
            count ++;
        }
        if (p.right().inRange(0, 0, pw, ph)) {
            Color colorR = pic.getPixel(p.right()).getColor();
            noise += Pixel.colorDistance(myColor, colorR);
            count ++;
        }
        if (p.up().inRange(0, 0, pw, ph)) {
            Color colorU = pic.getPixel(p.up()).getColor();
            noise += Pixel.colorDistance(myColor, colorU);
            count ++;
        }
        if (p.down().inRange(0, 0, pw, ph)) {
            Color colorD = pic.getPixel(p.down()).getColor();
            noise += Pixel.colorDistance(myColor, colorD);
            count ++;
        }
        noise = noise / (double)count;
        return noise;
    }
}
