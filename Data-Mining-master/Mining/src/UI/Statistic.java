package UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import weka.core.Attribute;
import weka.core.Instance;

import java.util.*;

/**
 * Created by masterubunto on 15/10/18.
 */
public class Statistic {


    public ArrayList<String> Statistiques_colonnes_name;
    public ArrayList<javafx.scene.control.TableColumn> Colonnes;
    public static ArrayList<Double> IQR = new ArrayList<>();


     public Statistic()
     {
         Statistiques_colonnes_name=new ArrayList<> (  );
         Colonnes=new ArrayList<TableColumn> (  );

     }



     public void Shape_stat()
     {
         ArrayList<String> ColAtt = new ArrayList<String>();
         ColAtt.add("Attribut");
         ColAtt.add("Type");
         ColAtt.add("Min");
         ColAtt.add("Max");
         ColAtt.add ( "Median" );
         ColAtt.add ( "Q1" );
         ColAtt.add("Q3");
         ColAtt.add ( "Mean" );
         ColAtt.add("MidRanges");
         ColAtt.add("Mode");
         ColAtt.add("Symetric?");

         //System.out.println (ColAtt.size () );


            /* inserer les noms des attributs */
         for(int AttIndex=0 ; AttIndex<ColAtt.size(); AttIndex++)
         {
             final int x = AttIndex;
             TableColumn colonne =
                     new TableColumn (ColAtt.get(AttIndex));

             colonne.setCellValueFactory(
                     new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>> (){
                 public ObservableValue<String>
                 call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                     return new SimpleStringProperty (param.getValue().get(x).toString());
                 }

             });

           Colonnes.add ( colonne );

         }

     }
     public ArrayList Calcul_min_max(Dataset weka)
     {//<ObservableList<String>>
        ArrayList MyDetails=new ArrayList<> (  );
        Double Mean = 0.0; String Mode;
         for(Attribute attr:weka.attributes){

             ObservableList<String> MyDetail = FXCollections.observableArrayList();

             MyDetail.add(attr.name());//nom de l'attribut
             MyDetail.add(Attribute.typeToString(attr));//type de l'attribut

             if (!Attribute.typeToString(attr).equalsIgnoreCase("nominal"))
             {
                 ArrayList<Double> listeValAtt = new ArrayList<>();
                 for(Instance inst:weka.instances){
                     listeValAtt.add((Double)inst.value(attr));
                 }

                 Collections.sort(listeValAtt);

                 Double Min=0.0,Max=0.0;
                 if(weka.instances.attributeStats ( attr.index () ).numericStats!=null) {
                     Min = weka.instances.attributeStats ( attr.index ( ) ).numericStats.min;
                     Max = weka.instances.attributeStats ( attr.index ( ) ).numericStats.max;
                 }

                 MyDetail.add(Min.toString());
                 MyDetail.add(Max.toString());
                 // appel de la fonction MedianQ1Q3
                 MedianQ1Q3 c= new MedianQ1Q3 ( listeValAtt );
                 c.CalculAll ( 0,listeValAtt.size ()-1 );

                 MyDetail.add(Double.toString (  c.mediane));//4:median
                 MyDetail.add(Double.toString (  c.q1));
                 MyDetail.add(Double.toString (  c.q2));
                 IQR.add(c.q2-c.q1);
                 if(weka.instances.attributeStats ( attr.index () ).numericStats!=null)
                 {
                     Mean = weka.instances.attributeStats ( attr.index () ).numericStats.mean;
                 }

                 MyDetail.add ( String.valueOf (Mean));//7:mean
                 MyDetail.add( Double.toString ((Max+Min)/2) );//8:midrang
                 Mode = Mode_of_numeric( attr,weka );
                 MyDetail.add ( Mode );//9:mode
                 if(Math.abs((Mean - Double.valueOf(Mode)) - 3*(Double.valueOf(Mode) - c.mediane)) <= 1){
                     MyDetail.add("Symetric");
                 }else if((Mean - Double.valueOf(Mode)) > 3*(Double.valueOf(Mode) - c.mediane)){
                     MyDetail.add("Positively skewed");
                 }else if((Mean - Double.valueOf(Mode)) < 3*(Double.valueOf(Mode) - c.mediane)){
                     MyDetail.add("Negatively skewed");
                 }

//ici on a calculé si Mean-Mode = 3 (mode-median) ? pour afficher si Symetric ? Positively skewed ? Negatively skewed ?
             }
             else{

                 MyDetail.add("none");
                 MyDetail.add("none");
                 MyDetail.add("none");
                 MyDetail.add("none");
                 MyDetail.add("none");
                 MyDetail.add("none");
                 MyDetail.add("none");

                 /* valeur la plus frequente*/
                 MyDetail.add (  Mode_of_nominal2( attr,weka ));
                 MyDetail.add("none");

             }

             MyDetails.add(MyDetail);

         }
         return MyDetails;
     }


    public static String Mode_of_nominal(Attribute attr,Dataset weka)
    {
        int max=0,maxi,i=0;
        String commonValue="";
        Enumeration<Object> enums=  attr.enumerateValues();

      //  System.out.println ("atttribut:"+attr.toString () );
        while (enums.hasMoreElements())
        {
            maxi =weka.instances.attributeStats ( attr.index () ).nominalCounts[i];

           //   System.out.println ( weka.instances.attributeStats ( attr.index () ).toString ());

            if(max<=maxi) {
                commonValue=enums.nextElement ().toString ();
                max=maxi;
            }
            else enums.nextElement ();
            i++;
        }
       return commonValue;
    }


    //calcule de l'ecart inter quartile
    /*
    public Double IRQ(Attribute attribute, Dataset weka){
         ArrayList ListInfoAtt = this.Calcul_min_max(weka);
         return (Double) ListInfoAtt.get(6)- (Double) ListInfoAtt.get(5);
    }

    */

/****************************************************************************/
    public static String Mode_of_numeric(Attribute attr,Dataset weka)
    {
        int max=0;
        String commonValue="";
        int freq;
        int indexAtt = attr.index();
        Double val1, val2;
        ArrayList<String> enums = new ArrayList();
        ArrayList al = new ArrayList();

        //liste de toutes les valeurs distinctes d'un attribut
        if(weka.attributes.get(indexAtt).isNominal()){
            al =  Collections.list(weka.attributes.get(indexAtt).enumerateValues());
        }else{
            for (int x=0 ; x < weka.numinstances ; x++){
                if(al.size()==0 || !al.contains(String.valueOf(weka.instances.get(x).value(indexAtt)))){
                    al.add(String.valueOf(weka.instances.get(x).value(indexAtt)));
                }
            }
        }

        //calculer la frequence de chaque valeur de cet attribut
        for (int j = 0; j< al.size();j++){
            freq = 0;
            for (int i=0; i < weka.numinstances; i++){
                if(weka.attributes.get(indexAtt).isNumeric()) {
                    val1 = Double.valueOf(weka.instances.get(i).value(indexAtt));
                    val1 = Math.round(val1*1000.0)/1000.0;
                    val2 = Double.valueOf(al.get(j).toString());
                    val2 = Math.round(val2*1000.0)/1000.0;
                    if (val1.equals(val2)){
                        freq++;
                    }
               /* }else{
                    if (weka.instances.get(i).toString(indexAtt).equalsIgnoreCase(al.get(j).toString())) {
                        freq++;
                    }*/
                }
            }
            if (max < freq){//ne garder que la freq max
                commonValue = al.get(j).toString();
                max = freq;

            }
        }

        return commonValue;
    }

/*********************************************************************************/

    /****************************************************************************/
    public static String Mode_of_nominal2(Attribute attr,Dataset weka)
    {
        int max=0;
        String commonValue="";
        int freq;
        int indexAtt = attr.index();
        String val1, val2;
        ArrayList<String> enums = new ArrayList();
        ArrayList al = new ArrayList();

        //liste de toutes les valeurs distinctes d'un attribut
        for (int x=0 ; x < weka.numinstances ; x++){
            if(al.size()==0 || !al.contains(weka.instances.get(x).stringValue(indexAtt))){
                al.add(weka.instances.get(x).stringValue(indexAtt));
            }
        }


        //calculer la frequence de chaque valeur de cet attribut
        for (int j = 0; j< al.size();j++){
            freq = 0;
            for (int i=0; i < weka.numinstances; i++){
                if(weka.attributes.get(indexAtt).isNominal()) {
                    val1 = weka.instances.get(i).stringValue(indexAtt);
                    val2 = al.get(j).toString();
                    if (val1.equalsIgnoreCase(val2)){
                        freq++;
                    }
                }
            }
            if (max < freq){//ne garder que la freq max
                commonValue = al.get(j).toString();
                max = freq;

            }
        }

        return commonValue;
    }


}
