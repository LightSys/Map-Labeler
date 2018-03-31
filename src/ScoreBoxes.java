/**
 * Created by Edric on 3/27/2018.
 */

/**
 * Score boxes is a collection of noise points that is used to determine the sum of noise in an area
 * This should be used to determine which area is the best to place a label
 */

public class ScoreBoxes {
    NoisePoint[][] nPixels;
    Double[][] boxNoise;
    private Point center;

    private int w;
    private int h;

    public ScoreBoxes(Picture pic, int boxW, int boxH){
        this.w = boxW;
        this.h = boxH;

        int pw = pic.getWidth();
        int ph = pic.getHeight();

        if (Options.locationX >= pw) throw new IllegalArgumentException("Specified X Location is out of bounds.");
        if (Options.locationX >= ph) throw new IllegalArgumentException("Specified Y Location is out of bounds.");


        nPixels = new NoisePoint[ph][pw];
        for (int y = 0; y < ph; y++){
            for (int x = 0; x < pw; x++){
                nPixels[y][x] = new NoisePoint(new Point(x, y), pic);
            }
        }

        boxNoise = new Double[ph - boxH][pw- boxW];

        setCenter(new Point(pw/2, ph/2));
    }

    public void setCenter(Point p) {
        this.center = p;
    }

    /**
     * Method to calculate which area in the picture will best hold the label
     * If target locations have been input, will not consider boxes that do not contain those coordinates
     * @return a score point at the top left corner of the best box
     */
    public ScorePoint getBestBoxLocation() {
        Double best = null;
        Point loc = null;

        boolean restrictX = Options.locationX != -1;
        boolean restrictY = Options.locationY != -1;

        for (int y = 0; y < boxNoise.length; y++) {
            for (int x = 0; x < boxNoise[0].length; x++) {
                if (restrictX){
                    if (Options.locationX < x) continue;
                    if (Options.locationX > x+w) continue;
                }
                if (restrictY){
                    if (Options.locationY < y) continue;
                    if (Options.locationY > y+h) continue;
                }

                double score = getBoxScore(x, y);
                if (best == null || score > best){
                    best = score;
                    loc = new Point(x, y);
                }
            }
        }
        return new ScorePoint(loc, best);
    }

    /**
     * Method to get the score for a certain box
     * Takes in the x and y coordinates of the top left corner of the box to score
     * @return a score for the box
     */
    private double getBoxScore(int x, int y) {
        double score = 0.0;
        score += getScoreForNoise(x, y);
        score += getScoreForCenterDist(x, y);
        return score;
    }

    /**
     * Method to get the score for a certain box
     * Takes in the point at the top left corner of the box to score
     * @return a score for the box
     */
    public double getBoxScore(Point p) {
        return getBoxScore(p.getX(), p.getY());
    }


    /**
     * Method to calculate how noisy a certain box is
     * If no adjacent boxes have had their noise caluclated, will calculate the noise by adding the noise for each pixel in the box
     * Otherwise, will calculate the noise by subtracting the noise for each pixel that is in the adjacent box but not in this one, and adding the noise for each pixel that is in this box but not in the other
     * Takes in the x and y coordinates of the top left point of the box
     * @return a value that is less than 0 that represents how noisy the box is
     */
    private double getScoreForNoise(int x, int y) {
        double noise = 0.0;
        if (boxNoise[y][x] != null) {
            return boxNoise[y][x];
        }
        else if (x > 0 && boxNoise[y][x-1] != null) { //left
            noise = boxNoise[y][x-1];
            noise -= getVerticalScore(x-1, y, h);
            noise += getVerticalScore(x+w-1, y, h);
        }
        else if (x < boxNoise[0].length - 1  && boxNoise[y][x+1] != null) { //right
            noise = boxNoise[y][x+1];
            noise -= getVerticalScore(x+w, y, h);
            noise += getVerticalScore(x, y, h);
        }
        else if (y > 0 && boxNoise[y-1][x] != null) { //up
            noise = boxNoise[y-1][x];
            noise -= getHorizontalScore(x, y-1, w);
            noise += getHorizontalScore(x, y+h-1, w);
        }
        else if (y < boxNoise.length - 1 && boxNoise[y+1][x] != null) { //down
            noise = boxNoise[y+1][x];
            noise -= getHorizontalScore(x, y+h, w);
            noise += getHorizontalScore(x, y, w);
        }
        else {
            for (int i = x; i < x + w; i++) {
                noise += getVerticalScore(i, y, h);
            }
        }
        boxNoise[y][x] = noise;
        return -noise;
    }

    /**
     * Method to calculate the noise for a set of pixels in a column
     * @param x x-coordinate of the top pixel in the column
     * @param y y-coordinate of the top pixel in the column
     * @param h number of pixels in the column
     * @return the sum of the noise of the pixels in the column
     */
    private double getVerticalScore(int x, int y, int h) {
        double score = 0.0;
        for (int i = y; i < y+h; i++) {
            score += nPixels[i][x].getScore();
        }
        return score;
    }

    /**
     * Method to calculate the noise for a set of pixels in a row
     * @param x x-coordinate of the left-most pixel in the row
     * @param y y-coordinate of the left-most pixel in the row
     * @param w number of pixels in the row
     * @return the sum of the noise of the pixels in the row
     */
    private double getHorizontalScore(int x, int y, int w) {
        double score = 0.0;
        for (int i = x; i < x+w; i++) {
            score += nPixels[y][i].getScore();
        }
        return score;
    }

    private double getScoreForCenterDist(int x, int y) {
        Point p = new Point(x + w/2, y + h/2);
        return -p.distanceTo(center);
    }

    private boolean contains(Point p, int boxX, int boxY) {
        return p.inRange(boxX, boxY, w, h);
    }
}
