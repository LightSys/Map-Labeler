/**
 * Created by Edric on 3/30/2018.
 */
public class Dimension {
    int w;
    int h;
    public Dimension(){
        w = 0;
        h = 0;
    }
    public Dimension(int w, int h){
        this.w = w;
        this.h = h;
    }
    public boolean fitsInside(int otherW, int otherH){
        if (this.w >= otherW) return false;
        if (this.h >= otherH) return false;
        return true;
    }

    public boolean fitsInside(Dimension other){
        return fitsInside(other.w, other.h);
    }
}
