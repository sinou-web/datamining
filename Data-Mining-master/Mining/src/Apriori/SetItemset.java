package Apriori;

import java.util.HashSet;

/**
 * Created by masterubunto on 11/11/18.
 */
public class SetItemset
{

    public HashSet<itemset> set_itemset;




    public SetItemset ()
    {
        this.set_itemset=new HashSet<itemset> (  );
    }
    public void addItemset(itemset set)
    {
        if(set_itemset== null) this.set_itemset =new HashSet<itemset> (  );
        this.set_itemset.add ( set );

    }
    public int size ()
    {
        return this.set_itemset.size ();
    }
    public itemset get(int i)
    {
        return (itemset) this.set_itemset.toArray ()[i];
    }


    public String show ()
    {
         String show=";";
        for (itemset s :this.set_itemset)
             show+=(s.show () )+"\n";

        return show;

    }

}
