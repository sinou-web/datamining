package DBSCAN;

import weka.core.Instance;

import java.lang.Math;
import java.util.HashMap;


/**
 * Created by masterubunto on 27/11/18.
 */
public  class Distances {


    public static Double powerxy2(double x, double y) {
        return (Math.pow(x - y, 2));
    }


    public static Double Dxy(Instance inst1, Instance inst2) {
        double s = 0, snominal = 0;
        //  System.out.println ("len of instance:"+(inst1.toDoubleArray ().length-1 ));
        for (int i = 0; i < inst1.toDoubleArray().length - 1; i++)
            if (inst1.attribute(i).isNumeric())
                s = Double.sum(s, powerxy2(inst1.value(i), inst2.value(i)));
            else if (inst1.attribute(i).isNominal())
                snominal = Double.sum(snominal, DxyNominal(inst1.stringValue(i), inst2.stringValue(i)));

        return Math.sqrt(s+ snominal) ;
    }

    public static double DxyNominal(String i1, String i2) {
        int compare = i1.compareTo(i2);
        if (compare != 0) return 1;
        else return 0;

    }


    public static Double SommeInerties(HashMap<String,Object> inst1, Instance inst2) {
        double s = 0, snominal = 0;
        //  System.out.println ("len of instance:"+(inst1.toDoubleArray ().length-1 ));

        for (int i = 0; i < inst2.toDoubleArray().length - 1; i++)
            if (inst2.attribute(i).isNumeric()) {
                System.out.println("nom att" + inst2.attribute(i).name());
                s = Double.sum(s, powerxy2((Double) inst1.get(inst2.attribute(i).name()), inst2.value(i)));
            }
            else if (inst2.attribute(i).isNominal())
                snominal = Double.sum(snominal, DxyNominal((String)inst1.get(inst2.attribute(i).name()), inst2.stringValue(i)));

        return Math.sqrt(s+ snominal) ;
    }


    public static Double SommeInerties2(HashMap<String,Object> inst1, HashMap<String,Object> inst2) {
        double s = 0, snominal = 0;
        //  System.out.println ("len of instance:"+(inst1.toDoubleArray ().length-1 ));
        for (String key : inst1.keySet()) {
            Class<?> TheClass = inst1.get(key).getClass();
            if (TheClass == Integer.class || TheClass == Double.class)
                s = Double.sum(s, powerxy2(Double.valueOf(inst1.get(key).toString()), Double.valueOf(inst2.get(key).toString())));
            else if (TheClass == String.class)
                snominal = Double.sum(snominal, DxyNominal(inst1.get(key).toString(), inst2.get(key).toString()));
        }
        return Math.sqrt(s + snominal) ;
    }


}