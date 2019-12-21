package KNN;

import UI.Dataset;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by masterubunto on 27/11/18.
 */
public class KNN {
    public int K;
    public int truepostive;
    public Dataset data;

    public Instances TrainingSet ;
    public Instances TestSet;
    public int PoucentageofTrain;
    public int PourcentageofTest;
    public ArrayList<String> classes;

    public void setK(int k) {
        K = k;
    }

    public void setPoucentageofTrain(int poucentageofTrain) {
        PoucentageofTrain = poucentageofTrain;
    }

    public void setPourcentageofTest(int pourcentageofTest) {
        PourcentageofTest = pourcentageofTest;
    }
    public void InitNewSAMPLING()
    { TrainingSet= new Instances ( data.instances, 0,(this.PoucentageofTrain* data.instances.size ())/100 );
        TestSet=new Instances ( data.instances,(this.PoucentageofTrain* data.instances.size ())/100 ,(this.PourcentageofTest *data.instances.size ())/100);}

    public KNN(int k, String filename, int poucentageofTrain, int pourcentageofTest) {
        K = k;
        this.data = new Dataset ( filename );

        this.data.instances.randomize ( new Random ( 6 ) );
        this.PoucentageofTrain=poucentageofTrain;

        this.PourcentageofTest=pourcentageofTest;
        /* normaliser et filling missing values before any step*/
        data.missingvalues_detector ();
        try {
            data.normalizer ();
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        TrainingSet= new Instances ( data.instances, 0,(this.PoucentageofTrain* data.instances.size ())/100 );
        TestSet=new Instances ( data.instances,(this.PoucentageofTrain* data.instances.size ())/100 ,(this.PourcentageofTest *data.instances.size ())/100);
          // System.out.println ("Train:"+ (this.PoucentageofTrain* data.instances.size ())/100);
         //  System.out.println  ("TestsiZE:"+(this.PourcentageofTest *data.instances.size ()/100)+",,testdebutindex:"+(data.numinstances-(this.PourcentageofTest *data.instances.size ())/100));


    }

    public KNN(int k, Dataset data,int poucentageofTrain,int pourcentageofTest) {
        K = k;
        this.data = data;
        this.data.instances.randomize ( new Random ( 6 ) );
        this.PoucentageofTrain=poucentageofTrain;
        this.PourcentageofTest=pourcentageofTest;


    
        TrainingSet= new Instances ( data.instances, 0,(this.PoucentageofTrain* data.instances.size ())/100 );
        TestSet=new Instances ( data.instances,(this.PoucentageofTrain* data.instances.size ())/100 ,(this.PourcentageofTest *data.instances.size ())/100);
    }


    public String TestsetKNN()
    {  classes=new ArrayList<> (  );
        int VraiPositive=0;
        for (Instance inst : TestSet)
        {  String classe=this.ClassifierKNN ( inst );
            if(TestSet.checkForAttributeType ( 1 )) {
                if (classe.compareTo ( inst.stringValue ( inst.numAttributes ( ) - 1 ) ) == 0) {
                    classes.add ( "YES" );
                    VraiPositive++;
                } else classes.add ( "NO:" + classe );
            }
            else
            {  if (classe.compareTo ( String.valueOf (  inst.value ( inst.numAttributes ( ) - 1 )) ) == 0) {
                classes.add ( "YES" );
                VraiPositive++;
            } else classes.add ( "NO:" + classe );
            }
        }
        truepostive=VraiPositive;
        double v=VraiPositive;
        return String.valueOf ( v/(TestSet.size ()) );
    }

    public String ClassifierKNN(Instance instunlabelled) {
    VecteurD arrayofDistances=new VecteurD();
        for (Instance inst : TrainingSet)
            if( TrainingSet.checkForAttributeType ( 1 ))

                arrayofDistances.add ( new Element ( inst.stringValue ( data.numatt - 1 ), Distances.Dxy ( instunlabelled, inst ) ) );
            else
                arrayofDistances.add ( new Element ( String.valueOf (  inst.value ( data.numatt - 1 )), Distances.Dxy ( instunlabelled, inst ) ) );

        return arrayofDistances.getMostcommonInK ( K ) ;
    }
    public static void main (String[] args) throws Exception {
        int training =90,test=10,k=3;
        String accuracy ="";
        KNN exampleknn = new KNN (k, "./data/unbalanced.arff", training,test);




        String statK=""; // plot for k and accuracy
        String statT=""; // plot for time and k
        String Kvariation="";//variation de kk


             for (int train=training; train >20; train-=10)
             {
                 System.out.println ("Train:"+train+":Test:"+(100-train) );
                 exampleknn.setPoucentageofTrain ( train );
                 exampleknn.setPourcentageofTest ( 100-train );
                 Kvariation="K";
                 for (int i =k ;i< 8;i++)
                 {
                     Kvariation=Kvariation+":"+i;
                     exampleknn.setK ( i );
                 /* modifier les new instances*/
                     exampleknn.InitNewSAMPLING ();


                     long startTime = System.currentTimeMillis ( );
                      accuracy = exampleknn.TestsetKNN ( );
                      statK = statK.concat ( "," + accuracy );

                      long stopTime = System.currentTimeMillis ( );
                      double elapsedTime = (stopTime - startTime);
                      statT = statT.concat ( "," + elapsedTime );
                        }
                 System.out.println ( Kvariation );
                 System.out.println ("accuracy:"+statK );
                 System.out.println ("time:"+statT );
                 statK="";
                 statT="";
             }


        }


    }





