package DBSCAN;

import weka.core.Instance;

import java.util.ArrayList;

public class Cluster {
    public String name;
    public ArrayList<Instance> listInstances;

    Cluster(String name){
        this.name = name;
        this.listInstances = new ArrayList<>();
    }

    public void InsertIntoCluster(Instance instance){
        this.listInstances.add(instance);
    }

    public boolean IsInCluster(Instance instance){
        return (this.listInstances.contains(instance));
    }

    static boolean IsClustered(ArrayList<Cluster> listCluster, Instance instance){
        for(Cluster cluster: listCluster){
            if(cluster.IsInCluster(instance)){
                return true;
            }
        }
        return false;
    }

    public static String WhichCluster(ArrayList<Cluster> listCluster, Instance instance){
        for(Cluster cluster: listCluster){
            if(cluster.IsInCluster(instance)){
                return cluster.name;
            }
        }
        return "Bruit";
    }

    @Override
    public String toString() {
        return this.name+ " : " + this.listInstances.size()+"\n";
    }

    public static String show(ArrayList<Cluster> listCluster){
        String s = "";
        for(Cluster c:listCluster){
            s = s + c.toString() + ",";
        }
        return s;
    }
}
