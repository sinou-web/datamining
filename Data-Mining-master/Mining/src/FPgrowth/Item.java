package FPgrowth;

import java.util.ArrayList;
import java.util.HashMap;

public class Item implements Comparable{

    public String item;
    public int supp;



    public Item(String item, int supp) {
        this.item = item;
        this.supp = supp;
    }

    public Item(String item) {
        this.item = item;
        this.supp = 1;
    }


    //appele avec l'item et son chemin en parametre

    public ArrayList<Item> DeleteItem(int i, ArrayList<Item> Path, int supp_min) {

        if(Path.size()>0)
            Path=Path.get(i).DeleteItems(i, Path, supp_min,this.supp);
        return Path;
    }



    public ArrayList<Item> DeleteItems(int i, ArrayList<Item> Path, int supp_min, int ItemSupport){
        if(this.supp < supp_min){

            Path = new ArrayList<>(Path.subList(1,Path.size()));
            if(Path.size()>0)
                Path.get(i).DeleteItems(i,Path,supp_min,ItemSupport);
        }
        return Path;
    }


    public ArrayList<Item> setPathSupp(ArrayList<Item> Path){
        ArrayList<Item> P = Item.copy(Path);
        for (Item I: P) {
            I.supp=this.supp;
        }
        return P;
    }

    public static ArrayList<Item> copy(ArrayList<Item> pathList){
        ArrayList<Item> newPath = new ArrayList<>();
        for (Item I:pathList) {
            newPath.add(new Item(I.item,I.supp));
        }
        return newPath;
    }

    public static ArrayList<Item> Union(ArrayList<ArrayList<Item>> PathList, int supp_min ){
        ArrayList<Item> unionResult = new ArrayList<>();
        HashMap<String,Integer> dico = new HashMap<String, Integer>();
        int supp;
        for (int i = 0; i<PathList.size(); i++){
            for(int j =0; j<PathList.get(i).size() ; j++){
                dico.put(PathList.get(i).get(j).item,dico.getOrDefault(PathList.get(i).get(j).item,0)+PathList.get(i).get(j).supp);
            }
        }

        for (String key: dico.keySet()) {
            supp = dico.get(key);
            if(supp >= supp_min)
                unionResult.add(new Item(key,supp));
        }

        return unionResult;
    }





    @Override
    public int compareTo(Object o) {
        Item I = (Item) o;
        if(this.supp < I.supp) return 1;
        if(this.supp > I.supp) return -1;

        return this.item.compareTo(I.item);
    }

    @Override
    public String toString() {
        return ("("+this.item+":"+this.supp +") ");
    }
}
