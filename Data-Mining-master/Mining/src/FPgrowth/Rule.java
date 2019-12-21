package FPgrowth;


import java.util.ArrayList;

public class Rule {

    ItemSet Condition;
    ItemSet Conclusion;
    Double Confiance;


    public Rule() {
        Condition = new ItemSet();
        Conclusion = new ItemSet();
        Confiance = 0.;
    }


    public Rule(ItemSet condition, ItemSet conclusion, Double confiance) {
        Condition = condition;
        Conclusion = conclusion;
        Confiance = confiance;
    }


    public Rule(ItemSet condition, ItemSet conclusion) {
        Condition = condition;
        Conclusion = conclusion;
        Confiance = 0.;
    }


    //retourn le nombre de transaction contenant l'itemSet AUB
    public int SuppCountAUB(ArrayList<ItemSet> ListMotifFreq){
        int NewSupp=0;
        ItemSet CC = new ItemSet();
        CC.Items.addAll(Condition.Items);
        CC.Items.addAll(Conclusion.Items);

        for(ItemSet I:ListMotifFreq){
            if(CC.existsIn(I)){
                if(I.Supp>NewSupp)
                    NewSupp = I.Supp;
            }
        }

        return NewSupp;
    }


    //retourn le nombre de transaction contenant l'itemSet A
    public int SuppCountA(ArrayList<ItemSet> ListMotifFreq, ArrayList<Item> ListItem){
        int NewSupp=0;

        if(Condition.Items.size()==1 ){
            int position=-1;
            for(int i=0; i<ListItem.size(); i++){
                if(ListItem.get(i).item.equalsIgnoreCase(Condition.Items.get(0))) {
                    position = i;
                    break;
                }
            }
            NewSupp = ListItem.get(position).supp;
        }else{
            for(ItemSet I:ListMotifFreq){
                if(Condition.existsIn(I)){
                    if(I.Supp>NewSupp)
                        NewSupp = I.Supp;
                }
            }
        }

        return NewSupp;
    }


    public boolean existsIn(ArrayList<Rule> rules){
        if(rules.size()<=0 )return false;
        for(int i=0 ; i<rules.size()  ; i++){
            if(rules.get(i).Condition.equals(this.Condition) &&
                    rules.get(i).Conclusion.equals(this.Conclusion)){
                return true;
            }
        }
        return false;
    }






    @Override
    public String toString() {
        return "rule: "+this.Condition.Items+" -> "+this.Conclusion.Items+" conf = "+this.Confiance+"\n";
    }

    public static String show(ArrayList<Rule> Rules){
        String rules = "";
        for (Rule rule:Rules) {
            rules += " \t [Confiance = "+ (Math.round(rule.Confiance*100)/100) +
                    "] \t Rule: "+ rule.Condition.Items + " --> "+rule.Conclusion.Items+";";
        }
        return rules;
    }
}
