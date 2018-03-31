/**
 * Created by Edric on 3/27/2018.
 */
import java.awt.Color;

public class NoisePoint extends ScorePoint {

    public NoisePoint(Point p, Picture pic){
        super(p);
        this.setScore(calculateScore(p, pic));
    }

    public NoisePoint(Point p, double noise){
        super(p, noise);
    }

    public NoisePoint(int x, int y, double noise){
        super(new Point(x, y), noise);
    }

    /**
     * Method to assign a score to a pixel based on the difference from it to the pixels around it
     * All scores are less than 0 with higher scores being better
     * If the Target color option is set, will also subtract from the score if the pixel is far from the target color
     *
     * @param p Location of te pixel to score
     * @param pic picture that the pixel exists in
     * @return a noise score for the given pixel
     */
    public static double calculateScore(Point p, Picture pic) {
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

        if (Options.targetColor != null) {
            noise += distToTarget(myColor, Options.targetColor)/2.0;
        }

        return noise;
    }

    public static double distToTarget(Color myColor, Color targetColor) {
        return Math.abs(Pixel.colorDistance(myColor, targetColor));
    }
}
