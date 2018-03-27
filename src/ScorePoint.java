/**
 * Created by Edric on 3/27/2018.
 */
public class ScorePoint {
    private Point p;
    private double score = 0.0;

    public ScorePoint() {}

    public ScorePoint(Point p){
        this.p = p;
    }

    public ScorePoint(Point p, double score){
        this.p = p;
        this.score = score;
    }

    public ScorePoint(int x, int y, double score){
        this.p = new Point(x, y);
        this.score = score;
    }

    public Point getP() {
        return p;
    }

    public void setScore(double s) {
        this.score = s;
    }

    public double getScore() {
        return score;
    }
}
