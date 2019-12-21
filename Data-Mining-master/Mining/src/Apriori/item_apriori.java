package Apriori;

/**
 * Created by masterubunto on 10/11/18.
 */
public class item_apriori {

    public String Itemname;
    public   double  quantity;



    public item_apriori(String itemname, String quantity)
    {
        Itemname=new String ( itemname );
        if( quantity!=null)
        this.quantity= Double.parseDouble ( quantity ) ;

    }

    public String getItemname() {
        return Itemname;
    }

    public void setQuantity(double quantity) {
        this.quantity = new Double (  quantity);
    }

    public String toString()
    {

        return this.Itemname+":";
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
        item_apriori geek = (item_apriori)obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.getItemname ().compareTo (  this.Itemname )==0); }
    @Override
    public int hashCode() {
        int hash = (int) Math.random ();
      //  hash = Integer.parseInt (  this.getItemname ().substring ( this.getItemname ().length ()-1,this.getItemname ().length () ));
        return hash;
    }


}
