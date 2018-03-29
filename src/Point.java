/**
 * Created by jbern on 3/27/2018.
 */
public class Point {
    private int x;
    private int y;
    public Point(){
        x = 0;
        y = 0;
    }
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Point right(){
        return new Point(this.getX() + 1, this.getY());
    }
    public Point left(){
        return new Point(this.getX() - 1, this.getY());
    }
    public Point down(){
        return new Point(this.getX(), this.getY() + 1);
    }
    public Point up(){
        return new Point(this.getX(), this.getY() - 1);
    }

    public boolean inRange(int x, int y, int w, int h) {
        if (this.x < x)    return false;
        if (this.y < y)    return false;
        if (this.x >= x+w) return false;
        if (this.y >= y+h) return false;
        return true;
    }

    public double distanceTo(Point p){
        return Math.sqrt(distSqTo(p));
    }

    public double distSqTo(Point p){
        int dx = p.getX() - this.getX();
        int dy = p.getY() - this.getY();
        return dx*dx + dy*dy;
    }
}
