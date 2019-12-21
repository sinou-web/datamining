package FPgrowth;

import Apriori.DataSet;
import Apriori.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {

        int supp_min = 3;

        ArrayList<Item> tr;Item itemFPG;

        ArrayList<Tree> NodeList = new ArrayList<>();
        ArrayList<Item> NodePath = new ArrayList<>();
        ArrayList<Item> NodePath2 = new ArrayList<>();
        ArrayList<Item> NodeL = new ArrayList<>();
        ArrayList<ArrayList<Item>> PathList;
        ArrayList<Item> UnionItemList;
        ArrayList<ItemSet> ListMotifFreq = new ArrayList<>();

        Combinaison combinaison = new Combinaison();



        Tree fpTree =  new Tree("{}");


/***************************************************************************/
        DataSet dataSet = new DataSet("./dataset.xlsx");

        try {
            dataSet.readcsvdata();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

            //Inserer le transaction tr dans l'arbre
            fpTree.insertItem(tr,0);
        }






        //list des item dans l'ordre decroissant de support
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



        for (Item I: ListItems) {
        //item_apriori I = ListItems.get(0);
            //recuperer dans une liste NodeList la liste de tous les noeuds de l'item I
            NodeList = new ArrayList<>(fpTree.getItemNodes(new ArrayList<>(),I.item));
            PathList = new ArrayList<>();

            System.out.println("\n item_apriori : "+I.item);

            //Pour chaque noeud de la liste des noeuds précédente
            for (int i=0; i < NodeList.size(); i++){

                //mettre dans une liste NodePath le chemin de la racine vers le i ème noeuds
                NodePath = fpTree.getPath(NodeList.get(i));
                Tree t = NodeList.get(i);
                NodePath2 = Item.copy(NodePath);
                //supprimer les items ayant un support insufisant
                NodeL = new ArrayList<>(t.item.DeleteItem(0,NodePath2,supp_min));
                //mettre a jours le support des item du chemin
                NodeL = new ArrayList<>(t.item.setPathSupp(NodeL));
                PathList.add(NodeL);

                System.out.println("supp = "+t.item.supp+" path "+(i+1)+" : "+NodeL);

            }

            //union des chemins d'un meme item_apriori
            UnionItemList = Item.Union(PathList,supp_min);
            //System.out.println("UnionItemList : "+UnionItemList);

            //generer toutes les combinaisons
            ArrayList<ItemSet> al = combinaison.combinaisons(UnionItemList,I);
            System.out.println("item_apriori: "+I+" combinaisons: "+al);

            ListMotifFreq.addAll(al);
            //System.out.println("**"+combinaison.SubSets(al.get(3))+"**");
        }
        //System.out.println(fpTree);
        //System.out.println(ListItems);
        System.out.println(ListMotifFreq);

        //les regles
        System.out.println( combinaison.generateRules(50,ListMotifFreq.get(3),ListMotifFreq,ListItems) );

/*
        NodeList = fpTree.getItemNodes(NodeList,"a");
        NodePath = fpTree.getPath(NodeList.get(0));

        Tree t = NodeList.get(NodeList.size()-1);
        NodeL = t.item.DeleteItem(0,NodePath,3);
        System.out.println("apres "+NodeL);

*/
    }

}
/*
(null:1) child :
        [
                (B:6) child :[
                                (E:5) child :[
                                        (A:4) child : [
                                                    (D:2) child : []
                                                    ,(C:2) child :[(D:1) child : [] ]
                                                        ]
                                        ,(C:1) child : 	 []
                                             ]
                                ,(C:1) child : [(D:1) child :[] ]
                              ]
        ]



[

item_apriori : D
path 1 : [(A:2), (E:2), (B:2)]
path 2 : [(A:1), (E:1), (B:1)]
path 3 : [(B:1)]

B:4
E:3
A:3

DA:3
DE:3
DB:4
DAE:3
DAB:3
DEB:3
DAEB:3
*/

