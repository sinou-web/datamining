package UI;

import java.util.ArrayList;

public class MedianQ1Q3 {
     public double mediane;
     public double q1;
     public double q2;
     public ArrayList<Double> Datavalues;


     public MedianQ1Q3(ArrayList<Double> s)
     {
         this.Datavalues=s;
         mediane=q1=q2=0;
     }
     public MedianQ1Q3()
     {
         this.Datavalues=new ArrayList<> (  );
         mediane=q1=q2=0;
     }

    public  void CalculAll(int deb, int fin ){

        double median=0;
        int indecx=0,i=0;


        if(fin-deb+1 % 2 == 0){
            median = Datavalues.get((fin-deb)/2)+Datavalues.get((fin-deb+1)/2);
            indecx=fin-deb+1;


        }else {
            median = Datavalues.get((fin-deb)/2);
            indecx=(fin-deb)/2;

        }
        this.mediane=new Double (  median);
        //q1
        if(indecx%2==0)
        {
            median=(Datavalues.get(indecx/2)+Datavalues.get((indecx+1)/2))/2;


        }
        else
        {
            median = Datavalues.get((indecx)/2);

        }

        this.q1=new Double (  median);
        //q2
        if((Datavalues.size ()-indecx+1)%2==0)
        {
            median = Datavalues.get(Datavalues.size() -((Datavalues.size ()-indecx)/2))
                    +Datavalues.get(Datavalues.size ()-((Datavalues.size ()-indecx+1)/2))
            /2;
        }
        else {
            median = Datavalues.get(Datavalues.size() -((Datavalues.size ()-indecx)/2));

        }


        this.q2=new Double (  median);




    }

    //hashmap (classe,liste des instances)
    //
}
