package Apriori;

import FPgrowth.Combinaison;
import FPgrowth.Item;
import FPgrowth.ItemSet;
import FPgrowth.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by masterubunto on 11/11/18.
 */
public class Apriori
{
    public int threshold_confidence;
    public static DataSet data;
    public static int iteration=0;
    public int threshold_support;

    public SetItemset Lk;
    public SetItemset Ck;

    Combinaison combinaison = new Combinaison();
    public static HashMap<String,Integer> data2;


    public Apriori(String filename, int threshold_support,int threshold_confidence)
    {
        iteration++;
        data=new DataSet( filename );
        try {
            data.readcsvdata();
            data2 = new HashMap<>(data.Items);
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        Ck =new SetItemset ();
        Lk=new SetItemset ();

        this.threshold_support = threshold_support;
        this.threshold_confidence=threshold_confidence;
    }



    public SetItemset elimnate_less_than_threshold()
    {
        ArrayList<String> keys = new ArrayList<> (  );
         for( String S : data.Items.keySet ())
         {     keys.add ( S );}

        for (String item :keys)
        {
            if(data.Items.get ( item )<threshold_support)
                data.Items.remove ( item );
        }

        this.Ck= data.set_t.tranfortoitemset ( data.Items);
        return Ck;


    }

    public SetItemset getLk() {
        return Lk;
    }

    public void setLk(SetItemset lk) {
        Lk = lk;
    }

    public SetItemset join ()
    {

       iteration++;
        SetItemset C_kprime = new SetItemset ();

        for (int i=0; i< Ck.size ()-1; i++)
        {
            for(int j=i+1;j<Ck.size ();j++)
           { /* join 1 with 2 */

            itemset set = new itemset ( Ck.get ( i ) );

               set.addItems (  Ck.get ( j ).getItems ());
              // System.out.println ("joinA:"+set.show ());
            /*calculer la frequence */
               set.setFrequence (data.getSet_t ().getfreqitems ( set));
             //  System.out.println ("joinA:"+set.show ());
            /*calculer la frequence */

               if( set.getFrequence ()>=threshold_support)
                C_kprime.addItemset (  set);
           }
        }

        //System.out.println ("C("+iteration+"):");
        C_kprime.show ();

         return C_kprime;
    }

    public String apriori()
    {
        String L_k="";
        /* C1 is done already*/

        /* the join part */

         Lk=this.elimnate_less_than_threshold ();

       while( Ck.size ()>0)
        {
          /* join Lk-1 with Lk-1 to get Ck BUT in Join we do the prunning by removing the itemset not having the equals support*/

            Ck=this.join ();
            /* adding les K itemset satisfying the min support*/

            for(itemset s : Ck.set_itemset)

                this.Lk.addItemset (  s);
        }
        L_k=this.Lk.show ();

     return L_k;
    }


    public ArrayList<ItemSet> toArrayofItemSe()
    {
        ArrayList<ItemSet> set = new ArrayList<>();
        ItemSet itemSet;
        for (itemset i : this.getLk().set_itemset)
        {
            itemSet = i.ConvertToArray();
            if(itemSet.Supp != 0)
                set.add(itemSet);
        }
        return set;
    }

    public ArrayList<Item> ListItemFreq(){
        //list des item dans l'ordre croissant de support
        ArrayList<Item> ListItems = new ArrayList<>();
        ArrayList<Item> ListItemss = new ArrayList<>();

        for(String ItemAp: data2.keySet()){
            ListItemss.add(new Item(ItemAp, data2.get(ItemAp)));
        }
        //ordoner puis inverser
        Collections.sort(ListItemss);
        for(Item iteem:ListItemss){
            ListItems.add(0,iteem);
        }

        return ListItems;
    }



    public ArrayList<Rule> GenererRules(ArrayList<ItemSet> ListMotifFreq, ArrayList<Item> ListItems){
        ArrayList<Rule> AllRules = new ArrayList<>();
        ArrayList<Rule> ItemSetRules = new ArrayList<>();

        for(int i=0; i<ListMotifFreq.size(); i++){
            ItemSetRules = combinaison.generateRules(threshold_confidence ,
                    ListMotifFreq.get(i),ListMotifFreq,ListItems);
            for(int j=0 ; j<ItemSetRules.size(); j++){
                //System.out.println("rule "+ItemSetRules.get(j));
                if(!ItemSetRules.get(j).existsIn(AllRules))
                    AllRules.add(ItemSetRules.get(j));
            }
        }
        return AllRules;
    }


}
