package Apriori;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by masterubunto on 10/11/18.
 */
public class SetofTransaction {

    public HashMap<Transaction, ArrayList<item_apriori>> set_T;


    public SetofTransaction() {
        set_T = new HashMap<> ( );
    }

    public void addTransaction(Transaction t) {

        if (this.set_T == null) this.set_T = new HashMap<> ( );
        if (!this.set_T.containsKey ( t )) this.set_T.put ( t, null );
        if(this.set_T.get ( t )==null) this.set_T.replace ( t,null, new ArrayList<item_apriori> (  ));
    }



    public void addItem_to_transaction(Transaction t, item_apriori I) {

        addTransaction ( t );
        this.set_T.get ( t ).add ( I );
       // System.out.println ("additem:" +this.set_T.get ( t ).get ( 0).getItemname ());

    }

    public boolean existitem(item_apriori i, Transaction t) {
        for (item_apriori value : this.set_T.get ( t )) {

//            System.out.println ("value:"+value+"itemcomparing:"+i+"compare:"+value.getItemname ( ).compareTo ( i.getItemname ( ) ) );
            if (value.getItemname ( ).compareTo ( i.getItemname ( ) ) == 0) {
                return true;
            }
        }
        return false;
    }

    public SetItemset tranfortoitemset( HashMap<String,Integer> uniqueset)
    {
        SetItemset set=new SetItemset ();
        for(String s : uniqueset.keySet ())
        {
            itemset set_items =new itemset ();
            set_items.addItem ( new item_apriori( s ,"0") );
            set_items.setFrequence ( uniqueset.get ( s ) );
            set.addItemset (set_items);

        }
        System.out.println ("transformsize:"+set.size () );
        return set;
    }

    public int getfreqitems(itemset set)
    {
        int freq = 0;
        boolean exist;
        int i=0;
        for (Transaction key : this.set_T.keySet ( )) {
            exist = true;
             i= 0;

            while (exist && i < set.size ( ))
            {
                 exist = exist && existitem ( set.get ( i ), key );
                 i++;
            }
            if( exist== true)  freq++;
        }
     //   System.out.println ("frequence:"+freq );
              return freq;
    }





public void Show ()
{

    for ( Transaction key: this.set_T.keySet ())
    {
        System.out.println ("Transactions:"+key+":items:" );

        for( item_apriori value :this.set_T.get ( key ))
        {

            System.out.print(","+value );
        }
        System.out.println ( );


    }



}









}
