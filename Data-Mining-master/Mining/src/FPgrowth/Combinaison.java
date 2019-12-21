package FPgrowth;


import java.util.ArrayList;

public class Combinaison {


    public Combinaison() {
        super();
    }

    // nombre de combinaison possible de k parmi n
    public int NbrCombinaisons(int k, int n) {
        if (k == n) return 1;
        if (k > n / 2) {
            k = n - k;
        }
        int res = n - k + 1;
        for (int i = 2; i <= k; i++) {
            res = res * (n - k + i) / i;
        }
        return res;
    }


    // générer les combinaison
    public ArrayList<ItemSet> combinaisons(ArrayList<Item> a, Item I) { // a = new Array(1,2)
        int min;ItemSet itemSet;
        ArrayList<ArrayList<Item>> tout = new ArrayList<>();
        ArrayList<ItemSet> resultat = new ArrayList<>();

        for (int i=0; i < a.size(); i++) {
            function(i, new ArrayList<Item>(a), new ArrayList<>(), tout);
        }
        tout.add(a);

        for(int i = 0; i<tout.size();i++){
            if(tout.get(i).size()>0) {
                tout.get(i).add(0,I);
                itemSet = new ItemSet();
                min=I.supp;

                for (int j = 0; j < tout.get(i).size(); j++) {
                    itemSet.addItem(tout.get(i).get(j));
                    if (min > tout.get(i).get(j).supp)
                        min = tout.get(i).get(j).supp;
                }
                itemSet.Supp = min;
                resultat.add(itemSet);
            }
        }


        return resultat;
    }

    public void function(int n, ArrayList<Item> source, ArrayList<Item> en_cours ,ArrayList<ArrayList<Item>> tout) {
        if (n == 0) {
            if (en_cours.size() > 0) {
                tout.add(en_cours);

            }
            return ;
        }
        for (int j = 0; j < source.size(); j++) {
            ArrayList<Item> concat = new ArrayList<>(en_cours);
            concat.add(source.get(j));
            ArrayList<Item> liste = new ArrayList<>(source.subList(j + 1, source.size()));
            function(n - 1, liste, concat , tout);
        }
        return ;
    }



    public ArrayList<ItemSet> SubSets(ItemSet a) { // a = new Array(1,2)
        ArrayList<ItemSet> tout = new ArrayList<>();
        ArrayList<ItemSet> resultat = new ArrayList<>();

        for (int i=0; i < a.Items.size(); i++) {
            function2(i, new ItemSet(a.Items,a.Supp), new ItemSet(), tout);
        }
        tout.add(a);

        return tout;
    }


    public void function2(int n, ItemSet source, ItemSet en_cours ,ArrayList<ItemSet> tout) {
        if (n == 0) {
            if (en_cours.Items.size() > 0) {
                tout.add(en_cours);
            }
            return ;
        }
        for (int j = 0; j < source.Items.size(); j++) {
            ItemSet concat = new ItemSet(new ArrayList<>(en_cours.Items),en_cours.Supp);
            concat.Items.add(source.Items.get(j));
            ItemSet liste = new ItemSet(new ArrayList<>(source.Items.subList(j + 1, source.Items.size())),source.Supp);
            function2(n - 1, liste, concat , tout);
        }
        return ;
    }


    /*
    For each frequent itemset l, generate all nonempty subsets of l.
    For every nonempty subset s of l, output the rule “s ⇒ (l − s)”

     */
    public ArrayList<Rule> generateRules(int min_conf,ItemSet itemSet, ArrayList<ItemSet> ListMotifFreq, ArrayList<Item> ListItem){
        ArrayList<ItemSet> subsets = new ArrayList<>();
        ArrayList<Rule> Rules = new ArrayList<>();
        ItemSet ll ;  Rule rule ;

        subsets = SubSets(itemSet);
        //System.out.println("subset de "+itemSet+" = "+subsets);
        for (ItemSet S: subsets) {
            for(ItemSet l: subsets){
                if(S.Items.size() <  l.Items.size()){
                    ll = l.supp(S).copy();
                    rule = new Rule(S,ll);
                    rule.Confiance = 100.0 * rule.SuppCountAUB(ListMotifFreq)  / rule.SuppCountA( ListMotifFreq ,  ListItem);
                    //System.out.println("( "+S+ " --> "+ll+ "conf= "+rule.Confiance);
                    if(rule.Confiance >= min_conf && !rule.existsIn(Rules) ){
                        Rules.add(rule);
                    }
                }
            }
        }
        return Rules;
    }

}