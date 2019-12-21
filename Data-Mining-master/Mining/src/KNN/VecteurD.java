package KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by masterubunto on 27/11/18.
 */
public class VecteurD
{
    public ArrayList<Element> ListofDistances;


    public VecteurD(ArrayList<Element> listofDistances) {
        ListofDistances = listofDistances;
    }
    public VecteurD()
    {
        ListofDistances=new ArrayList<> (  );
    }
    public void add (Element e)
    {
        this.ListofDistances.add ( e );
    }
    public void Sort()

    {
        Collections.sort ( this.ListofDistances, new Sortbyroll () );
    }
    public List GetKfirstelements(int k)
    {
        this.Sort ();
        List<Element> head = this.ListofDistances.subList(0, k);
        return  head;

    }
    public String getMostcommonInK(int k)
    { ArrayList<Element> slide= new ArrayList<Element> (  );
        slide.addAll (  this.GetKfirstelements ( k ));
        // pour calculer la classe la plus frequente dans K element i used a dictionnary of Class=>Frequency
        Hashtable<String,Integer> Dicoffrequency= new Hashtable<> (  );

        //creation of dic of frequency per class
        for (Element e : slide)
        { if( Dicoffrequency.get ( e.Classi) ==null)
               Dicoffrequency.put ( e.Classi, 1);
           else
               Dicoffrequency.replace ( e.Classi,Dicoffrequency.get ( e.Classi ) ,Dicoffrequency.get (e.Classi)+1);

        }
        String classi =null;
        int max=0;
        // lookup for class having the max Frequency
        for (String key : Dicoffrequency.keySet ())
          {
             // System.out.println ("key:"+key+"value:"+Dicoffrequency.get ( key ) );

            if( max<= Dicoffrequency.get ( key ))
            { max = Dicoffrequency.get ( key );
              classi=new String ( key );
            }

        }
       // System.out.println ("mostcommon:"+classi );
        return classi;
   }








}
