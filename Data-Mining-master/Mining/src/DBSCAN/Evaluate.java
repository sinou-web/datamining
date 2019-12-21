package DBSCAN;

import weka.core.Attribute;

import java.util.ArrayList;
import java.util.HashMap;

public class Evaluate {

    public static HashMap<String,Object> getCentreGravité(Cluster cluster){
        HashMap<String,Object> CG = new HashMap<>();
        String att = "";
        double somme = 0.0;
        Attribute attribute;

        for(int j=0; j<cluster.listInstances.get(0).numAttributes();j++){
            attribute = cluster.listInstances.get(0).attribute(j);
            att = attribute.name();

            if(attribute.isNumeric()){
                //calculer la moyenne
                for(int i = 0; i< cluster.listInstances.size(); i++){

                    somme = somme + Double.valueOf(cluster.listInstances.get(i).value(j));
                }
                CG.put(att,(somme/cluster.listInstances.size()));

            }else if(attribute.isNominal()){
                //calculer le mode
                CG.put(att,ModeNominal(cluster,j));
            }

        }
        return CG;
    }


    public static double inertieIntraClasse(Cluster cluster,HashMap<String,Object> CG){

        double somme = 0.0;

        for(int i =0 ; i<cluster.listInstances.size(); i++){
            //somme = somme + Distances.SommeInerties(CG,
            //        cluster.listInstances.get(i));
            somme = somme + Math.pow(Distances.SommeInerties(CG,
                            cluster.listInstances.get(i)),2);
        }

        //return somme/cluster.listInstances.size();
        return (somme);
    }

    public static double inertieInterClasse(ArrayList<HashMap<String,Object>> listCG){
        HashMap<String,Object> CGglobale = new HashMap<>();
        double somme = 0;
        Object val;
        for(String key : listCG.get(0).keySet()){
            //get la classe de l'attribut
            Class<?> TheClass = listCG.get(0).get(key).getClass();
            if(TheClass == Integer.class || TheClass == Double.class){
                //calculer la moyenne
                for(int i = 0; i< listCG.size(); i++){
                    somme = somme + Double.valueOf(listCG.get(i).get(key).toString());
                }
                CGglobale.put(key,(somme/listCG.size()));

            }else if(TheClass == String.class){
                //calculer le mode
                CGglobale.put(key,ModeNominal2(listCG,key));
            }

        }


        for(int i = 0; i< listCG.size(); i++){
            //somme = somme + Distances.SommeInerties2(CGglobale,
            //        listCG.get(i));
            somme = somme + Math.pow(Distances.SommeInerties2(CGglobale,
                            listCG.get(i)),2);
        }

        return somme/listCG.size();
        //return (somme);
    }


    /*******************************************************************/


    public static String ModeNominal(Cluster cluster,int indexAtt){
        ArrayList al = new ArrayList();
        int max=0;
        String commonValue="";
        int freq;
        String val1, val2;

        //liste de toutes les valeurs distinctes d'un attribut
            for (int x=0 ; x < cluster.listInstances.size() ; x++){
            if(al.size()==0 || !al.contains(cluster.listInstances.get(x).stringValue(indexAtt))){
                al.add(cluster.listInstances.get(x).stringValue(indexAtt));
            }
        }

        //calculer la frequence de chaque valeur de cet attribut
        for (int j = 0; j< al.size();j++){
            freq = 0;
            for (int i=0; i < cluster.listInstances.size(); i++){
                    val1 = cluster.listInstances.get(i).stringValue(indexAtt);
                    val2 = al.get(j).toString();
                    if (val1.equalsIgnoreCase(val2)){
                        freq++;
                    }

            }
            if (max < freq){//ne garder que la freq max
                commonValue = al.get(j).toString();
                max = freq;

            }
        }

        return commonValue;
    }



    public static String ModeNominal2(ArrayList<HashMap<String,Object>> listCG, String key){
        ArrayList al = new ArrayList();
        int max=0;
        String commonValue="";
        int freq;
        String val1, val2;

        //liste de toutes les valeurs distinctes d'un attribut
        for (int x=0 ; x < listCG.size() ; x++){
            if(al.size()==0 || !al.contains(listCG.get(x).get(key))){
                al.add(listCG.get(x).get(key));
            }
        }

        //calculer la frequence de chaque valeur de cet attribut
        for (int j = 0; j< al.size();j++){
            freq = 0;
            for (int i=0; i < listCG.size(); i++){
                val1 =( String) listCG.get(i).get(key);
                val2 = al.get(j).toString();
                if (val1.equalsIgnoreCase(val2)){
                    freq++;
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
