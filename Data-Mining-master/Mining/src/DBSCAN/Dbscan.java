package DBSCAN;

import UI.Dataset;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public class Dbscan {


    public ArrayList<Cluster> dbscan(Dataset data , double epsilon, int  MinVoisin){
        ArrayList<Cluster> listCluster = new ArrayList<>();
        ArrayList<Instance> Bruit = new ArrayList<>();
        int NumC=0;

        ArrayList<Instance> nonVisiteData = new ArrayList<>();
        for(Instance in:data.instances){
            nonVisiteData.add(in);
        }

        //data.instances.randomize ( new Random( 10 ) );
        while( nonVisiteData.size()>0) {
            Instance instance = nonVisiteData.get(0);

            //supprimer instance de la liste nonVisiteData (marquer comme visité)
            nonVisiteData.remove(instance);

            ArrayList<Instance> listVoisins = epsilonVoisinage(data.instances, instance, epsilon);
            //System.out.println("nbr voisins "+listVoisins.size());

            if(listVoisins.size() < MinVoisin){
                //marquer instance comme bruit
                Bruit.add(instance);
            }
            else{
                NumC++;
                Cluster C = new Cluster("Cluster"+String.valueOf(NumC));

                C.InsertIntoCluster(instance);
                listCluster.add(C);
                etendreCluster(data.instances,nonVisiteData, instance, listVoisins,listCluster, C, epsilon, MinVoisin);
            }
        }

        System.out.println(listCluster.toString());
        System.out.println("bruit : "+Bruit.size());
        return listCluster;

    }


    public ArrayList<Instance> epsilonVoisinage(Instances instances,
                                            Instance instance,double epsilon){

        ArrayList<Instance> listVoisins = new ArrayList<>();

        for(Instance inst:instances) {
            //System.out.println("distance: "+Distances.Dxy(inst,instance));
            if(Distances.Dxy(inst,instance) <= epsilon){
                listVoisins.add(inst);
            }
        }

        return listVoisins;
    }



    public void etendreCluster(Instances instances, ArrayList<Instance> nonVisiteData,Instance instance,
                       ArrayList<Instance> listVoisins,ArrayList<Cluster> listCluster ,
                               Cluster C,double epsilon, int MinVoisin){

        System.out.println("huh"+C);
        //pour chaque voisin de l'instance courante
        for (int i = 0 ; i < listVoisins.size() ; i++) {
            Instance inst = listVoisins.get(i);
            //si elle n'est pas déja visitée
            if(nonVisiteData.contains(inst)){
                //supprimer instance de la liste nonVisiteData (marquer comme visité)
                nonVisiteData.remove(inst);
                ArrayList<Instance> listVoisins2 = epsilonVoisinage(instances, inst, epsilon);
                if(listVoisins2.size() >= MinVoisin){
                    listVoisins.addAll(listVoisins2);
                    listVoisins.add(inst);
                }
            }
            if(!Cluster.IsClustered(listCluster,inst)){
                C.InsertIntoCluster(inst);
            }
        }
    }

}
