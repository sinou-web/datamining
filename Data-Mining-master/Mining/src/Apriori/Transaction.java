package Apriori;

/**
 * Created by masterubunto on 10/11/18.
 */
public class Transaction
{
   public String idtransaction;




   public Transaction( String id)
   {

       idtransaction = new String ( id );



   }
   public String toString()
   {
       return this.idtransaction;

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
        Transaction geek = (Transaction )obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.idtransaction.compareTo (  this.idtransaction )==0); }
    @Override
    public int hashCode() {
        int hash = 1;
        hash = Integer.parseInt (  this.idtransaction.substring ( this.idtransaction.length ()-1,this.idtransaction.length () ));

        return hash;
    }

    }
