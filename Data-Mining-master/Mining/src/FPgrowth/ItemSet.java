package FPgrowth;

import java.util.ArrayList;

public class ItemSet {


    public ArrayList<String> Items;
    public int Supp;


    public ItemSet(ArrayList<String> items,int supp) {
        Items = items;
        Supp = supp;
    }


    public ItemSet() {
        Items = new ArrayList<>();
        Supp = 0;
    }

    public ItemSet(int supp) {
        Items = new ArrayList<>();
        Supp = supp;
    }

    public ItemSet addItem(Item item){
        this.Items.add(item.item);

        if(this.Supp > item.supp || this.Supp==0){
            this.Supp = item.supp;
        }
        return this;
    }




    @Override
    public String toString() {
        return ("("+this.Items+":"+this.Supp +")");
    }



    public boolean equals(ItemSet itemSet){
        if(this.Items.size() != itemSet.Items.size())return false;
        for(int i=0 ; i<this.Items.size()  ; i++){
            if(!this.Items.contains(itemSet.Items.get(i)) ||
                    !itemSet.Items.contains(this.Items.get(i))){
                return false;
            }
        }
        return true;
    }




    public boolean existsIn(ItemSet itemSet){
        if(this.Items.size() > itemSet.Items.size())return false;
        System.out.println(this.Items+"---"+itemSet.Items);
        for(int i=0 ; i<this.Items.size()  ; i++){
            if(!itemSet.Items.contains(this.Items.get(i))){
                return false;
            }
        }
        return true;
    }


    public ItemSet copy(){
        ItemSet itemSet = new ItemSet();
        for (String S:this.Items) {
            itemSet.Items.add(S);
        }
        itemSet.Supp = this.Supp;

        return itemSet;
    }

    public ItemSet supp(ItemSet itemSet){
        boolean trouve=false;
        ItemSet newItemSet = new ItemSet();

        for(int j=0; j<this.Items.size(); j++){
            trouve = false;
            for(int i=0; i<itemSet.Items.size(); i++){
                if(itemSet.Items.get(i).equalsIgnoreCase(this.Items.get(j))) {
                    trouve = true; break;
                }
            }
            if(trouve == false){
                Item ii = new Item(this.Items.get(j));
                newItemSet.addItem(ii);
            }

        }
        return newItemSet;
    }
}
