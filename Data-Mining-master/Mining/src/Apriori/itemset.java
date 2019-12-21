package Apriori;

import FPgrowth.Item;
import FPgrowth.ItemSet;

import java.util.HashSet;

/**
 * Created by masterubunto on 11/11/18.
 */
public class itemset
{
  public HashSet<item_apriori> items;
  public int frequence;


  public itemset(itemset s)
  {
      this.items= new HashSet<item_apriori> (  )
      ;
      for (item_apriori i : s.getItems ())
      {
          this.items.add ( new item_apriori( i.getItemname (),"0" ) );


      }

  }


    public void setItems(HashSet<item_apriori> items) {
        this.items = items;
    }

    /*************************************/
    public ItemSet ConvertToArray(){

        ItemSet s=new ItemSet();

        if(this.items.size() > 1) {
            for (item_apriori i : this.getItems()) {
                //s.Items.add(i.getItemname());
                s.addItem(new Item(i.Itemname, (int) i.quantity));
            }
            s.Supp = this.getFrequence();
        }
        System.out.println(this.frequence);

        return s;
    }

  public itemset ()
  {
      items=new HashSet<item_apriori> (  );
  }

    public HashSet<item_apriori> getItems() {
        return items;
    }

    public void addItem(item_apriori e)
  {
      if( items ==null) items=new HashSet<item_apriori> (  );

      items.add ( e );

  }
  public void addItems (HashSet<item_apriori> items)
  {
      for (item_apriori e : items)
          this.addItem ( e );

  }
  public int size()
  {return this.items.size ();}
  public item_apriori get(int i)
  {return (item_apriori) this.items.toArray ()[i];}
  public String show ()
  { String s="";
      for (item_apriori i : this.items)
      {
          s+=i.toString ();
      }
      return s+","+this.frequence;
  }
  public boolean existItem(item_apriori i)
  {
      for (item_apriori item:items)
      {
         if(i.equals ( item )==true) return true;

      }
      return false;


  }

    @Override
    public boolean equals(Object obj) {
        // checking if both the object references are
        // referring to the same object.
        if(this==obj) return true;

        // it checks if the argument is of the
        // type Geek by comparing the classes
        // of the passed argument and this object.
        // if(!(obj instanceof Geek)) return false; ---> avoid.
        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        // type casting of the argument.
        itemset geek = (itemset) obj;
        if(this.items.size ()!=geek.size ()) return false;

        for(item_apriori i:geek.getItems ())
        {

            if(existItem ( i )==false) return false;

        }
        return true;

        // comparing the state of argument with

  }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = frequence;
        return hash;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }
}
