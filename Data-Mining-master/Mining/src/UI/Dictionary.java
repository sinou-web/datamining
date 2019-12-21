package UI;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by masterubunto on 19/10/18.
 */
public class Dictionary<T,Instance>
{

   public HashMap<T,HashSet<Instance>> index_list;

   public Dictionary()
   {
       index_list= new HashMap<T, HashSet<Instance>>  ();

   }
    public HashMap<T, HashSet<Instance>> getIndex_list() {
        return index_list;
    }


    public void add (T key)
    {

        this.index_list.put ( key,new HashSet<Instance> (  ) );

    }
    public void set_values( T key ,Instance instance)
    {

        if(this.index_list.get(key)==null)
            this.index_list.put ( key,new HashSet<> (  ) );
           this.index_list.get ( key ).add ( instance );


    }
    public HashSet get_values( T key)
    {

        return  this.index_list.get ( key );
    }
    public void Show_dic()
    {
        for(T key:getIndex_list ().keySet ())
        {
            System.out.println ("clé:{"+key+"}: values:{" );
            for (Instance value: getIndex_list ().get ( key ))
            {
                System.out.println ( value);
            }
            System.out.println ("}" );
        }
    }


}
