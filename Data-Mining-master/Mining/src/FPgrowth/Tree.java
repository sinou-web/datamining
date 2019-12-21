package FPgrowth;

import java.util.ArrayList;

public class Tree {


    public Item item;
    public ArrayList<Tree> child;
    public Tree dad;

    public Tree(String item, int supp, Tree dad) {
        this.item = new Item(item,supp);
        this.dad = dad;
        this.child = new ArrayList<>();
    }


    public Tree(Item item, Tree dad) {
        this.item = new Item(item.item,item.supp);
        this.dad = dad;
        this.child = new ArrayList<>();
    }

    public Tree(String item) {
        this.item = new Item(item,1);
        this.dad = null;
        this.child = new ArrayList<>();
    }




    public void IncrementerNode() {
        this.item.supp++;
    }
    
    public void addChild(Tree n){
        this.child.add(n);
        n.dad = this;
    }


    public Tree getChild(Item item){
        return this.child.get(this.child.indexOf(item));
    }


    public void insertItem(ArrayList<Item> transaction, int i){
        Tree tree, trans = new Tree(transaction.get(i).item);
        Boolean find;
        String item = trans.item.item;
            find = false;
            tree = new Tree(item);
            for (Tree t: this.child) {
                if(t.item.item.equalsIgnoreCase(item)){
                    t.IncrementerNode();
                    find = true;
                    tree = t;
                }
                if(find)break;
            }
            if(!find){
                this.addChild(tree);
            }
            i++;
            if(i<transaction.size()) tree.insertItem(transaction,i);
    }




    //on fait l'appel avec la racine de l'arbre, on donne en parametre :
    //la liste vide dans la quelle sera ajouté les noeuds ayant le meme item
    //ainsi que l'item recherché
    public ArrayList<Tree> getItemNodes(ArrayList<Tree> NodeList, String item) {
        if(item.equalsIgnoreCase(this.item.item)) {
            Tree tree = this;
            NodeList.add(tree);
        }
        for (Tree fils : this.child) {
            fils.getItemNodes(NodeList,item);
        }
        return NodeList;
    }


    public ArrayList<Item> getPath(Tree t) {
        ArrayList<Item> path = new ArrayList<>();
        Tree tree = t.dad;
        while(tree.dad != null){
            path.add(tree.item);
            tree = tree.dad;
        }
        return path;
    }


    public ArrayList<String> getPathS() {
        ArrayList<String> path = new ArrayList<>();
        Tree tree = this.dad;
        while(tree.dad != null){
            path.add(tree.item.item);
            tree = tree.dad;
        }
        return path;
    }

    @Override
    public String toString() {
        return ("("+this.item.item+":"+this.item.supp +") child : \t "+this.child.toString()+" \n ");
    }
}
