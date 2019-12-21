package KNN;

/**
 * Created by masterubunto on 27/11/18.
 */
public class Element
{
       public String Classi;
       public double Distancei;

    public String getClassi() {
        return Classi;
    }

    public void setClassi(String classi) {
        Classi = classi;
    }

    public double getDistancei() {
        return Distancei;
    }

    public void setDistancei(double distancei) {
        Distancei = distancei;
    }

    public Element(String classi) {
        Classi = classi;
    }
    public Element(String classi,double v)
    {
        Classi=classi;
        Distancei=v;
    }

    @Override
    public String toString() {
        return "Class:"+this.Classi+":"+this.Distancei;
    }
}
