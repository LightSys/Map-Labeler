/**
 * Created by Edric on 3/27/2018.
 */
public class NoisePoint {
    private Point p;
    private double noise = 0.0;

    public NoisePoint() {}

    public NoisePoint(Point p){
        this.p = p;
    }

    public NoisePoint(Point p, double noise){
        this.p = p;
        this.noise = noise;
    }

    public NoisePoint(int x, int y, double noise){
        this.p = new Point(x, y);
        this.noise = noise;
    }

    public Point getP() {
        return p;
    }

    public void setNoise(double n) {
        this.noise = n;
    }

    public double getNoise() {
        return noise;
    }
}
