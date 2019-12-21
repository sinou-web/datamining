package DBSCAN;


public class main {

    public static void main(String[] args) {

/*
        Dataset data = new Dataset("./data/labor.arff");
        data.missingvalues_detector();
        double epsilon = 5.0;
        int MinVoisin = 5;
        Dbscan dbscan = new Dbscan();
        dbscan.dbscan( data , epsilon,MinVoisin);
*/

        //pour iris : 0.4  et 4
        //pour labor: 5 et 5

        Integer integer = 1;
        Class<?> clazzInteger = integer.getClass();
        if(clazzInteger==Integer.class)
            System.out.println( "class of integer=" + clazzInteger );
        Class<?> clazzClazzInteger = clazzInteger.getClass();
        System.out.println( "class of class Integer's class=" + clazzClazzInteger );
        String string = "x";
        Class<?> clazzString = string.getClass();
        System.out.println( "class of string=" + clazzString );
        Class<?> clazzClazzString = clazzString.getClass();
        System.out.println( "class of class String's class=" + clazzClazzString );
    }
}
