package FPgrowth;

import Apriori.DataSet;
import Apriori.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FpGrowth {

    public int threshold_confidence;
    public int threshold_support;
    public static DataSet dataSet;
    Tree fpTree =  new Tree("{}");
    Combinaison combinaison = new Combinaison();

    public FpGrowth(String filename, int threshold_support,int threshold_confidence)
    {
        dataSet=new DataSet( filename );
        try {
            dataSet.readcsvdata();
        } catch (IOException e) {
            e.printStackTrace ( );
        }

        this.threshold_support = threshold_support;
        this.threshold_confidence=threshold_confidence;

        createTree();
    }


    public void createTree(){
        ArrayList<Item> tr;Item itemFPG;

        ArrayList<Transaction> Ts = new ArrayList<>(dataSet.set_t.set_T.keySet());
        for(int i=0; i<Ts.size(); i++){
            tr=new ArrayList<>();
            for(int j=0; j<dataSet.set_t.set_T.get(Ts.get(i)).size(); j++){
                itemFPG = new Item(dataSet.set_t.set_T.get(Ts.get(i)).get(j).Itemname,
                        dataSet.Items.get(dataSet.set_t.set_T.get(Ts.get(i)).get(j).Itemname));
                tr.add(itemFPG);
            }
            //trier tr selon support de ses Items
            Collections.sort(tr);
            fpTree.insertItem(tr,0);
        }
    }



    public ArrayList<Item> ListItemFreq(){
        //list des item dans l'ordre croissant de support
        ArrayList<Item> ListItems = new ArrayList<>();
        ArrayList<Item> ListItemss = new ArrayList<>();

        for(String ItemAp: dataSet.Items.keySet()){
            ListItemss.add(new Item(ItemAp, dataSet.Items.get(ItemAp)));
        }
        //ordoner puis inverser
        Collections.sort(ListItemss);
        for(Item iteem:ListItemss){
            ListItems.add(0,iteem);
        }
        return ListItems;
    }



    public ArrayList<ItemSet> ListMotifFreq(ArrayList<Item> ListItems){

        ArrayList<Tree> NodeList = new ArrayList<>();
        ArrayList<Item> NodePath = new ArrayList<>();
        ArrayList<Item> NodePath2 = new ArrayList<>();
        ArrayList<Item> NodeL = new ArrayList<>();
        ArrayList<ArrayList<Item>> PathList;
        ArrayList<Item> UnionItemList;
        ArrayList<ItemSet> ListMotifFreq = new ArrayList<>();

        for (Item I: ListItems) {
            //Item I = ListItems.get(0);
            //recuperer dans une liste NodeList la liste de tous les noeuds de l'item I
            NodeList = new ArrayList<>(fpTree.getItemNodes(new ArrayList<>(),I.item));
            PathList = new ArrayList<>();

            //System.out.println("Item: "+I);

            //Pour chaque noeud de la liste des noeuds précédente
            for (int i=0; i < NodeList.size(); i++){

                //mettre dans une liste NodePath le chemin de la racine vers le i ème noeuds
                NodePath = fpTree.getPath(NodeList.get(i));
                Tree t = NodeList.get(i);
                NodePath2 = Item.copy(NodePath);
                //supprimer les items ayant un support insufisant
                //NodeL = new ArrayList<>(t.item.DeleteItem(0,NodePath2,threshold_support));
                //mettre a jours le support des item du chemin
                NodeL = new ArrayList<>(t.item.setPathSupp(NodePath2));
                PathList.add(NodeL);

                //System.out.println("supp = "+t.item.supp+" path "+(i+1)+" : "+NodeL);
            }

            //union des chemins d'un meme Item
            UnionItemList = Item.Union(PathList,threshold_support);
            //System.out.println("UnionItemList : "+UnionItemList);

            //generer toutes les combinaisons
            ArrayList<ItemSet> al = combinaison.combinaisons(UnionItemList,I);

            ListMotifFreq.addAll(al);
        }
        return ListMotifFreq;
    }



    public ArrayList<Rule> GenererRules(ArrayList<ItemSet> ListMotifFreq, ArrayList<Item> ListItems){
        ArrayList<Rule> AllRules = new ArrayList<>();
        ArrayList<Rule> ItemSetRules = new ArrayList<>();

        for(int i=0; i<ListMotifFreq.size(); i++){
            ItemSetRules = combinaison.generateRules(threshold_confidence ,
                    ListMotifFreq.get(i),ListMotifFreq,ListItems);

            for(int j=0 ; j<ItemSetRules.size(); j++){
                if(!ItemSetRules.get(j).existsIn(AllRules))
                    AllRules.add(ItemSetRules.get(j));
            }
        }


        return AllRules;
    }

}
